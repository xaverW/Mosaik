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


package de.p2tools.mosaik.controller.worker;

import de.p2tools.mosaik.controller.RunEvent;
import de.p2tools.mosaik.controller.RunListener;
import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.controller.data.mosaikData.MosaikData;
import de.p2tools.mosaik.controller.data.thumb.ThumbCollection;
import de.p2tools.mosaik.controller.data.wallpaperData.WallpaperData;
import de.p2tools.mosaik.controller.worker.genMosaik.GenMosaik;
import de.p2tools.mosaik.controller.worker.genThumbList.GenThumbList;
import de.p2tools.mosaik.controller.worker.genWallpaper.GenWallpaper;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.tools.FileUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import javax.swing.event.EventListenerList;

public class Worker {
    private final ProgData progData;

    private final GenThumbList genThumbList;
    private final GenMosaik genMosaik;
    private final GenWallpaper genWallpaper;

    private EventListenerList listeners = new EventListenerList();
    private BooleanProperty working = new SimpleBooleanProperty(false);

    public Worker(ProgData progData) {
        this.progData = progData;
        genMosaik = new GenMosaik(progData);
        genThumbList = new GenThumbList(progData);
        genWallpaper = new GenWallpaper();

        genThumbList.addAdListener(new RunListener() {
            @Override
            public void ping(RunEvent runEvent) {
                notifyEvent(runEvent);
            }
        });
        genMosaik.addAdListener(new RunListener() {
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
        genThumbList.setStop();
        genWallpaper.setStop();
        genMosaik.setStop();
    }

    public void createThumbList(ThumbCollection thumbCollection, String thumbDir) {
        genThumbList.create(thumbCollection, thumbDir);
    }

    public void readThumbList(ThumbCollection thumbCollection, String thumbDir) {
        genThumbList.read(thumbCollection, thumbDir);
    }

    public void createMosaik(MosaikData mosaikData, ThumbCollection thumbCollection) {
        genMosaik.create(mosaikData, thumbCollection);
    }


    public void createWallpaper(ThumbCollection thumbCollection, WallpaperData wallpaperData) {
        genWallpaper.create(thumbCollection, wallpaperData);
    }

    public boolean moveProject(String destDir) {
        boolean ret = false;

        if (FileUtils.movePath(progData.selectedProjectData.getDestDir(), destDir)) {
            ret = true;
        } else {
            new PAlert().showErrorAlert("Projekt verschieben", "Das Verschieben des Verzeichnisses nach\n" +
                    destDir + "\n\n" +
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
