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

package de.p2tools.mosaic.controller.worker.genMosaic;

import de.p2tools.mosaic.controller.RunListener;
import de.p2tools.mosaic.controller.config.ProgData;
import de.p2tools.mosaic.controller.data.mosaikData.MosaicData;
import de.p2tools.mosaic.controller.data.mosaikData.MosaicDataBase;
import de.p2tools.mosaic.controller.data.thumb.ThumbCollection;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.image.ImgFile;
import de.p2tools.p2Lib.tools.log.PLog;

import javax.swing.event.EventListenerList;
import java.io.File;

public class CreateMosaic {

    String dest;
    String src;
    private int anz = 5;
    private EventListenerList listeners = new EventListenerList();

    private ProgData progData;
    private CreateMosaicThread createMosaicThread = null;

    public CreateMosaic(ProgData progData) {
        this.progData = progData;
    }


    /**
     * @param listener
     */
    public void addAdListener(RunListener listener) {
        listeners.add(RunListener.class, listener);
    }

    public void setStop() {
        if (createMosaicThread != null) {
            createMosaicThread.setStop();
        }
    }

    /**
     * @param mosaicData
     */
    public void create(MosaicData mosaicData, ThumbCollection thumbCollection) {
        dest = mosaicData.getFotoDest();
        src = mosaicData.getFotoSrc();
        anz = mosaicData.getThumbCount();

        if (dest.isEmpty()) {
            PLog.errorLog(945120365, "Keine Zieldatei angegeben!");
            return;
        }

        if (mosaicData.getFormat().equals(ImgFile.IMAGE_FORMAT_PNG) &&
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
            PLog.errorLog(912030578, "Es gibt keine Miniaturbilder!");
            return;
        }


        MosaicDataBase.MOSAIC_TYPE mosaicType;
        String threadName;
        if (mosaicData.getMosaicType().equals(MosaicDataBase.MOSAIC_TYPE.THUMBS.toString())) {
            // Mosaik aus Thumbs
            mosaicType = MosaicDataBase.MOSAIC_TYPE.THUMBS;
            threadName = "MosaicThumb";

        } else if (mosaicData.getMosaicType().equals(MosaicDataBase.MOSAIC_TYPE.THUMBS_GRAY.toString())) {
            // Mosaik aus GayThumbs
            mosaicType = MosaicDataBase.MOSAIC_TYPE.THUMBS_GRAY;
            threadName = "MosaicGrayThumb";

        } else if (mosaicData.getMosaicType().equals(MosaicDataBase.MOSAIC_TYPE.THUMBS_COLORED.toString())) {
            // Mosaik aus ColoredThumbs
            mosaicType = MosaicDataBase.MOSAIC_TYPE.THUMBS_COLORED;
            threadName = "MosaicColoredThumb";

        } else if (mosaicData.getMosaicType().equals(MosaicDataBase.MOSAIC_TYPE.THUMBS_ALL_PIXEL_COLORED.toString())) {
            // Mosaik aus Thumbs und alles colored
            mosaicType = MosaicDataBase.MOSAIC_TYPE.THUMBS_ALL_PIXEL_COLORED;
            threadName = "MosaicAllPixelColored";

        } else {
            // Mosaik aus dem SRC-Image
            mosaicType = MosaicDataBase.MOSAIC_TYPE.FROM_SRC_IMG;
            threadName = "MosaicFromSrcImage";

        }

        createMosaicThread = new CreateMosaicThread(mosaicType, src, dest, thumbCollection, mosaicData, listeners);
        Thread thread = new Thread(createMosaicThread);
        thread.setName(threadName);
        thread.setDaemon(true);
        thread.start();
    }

}
