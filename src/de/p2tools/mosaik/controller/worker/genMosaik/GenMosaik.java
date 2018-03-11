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

package de.p2tools.mosaik.controller.worker.genMosaik;

import de.p2tools.mosaik.controller.RunListener;
import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.controller.data.mosaikData.MosaikData;
import de.p2tools.mosaik.controller.data.thumb.ThumbCollection;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.image.ImgFile;
import de.p2tools.p2Lib.tools.Log;

import javax.swing.event.EventListenerList;
import java.io.File;

public class GenMosaik {

    String dest;
    String src;
    private int anz = 5;
    private EventListenerList listeners = new EventListenerList();
    private int progress = 0;
    private boolean stopAll = false;

    private ProgData progData;
    private ThumbCollection thumbCollection;
    private MosaikThumb mosaikThumb = null;
    private MosaikBw mosaikBw = null;

    public GenMosaik(ProgData progData) {
        this.progData = progData;
    }


    /**
     * @param listener
     */
    public void addAdListener(RunListener listener) {
        listeners.add(RunListener.class, listener);
    }

    public void setStop() {
        stopAll = true;
        if (mosaikThumb != null) {
            mosaikThumb.setStop();
        }
        if (mosaikBw != null) {
            mosaikBw.setStop();
        }
    }

    /**
     * @param mosaikData
     */
    public void create(MosaikData mosaikData, ThumbCollection thumbCollection) {
        this.thumbCollection = thumbCollection;

        dest = mosaikData.getFotoDest();
        src = mosaikData.getFotoSrc();
        anz = mosaikData.getThumbCount();
        progress = 0;
        stopAll = false;

        if (dest.isEmpty()) {
            Log.errorLog(945120365, "Keine Zieldatei angegeben!");
            return;
        }

        if (mosaikData.getFormat().equals(ImgFile.IMAGE_FORMAT_PNG) &&
                !dest.endsWith("." + ImgFile.IMAGE_FORMAT_PNG)) {
            dest += "." + ImgFile.IMAGE_FORMAT_PNG;

        } else if (!dest.endsWith("." + ImgFile.IMAGE_FORMAT_JPG)) {
            dest += "." + ImgFile.IMAGE_FORMAT_JPG;
        }

        if (new File(dest).exists() &&
                !new PAlert().showAlert_yes_no("Ziel existiert", dest,
                        "Soll die bereits vorhandene Datei überschrieben werden?").equals(PAlert.BUTTON.YES)) {
            return;
        }

        int len = thumbCollection.getThumbList().getSize();
        if (len <= 0) {
            return;
        }

        if (mosaikData.getThumbSrc().equals(MosaikData.THUMB_SRC.THUMBS.toString())) {
            mosaikThumb = new MosaikThumb(src, dest, thumbCollection, mosaikData, listeners);
            Thread startenThread = new Thread(mosaikThumb);
            startenThread.setName("MosaikThumb");
            startenThread.setDaemon(true);
            startenThread.start();
        } else {
            mosaikBw = new MosaikBw(src, dest, thumbCollection, mosaikData, listeners);
            Thread startenThread = new Thread(mosaikBw);
            startenThread.setName("MosaikBw");
            startenThread.setDaemon(true);
            startenThread.start();
        }

    }

}
