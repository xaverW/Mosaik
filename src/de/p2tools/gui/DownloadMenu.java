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

package de.p2tools.gui;

import de.p2tools.controller.config.Config;
import de.p2tools.controller.config.Daten;
import de.p2tools.controller.data.Icons;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

public class DownloadMenu {
    final private VBox vbox;
    final private Daten daten;
    private static final String UPDATE_DOWNLOADS_TEXT = "Liste der Downloads aktualisieren";
    private static final String START_ALL_DOWNLOADS_TEXT = "alle Downloads starten";
    private static final String REMOVE_DOWNLOADS_TEXT = "Downloads aus Liste entfernen";
    private static final String CLEANUP_DL_LIST_TEXT = "Liste der Downloads aufräumen";
    private static final String PUTBACK_DL_TEXT = "Downloads zurückstellen";
    BooleanProperty boolDivOn = Config.DOWNLOAD_GUI_FILTER_DIVIDER_ON.getBooleanProperty();
    BooleanProperty boolInfoOn = Config.DOWNLOAD_GUI_DIVIDER_ON.getBooleanProperty();

    public DownloadMenu(VBox vbox) {
        this.vbox = vbox;
        daten = Daten.getInstance();
    }


    public void init() {
        vbox.getChildren().clear();

        initMenu();
        initButton();
    }

    private void initButton() {
        // Button
        final ToolBarButton btDownloadRefresh =
                new ToolBarButton(vbox, UPDATE_DOWNLOADS_TEXT, UPDATE_DOWNLOADS_TEXT, new Icons().FX_ICON_TOOLBAR_DOWNLOAD_REFRESH);

        final ToolBarButton btDownloadAll = new ToolBarButton(vbox,
                START_ALL_DOWNLOADS_TEXT,
                START_ALL_DOWNLOADS_TEXT,
                new Icons().FX_ICON_TOOLBAR_DOWNLOAD_ALLE_STARTEN);

        final ToolBarButton btStartDownloads = new ToolBarButton(vbox,
                "Downloads Starten",
                "markierte Downloads starten",
                new Icons().FX_ICON_TOOLBAR_DOWNLOAD_STARTEN);

        final ToolBarButton btDownloadFilm = new ToolBarButton(vbox,
                "Film Starten",
                "gespeicherten Film abspielen",
                new Icons().FX_ICON_TOOLBAR_DOWNLOAD_FILM_START);

        final ToolBarButton btDownloadBack =
                new ToolBarButton(vbox, PUTBACK_DL_TEXT, PUTBACK_DL_TEXT, new Icons().FX_ICON_TOOLBAR_DOWNLOAD_UNDO);
        final ToolBarButton btDownloadDel =
                new ToolBarButton(vbox, REMOVE_DOWNLOADS_TEXT, REMOVE_DOWNLOADS_TEXT, new Icons().FX_ICON_TOOLBAR_DOWNLOAD_DEL);
        final ToolBarButton btDownloadClear =
                new ToolBarButton(vbox, CLEANUP_DL_LIST_TEXT, CLEANUP_DL_LIST_TEXT, new Icons().FX_ICON_TOOLBAR_DOWNLOAD_CLEAR);

    }

    private void initMenu() {

        // MenuButton
        final MenuButton mb = new MenuButton("");
        mb.setGraphic(new Icons().FX_ICON_TOOLBAR_MENUE);
        mb.getStyleClass().add("btnFunction");

        final MenuItem mbStartAll = new MenuItem("alle Downloads starten");
        final MenuItem mbStopAll = new MenuItem("alle Downloads stoppen");


        mb.getItems().addAll(mbStartAll, mbStopAll);
        vbox.getChildren().add(mb);

    }
}
