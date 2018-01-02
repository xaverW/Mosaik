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

import de.p2tools.controller.config.ProgConfig;
import de.p2tools.controller.config.ProgData;
import de.p2tools.controller.data.Icons;
import de.p2tools.controller.data.thumb.Thumb;
import de.p2tools.controller.data.thumb.ThumbCollection;
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

public class StartGuiController extends AnchorPane {
    SplitPane splitPane = new SplitPane();
    ScrollPane scrollPane = new ScrollPane();
    TableView table = new TableView<>();
    AnchorPane contPane = new AnchorPane();
    AnchorPane collectPane = new AnchorPane();


    ThumbCollection thumbCollection = null;
    ComboBox<ThumbCollection> cbCollection = new ComboBox<>();
    TextField txtName = new TextField("");
    TextField txtDir = new TextField("");

    private final ProgData progData;
    DoubleProperty splitPaneProperty = ProgConfig.THUMB_GUI_DIVIDER.getDoubleProperty();

    public StartGuiController() {
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
        selectThumbCollection();

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

    private void initCollection() {
        cbCollection.setItems(progData.thumbCollectionList);

        try {
            String col = ProgConfig.THUMB_GUI_THUMB_COLLECTION.get();
            ThumbCollection thumbCollection = progData.thumbCollectionList.getThumbCollection(Integer.parseInt(col));
            cbCollection.getSelectionModel().select(thumbCollection);
        } catch (Exception ex) {
            cbCollection.getSelectionModel().selectFirst();
        }
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
        cbCollection.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                selectThumbCollection()
        );

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

        HBox hBox = new HBox();
        hBox.setStyle("-fx-border-color: black;");
        AnchorPane.setTopAnchor(hBox, 5.0);
        AnchorPane.setLeftAnchor(hBox, 5.0);
        AnchorPane.setBottomAnchor(hBox, 5.0);
        AnchorPane.setRightAnchor(hBox, 5.0);
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);
        HBox.setHgrow(cbCollection, Priority.ALWAYS);
        hBox.getChildren().addAll(cbCollection, btnNew, btnDel);
        collectPane.getChildren().add(hBox);
    }


    private void selectThumbCollection() {
        table.setItems(null);

        if (thumbCollection != null) {
            txtName.textProperty().unbindBidirectional(thumbCollection.nameProperty());
            txtDir.textProperty().unbindBidirectional(thumbCollection.fotoSrcDirProperty());
        }
        thumbCollection = cbCollection.getSelectionModel().getSelectedItem();
        progData.selectedThumbCollection = thumbCollection;
        String s = String.valueOf(thumbCollection.getId());

        ProgConfig.THUMB_GUI_THUMB_COLLECTION.setValue(thumbCollection.getId());


        if (thumbCollection == null) {
            contPane.setDisable(true);
        } else {
            contPane.setDisable(false);

            txtName.textProperty().bindBidirectional(thumbCollection.nameProperty());
            txtDir.textProperty().bindBidirectional(thumbCollection.fotoSrcDirProperty());

            table.setItems(thumbCollection.getThumbList());
        }
    }

    private void initCont() {
        Label lblName = new Label("Name der Sammlung");
        Label lblDir = new Label("Ordner mit Fotos auswählen");

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


        HBox hBoxDir = new HBox();
        hBoxDir.setSpacing(10);
        hBoxDir.getChildren().addAll(txtDir, btnDir, btnHelp);

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        vBox.getChildren().addAll(lblName, txtName, lblDir, hBoxDir);

        AnchorPane.setTopAnchor(vBox, 5.0);
        AnchorPane.setLeftAnchor(vBox, 5.0);
        AnchorPane.setBottomAnchor(vBox, 5.0);
        AnchorPane.setRightAnchor(vBox, 5.0);
        vBox.setStyle("-fx-border-color: black;");
        contPane.getChildren().add(vBox);
    }

    private void initTableColor(TableView<Thumb> tableView) {
        final TableColumn<Thumb, Color> colorColumn = new TableColumn<>("Farbe");
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        colorColumn.setCellFactory(cellFactoryColor);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().addAll(colorColumn);
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
