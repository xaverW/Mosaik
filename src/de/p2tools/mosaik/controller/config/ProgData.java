/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://sourceforge.net/projects/mtplayer/
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


package de.p2tools.mosaik.controller.config;

import de.p2tools.mosaik.MosaikController;
import de.p2tools.mosaik.controller.data.projectData.ProjectData;
import de.p2tools.mosaik.controller.data.projectData.ProjectDataDataList;
import de.p2tools.mosaik.controller.worker.Worker;
import de.p2tools.mosaik.gui.*;
import de.p2tools.mosaik.gui.tools.Listener;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ProgData {

    private static ProgData instance;

    // flags
    public static boolean debug = false; // Debugmodus
    public static boolean saveMem = false; // es wird Speicher gespart, Ablauf dadurch langsamer

    // Infos
    public static String configDir; // Verzeichnis zum Speichern der Programmeinstellungen

    // zentrale Klassen
    public Worker worker = null;
    public ProjectDataDataList projectDataList = null;
    public ProjectData selectedProjectData = null;


    // Gui
    public Stage primaryStage = null;
    public MosaikController mosaikController = null;

    public GuiStart guiStart = null; // StartTab
    public GuiThumb guiThumb = null; // ThumbTab
    public GuiMosaik guiMosaik = null; // MosaikTab

    public GuiThumbController guiThumbController = null;
    public GuiThumbChangeController guiThumbChangeController = null;

    public GuiMosaikController guiMosaikController = null;
    public GuiWallpaperController guiWallpaperController = null;


    private ProgData() {
        projectDataList = new ProjectDataDataList();
        worker = new Worker(this);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000), ae -> {
            Listener.notify(Listener.EREIGNIS_TIMER, ProgData.class.getName());
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setDelay(Duration.seconds(5));
        timeline.play();
    }

    public static final ProgData getInstance(String dir) {
        configDir = dir;
        return getInstance();
    }

    public static final ProgData getInstance() {
        return instance == null ? instance = new ProgData() : instance;
    }


}
