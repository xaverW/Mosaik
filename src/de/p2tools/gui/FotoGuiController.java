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
import de.p2tools.controller.config.ProgInfos;
import de.p2tools.controller.data.Icons;
import de.p2tools.controller.data.thumb.Thumb;
import de.p2tools.controller.data.thumb.ThumbCollection;
import de.p2tools.controller.genFotoList.GenThumbList;
import de.p2tools.gui.dialog.MTAlert;
import de.p2tools.gui.tools.Table;
import de.p2tools.mLib.tools.DirFileChooser;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class FotoGuiController extends AnchorPane {
    SplitPane splitPane = new SplitPane();
    ScrollPane scrollPane = new ScrollPane();
    TableView table = new TableView<>();
    AnchorPane contPane = new AnchorPane();
    AnchorPane collectPane = new AnchorPane();


    ThumbCollection thumbCollection = null;
    ComboBox<ThumbCollection> cbCollection = new ComboBox<>();
    TextField txtName = new TextField("");
    TextField txtDir = new TextField("");
    Button btnLod = new Button("Fotos hinzufügen");

    private final ProgData progData;
    DoubleProperty splitPaneProperty = Config.FILM_GUI_DIVIDER.getDoubleProperty();

    public FotoGuiController() {
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

        initCollection();
        initCont();
        setContPane();

        VBox vBox = new VBox();
        vBox.setSpacing(10);

        vBox.getChildren().addAll(collectPane, contPane);
        splitPane.getItems().addAll(vBox, scrollPane);
        splitPane.getDividers().get(0).positionProperty().bindBidirectional(splitPaneProperty);

        initListener();
    }

    public void isShown() {
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

        initTableColor(table);
    }

    private void initCollection() {
        cbCollection.setItems(progData.thumbCollectionList);
        cbCollection.getSelectionModel().selectFirst();
        final StringConverter<ThumbCollection> converter = new StringConverter<ThumbCollection>() {
            @Override
            public String toString(ThumbCollection fc) {
                return fc == null ? "" : fc.getName();
            }

            @Override
            public ThumbCollection fromString(String id) {
                final int i = cbCollection.getSelectionModel().getSelectedIndex();
                return progData.thumbCollectionList.get(i);
            }
        };
        cbCollection.setConverter(converter);
        cbCollection.setMaxWidth(Double.MAX_VALUE);
        cbCollection.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setContPane();
        });

        Button btnNew = new Button("");
        btnNew.setGraphic(new Icons().ICON_BUTTON_ADD);
        btnNew.setOnAction(event -> {
            ThumbCollection fc = new ThumbCollection("Neu-" + progData.thumbCollectionList.size());
            progData.thumbCollectionList.add(fc);
            cbCollection.getSelectionModel().select(fc);
        });

        Button btnDel = new Button("");
        btnDel.setGraphic(new Icons().ICON_BUTTON_REMOVE);
        btnDel.setOnAction(event -> {
            int i = cbCollection.getSelectionModel().getSelectedIndex();
            if (i < 0) {
                return;
            }
            ThumbCollection fc = progData.thumbCollectionList.get(i);
            if (fc != null) {
                progData.thumbCollectionList.remove(fc);
                cbCollection.getSelectionModel().selectFirst();
            }
        });

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);
        vBox.getChildren().add(cbCollection);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(btnNew, btnDel);
        vBox.getChildren().add(hBox);
        collectPane.getChildren().add(vBox);
    }


    private void setContPane() {
        if (thumbCollection != null) {
            txtName.textProperty().unbindBidirectional(thumbCollection.nameProperty());
            txtDir.textProperty().unbindBidirectional(thumbCollection.fotoSrcDirProperty());
            table.getItems().clear();
        }
        thumbCollection = cbCollection.getSelectionModel().getSelectedItem();

        if (thumbCollection == null) {
            contPane.setDisable(true);
            table.getItems().clear();
        } else {
            contPane.setDisable(false);
            txtName.textProperty().bindBidirectional(thumbCollection.nameProperty());
            txtDir.textProperty().bindBidirectional(thumbCollection.fotoSrcDirProperty());
            table.getItems().clear();
            table.setItems(thumbCollection.getThumbList());

        }
    }

    private void initCont() {
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));
        HBox hBox = new HBox();
        hBox.setSpacing(10);

        Label lblTitle = new Label("Ordner mit Fotos auswählen");

        txtDir.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(txtDir, Priority.ALWAYS);
        txtName.textProperty().addListener((observable, oldValue, newValue) -> progData.thumbCollectionList.setListChanged());

        final Button btnDir = new Button();
        btnDir.setOnAction(event -> {
            DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, txtDir);
        });
        btnDir.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        final Button btnHelp = new Button("");
        btnHelp.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelp.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.FILEMANAGER));

        btnLod.setOnAction(a -> {
            if (txtDir.getText().isEmpty()) {
                return;
            }
            // todo destDir ist leer
            String destDir = ProgInfos.getFotoCollectionsDirectory_String(thumbCollection.getName());
            thumbCollection.setThumbDir(destDir);

            new GenThumbList(thumbCollection).create(txtDir.getText(),
                    destDir,
                    true);
        });


        hBox.getChildren().addAll(txtDir, btnDir, btnHelp);
        vBox.getChildren().addAll(txtName, lblTitle, hBox, btnLod);

        AnchorPane.setTopAnchor(vBox, 0.0);
        AnchorPane.setLeftAnchor(vBox, 0.0);
        AnchorPane.setBottomAnchor(vBox, 0.0);
        AnchorPane.setRightAnchor(vBox, 0.0);
        contPane.getChildren().add(vBox);
    }

    private void initTableColor(TableView<Thumb> tableView) {

        final TableColumn<Thumb, String> nrColumn = new TableColumn<>("Nr");
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("text"));

        final TableColumn<Thumb, Color> colorColumn = new TableColumn<>("Farbe");
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        colorColumn.setCellFactory(cellFactoryColor);

        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        tableView.getColumns().addAll(nrColumn, colorColumn);
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
}
