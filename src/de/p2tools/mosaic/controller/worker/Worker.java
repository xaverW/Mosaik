/*
 * MTPlayer Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
 * http://zdfmediathk.sourceforge.net/
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */


package de.p2tools.mosaic.controller.worker;

import de.p2tools.mosaic.controller.RunEvent;
import de.p2tools.mosaic.controller.RunListener;
import de.p2tools.mosaic.controller.config.ProgData;
import de.p2tools.mosaic.controller.data.mosaikData.MosaicData;
import de.p2tools.mosaic.controller.data.mosaikData.WallpaperData;
import de.p2tools.mosaic.controller.data.thumb.ThumbCollection;
import de.p2tools.mosaic.controller.worker.genMosaic.CreateMosaic;
import de.p2tools.mosaic.controller.worker.genThumbList.CreateThumbList;
import de.p2tools.mosaic.controller.worker.genThumbList.ReadThumbList;
import de.p2tools.mosaic.controller.worker.genWallpaper.GenWallpaper;
import de.p2tools.p2Lib.PConst;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.tools.PFileUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import javax.swing.event.EventListenerList;

public class Worker {
    private final ProgData progData;

    private final CreateThumbList createThumbList;
    private final ReadThumbList readThumbList;
    private final CreateMosaic createMosaic;
    private final GenWallpaper genWallpaper;

    private EventListenerList listeners = new EventListenerList();
    private BooleanProperty working = new SimpleBooleanProperty(false);

    public Worker(ProgData progData) {
        this.progData = progData;
        createMosaic = new CreateMosaic(progData);
        createThumbList = new CreateThumbList(progData);
        readThumbList = new ReadThumbList(progData);
        genWallpaper = new GenWallpaper();

        createThumbList.addAdListener(new RunListener() {
            @Override
            public void ping(RunEvent runEvent) {
                notifyEvent(runEvent);
            }
        });
        readThumbList.addAdListener(new RunListener() {
            @Override
            public void ping(RunEvent runEvent) {
                notifyEvent(runEvent);
            }
        });
        createMosaic.addAdListener(new RunListener() {
            @Override
            public void ping(RunEvent runEvent) {
                notifyEvent(runEvent);
            }
        });
        genWallpaper.addAdListener(new RunListener() {
            @Override
            public void ping(RunEvent runEvent) {
                notifyEvent(runEvent);
            }
        });
    }

    public boolean isWorking() {
        return working.get();
    }

    public BooleanProperty workingProperty() {
        return working;
    }

    /**
     * @param listener
     */
    public void addAdListener(RunListener listener) {
        listeners.add(RunListener.class, listener);
    }

    public void setStop() {
        createThumbList.setStop();
        readThumbList.setStop();
        genWallpaper.setStop();
        createMosaic.setStop();
    }

    // thumblist
    public void createThumbList(ThumbCollection thumbCollection, String thumbDir) {
        createThumbList.create(thumbCollection, thumbDir);
    }

    public void readThumbList(ThumbCollection thumbCollection, String thumbDir) {
        readThumbList.read(thumbCollection, thumbDir);
    }


    // mosaic
    public void createMosaic(MosaicData mosaikData, ThumbCollection thumbCollection) {
        createMosaic.create(mosaikData, thumbCollection);
    }


    // wallpaper
    public void createWallpaper(ThumbCollection thumbCollection, WallpaperData wallpaperData) {
        genWallpaper.create(thumbCollection, wallpaperData);
    }

    public boolean moveProject(String destDir) {
        boolean ret = false;

        if (PFileUtils.movePath(progData.selectedProjectData.getDestDir(), destDir)) {
            ret = true;
        } else {
            new PAlert().showErrorAlert("Projekt verschieben", "Das Verschieben des Verzeichnisses nach " + PConst.LINE_SEPARATOR +
                    destDir + PConst.LINE_SEPARATORx2 +
                    "hat nicht geklappt.");
        }

        return ret;
    }

    private void notifyEvent(RunEvent runEvent) {
        working.setValue(!runEvent.nixLos());

        for (RunListener l : listeners.getListeners(RunListener.class)) {
            l.notify(runEvent);
        }
    }

}
