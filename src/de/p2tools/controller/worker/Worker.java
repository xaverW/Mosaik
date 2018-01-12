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


package de.p2tools.controller.worker;

import de.p2tools.controller.RunEvent;
import de.p2tools.controller.RunListener;
import de.p2tools.controller.config.ProgData;
import de.p2tools.controller.data.mosaikData.MosaikData;
import de.p2tools.controller.data.thumb.ThumbCollection;
import de.p2tools.controller.data.wallpaperData.WallpaperData;
import de.p2tools.controller.worker.genMosaik.GenMosaik;
import de.p2tools.controller.worker.genThumbList.GenThumbList;
import de.p2tools.controller.worker.genWallpaper.GenWallpaper;

import javax.swing.event.EventListenerList;

public class Worker {
    private final ProgData progData;

    private final GenThumbList genThumbList;
    private final GenMosaik genMosaik;
    private final GenWallpaper genWallpaper;

    private EventListenerList listeners = new EventListenerList();

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

    public void createThumbList(ThumbCollection thumbCollection) {
        genThumbList.create(thumbCollection);
    }

    public void readThumbList(ThumbCollection thumbCollection) {
        genThumbList.read(thumbCollection);
    }

    public void createMosaik(MosaikData mosaikData) {
        genMosaik.create(mosaikData);
    }

    public void createWallpaper(ThumbCollection thumbCollection, WallpaperData wallpaperData) {
        genWallpaper.create(thumbCollection, wallpaperData);
    }

    private void notifyEvent(RunEvent runEvent) {
        for (RunListener l : listeners.getListeners(RunListener.class)) {
            l.notify(runEvent);
        }
    }

}