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
import de.p2tools.controller.data.projectData.ProjectData;
import de.p2tools.controller.data.thumb.ThumbCollection;
import de.p2tools.gui.dialog.MTAlert;
import de.p2tools.p2Lib.tools.DirFileChooser;
import de.p2tools.p2Lib.tools.PAlert;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.nio.file.Path;
import java.nio.file.Paths;

public class GuiStart extends AnchorPane {

    private final ScrollPane scrollPane = new ScrollPane();
    private final TitledPane contPane = new TitledPane();
    private final TitledPane projectDataPane = new TitledPane();
    private final ComboBox<ProjectData> cbProjectDataList = new ComboBox<>();
    private final TextField txtName = new TextField("");
    private final TextField txtDir = new TextField("");
    Button btnNew = new Button("Neues Mosaik erstellen");
    Button btnDel = new Button("gewähltes Mosaik löschen");

    private ProjectData projectData = null;
    private final ProgData progData;
    private final BooleanProperty allOk = new SimpleBooleanProperty(false);

    public GuiStart() {
        progData = ProgData.getInstance();
        getStyleClass().add("layoutBackground");

        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        getChildren().addAll(scrollPane);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        projectDataPane.setCollapsible(false);
        projectDataPane.setAlignment(Pos.CENTER);
        projectDataPane.getStyleClass().add("contPane");
        projectDataPane.setText("Mosaik auswählen");

        contPane.setCollapsible(false);
        contPane.setAlignment(Pos.CENTER);
        contPane.getStyleClass().add("contPane");
        contPane.setText("Einstellungen des gewählten Mosaik");

        initCollection();
        initCont();
        selectProjectData();

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(projectDataPane, contPane);
        scrollPane.setContent(vBox);

        initColor();
    }

    public void isShown() {
    }

    public boolean isAllOk() {
        return allOk.get();
    }

    public BooleanProperty allOkProperty() {
        return allOk;
    }

    private void initColor() {
        BooleanBinding dirBinding = Bindings.createBooleanBinding(() -> txtDir.getText().trim().isEmpty(), txtDir.textProperty());
        BooleanBinding nameBinding = Bindings.createBooleanBinding(() -> txtName.getText().trim().isEmpty(), txtName.textProperty());
        allOk.bind(dirBinding.not().and(nameBinding.not()));

        setColor(txtDir, dirBinding);
        setColor(txtName, nameBinding);

        dirBinding.addListener(l -> setColor(txtDir, dirBinding));
        nameBinding.addListener(l -> setColor(txtName, nameBinding));
    }

    private void setColor(Control tf, BooleanBinding bb) {
        if (bb.get()) {
            tf.getStyleClass().add("txtIsEmpty");
        } else {
            tf.getStyleClass().remove("txtIsEmpty");
        }

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
        cbProjectDataList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    selectProjectData();
                    btnDel.setDisable(projectData == null ? true : false);
                }
        );

        btnNew.setGraphic(new Icons().ICON_BUTTON_ADD);
        btnNew.setOnAction(event -> {
            ProjectData pd = new ProjectData();
            progData.projectDataList.add(pd);

            cbProjectDataList.getSelectionModel().select(pd);
        });

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

        VBox vBox = new VBox(10);
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(btnNew, btnDel);

        vBox.getChildren().addAll(cbProjectDataList, hBox);
        projectDataPane.setContent(vBox);
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
            txtDir.setText("");
            txtName.setText("");
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
        txtDir.setEditable(false);
        HBox.setHgrow(txtDir, Priority.ALWAYS);
        txtName.textProperty().addListener((observable, oldValue, newValue) -> progData.projectDataList.setListChanged());

        final Button btnDir = new Button();
        btnDir.setOnAction(event -> {
            String dir = DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, txtDir.getText());
            if (dir.isEmpty()) {
                return;
            }

            if (progData.selectedProjectData.getDestDir().isEmpty()) {
                // dann ist das Projektverzeichnis noch nicht angelegt
                progData.selectedProjectData.setDestDir(dir);
                return;
            } else {
                String oldDir = progData.selectedProjectData.getDestDir();
                Path oldDirPath = Paths.get(oldDir);
                if (!oldDirPath.toFile().exists()) {
                    // dann ist das Projektverzeichnis noch nicht angelegt
                    progData.selectedProjectData.setDestDir(dir);
                    return;
                }
            }


            if (new MTAlert().showAlert_yes_no("Pfad ändern", "Projekt verschieben?", "Soll das Projekt von:\n" +
                    txtDir.getText() + "\n\n" +
                    "nach:\n" +
                    dir + "\n\n" +
                    "verschoben werden?").equals(PAlert.BUTTON.YES)) {
                if (progData.worker.moveProject(dir)) {
                    txtDir.setText(dir);
                }

                ThumbCollection thumbCollection = progData.selectedProjectData.getThumbCollection();
                if (thumbCollection == null) {
                    return;
                }
                String thumbDir = progData.selectedProjectData.getThumbDirString();
                if (thumbDir.isEmpty()) {
                    return;
                }
                progData.worker.readThumbList(thumbCollection, thumbDir);

            }
        });
        btnDir.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        final Button btnHelp = new Button("");
        btnHelp.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelp.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.PROJECT_PATH));


        HBox hBoxDir = new HBox(10);
        hBoxDir.getChildren().addAll(txtDir, btnDir, btnHelp);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(lblName, txtName, lblDir, hBoxDir);
        contPane.setContent(vBox);
    }

}
