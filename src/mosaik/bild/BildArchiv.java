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

package mosaik.bild;

import java.io.IOException;
import mosaik.*;
import mosaik.daten.Daten;
import java.io.File;
import java.util.LinkedList;
import javax.swing.event.EventListenerList;
import mosaik.daten.Konstanten;

public class BildArchiv {

    private Daten daten;
    private EventListenerList listeners = new EventListenerList();
    private int progress = 0;
    private boolean stopAll = false;
    private int fileCount = 0;
    private LinkedList<File> fileListeEinlesen = new LinkedList<File>();
    private LinkedList<File[]> fileListeErstellen = new LinkedList<File[]>();
    private ScaleImage_ scaleImage;
    private File src;
    private File destDir;
    private boolean rekurs;
    private int anzThread = 1;
    private int threads = 0;
    private CountFile countFile;

    /**
     * 
     * @param ddaten
     */
    public BildArchiv(Daten ddaten) {
        daten = ddaten;
        scaleImage = new ScaleImage_(daten);
        anzThread = Runtime.getRuntime().availableProcessors();
        countFile = new CountFile();
//////        anzThread = 5;
    }
    /////////////////////////
    // public
    /////////////////////////
    /**
     * 
     */
    public void setStop() {
        stopAll = true;
    }

    /**
     * 
     * @param listener
     */
    public void addAdListener(BildListener listener) {
        listeners.add(BildListener.class, listener);
    }

    public void erstellen(String ssrc, String ddest, boolean rrekurs) {
        progress = 0;
        stopAll = false;
        fileListeErstellen.clear();
        Erstellen erst = new Erstellen(ssrc, ddest, rrekurs);
        Thread tErst = new Thread(erst);
        tErst.setDaemon(true);
        tErst.start();
    }

    public void einlesen(String ssrc) {
        progress = 0;
        stopAll = false;
        fileListeEinlesen.clear();
        Einlesen einl = new Einlesen(ssrc);
        Thread tErst = new Thread(einl);
        tErst.setDaemon(true);
        tErst.start();
    }

    public int count(File pfad) {
        fileCount = countFile.countFile(pfad);
        return fileCount;
    }

    ////////////////////////
    // private
    ////////////////////////
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

    private class Erstellen implements Runnable {

        public Erstellen(String ssrc, String ddestDir, boolean rrekurs) {
            src = new File(ssrc);
            destDir = new File(ddestDir);
            rekurs = rrekurs;
        }

        public synchronized void run() {
            try {
                fileCount = 0;
                //src und dest prüfen
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }
                if (src.isDirectory() && destDir.isDirectory()) {
                    //Dateien zählen
                    fileCount = countFile.countFile(src);
                    notifyEvent(fileCount, 0, "");
                    erstellenMakeThumb(src);
                } else {
                    daten.fehler.fehlermeldung("Quelle oder Ziel ist kein Verzeichnis!");
                }
                Thread t;
                for (int i = 0; i < anzThread; ++i) {
                    ++threads;
                    t = new Thread(new ErstellenMakeThumb());
                    t.setDaemon(true);
                    t.start();
                }
            } catch (Exception ex) {
                daten.fehler.fehlermeldung(ex, "BildArchiv.Erstellen.start");
            }
        }

        private void erstellenMakeThumb(File pfad) {
            File[] liste = null;
            String str = "";
            if (pfad.isDirectory()) {
                liste = pfad.listFiles();
                for (int i = 0; i < liste.length && !stopAll; i++) {
                    if (liste[i].isFile()) {
                        if (countFile.checkSuffix(liste[i])) {
                            try {
                                File dest = new File(destDir.getAbsolutePath() + File.separator + liste[i].getName() + "_" + daten.random.nextInt(
                                                     Integer.MAX_VALUE) + "." + daten.datenProjekt.arr[Konstanten.PROJEKT_ARCHIV_FORMAT_NR]);
                                str = dest.getAbsolutePath();
                                addFileErstellen(liste[i], dest);
                            } catch (Exception ex) {
                                daten.fehler.fehlermeldung(ex, "BildArchiv.makeThumb");
                                System.out.println("----------------------------------");
                                System.out.println("Fehler - Src: " + liste[i].getAbsolutePath());
                                System.out.println("Fehler - Dest: " + str);
                            }
                        }
                    } else if (liste[i].isDirectory() && rekurs) {
                        erstellenMakeThumb(liste[i]);
                    }
                }
            }
        }

        private class ErstellenMakeThumb implements Runnable {

            public void run() {
                File[] file;
                while (!stopAll && (file = getFileErstellen()) != null) {
                    erstellenThumb(file[0], file[1]);
                }
                --threads;
                if (threads <= 0) {
                    notifyEvent(0, 0, "");
                }
            }

            private void erstellenThumb(File fileSrc, File fileDest) {
                try {
                    ++progress;
                    notifyEvent(fileCount, progress, fileSrc.getName());
                    scaleImage.tus(fileSrc, fileDest);
                    GetColor_.getColor(daten, fileDest);
                } catch (IOException ex) {
                    daten.fehler.fehlermeldung(ex, "MakeThumb.thumb");
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
                    fileCount = countFile.countFile(src);
                    notifyEvent(fileCount, 0, "");
                    einlesenGetFile(src);
                } else {
                    daten.fehler.fehlermeldung("Quelle ist kein Verzeichnis!");
                }
            } catch (Exception ex) {
                daten.fehler.fehlermeldung(ex, "BildArchiv.Einlesen.start");
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
                        if (countFile.checkSuffix(liste[i])) {
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
                    GetColor_.getColor(daten, file);
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

}
