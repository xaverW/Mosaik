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


package de.mtplayer.controller.config;

import de.mtplayer.MTFxController;
import de.mtplayer.controller.data.*;
import de.mtplayer.controller.data.abo.AboList;
import de.mtplayer.controller.data.download.DownloadList;
import de.mtplayer.controller.data.film.FilmList;
import de.mtplayer.controller.loadFilmlist.LoadFilmList;
import de.mtplayer.controller.starter.StarterClass;
import de.mtplayer.gui.AboGuiController;
import de.mtplayer.gui.DownloadGuiController;
import de.mtplayer.gui.FilmGuiController;
import de.mtplayer.gui.dialog.FilmInfosDialogController;
import de.mtplayer.gui.mediaDb.MediaDbList;
import de.mtplayer.gui.tools.Listener;
import de.mtplayer.tools.filmListFilter.FilmListFilter;
import de.mtplayer.tools.storedFilter.StoredFilter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Daten {

    private static Daten instance;

    // flags
    public static boolean debug = false; // Debugmodus
    public static boolean reset = false; // Programm auf Starteinstellungen zurücksetzen

    // Infos
    public static String configDir; // Verzeichnis zum Speichern der Programmeinstellungen
    public NamesList namesList = null; // Liste aller Sender, Themen, ...

    // zentrale Klassen
    public StarterClass starterClass = null; // Klasse zum Ausführen der Programme (für die Downloads): VLC, flvstreamer, ...
    public LoadFilmList loadFilmList; // erledigt das updaten der Filmliste
    public static final MTColor mTColor = new MTColor(); // verwendete Farben
    public StoredFilter storedFilter = null; // gespeicherte Filterprofile
    public FilmListFilter filmListFilter = null;

    // Gui
    public Stage primaryStage = null;
    public MTFxController mtFxController = null;
    public FilmGuiController filmGuiController = null; // Tab mit den Filmen
    public DownloadGuiController downloadGuiController = null; // Tab mit den Downloads
    public AboGuiController aboGuiController = null; // Tab mit den Abos
//    public MsgGuiController msgGuiController = null; // Tab mit den Abos

    public FilmInfosDialogController filmInfosDialogController = null;

    // Programmdaten
    public FilmList filmList = null; // ist die komplette Filmliste
    public FilmList filmListFiltered = null; // Filmliste, wie im TabFilme angezeigt
    public DownloadList downloadList = null; // Filme die als "Download" geladen werden sollen
    public DownloadList downloadListButton = null; // Filme die über "Tab Filme" als Button/Film abspielen gestartet werden
    public AboList aboList = null;
    public BlackList blackList = null;
    public static SetList setList = null;
    public MediaDbList mediaDbList = null;
    public MediaPathList mediaPathList = null;
    public HistoryList history = null; // alle angesehenen Filme
    public HistoryList erledigteAbos = null; // erfolgreich geladenen Abos
    public ReplaceList replaceList = null;


    private Daten() {
        replaceList = new ReplaceList();
        storedFilter = new StoredFilter(this);
        filmList = new FilmList();
        loadFilmList = new LoadFilmList(this);

        filmListFiltered = new FilmList();
        blackList = new BlackList(this);

        setList = new SetList();

        aboList = new AboList(this);

        downloadList = new DownloadList(this);
        downloadListButton = new DownloadList(this);

        filmListFilter = new FilmListFilter(this);

        erledigteAbos = new HistoryList(Const.FILE_ERLEDIGTE_ABOS,
                ProgInfos.getSettingsDirectory_String());

        history = new HistoryList(Const.FILE_HISTORY,
                ProgInfos.getSettingsDirectory_String());

        mediaDbList = new MediaDbList(this);
        mediaPathList = new MediaPathList();

        starterClass = new StarterClass(this);

        namesList = new NamesList(this);


        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000), ae -> {
            downloadList.makeDownloadInfos();
            Listener.notify(Listener.EREIGNIS_TIMER, Daten.class.getName());
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setDelay(Duration.seconds(5));
        timeline.play();
    }

    public static final Daten getInstance(String dir) {
        configDir = dir;
        return getInstance();
    }

    public static final Daten getInstance() {
        return instance == null ? instance = new Daten() : instance;
    }


    public void initDialogs() {
        filmInfosDialogController = new FilmInfosDialogController(this);
    }


}
