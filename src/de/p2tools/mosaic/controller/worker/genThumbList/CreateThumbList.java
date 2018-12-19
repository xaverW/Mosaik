/*
 *    Copyright (C) 2008
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.p2tools.mosaic.controller.worker.genThumbList;

import de.p2tools.mosaic.controller.RunEvent;
import de.p2tools.mosaic.controller.RunListener;
import de.p2tools.mosaic.controller.config.ProgData;
import de.p2tools.mosaic.controller.data.thumb.ThumbCollection;
import de.p2tools.mosaic.controller.data.thumb.ThumbDataList;
import de.p2tools.p2Lib.image.ImgFile;
import de.p2tools.p2Lib.tools.file.PFileUtils;
import de.p2tools.p2Lib.tools.log.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;

import javax.swing.event.EventListenerList;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.Random;

public class CreateThumbList {

    private ProgData progData;
    private EventListenerList listeners = new EventListenerList();
    private boolean stopAll = false;
    private LinkedList<File[]> filesCreateThumb = new LinkedList<>();

    private int anzThread = 1;
    private int threads = 0;
    private Random random = new Random();

    /**
     *
     */
    public CreateThumbList(ProgData progData) {
        this.progData = progData;
        anzThread = Runtime.getRuntime().availableProcessors();
    }


    /**
     * @param listener
     */
    public void addAdListener(RunListener listener) {
        listeners.add(RunListener.class, listener);
    }

    /**
     * stop the process
     */
    public void setStop() {
        stopAll = true;
    }

    public void create(ThumbCollection thumbCollection, String destDir) {
        PDuration.counterStart("Thumb erstellen");
        stopAll = false;
        CreateListOfThumbs createListOfThumbs = new CreateListOfThumbs(thumbCollection, destDir);
        Thread thread = new Thread(createListOfThumbs);
        thread.setDaemon(true);
        thread.start();
    }

    private void notifyEvent(int max, int progress, String text) {
        RunEvent event = new RunEvent(this, progress, max, text);
        for (RunListener l : listeners.getListeners(RunListener.class)) {
            l.notify(event);
        }
    }


    private synchronized void addCreationsList(File file1, File file2) {
        filesCreateThumb.add(new File[]{file1, file2});
    }

    private synchronized File[] getFromCreationsList() {
        return filesCreateThumb.poll();
    }

    private class CreateListOfThumbs implements Runnable {
        private ThumbDataList tmpList = new ThumbDataList();
        private ThumbCollection thumbCollection;
        private File fileSrcDir;
        private File fileDestDir;
        private boolean rekursiv;
        private int maxFile = 0;
        private int progressFile = 0;

        public CreateListOfThumbs(ThumbCollection thumbCollection, String thumbDir) {
            this.thumbCollection = thumbCollection;
            this.fileSrcDir = new File(thumbCollection.getFotoSrcDir());
            this.fileDestDir = new File(thumbDir);
            this.rekursiv = thumbCollection.isRecursive();

            PLog.sysLog("CreateListOfThumbs");
        }

        @Override
        public synchronized void run() {
            maxFile = 0;
            try {
                if (!fileDestDir.exists()) {
                    fileDestDir.mkdirs();
                }

                if (!fileSrcDir.isDirectory() || !fileDestDir.isDirectory()) {
                    PLog.errorLog(912364587, "Quelle oder Ziel ist kein Verzeichnis!");
                    return;
                }

                // Dateien zählen
                maxFile = PFileUtils.countFilesInDirectory(fileSrcDir);
                notifyEvent(maxFile, 0, maxFile + " Miniaturbilder erstellen");

                // Fotos zum Erstellen der Thumbs suchen
                createFileList(fileSrcDir);

                // Thumbs erstellen
                Thread t;
                for (int i = 0; i < anzThread; ++i) {
                    ++threads;
                    t = new Thread(new CreateThumbs());
                    t.setName("CreateThumList");
                    t.setDaemon(true);
                    t.start();
                }
            } catch (Exception ex) {
                PLog.errorLog(912020201, ex);
            }
        }

        private void createFileList(File pfad) {
            Path dir = pfad.toPath();
            String str = "";

            try {
                Path start = pfad.toPath();
                Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                        if (!checkSuffix(file.toFile())) {
                            return FileVisitResult.CONTINUE;
                        }

                        try {
                            File dest = new File(fileDestDir.getAbsolutePath() + File.separator +
                                    file.toFile().getName() + "_" +
                                    random.nextInt(Integer.MAX_VALUE) + "." + ImgFile.IMAGE_FORMAT_JPG);
                            final String str = dest.getAbsolutePath();
                            addCreationsList(file.toFile(), dest);
                        } catch (Exception ex) {
                            PLog.errorLog(391201245, ex, new String[]{"CreateThumbList.createFileList",
                                    "Fehler - Src: " + file.toFile().getAbsolutePath(),
                                    "Fehler - Dest: " + str});
                        }

                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
                        if (e == null) {
                            return FileVisitResult.CONTINUE;
                        } else {
                            // directory iteration failed
                            throw e;
                        }
                    }
                });
            } catch (IOException ex) {
                PLog.errorLog(965412014, ex);
            }

