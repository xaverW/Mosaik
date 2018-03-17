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

package de.p2tools.mosaik.gui.tools;

import de.p2tools.mosaik.controller.data.thumb.Thumb;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class TableThumb {

    private TableView tableView;

    public TableColumn[] initColumn(TableView tableView) {
        this.tableView = tableView;

        tableView.getColumns().clear();

        tableView.setTableMenuButtonVisible(false);
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        final TableColumn<Thumb, Integer> nrColumn = new TableColumn<>("Nr");
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("nr"));

        final TableColumn<Thumb, Color> colorColumn = new TableColumn<>("Farbe");
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        colorColumn.setCellFactory(cellFactoryColor);

        nrColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        colorColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);

        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                tableView.setContextMenu(getMenu());
            }
        });

        return new TableColumn[]{
                nrColumn, colorColumn
        };
    }

    private ContextMenu getMenu() {
        final ContextMenu contextMenu = new ContextMenu();

//        final ThumbCollection thumbCollection;
//        ProjectData projectData = ProgData.getInstance().selectedProjectData;
//        if (projectData != null) {
//            thumbCollection = projectData.getThumbCollection();
//        } else {
//            thumbCollection = null;
//        }
//
//
//        ArrayList<Thumb> thumbs = new ArrayList<>();
//        thumbs.addAll(tableView.getSelectionModel().getSelectedItems());
//
//        if (thumbCollection != null && !thumbs.isEmpty()) {
//            MenuItem delThumb = new MenuItem("Miniaturbilder löschen");
//            contextMenu.getItems().add(delThumb);
//
//            delThumb.setOnAction(a -> {
//                if (thumbs.size() == 1 &&
//                        !new PAlert().showAlert("Datei Löschen?", "", "Die Datei löschen:\n\n" + thumbs.get(0).getFileName())) {
//                    return;
//
//                } else if (thumbs.size() > 1 &&
//                        !new PAlert().showAlert("Dateien Löschen?", thumbs.size() + " Dateien löschen",
//                                "Sollen die Datei gelöscht werden?")) {
//                    return;
//                }
//
//                for (Thumb thumb : thumbs) {
//                    if (de.p2tools.p2Lib.tools.FileUtils.deleteFileNoMsg(thumb.getFileName())) {
//                        thumbCollection.getThumbList().remove(thumb);
//                    } else {
//                        break;
//                    }
//                }
//
//                tableView.refresh();
//
//            });
//        }


        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> new Table().resetTable(tableView, Table.TABLE.THUMB));
        contextMenu.getItems().addAll(resetTable);
        return contextMenu;
    }

    private Callback<TableColumn<Thumb, Color>, TableCell<Thumb, Color>> cellFactoryColor
            = (final TableColumn<Thumb, Color> param) -> {

        final TableCell<Thumb, Color> cell = new TableCell<Thumb, Color>() {


            @Override
            public void updateItem(Color item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                Thumb thumb = getTableView().getItems().get(getIndex());
                setBackground(new Background(new BackgroundFill(thumb.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
            }

        };

        return cell;
    };

}

