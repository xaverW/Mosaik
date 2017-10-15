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

package de.mtplayer.gui;

import de.mtplayer.controller.config.Config;
import de.mtplayer.controller.config.Daten;
import de.mtplayer.controller.data.Icons;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.VBox;

public class FilmMenu {
    final private VBox vbox;
    final private Daten daten;
    private static final String FILM_ABSPIELEN_TEXT = "Film abspielen";
    private static final String FILM_RECORD_TEXT = "Film aufzeichnen";
    BooleanProperty boolFilterOn = Config.FILM_GUI_FILTER_DIVIDER_ON.getBooleanProperty();
    BooleanProperty boolInfoOn = Config.FILM_GUI_DIVIDER_ON.getBooleanProperty();

    public FilmMenu(VBox vbox) {
        this.vbox = vbox;
        daten = Daten.getInstance();
    }


    public void init() {
        vbox.getChildren().clear();

        initFilmMenu();
        initButton();
    }

    private void initButton() {
        // Button
        final ToolBarButton btPlay =
                new ToolBarButton(vbox, "Abspielen", FILM_ABSPIELEN_TEXT, new Icons().FX_ICON_TOOLBAR_FILME_START);

        final ToolBarButton btSave =
                new ToolBarButton(vbox, "Speichern", FILM_RECORD_TEXT, new Icons().FX_ICON_TOOLBAR_FILME_REC);

    }

    private void initFilmMenu() {
        final MenuButton mb = new MenuButton("");
        mb.setGraphic(new Icons().FX_ICON_TOOLBAR_MENUE);
        mb.getStyleClass().add("btnFunction");

        final MenuItem mbPlay = new MenuItem("Film abspielen");
         final MenuItem mbSave = new MenuItem("Film aufzeichnen");

        mb.getItems().addAll(mbPlay, mbSave);
         vbox.getChildren().add(mb);
    }
}
