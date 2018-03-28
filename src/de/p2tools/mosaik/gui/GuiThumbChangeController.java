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

package de.p2tools.mosaik.gui;

import de.p2tools.mosaik.controller.RunEvent;
import de.p2tools.mosaik.controller.RunListener;
import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.controller.data.thumb.ThumbCollection;
import de.p2tools.mosaik.controller.worker.genThumbList.CreateThumbList;
import de.p2tools.mosaik.gui.tools.Table;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GuiThumbChangeController extends AnchorPane {
    VBox contPane = new VBox(10);

    ScrollPane scrollPaneTable = new ScrollPane();
    TableView table = new TableView<>();

    ThumbCollection thumbCollection = null;

    private final ProgData progData;

    public GuiThumbChangeController() {
        progData = ProgData.getInstance();


        AnchorPane.setLeftAnchor(contPane, 0.0);
        AnchorPane.setBottomAnchor(contPane, 0.0);
        AnchorPane.setRightAnchor(contPane, 0.0);
        AnchorPane.setTopAnchor(contPane, 0.0);

        getChildren().addAll(contPane);

        scrollPaneTable.setFitToHeight(true);
        scrollPaneTable.setFitToWidth(true);
        scrollPaneTable.setContent(table);

        contPane.getChildren().add(scrollPaneTable);
        VBox.setVgrow(scrollPaneTable, Priority.ALWAYS);

        initTable();
        selectThumbCollection();
        progData.worker.addAdListener(new RunListener() {
            @Override
            public void ping(RunEvent runEvent) {
                if (runEvent.nixLos() && runEvent.getSource().getClass().equals(CreateThumbList.class)) {
                    new Table().resetTable(table, Table.TABLE.CHANGE_THUMB);
                }
            }
        });
    }

    public void isShown() {
        selectThumbCollection();
    }


    public void saveTable() {
        new Table().saveTable(table, Table.TABLE.CHANGE_THUMB);
    }

    private void selectThumbCollection() {
        table.setItems(null);

        if (progData.selectedProjectData == null) {
            contPane.setDisable(true);
            return;
        }

        thumbCollection = progData.selectedProjectData.getThumbCollection();
        if (thumbCollection == null) {
            contPane.setDisable(true);
        } else {
            contPane.setDisable(false);
            table.setItems(thumbCollection.getThumbList());
        }
    }


    private void initTable() {
        new Table().setTable(table, Table.TABLE.CHANGE_THUMB);
    }

}
