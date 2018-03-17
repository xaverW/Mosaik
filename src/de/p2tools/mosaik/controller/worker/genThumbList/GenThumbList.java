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

package de.p2tools.mosaik.controller.worker.genThumbList;

import de.p2tools.mosaik.controller.RunEvent;
import de.p2tools.mosaik.controller.RunListener;
import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.controller.data.thumb.Thumb;
import de.p2tools.mosaik.controller.data.thumb.ThumbCollection;
import de.p2tools.mosaik.controller.data.thumb.ThumbDataList;
import de.p2tools.p2Lib.image.ImgFile;
import de.p2tools.p2Lib.tools.Duration;
import de.p2tools.p2Lib.tools.FileUtils;
import de.p2tools.p2Lib.tools.Log;

import javax.swing.event.EventListenerList;
import java.io.File;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeSet;

public class GenThumbList {

    private ProgData progData;
    private EventListenerList listeners = new EventListenerList();
    private boolean stopAll = false;
    private int maxFile = 0;
    private int progressFile = 0;
    private LinkedList<File> fileListRead = new LinkedList<>();
    private LinkedList<File[]> filesCreateThumb = new LinkedList<>();

    private int anzThread = 1;
    private int threads = 0;
    private Random random = new Random();

    /**
     */
    public GenThumbList(ProgData progData) {
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
     *
     */
    public void setStop() {
        stopAll = true;
    }

    public void create(ThumbCollection thumbCollection, String destDir) {
        Duration.counterStart("Thumb erstellen");
        progressFile = 0;
        stopAll = false;
        filesCreateThumb.clear();
        CreateListOfThumbs createListOfThumbs = new CreateListOfThumbs(thumbCollection, destDir);
        Thread thread = new Thread(createListOfThumbs);
        thread.setDaemon(true);
        thread.start();
    }

    public void read(ThumbCollection thumbCollection, String destDir) {
        progressFile = 0;
        stopAll = false;
        fileListRead.clear();
        thumbCollection.getThumbList().clear();
        ReadListOfThumbs readListOfThumbs = new ReadListOfThumbs(thumbCollection, destDir);
        Thread tErst = new Thread(readListOfThumbs);
        tErst.setDaemon(true);
        tErst.start();
    }

    private void notifyEvent(int max, int progress, String text) {
        RunEvent event = new RunEvent(this, progress, max, text);
        for (RunListener l : listeners.getListeners(RunListener.class)) {
            l.notify(event);
        }
    }

    private synchronized void addReadFile(File file) {
        fileListRead.add(file);
    }

    private synchronized File getReadFile() {
        return fileListRead.poll();
    }

    private synchronized void addCreationsList(File file1, File file2) {
        filesCreateThumb.add(new File[]{file1, file2});
    }

    private synchronized File[] getFromCreationsList() {
        return filesCreateThumb.poll();
    }

    private class CreateListOfThumbs implements Runnable {
        ThumbCollection thumbCollection;
        private File fileSrcDir;
        private File fileDestDir;
        private boolean rekursiv;

        public CreateListOfThumbs(ThumbCollection thumbCollection, String thumbDir) {
            this.thumbCollection = thumbCollection;
            fileSrcDir = new File(thumbCollection.getFotoSrcDir());
            fileDestDir = new File(thumbDir);
            this.rekursiv = thumbCollection.isRecursive();
        }

        @Override
        public synchronized void run() {
            maxFile = 0;
            try {
                if (!fileDestDir.exists()) {
                    fileDestDir.mkdirs();
                }

                if (!fileSrcDir.isDirectory() || !fileDestDir.isDirectory()) {
                    Log.errorLog(912364587, "Quelle oder Ziel ist kein Verzeichnis!");
                    return;
                }

                // Dateien zählen
                maxFile = FileUtils.countFilesInDirectory(fileSrcDir);
                notifyEvent(maxFile, 0, maxFile + " Miniaturbilder erstellen");

                // Fotos zum Erstellen der Thumbs suchen
                createFileList(fileSrcDir);

                // Thumbs erstellen
                Thread t;
                for (int i = 0; i < anzThread; ++i) {
                    ++threads;
                    t = new Thread(new CreateThumbs());
                    t.setDaemon(true);
                    t.start();
                }
            } catch (Exception ex) {
                Log.errorLog(912020201, ex);
            }
        }

        private void createFileList(File pfad) {
            File[] liste;
            String str = "";
            if (pfad.isDirectory()) {
                liste = pfad.listFiles();
                for (int i = 0; i < liste.length && !stopAll; i++) {
                    if (liste[i].isFile()) {
                        if (checkSuffix(liste[i])) {
                            try {
                                File dest = new File(fileDestDir.getAbsolutePath() + File.separator +
                                        liste[i].getName() + "_" +
                                        random.nextInt(Integer.MAX_VALUE) + "." + ImgFile.IMAGE_FORMAT_JPG);
                                str = dest.getAbsolutePath();
                                addCreationsList(liste[i], dest);
                            } catch (Exception ex) {
                                Log.errorLog(391201245, ex, new String[]{"GenThumbList.createFileList",
                                        "Fehler - Src: " + liste[i].getAbsolutePath(),
                                        "Fehler - Dest: " + str});
                            }
                        }
                    } else if (liste[i].isDirectory() && rekursiv) {
                        createFileList(liste[i]);
                    }
                }
            }
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
                    thumbCollection.getThumbList().sort();
                    notifyEvent(0, 0, "");
                    Duration.counterStop("Thumb erstellen");
                }
            }

            private void create(File fileSrc, File fileDest) {
                ++progressFile;
                notifyEvent(maxFile, progressFile, fileSrc.getName() +
                        (maxFile == 0 ? "" : " [" + 100 * progressFile / maxFile + " Prozent]"));
                ScaleImage.getScaledThumb(fileSrc, fileDest, thumbCollection);
            }
        }

    }

    private class ReadListOfThumbs implements Runnable {

        private ThumbDataList tmpList = new ThumbDataList();
        private File fileThumbDir;
        private ThumbCollection thumbCollection;

        public ReadListOfThumbs(ThumbCollection thumbCollection, String thumbDir) {
            this.thumbCollection = thumbCollection;
            fileThumbDir = new File(thumbDir);
        }

        @Override
        public synchronized void run() {
            try {
                thumbCollection.getThumbList().clear();
                maxFile = 0;
                //src und prüfen
                if (fileThumbDir.isDirectory()) {
                    //Dateien zählen
                    maxFile = FileUtils.countFilesInDirectory(fileThumbDir);
                    notifyEvent(maxFile, 0, "Miniaturbilder einlesen");
                    createFileList(fileThumbDir);
                } else {
                    Log.errorLog(912020237, "Quelle ist kein Verzeichnis!");
                }
            } catch (Exception ex) {
                Log.errorLog(975421310, ex);
            }
            ReadColor readColor;
            Thread t;
            treeSet.clear();
            for (int i = 0; i < anzThread; ++i) {
                ++threads;
                readColor = new ReadColor();
                t = new Thread(readColor);
                t.setDaemon(true);
                t.start();
            }
        }

        private synchronized void addThumb(Thumb thumb) {
            tmpList.add(thumb);
        }

        private void createFileList(File file) {
            File[] liste;
            if (file.isDirectory()) {
                liste = file.listFiles();
                for (int i = 0; i < liste.length; i++) {
                    if (liste[i].isFile()) {
                        if (checkSuffix(liste[i])) {
                            addReadFile(liste[i]);
                        }
                    } else if (liste[i].isDirectory()) {
                        createFileList(liste[i]);
                    }
                }
            }

        }

        TreeSet<Thumb> treeSet = new TreeSet<>();

        private class ReadColor implements Runnable {

            @Override
            public void run() {
                File file;
                while (!stopAll && (file = getReadFile()) != null) {
                    Thumb thumb;
                    if ((thumb = ScaleImage.getThumb(file)) != null) {
//                        treeSet.add(thumb);
                        addThumb(thumb);
                    }

                    ++progressFile;
                    notifyEvent(maxFile, progressFile,
                            maxFile == 0 ? "" : 100 * progressFile / maxFile + " Prozent");
                }
                --threads;
                if (threads <= 0) {
                    tmpList.sort();
                    thumbCollection.getThumbList().setAll(tmpList);
                    tmpList.clear();
                    notifyEvent(0, 0, "");
                }
            }

        }

    }

    public boolean checkSuffix(File file) {
        boolean ret = false;
        if (file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1).equalsIgnoreCase(ImgFile.IMAGE_FORMAT_JPG) ||
                file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1).equalsIgnoreCase("jpeg") ||
                file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1).equalsIgnoreCase(ImgFile.IMAGE_FORMAT_PNG)) {
            ret = true;
        }
        return ret;
    }
}