//            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
//                for (Path entry : stream) {
//                    final File file = entry.toFile();
//
//                    if (file.isDirectory() && rekursiv) {
//                        createFileList(file);
//                    }
//
//                    if (!file.isFile() || !checkSuffix(file)) {
//                        continue;
//                    }
//
//                    try {
//                        File dest = new File(fileDestDir.getAbsolutePath() + File.separator +
//                                file.getName() + "_" +
//                                random.nextInt(Integer.MAX_VALUE) + "." + ImgFile.IMAGE_FORMAT_JPG);
//                        str = dest.getAbsolutePath();
//                        addCreationsList(file, dest);
//                    } catch (Exception ex) {
//                        Log.errorLog(391201245, ex, new String[]{"CreateThumbList.createFileList",
//                                "Fehler - Src: " + file.getAbsolutePath(),
//                                "Fehler - Dest: " + str});
//                    }
//                }
//            } catch (IOException ex) {
//                Log.errorLog(965412014, ex);
//            }

//            File[] liste;
//            if (pfad.isDirectory()) {
//                liste = pfad.listFiles();
//                for (int i = 0; i < liste.length && !stopAll; i++) {
//                    if (liste[i].isFile()) {
//                        if (checkSuffix(liste[i])) {
//                            try {
//                                File dest = new File(fileDestDir.getAbsolutePath() + File.separator +
//                                        liste[i].getName() + "_" +
//                                        random.nextInt(Integer.MAX_VALUE) + "." + ImgFile.IMAGE_FORMAT_JPG);
//                                str = dest.getAbsolutePath();
//                                addCreationsList(liste[i], dest);
//                            } catch (Exception ex) {
//                                Log.errorLog(391201245, ex, new String[]{"CreateThumbList.createFileList",
//                                        "Fehler - Src: " + liste[i].getAbsolutePath(),
//                                        "Fehler - Dest: " + str});
//                            }
//                        }
//                    } else if (liste[i].isDirectory() && rekursiv) {
//                        createFileList(liste[i]);
//                    }
//                }
//            }
        }

        private class CreateThumbs implements Runnable {

            @Override
            public void run() {
                File[] file;
                while (!stopAll && (file = getFromCreationsList()) != null) {
                    create(file[0], file[1]);
                }
                --threads;
                if (threads <= 0) {
                    thumbCollection.getThumbList().addAll(tmpList);
                    tmpList.clear();

                    thumbCollection.getThumbList().sort();
                    notifyEvent(0, 0, "");
                    PDuration.counterStop("Thumb erstellen");
                }
            }

            private void create(File fileSrc, File fileDest) {
                ++progressFile;
                notifyEvent(maxFile, progressFile, fileSrc.getName() +
                        (maxFile == 0 ? "" : " [" + 100 * progressFile / maxFile + " Prozent]"));
                ScaleImage.getScaledThumb(fileSrc, fileDest, tmpList);
            }
        }

    }

    public static boolean checkSuffix(File file) {
        boolean ret = false;
        if (file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1).equalsIgnoreCase(ImgFile.IMAGE_FORMAT_JPG) ||
                file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1).equalsIgnoreCase("jpeg") ||
                file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1).equalsIgnoreCase(ImgFile.IMAGE_FORMAT_PNG)) {
            ret = true;
        }
        return ret;
    }
}
