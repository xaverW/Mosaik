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
import de.p2tools.controller.data.destData.ProjectData;
import de.p2tools.gui.dialog.MTAlert;
import de.p2tools.mLib.tools.DirFileChooser;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class StartGuiController extends AnchorPane {
    ScrollPane scrollPane = new ScrollPane();
    AnchorPane contPane = new AnchorPane();
    AnchorPane collectPane = new AnchorPane();


    ProjectData projectData = null;
    ComboBox<ProjectData> cbProjectDataList = new ComboBox<>();
    TextField txtName = new TextField("");
    TextField txtDir = new TextField("");

    private final ProgData progData;

    public StartGuiController() {
        progData = ProgData.getInstance();

        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        getChildren().addAll(scrollPane);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);


        initCollection();
        initCont();
        selectProjectData();

        VBox vBox = new VBox();
        vBox.setSpacing(10);

        vBox.getChildren().addAll(collectPane, contPane);
        scrollPane.setContent(vBox);

        initListener();
    }

    public void isShown() {
    }


    private void initListener() {
    }


    private void initCollection() {
        cbProjectDataList.setItems(progData.projectDataList);

        try {
            String col = ProgConfig.START_GUI_PROJECT_DATA.get();
            ProjectData projectData = progData.projectDataList.get(Integer.parseInt(col));
            cbProjectDataList.getSelectionModel().select(projectData);
        } catch (Exception ex) {
            cbProjectDataList.getSelectionModel().selectFirst();
        }

        final StringConverter<ProjectData> converter = new StringConverter<ProjectData>() {
            @Override
            public String toString(ProjectData pd) {
                return pd == null ? "" : pd.getName();
            }

            @Override
            public ProjectData fromString(String id) {
                final int i = cbProjectDataList.getSelectionModel().getSelectedIndex();
                return progData.projectDataList.get(i);
            }
        };

        cbProjectDataList.setConverter(converter);
        cbProjectDataList.setMaxWidth(Double.MAX_VALUE);
        cbProjectDataList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                selectProjectData()
        );

        Button btnNew = new Button("");
        btnNew.setGraphic(new Icons().ICON_BUTTON_ADD);
        btnNew.setOnAction(event -> {
            ProjectData pd = new ProjectData("Neu-" + progData.projectDataList.size());
            progData.projectDataList.add(pd);
            cbProjectDataList.getSelectionModel().select(pd);
        });

        Button btnDel = new Button("");
        btnDel.setGraphic(new Icons().ICON_BUTTON_REMOVE);
        btnDel.setOnAction(event -> {
            int i = cbProjectDataList.getSelectionModel().getSelectedIndex();
            if (i < 0) {
                return;
            }
            ProjectData pd = progData.projectDataList.get(i);
            if (pd != null) {
                progData.projectDataList.remove(pd);
                cbProjectDataList.getSelectionModel().selectFirst();
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
        HBox.setHgrow(cbProjectDataList, Priority.ALWAYS);
        hBox.getChildren().addAll(cbProjectDataList, btnNew, btnDel);
        collectPane.getChildren().add(hBox);
    }


    private void selectProjectData() {
        if (projectData != null) {
            txtName.textProperty().unbindBidirectional(projectData.nameProperty());
            txtDir.textProperty().unbindBidirectional(projectData.destDirProperty());
        }

        projectData = cbProjectDataList.getSelectionModel().getSelectedItem();
        progData.selectedProjectData = projectData;

        if (projectData == null) {
            contPane.setDisable(true);
        } else {
            contPane.setDisable(false);

            ProgConfig.START_GUI_PROJECT_DATA.setValue(cbProjectDataList.getSelectionModel().getSelectedIndex());
            txtName.textProperty().bindBidirectional(projectData.nameProperty());
            txtDir.textProperty().bindBidirectional(projectData.destDirProperty());
        }
    }

    private void initCont() {
        Label lblName = new Label("Name des Mosaik");
        Label lblDir = new Label("Ordner in dem das Mosaik erstellt wird");

        txtDir.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(txtDir, Priority.ALWAYS);
        txtName.textProperty().addListener((observable, oldValue, newValue) -> progData.projectDataList.setListChanged());

        final Button btnDir = new Button();
        btnDir.setOnAction(event -> {
            DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, txtDir);
        });
        btnDir.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        final Button btnHelp = new Button("");
        btnHelp.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelp.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.PROG_PATHS));


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

}
