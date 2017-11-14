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

import de.p2tools.controller.config.ProgData;
import de.p2tools.gui.tools.Table;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;

public class FilmGuiContextMenu {

    private final ProgData progData;
    private final FotoGuiController fotoGuiController;
    private final TableView tableView;

    public FilmGuiContextMenu(ProgData progData, FotoGuiController fotoGuiController, TableView tableView) {
        this.progData = progData;
        this.fotoGuiController = fotoGuiController;
        this.tableView = tableView;
    }

    public ContextMenu getContextMenue() {
        final ContextMenu contextMenu = new ContextMenu();
        getMenu(contextMenu);
        return contextMenu;
    }

    private void getMenu(ContextMenu contextMenu) {

        // Start/Stop
        MenuItem miStart = new MenuItem("Film abspielen");
        MenuItem miSave = new MenuItem("Film speichern");

        contextMenu.getItems().addAll(miStart, miSave);


        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> new Table().resetTable(tableView, Table.TABLE.FILM));
        contextMenu.getItems().add(resetTable);
    }

}