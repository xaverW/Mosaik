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
import de.p2tools.controller.config.ProgData;
import de.p2tools.controller.data.download.Download;
import de.p2tools.gui.tools.Table;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class DownloadGuiController extends AnchorPane {

    private final SplitPane splitPane = new SplitPane();
    private final TableView<Download> table = new TableView<>();

    private final TabPane tabPane = new TabPane();
    private final AnchorPane tabFilmInfo = new AnchorPane();
    private final AnchorPane tabBandwidth = new AnchorPane();
    private final AnchorPane tabDownloadInfos = new AnchorPane();
    private final ScrollPane scrollPane = new ScrollPane();

    private final ProgData progData;
    DoubleProperty splitPaneProperty = Config.DOWNLOAD_GUI_DIVIDER.getDoubleProperty();
    BooleanProperty boolInfoOn = Config.DOWNLOAD_GUI_DIVIDER_ON.getBooleanProperty();
    private boolean bound = false;

    public DownloadGuiController() {
        progData = ProgData.getInstance();


        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(table);

        Tab tabFilm = new Tab("Filminfo");
        tabFilm.setClosable(false);
        tabFilm.setContent(tabFilmInfo);

        Tab tabBand = new Tab("Bandbreite");
        tabBand.setClosable(false);
        tabBand.setContent(tabBandwidth);

        Tab tabDown = new Tab("DownloadInfos");
        tabDown.setClosable(false);
        tabDown.setContent(tabDownloadInfos);

        tabPane.getTabs().addAll(tabFilm, tabBand, tabDown);

        splitPane.setOrientation(Orientation.VERTICAL);
        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        AnchorPane.setTopAnchor(splitPane, 0.0);

        boolInfoOn.addListener((observable, oldValue, newValue) -> setSplit());
        setSplit();
        getChildren().addAll(splitPane);


        initTable();
    }


    public void isShown() {

    }


    public void invertSelection() {
        for (int i = 0; i < table.getItems().size(); ++i)
            if (table.getSelectionModel().isSelected(i)) {
                table.getSelectionModel().clearSelection(i);
            } else {
                table.getSelectionModel().select(i);
            }
    }

    public void saveTable() {
        new Table().saveTable(table, Table.TABLE.DOWNLOAD);
    }

    private void setSplit() {
        tabPane.setVisible(boolInfoOn.getValue());
        tabPane.setManaged(boolInfoOn.getValue());
        if (!boolInfoOn.getValue()) {

            if (bound) {
                splitPane.getDividers().get(0).positionProperty().unbindBidirectional(splitPaneProperty);
            }

            splitPane.getItems().clear();
            splitPane.getItems().add(scrollPane);

        } else {
            bound = true;
            splitPane.getItems().clear();
            splitPane.getItems().addAll(scrollPane, tabPane);
            splitPane.getDividers().get(0).positionProperty().bindBidirectional(splitPaneProperty);
        }
    }


    private void initTable() {
        table.setTableMenuButtonVisible(true);
        table.setEditable(false);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        new Table().setTable(table, Table.TABLE.DOWNLOAD);

    }

}
