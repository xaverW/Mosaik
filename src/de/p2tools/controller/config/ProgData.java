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
import de.p2tools.controller.data.download.DownloadList;
import de.p2tools.controller.data.thumb.ThumbCollectionList;
import de.p2tools.gui.DownloadGuiController;
import de.p2tools.gui.FotoGuiController;
import de.p2tools.gui.tools.Listener;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class ProgData {

    private static ProgData instance;
    public Random random = new Random();
    // flags
    public static boolean debug = false; // Debugmodus
    public static boolean reset = false; // Programm auf Starteinstellungen zurücksetzen

    // Infos
    public static String configDir; // Verzeichnis zum Speichern der Programmeinstellungen

    // zentrale Klassen
    public ThumbCollectionList thumbCollectionList;
    // Gui
    public Stage primaryStage = null;
    public MosaikController mosaikController = null;
    public FotoGuiController fotoGuiController = null; // Tab mit den Filmen
    public DownloadGuiController downloadGuiController = null; // Tab mit den Downloads

    // Programmdaten
    public DownloadList downloadList = null; // Filme die als "Download" geladen werden sollen


    private ProgData() {
        thumbCollectionList = new ThumbCollectionList();
        downloadList = new DownloadList(this);

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
