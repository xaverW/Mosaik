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
import de.p2tools.p2Lib.tools.FileUtils;
import de.p2tools.p2Lib.tools.Log;

import javax.swing.event.EventListenerList;
import java.io.File;
import java.util.LinkedList;
import java.util.TreeSet;

public class ReadThumbList {

    private ProgData progData;
    private EventListenerList listeners = new EventListenerList();
    private boolean stopAll = false;
    private LinkedList<File> fileListRead = new LinkedList<>();

    private int anzThread = 1;
    private int threads = 0;

    /**
     */
    public ReadThumbList(ProgData progData) {
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

    public void read(ThumbCollection thumbCollection, String destDir) {
        stopAll = false;
        fileListRead.clear(); // die Liste wird komplett neu eingelesen
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

    private class ReadListOfThumbs implements Runnable {

        private ThumbDataList tmpList = new ThumbDataList();
        private File fileThumbDir;
        private ThumbCollection thumbCollection;
        private int maxFile = 0;
        private int progressFile = 0;

        public ReadListOfThumbs(ThumbCollection thumbCollection, String thumbDir) {
            this.thumbCollection = thumbCollection;
            fileThumbDir = new File(thumbDir);

            Log.sysLog("ReadListOfThumbs");
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

        private void createFileList(File file) {
            File[] liste;
            if (file.isDirectory()) {
                liste = file.listFiles();
                for (int i = 0; i < liste.length; i++) {
                    if (liste[i].isFile()) {
                        if (CreateThumbList.checkSuffix(liste[i])) {
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
                        tmpList.add(thumb);
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

}
