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
import de.p2tools.controller.data.Icons;
import de.p2tools.controller.data.thumb.Thumb;
import de.p2tools.controller.data.thumb.ThumbCollection;
import de.p2tools.controller.genFotoList.GenThumbList;
import de.p2tools.controller.genFotoList.ScaleImage;
import de.p2tools.gui.tools.MTOpen;
import de.p2tools.gui.tools.Table;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.controlsfx.control.table.TableRowExpanderColumn;

import java.io.File;

public class ChangeThumbGuiController extends AnchorPane {
    SplitPane splitPane = new SplitPane();
    ScrollPane scrollPane = new ScrollPane();
    TableView table = new TableView<>();
    AnchorPane contPane = new AnchorPane();
    AnchorPane collectPane = new AnchorPane();


    ThumbCollection thumbCollection = null;
    TextField txtName = new TextField("");
    Button btnReload = new Button("Liste neu einlesen");

    private final ProgData progData;
    DoubleProperty splitPaneProperty = Config.CHANGE_THUMB_GUI_DIVIDER.getDoubleProperty();

    public ChangeThumbGuiController() {
        progData = ProgData.getInstance();

        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        AnchorPane.setTopAnchor(splitPane, 0.0);
        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        getChildren().addAll(splitPane);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(table);
        SplitPane.setResizableWithParent(scrollPane, Boolean.FALSE);
        initTable();

        initCont();
        selectThumbCollection();

        VBox vBox = new VBox();
        vBox.setSpacing(10);

        vBox.getChildren().addAll(collectPane, contPane);
        splitPane.getItems().addAll(vBox, scrollPane);
        splitPane.getDividers().get(0).positionProperty().bindBidirectional(splitPaneProperty);

        initListener();
    }

    public void isShown() {
        if (!thumbCollection.equals(progData.selectedThumbCollection)) {
            selectThumbCollection();
        }
    }


    public void saveTable() {
        new Table().saveTable(table, Table.TABLE.THUMB);
    }

    private void initListener() {
    }


    private void initTable() {
        table.setTableMenuButtonVisible(true);
        table.setEditable(false);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        initTableColor(table);
    }


    private void selectThumbCollection() {
        table.setItems(null);

        if (thumbCollection != null) {
            txtName.setText("");
        }
        thumbCollection = progData.selectedThumbCollection;

        if (thumbCollection == null) {
            contPane.setDisable(true);
        } else {
            contPane.setDisable(false);
            txtName.setText(thumbCollection.getName());
            table.setItems(thumbCollection.getThumbList());
        }
    }

    private void initCont() {
        Label lblName = new Label("Name der Sammlung");

        txtName.setEditable(false);
        txtName.textProperty().addListener((observable, oldValue, newValue) -> progData.thumbCollectionList.setListChanged());

        btnReload.setOnAction(a -> {
            new GenThumbList(thumbCollection).read();
            table.refresh();
        });

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        vBox.getChildren().addAll(lblName, txtName, btnReload);

        AnchorPane.setTopAnchor(vBox, 5.0);
        AnchorPane.setLeftAnchor(vBox, 5.0);
        AnchorPane.setBottomAnchor(vBox, 5.0);
        AnchorPane.setRightAnchor(vBox, 5.0);
        vBox.setStyle("-fx-border-color: black;");
        contPane.getChildren().add(vBox);
    }

    private void initTableColor(TableView<Thumb> tableView) {

        final TableColumn<Thumb, Integer> nrColumn = new TableColumn<>("Nr");
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("nr"));

        final TableColumn<Thumb, String> imageColumn = new TableColumn<>("Foto");
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        imageColumn.setCellFactory(cellFactoryImage);

        final TableColumn<Thumb, Color> colorColumn = new TableColumn<>("Farbe");
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        colorColumn.setCellFactory(cellFactoryColor);

        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().addAll(expander, nrColumn, imageColumn, colorColumn);
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
//                setStyle("-fx-background-color: rgb(" + thumb.getRed() + "," + thumb.getGreen() + ", " + thumb.getBlue() + ");");
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
                        200, 200, true, true);
                ImageView imageview = new ImageView(i);
                setGraphic(imageview);
            }

        };

        return cell;
    };

    TableRowExpanderColumn<Thumb> expander = new TableRowExpanderColumn<>(param -> {
        final Thumb thumb = param.getValue();
        final GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: #E0E0E0;");

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setMinWidth(Control.USE_PREF_SIZE);
        gridPane.setMaxWidth(Double.MAX_VALUE);

        Label lblFile = new Label();
        lblFile.textProperty().bind(thumb.fileNameProperty());
        Button btnDel = new Button("Foto löschen");
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
            try {
                ScaleImage.printMetaData(new File(thumb.getFileName()));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
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
        gridPane.add(hBoxButton, 1, 1);

        return gridPane;
    });


}
