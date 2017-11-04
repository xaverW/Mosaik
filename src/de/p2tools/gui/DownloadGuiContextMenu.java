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

import de.p2tools.controller.config.Daten;
import de.p2tools.controller.data.download.Download;
import de.p2tools.gui.tools.Table;
import javafx.scene.control.*;

public class DownloadGuiContextMenu {

    private final Daten daten;
    private final DownloadGuiController downloadGuiController;
    private final TableView tableView;

    public DownloadGuiContextMenu(Daten daten, DownloadGuiController downloadGuiController, TableView tableView) {

        this.daten = daten;
        this.downloadGuiController = downloadGuiController;
        this.tableView = tableView;

    }

    public ContextMenu getContextMenue(Download download) {
        final ContextMenu contextMenu = new ContextMenu();
        getMenu(contextMenu, download);
        return contextMenu;
    }

    private void getMenu(ContextMenu contextMenu, Download download) {

        MenuItem miStarten = new MenuItem("Download starten");

        MenuItem miStoppen = new MenuItem("Download stoppen");


        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> new Table().resetTable(tableView, Table.TABLE.DOWNLOAD));

        contextMenu.getItems().addAll(miStarten, miStoppen,
                new SeparatorMenuItem(),
                 resetTable);

    }

}
