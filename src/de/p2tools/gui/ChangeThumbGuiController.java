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
import de.p2tools.controller.data.Icons;
import de.p2tools.controller.data.thumb.Thumb;
import de.p2tools.controller.data.thumb.ThumbCollection;
import de.p2tools.controller.worker.genThumbList.ScaleImage;
import de.p2tools.gui.dialog.MTAlert;
import de.p2tools.gui.tools.MTOpen;
import de.p2tools.gui.tools.Table;
import de.p2tools.mLib.tools.MLAlert;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.File;
import java.util.ArrayList;

public class ChangeThumbGuiController extends AnchorPane {
    ScrollPane scrollPaneTable = new ScrollPane();
    ScrollPane scrollPaneCont = new ScrollPane();
    TableView table = new TableView<>();
    AnchorPane contPane = new AnchorPane();

    ThumbCollection thumbCollection = null;
    Button btnReload = new Button("Liste neu einlesen");
    Button btnDel = new Button("Bilder löschen");

    private final ProgData progData;

    public ChangeThumbGuiController() {
        progData = ProgData.getInstance();

        VBox vBox = new VBox();
        AnchorPane.setLeftAnchor(vBox, 0.0);
        AnchorPane.setBottomAnchor(vBox, 0.0);
        AnchorPane.setRightAnchor(vBox, 0.0);
        AnchorPane.setTopAnchor(vBox, 0.0);
        getChildren().addAll(vBox);

        scrollPaneTable.setFitToHeight(true);
        scrollPaneTable.setFitToWidth(true);
        scrollPaneTable.setContent(table);
        scrollPaneCont.setFitToHeight(true);
        scrollPaneCont.setFitToWidth(true);
        scrollPaneCont.setContent(contPane);
        VBox.setVgrow(scrollPaneTable, Priority.ALWAYS);
        vBox.getChildren().addAll(scrollPaneTable, scrollPaneCont);

        initTable();
        initCont();
        initListener();
        selectThumbCollection();
    }

    public void isShown() {
        selectThumbCollection();
    }


    public void saveTable() {
        new Table().saveTable(table, Table.TABLE.THUMB);
    }

    private void initListener() {
    }


    private void selectThumbCollection() {
//        if (thumbCollection != null &&
//                progData.selectedProjectData.getThumbCollection() != null &&
//                thumbCollection.equals(progData.selectedProjectData.getThumbCollection())) {
//            return;
//        }

        table.setItems(null);

        thumbCollection = progData.selectedProjectData.getThumbCollection();

        if (thumbCollection == null) {
            contPane.setDisable(true);
        } else {
            contPane.setDisable(false);
            table.setItems(thumbCollection.getThumbList());
        }
    }

    private void initCont() {
        btnReload.setOnAction(a -> {
            progData.worker.readThumbList(thumbCollection);
            table.refresh();
        });

        btnDel.setOnAction(a -> {
            ArrayList<Thumb> thumbs = new ArrayList<>();
            thumbs.addAll(table.getSelectionModel().getSelectedItems());

            if (thumbs.isEmpty()) {
                new MLAlert().showInfoNoSelection();

            } else if (thumbs.size() == 1 &&
                    !new MTAlert().showAlert("Datei Löschen?", "", "Die Datei löschen:\n\n" + thumbs.get(0).getFileName())) {
                return;

            } else if (thumbs.size() > 1 &&
                    !new MTAlert().showAlert("Dateien Löschen?", thumbs.size() + " Dateien löschen",
                            "Sollen die Datei gelöscht werden?")) {
                return;
            }

            for (Thumb thumb : thumbs) {
                if (de.p2tools.mLib.tools.FileUtils.deleteFileNoMsg(thumb.getFileName())) {
                    thumbCollection.getThumbList().remove(thumb);
                } else {
                    break;
                }
            }

            table.refresh();

        });

        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(10));
        hBox.getChildren().addAll(btnReload, btnDel);

        AnchorPane.setTopAnchor(hBox, 5.0);
        AnchorPane.setLeftAnchor(hBox, 5.0);
        AnchorPane.setBottomAnchor(hBox, 5.0);
        AnchorPane.setRightAnchor(hBox, 5.0);
        contPane.getChildren().add(hBox);
    }

    private void initTable() {
        table.setTableMenuButtonVisible(true);
        table.setEditable(false);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

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

        table.getColumns().addAll(nrColumn, imageColumn, colorColumn, changeColumn);
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
                setStyle("-fx-background-color: rgb(" + thumb.getRed() + "," + thumb.getGreen() + ", " + thumb.getBlue() + ");");
//                setBackground(new Background(new BackgroundFill(thumb.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
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
                        150, 150, true, true);
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
                lblFile.textProperty().bind(thumb.fileNameProperty());
                Button btnDel = new Button("Bild löschen");
                btnDel.setOnAction(a -> {
                    if (de.p2tools.mLib.tools.FileUtils.deleteFile(thumb.getFileName())) {
                        thumbCollection.getThumbList().remove(thumb);
                    }
                });

                Button btnOpenDir = new Button("Ordner öffnen");
                btnOpenDir.setOnAction(a -> MTOpen.openDestDir(de.p2tools.mLib.tools.FileUtils.getPath(thumb.getFileName())));

                Button rotateLeft = new Button("");
                rotateLeft.setGraphic(new Icons().ICON_BUTTON_ROTATE_LEFT);
                Button rotateRight = new Button("");
                rotateRight.setGraphic(new Icons().ICON_BUTTON_ROTATE_RIGHT);
                rotateLeft.setOnAction(a -> {
                    ScaleImage.rotate(new File(thumb.getFileName()), false);
                    table.refresh();
                });
                rotateRight.setOnAction(a -> {
                    ScaleImage.rotate(new File(thumb.getFileName()), true);
                    table.refresh();
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
