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

package de.p2tools.controller.worker.genThumbList;

import de.p2tools.controller.RunEvent;
import de.p2tools.controller.RunListener;
import de.p2tools.controller.config.ProgConst;
import de.p2tools.controller.config.ProgData;
import de.p2tools.controller.data.thumb.Thumb;
import de.p2tools.controller.data.thumb.ThumbCollection;
import de.p2tools.mLib.tools.Duration;
import de.p2tools.mLib.tools.FileUtils;

import javax.swing.event.EventListenerList;
import java.io.File;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeSet;

public class GenThumbList {

    private ProgData progData;
    private EventListenerList listeners = new EventListenerList();
    private int progress = 0;
    private boolean stopAll = false;
    private int fileCount = 0;
    private LinkedList<File> fileListRead = new LinkedList<>();
    private LinkedList<File[]> filesCreateThumb = new LinkedList<>();
    private ThumbCollection thumbCollection;

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
     *
     */
    public void setStop() {
        stopAll = true;
    }

    /**
     * @param listener
     */
    public void addAdListener(RunListener listener) {
        listeners.add(RunListener.class, listener);
    }

    public void create(ThumbCollection thumbCollection) {
        this.thumbCollection = thumbCollection;
        Duration.counterStart("Thumb erstellen");
        progress = 0;
        stopAll = false;
        filesCreateThumb.clear();
        CreateListOfThumbs thumbs = new CreateListOfThumbs();
        Thread thread = new Thread(thumbs);
        thread.setDaemon(true);
        thread.start();
    }

    public void read(ThumbCollection thumbCollection) {
        this.thumbCollection = thumbCollection;
        progress = 0;
        stopAll = false;
        fileListRead.clear();
        thumbCollection.getThumbList().clear();
        Einlesen einl = new Einlesen(thumbCollection.getThumbDir());
        Thread tErst = new Thread(einl);
        tErst.setDaemon(true);
        tErst.start();
    }

    private void notifyEvent(int max, int progress, String text) {
        RunEvent event;
        event = new RunEvent(this, progress, max, text);
        for (RunListener l : listeners.getListeners(RunListener.class)) {
            l.notify(event);
        }
    }

    private synchronized void addFileEinlesen(File file) {
        fileListRead.add(file);
    }

    private synchronized File getFileEinlesen() {
        return fileListRead.poll();
    }

    private synchronized void addCreationsList(File file1, File file2) {
        filesCreateThumb.add(new File[]{file1, file2});
    }

    private synchronized File[] getFromCreationsList() {
        return filesCreateThumb.poll();
    }

    private class CreateListOfThumbs implements Runnable {
        private File srcDir;
        private File destDir;
        private boolean rekursiv;

        public CreateListOfThumbs() {
            srcDir = new File(thumbCollection.getFotoSrcDir());
            destDir = new File(thumbCollection.getThumbDir());
            rekursiv = thumbCollection.isRecursive();
        }

        public synchronized void run() {
            fileCount = 0;
            try {
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }

                if (!srcDir.isDirectory() || !destDir.isDirectory()) {
                    System.out.println("Quelle oder Ziel ist kein Verzeichnis!");
                    return;
                }

                // Dateien zählen
                fileCount = FileUtils.countFilesInDirectory(srcDir);
                notifyEvent(fileCount, 0, "");

                // Fotos zum Erstellen der Thumbs suchen
                createFileList(srcDir);

                // Thumbs erstellen
                Thread t;
                for (int i = 0; i < anzThread; ++i) {
                    ++threads;
                    t = new Thread(new CreateThumbs());
                    t.setDaemon(true);
                    t.start();
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + "BildArchiv_.CreateThumbList.start");
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
                                File dest = new File(destDir.getAbsolutePath() + File.separator +
                                        liste[i].getName() + "_" +
                                        random.nextInt(Integer.MAX_VALUE) + "." + ProgConst.IMAGE_FORMAT_JPG);
                                str = dest.getAbsolutePath();
                                addCreationsList(liste[i], dest);
                            } catch (Exception ex) {
                                System.out.println(ex.getMessage() + "BildArchiv_.makeThumb");
                                System.out.println("----------------------------------");
                                System.out.println("Fehler - Src: " + liste[i].getAbsolutePath());
                                System.out.println("Fehler - Dest: " + str);
                            }
                        }
                    } else if (liste[i].isDirectory() && rekursiv) {
                        createFileList(liste[i]);
                    }
                }
            }
        }

        private class CreateThumbs implements Runnable {

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
                ++progress;
                notifyEvent(fileCount, progress, fileSrc.getName());
                ScaleImage.getScaledThumb(fileSrc, fileDest, thumbCollection);
            }
        }

    }

    private class Einlesen implements Runnable {

        private File src;

        public Einlesen(String ssrc) {
            src = new File(ssrc);
        }

        public synchronized void run() {
            try {
                fileCount = 0;
                //src und prüfen
                if (src.isDirectory()) {
                    //Dateien zählen
                    fileCount = FileUtils.countFilesInDirectory(src);
                    notifyEvent(fileCount, 0, "");
                    einlesenGetFile(src);
                } else {
                    System.out.println("Quelle ist kein Verzeichnis!");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + "BildArchiv_.Einlesen.start");
            }
            EinlesenGetColor gColor;
            Thread t;
            treeSet.clear();
            for (int i = 0; i < anzThread; ++i) {
                ++threads;
                gColor = new EinlesenGetColor();
                t = new Thread(gColor);
                t.setDaemon(true);
                t.start();
            }
        }

        private void einlesenGetFile(File file) {
            File[] liste = null;
            if (file.isDirectory()) {
                liste = file.listFiles();
                for (int i = 0; i < liste.length; i++) {
                    if (liste[i].isFile()) {
                        if (checkSuffix(liste[i])) {
                            addFileEinlesen(liste[i]);
                        }
                    } else if (liste[i].isDirectory()) {
                        einlesenGetFile(liste[i]);
                    }
                }
            }

        }

        TreeSet<Thumb> treeSet = new TreeSet<>();

        private class EinlesenGetColor implements Runnable {


            public void run() {
                File file;
                while (!stopAll && (file = getFileEinlesen()) != null) {
                    Thumb thumb;
                    if ((thumb = ScaleImage.getThumb(file)) != null) {
//                        treeSet.add(thumb);
                        thumbCollection.getThumbList().add(thumb);
                    }

                    ++progress;
                    notifyEvent(fileCount, progress, "");
                }
                --threads;
                if (threads <= 0) {
//                    thumbCollection.getThumbList().addAll(treeSet);
                    thumbCollection.getThumbList().sort();
                    notifyEvent(0, 0, "");
                }
            }

        }

    }

    public boolean checkSuffix(File file) {
        boolean ret = false;
        if (file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1).equalsIgnoreCase(ProgConst.IMAGE_FORMAT_JPG) ||
                file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1).equalsIgnoreCase("jpeg") ||
                file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1).equalsIgnoreCase(ProgConst.IMAGE_FORMAT_PNG)) {
            ret = true;
        }
        return ret;
    }
}
