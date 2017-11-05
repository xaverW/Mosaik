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

package de.p2tools.gui.tools;

import de.p2tools.controller.config.ProgData;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TableFilm {

    private final ProgData progData;

    public TableFilm(ProgData progData) {
        this.progData = progData;
    }

    public TableColumn[] initFilmColumn(TableView table) {
        table.getColumns().clear();

        final TableColumn nrColumn = new TableColumn<>("Nr");

        return new TableColumn[]{
                nrColumn
        };

    }


}
