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


package de.p2tools.controller.config;

import de.p2tools.MosaikController;
import de.p2tools.controller.data.destData.ProjectData;
import de.p2tools.controller.data.destData.ProjectDataList;
import de.p2tools.controller.genMosaik.GenMosaik;
import de.p2tools.controller.genThumbList.GenThumbList;
import de.p2tools.gui.*;
import de.p2tools.gui.tools.Listener;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ProgData {

    private static ProgData instance;

    // flags
    public static boolean debug = false; // Debugmodus
    public static BooleanProperty stopProp = new SimpleBooleanProperty();

    // Infos
    public static String configDir; // Verzeichnis zum Speichern der Programmeinstellungen

    // zentrale Klassen
    public ProjectDataList projectDataList = null;
    public ProjectData selectedProjectData = null;

    public GenThumbList genThumbList = null;
    public GenMosaik genMosaik = null;

    // Gui
    public Stage primaryStage = null;
    public MosaikController mosaikController = null;

    public StartGuiController startGuiController = null; // StartTab
    public ThumbGuiController thumbGuiController = null; // Tab mit den Filmen
    public ChangeThumbGuiController changeThumbGuiController = null; // Tab mit den Filmen
    public MosaikGuiController mosaikGuiController = null; // Tab mit den Downloads
    public WallpaperGuiController wallpaperGuiController = null; // Tab mit den Downloads


    private ProgData() {
        projectDataList = new ProjectDataList();
        genThumbList = new GenThumbList(this);
        genMosaik = new GenMosaik(this);

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
