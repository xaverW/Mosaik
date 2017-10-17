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
import de.mtplayer.gui.tools.Table;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class FilmGuiController extends AnchorPane {
    SplitPane splitPane = new SplitPane();
    ScrollPane scrollPane = new ScrollPane();
    TableView table = new TableView<>();

    private final AnchorPane filmInfoPane = new AnchorPane();

    private final Daten daten;

    DoubleProperty splitPaneProperty = Config.FILM_GUI_DIVIDER.getDoubleProperty();
    BooleanProperty boolInfoOn = Config.FILM_GUI_DIVIDER_ON.getBooleanProperty();
    private boolean bound = false;

    public FilmGuiController() {
        daten = Daten.getInstance();

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(table);

        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        AnchorPane.setTopAnchor(splitPane, 0.0);
        splitPane.setOrientation(Orientation.VERTICAL);
        getChildren().addAll(splitPane);

        boolInfoOn.addListener((observable, oldValue, newValue) -> setSplit());
        setSplit();

        initTable();
        initListener();
    }

    public void isShown() {

    }

    private void setSplit() {
             splitPane.getItems().addAll(scrollPane, filmInfoPane);
        filmInfoPane.setVisible(boolInfoOn.getValue());
        filmInfoPane.setManaged(boolInfoOn.getValue());
        if (!boolInfoOn.getValue()) {

            if (bound) {
                splitPane.getDividers().get(0).positionProperty().unbindBidirectional(splitPaneProperty);
            }

            splitPane.getItems().clear();
            splitPane.getItems().add(scrollPane);

        } else {
            bound = true;
            splitPane.getDividers().get(0).positionProperty().bindBidirectional(splitPaneProperty);
        }
    }

    public void saveTable() {
        new Table().saveTable(table, Table.TABLE.FILM);
    }

    private void initListener() {
    }


    private void initTable() {
        table.setTableMenuButtonVisible(true);
        table.setEditable(false);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        new Table().setTable(table, Table.TABLE.FILM);

    }



}
