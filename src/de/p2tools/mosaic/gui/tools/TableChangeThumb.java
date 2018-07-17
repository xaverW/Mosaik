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

package de.p2tools.mosaic.gui.tools;

import de.p2tools.mosaic.controller.config.ProgData;
import de.p2tools.mosaic.controller.data.Icons;
import de.p2tools.mosaic.controller.data.projectData.ProjectData;
import de.p2tools.mosaic.controller.data.thumb.Thumb;
import de.p2tools.mosaic.controller.data.thumb.ThumbCollection;
import de.p2tools.mosaic.controller.worker.genThumbList.ScaleImage;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.guiTools.POpen;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.File;
import java.util.ArrayList;

public class TableChangeThumb {

    private TableView tableView;

    public TableColumn[] initColumn(TableView tableView) {
        this.tableView = tableView;

        tableView.getColumns().clear();

        tableView.setTableMenuButtonVisible(true);
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        final TableColumn<Thumb, Integer> nrColumn = new TableColumn<>("Nr");
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("nr"));

        final TableColumn<Thumb, String> imageColumn = new TableColumn<>("Foto");
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        imageColumn.setCellFactory(cellFactoryImage);

        final TableColumn<Thumb, Color> colorColumn = new TableColumn<>("Farbe");
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        colorColumn.setCellFactory(cellFactoryColor);

        final TableColumn<Thumb, String> changeColumn = new TableColumn<>("Bearbeiten");
        changeColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        changeColumn.setCellFactory(cellFactoryChange);

        nrColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        imageColumn.setMaxWidth(1f * Integer.MAX_VALUE * 30);
        colorColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        changeColumn.setMaxWidth(1f * Integer.MAX_VALUE * 50);

        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                tableView.setContextMenu(getMenu());
            }
        });

        return new TableColumn[]{
                nrColumn, imageColumn, colorColumn, changeColumn
        };
    }

    private ContextMenu getMenu() {
        final ContextMenu contextMenu = new ContextMenu();

        final ThumbCollection thumbCollection;
        ProjectData projectData = ProgData.getInstance().selectedProjectData;
        if (projectData != null) {
            thumbCollection = projectData.getThumbCollection();
        } else {
            thumbCollection = null;
        }


        ArrayList<Thumb> thumbs = new ArrayList<>();
        thumbs.addAll(tableView.getSelectionModel().getSelectedItems());

        if (thumbCollection != null && !thumbs.isEmpty()) {
            MenuItem delThumb = new MenuItem("Miniaturbilder löschen");
            contextMenu.getItems().add(delThumb);

            delThumb.setOnAction(a -> {
                if (thumbs.size() == 1 &&
                        !new PAlert().showAlert("Datei Löschen?", "", "Die Datei löschen:\n\n" + thumbs.get(0).getFileName())) {
                    return;

                } else if (thumbs.size() > 1 &&
                        !new PAlert().showAlert("Dateien Löschen?", thumbs.size() + " Dateien löschen",
                                "Sollen die Datei gelöscht werden?")) {
                    return;
                }

                for (Thumb thumb : thumbs) {
                    if (de.p2tools.p2Lib.tools.FileUtils.deleteFileNoMsg(thumb.getFileName())) {
                        thumbCollection.getThumbList().remove(thumb);
                    } else {
                        break;
                    }
                }

                tableView.refresh();

            });
        }


        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> new Table().resetTable(tableView, Table.TABLE.CHANGE_THUMB));
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

    private Callback<TableColumn<Thumb, String>, TableCell<Thumb, String>> cellFactoryImage
            = (final TableColumn<Thumb, String> param) -> {

        final TableCell<Thumb, String> cell = new TableCell<Thumb, String>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                Image i = new Image(new File(item).toURI().toString(),
                        100, 100, true, true);
                ImageView imageview = new ImageView(i);
                setGraphic(imageview);
            }

        };

        return cell;
    };

    private Callback<TableColumn<Thumb, String>, TableCell<Thumb, String>> cellFactoryChange
            = (final TableColumn<Thumb, String> param) -> {

        final TableCell<Thumb, String> cell = new TableCell<Thumb, String>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                Thumb thumb = getTableView().getItems().get(getIndex());
                final GridPane gridPane = new GridPane();
                gridPane.setStyle("-fx-background-color: #E0E0E0;");

                gridPane.setHgap(10);
                gridPane.setVgap(10);
                gridPane.setPadding(new Insets(20, 20, 20, 20));
                gridPane.setMinWidth(Control.USE_PREF_SIZE);
                gridPane.setMaxWidth(Double.MAX_VALUE);

                Label lblFile = new Label();
                lblFile.setText(thumb.getFileName());
                Button btnDel = new Button("Bild löschen");
                btnDel.setOnAction(a -> {
                    if (de.p2tools.p2Lib.tools.FileUtils.deleteFile(thumb.getFileName())) {
                        ProgData.getInstance().selectedProjectData.getThumbCollection().getThumbList().remove(thumb);
                    }
                });

                Button btnOpenDir = new Button("Ordner öffnen");
                btnOpenDir.setOnAction(a -> POpen.openDir(de.p2tools.p2Lib.tools.FileUtils.getPath(thumb.getFileName())));

                // todo
                // erst wenns einen Config-Dialog gibt
                // btnOpenDir.setOnAction(a -> POpen.openDir(de.p2tools.p2Lib.tools.FileUtils.getPath(thumb.getFileName()),
                //      ProgConfig.SYSTEM_PROG_OPEN_DIR, new Icons().ICON_BUTTON_FILE_OPEN));

                Button rotateLeft = new Button("");
                rotateLeft.setGraphic(new Icons().ICON_BUTTON_ROTATE_LEFT);
                Button rotateRight = new Button("");
                rotateRight.setGraphic(new Icons().ICON_BUTTON_ROTATE_RIGHT);
                rotateLeft.setOnAction(a -> {
                    ScaleImage.rotate(new File(thumb.getFileName()), false);
                    tableView.refresh();
                });
                rotateRight.setOnAction(a -> {
                    ScaleImage.rotate(new File(thumb.getFileName()), true);
                    tableView.refresh();
                });

                gridPane.add(new Label("Datei:"), 0, 0);
                gridPane.add(lblFile, 1, 0);

                HBox hBoxButton = new HBox(10);
                hBoxButton.getChildren().addAll(btnDel, btnOpenDir, rotateLeft, rotateRight);
                gridPane.add(hBoxButton, 0, 1, 2, 1);


                setGraphic(gridPane);
            }

        };

        return cell;
    };
}

