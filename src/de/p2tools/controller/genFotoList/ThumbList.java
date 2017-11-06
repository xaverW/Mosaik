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

package de.p2tools.controller.genFotoList;

import de.p2tools.controller.config.Const;
import de.p2tools.controller.config.ProgData;
import de.p2tools.mLib.tools.FileUtils;
import mosaik.BildEvent;
import mosaik.BildListener;
import mosaik.daten.Konstanten;

import javax.swing.event.EventListenerList;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class ThumbList {

    private ProgData progData;
    private EventListenerList listeners = new EventListenerList();
    private int progress = 0;
    private boolean stopAll = false;
    private int fileCount = 0;
    private LinkedList<File> fileListeEinlesen = new LinkedList<>();
    private LinkedList<File[]> fileListeErstellen = new LinkedList<>();
    private ScaleImage scaleImage;


    private int anzThread = 1;
    private int threads = 0;

    /**
     */
    public ThumbList() {
        progData = ProgData.getInstance();
        scaleImage = new ScaleImage();
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
    public void addAdListener(BildListener listener) {
        listeners.add(BildListener.class, listener);
    }

    public void create(String srcDir, String destDir, boolean rekursiv) {
        progress = 0;
        stopAll = false;
        fileListeErstellen.clear();
        CreateListOfThumbs erst = new CreateListOfThumbs(srcDir, destDir, rekursiv);
        Thread tErst = new Thread(erst);
        tErst.setDaemon(true);
        tErst.start();
    }

    public void read(String srcDir) {
        progress = 0;
        stopAll = false;
        fileListeEinlesen.clear();
        Einlesen einl = new Einlesen(srcDir);
        Thread tErst = new Thread(einl);
        tErst.setDaemon(true);
        tErst.start();
    }

    private void notifyEvent(int max, int progress, String text) {
        BildEvent event;
        event = new BildEvent(this, progress, max, text, threads);
        for (BildListener l : listeners.getListeners(BildListener.class)) {
            l.tus(event);
        }
    }

    private synchronized void addFileEinlesen(File file) {
        fileListeEinlesen.add(file);
    }

    private synchronized File getFileEinlesen() {
        return fileListeEinlesen.poll();
    }

    private synchronized void addFileErstellen(File file1, File file2) {
        fileListeErstellen.add(new File[]{file1, file2});
    }

    private synchronized File[] getFileErstellen() {
        return fileListeErstellen.poll();
    }

    private class CreateListOfThumbs implements Runnable {
        private File srcDir;
        private File destDir;
        private boolean rekursiv;

        public CreateListOfThumbs(String srcDir, String destDir, boolean rekursiv) {
            this.srcDir = new File(srcDir);
            this.destDir = new File(destDir);
            this.rekursiv = rekursiv;
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

                //Dateien zählen
                fileCount = FileUtils.countFilesInDirectory(srcDir);
                notifyEvent(fileCount, 0, "");
                createFileList(srcDir);
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
                                        liste[i].getName() + "_" + progData.random.nextInt(
                                        Integer.MAX_VALUE) + "." + Const.IMAGE_FORMAT_JPG);
                                str = dest.getAbsolutePath();
                                addFileErstellen(liste[i], dest);
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
                while (!stopAll && (file = getFileErstellen()) != null) {
                    create(file[0], file[1]);
                }
                --threads;
                if (threads <= 0) {
                    notifyEvent(0, 0, "");
                }
            }

            private void create(File fileSrc, File fileDest) {
                try {
                    ++progress;
                    notifyEvent(fileCount, progress, fileSrc.getName());
                    scaleImage.tus(fileSrc, fileDest);
                    GetColor.getColor(progData, fileDest);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage() + "MakeThumb.thumb");
                    System.out.println("----------------------------------");
                    System.out.println("Fehler - Src: " + fileSrc.getAbsolutePath());
                    System.out.println("Fehler - Dest: " + fileDest.getAbsolutePath());
                }
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
            } finally {
                try {
                } catch (Exception ex) {
                }
            }
            EinlesenGetColor gColor;
            Thread t;
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

        private class EinlesenGetColor implements Runnable {

            public void run() {
                File file;
                while (!stopAll && (file = getFileEinlesen()) != null) {
                    GetColor.getColor(progData, file);
                    ++progress;
                    notifyEvent(fileCount, progress, "");
                }
                --threads;
                if (threads <= 0) {
                    notifyEvent(0, 0, "");
                }
            }

        }

    }

    public boolean checkSuffix(File file) {
        boolean ret = false;
        if (file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1).equalsIgnoreCase(Konstanten.IMAGE_FORMAT_JPG) ||
                file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1).equalsIgnoreCase("jpeg") ||
                file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1).equalsIgnoreCase(Konstanten.IMAGE_FORMAT_PNG)) {
            ret = true;
        }
        return ret;
    }
}
