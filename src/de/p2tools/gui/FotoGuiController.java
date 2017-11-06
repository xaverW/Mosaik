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
import de.p2tools.controller.data.fotos.FotoCollection;
import de.p2tools.gui.dialog.MTAlert;
import de.p2tools.gui.tools.Table;
import de.p2tools.mLib.tools.DirFileChooser;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class FotoGuiController extends AnchorPane {
    SplitPane splitPane = new SplitPane();
    ScrollPane scrollPane = new ScrollPane();
    TableView table = new TableView<>();
    AnchorPane contPane = new AnchorPane();
    AnchorPane collectPane = new AnchorPane();


    FotoCollection fotoCollection = null;
    ComboBox<FotoCollection> cbCollection = new ComboBox<>();
    TextField txtName = new TextField("");

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

        table.setItems(progData.fotoList);
        new Table().setTable(table, Table.TABLE.FILM);
    }

    private void initCollection() {
        cbCollection.setItems(progData.fotoCollectionList);
        cbCollection.getSelectionModel().selectFirst();
        final StringConverter<FotoCollection> converter = new StringConverter<FotoCollection>() {
            @Override
            public String toString(FotoCollection fc) {
                return fc == null ? "" : fc.getName();
            }

            @Override
            public FotoCollection fromString(String id) {
                final int i = cbCollection.getSelectionModel().getSelectedIndex();
                return progData.fotoCollectionList.get(i);
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
            FotoCollection fc = new FotoCollection("Neu-" + progData.fotoCollectionList.size());
            progData.fotoCollectionList.add(fc);
            cbCollection.getSelectionModel().select(fc);
        });

        Button btnDel = new Button("");
        btnDel.setGraphic(new Icons().ICON_BUTTON_REMOVE);
        btnDel.setOnAction(event -> {
            int i = cbCollection.getSelectionModel().getSelectedIndex();
            if (i < 0) {
                return;
            }
            FotoCollection fc = progData.fotoCollectionList.get(i);
            if (fc != null) {
                progData.fotoCollectionList.remove(fc);
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
        if (fotoCollection != null) {
            txtName.textProperty().unbindBidirectional(fotoCollection.nameProperty());
        }
        fotoCollection = cbCollection.getSelectionModel().getSelectedItem();

        if (fotoCollection == null) {
            contPane.setDisable(true);
        } else {
            contPane.setDisable(false);
            txtName.textProperty().bindBidirectional(fotoCollection.nameProperty());
        }
    }

    private void initCont() {
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        Label lblAdd = new Label("Ordner mit Fotos auswählen");
        TextField txtDir = new TextField("");
        txtDir.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(txtDir, Priority.ALWAYS);
        txtName.textProperty().addListener((observable, oldValue, newValue) -> progData.fotoCollectionList.setListChanged());

        final Button btnDir = new Button();
        btnDir.setOnAction(event -> {
            DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, txtDir);
        });
        btnDir.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        final Button btnHelp = new Button("");
        btnHelp.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelp.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.FILEMANAGER));

        hBox.getChildren().addAll(txtDir, btnDir, btnHelp);
        vBox.getChildren().addAll(txtName, lblAdd, hBox);

        AnchorPane.setTopAnchor(vBox, 0.0);
        AnchorPane.setLeftAnchor(vBox, 0.0);
        AnchorPane.setBottomAnchor(vBox, 0.0);
        AnchorPane.setRightAnchor(vBox, 0.0);
        contPane.getChildren().add(vBox);
    }
}
