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

package de.p2tools.mosaik.gui;

import de.p2tools.mosaik.controller.config.ProgConfig;
import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.controller.data.Icons;
import de.p2tools.mosaik.controller.data.projectData.ProjectData;
import de.p2tools.mosaik.controller.data.thumb.ThumbCollection;
import de.p2tools.mosaik.gui.dialog.AddMosaikDialogController;
import de.p2tools.mosaik.gui.tools.GuiTools;
import de.p2tools.p2Lib.dialog.DirFileChooser;
import de.p2tools.p2Lib.dialog.PAlert;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.StringConverter;

import java.nio.file.Path;
import java.nio.file.Paths;

public class GuiStart extends AnchorPane {

    private final ScrollPane scrollPane = new ScrollPane();
    private final ComboBox<ProjectData> cbProjectDataList = new ComboBox<>();
    private final TextField txtName = new TextField("");
    private final TextField txtDir = new TextField("");
    private final Button btnNew = new Button("Neues Projekt erstellen");
    private final Button btnDel = new Button("Projekt löschen");
    private final Button btnMov = new Button("Projektordner verschieben");
    private final VBox vBox = new VBox(10);

    private ProjectData projectData = null;
    private final ProgData progData;
    private final BooleanProperty allOk = new SimpleBooleanProperty(false);

    public GuiStart() {
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
        if (progData.projectDataList.isEmpty()) {
            addProjectDataDialog();
        }
        selectProjectData();

        vBox.setPadding(new Insets(10));
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

        GuiTools.setColor(txtDir, dirBinding.get());
        GuiTools.setColor(txtName, nameBinding.get());

        dirBinding.addListener(l -> GuiTools.setColor(txtDir, dirBinding.get()));
        nameBinding.addListener(l -> GuiTools.setColor(txtName, nameBinding.get()));
    }

    private void initCollection() {
        cbProjectDataList.setItems(progData.projectDataList);

        try {
            int col = ProgConfig.START_GUI_PROJECT_DATA.get();
            ProjectData projectData = progData.projectDataList.get(col);
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
                    btnDel.setDisable(projectData == null);
                    btnMov.setDisable(projectData == null);
                }
        );

        btnNew.setGraphic(new Icons().ICON_BUTTON_ADD);
        btnNew.setOnAction(event -> {
            addProjectDataDialog();
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

        btnMov.setGraphic(new Icons().ICON_BUTTON_MOVE);
        btnMov.setOnAction(event -> {
            int i = cbProjectDataList.getSelectionModel().getSelectedIndex();
            if (i < 0) {
                return;
            }
            ProjectData pd = progData.projectDataList.get(i);
            if (pd != null) {
                moveProject();
            }
        });
    }

    private void moveProject() {
        String oldDir = progData.selectedProjectData.getDestDir();
        String dir = DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, txtDir);
        if (dir.isEmpty()) {
            return;
        }

        if (progData.selectedProjectData.getDestDir().isEmpty()) {
            // dann ist das Projektverzeichnis noch nicht angelegt
            progData.selectedProjectData.setDestDir(dir);
            return;
        } else {
            Path oldDirPath = Paths.get(oldDir);
            if (!oldDirPath.toFile().exists()) {
                // dann ist das Projektverzeichnis noch nicht angelegt
                progData.selectedProjectData.setDestDir(dir);
                return;
            }
        }


        if (!progData.worker.moveProject(dir)) {
            progData.selectedProjectData.setDestDir(oldDir);
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

    private void addProjectDataDialog() {
        AddMosaikDialogController amc = new AddMosaikDialogController();
        ProjectData pd = amc.getProjectData();

        if (pd != null) {
            progData.projectDataList.add(pd);
            cbProjectDataList.getSelectionModel().select(pd);
        }
    }

    private void selectProjectData() {
        if (projectData != null) {
            txtName.textProperty().unbindBidirectional(projectData.nameProperty());
            txtDir.textProperty().unbindBidirectional(projectData.destDirProperty());
            projectData.srcPhotoProperty().unbind();
        }

        projectData = cbProjectDataList.getSelectionModel().getSelectedItem();
        progData.selectedProjectData = projectData;

        txtName.setDisable(projectData == null);
        txtDir.setDisable(projectData == null);
        btnMov.setDisable(projectData == null);
        btnDel.setDisable(projectData == null);

        if (projectData == null) {
            txtDir.setText("");
            txtName.setText("");

        } else {

            ProgConfig.START_GUI_PROJECT_DATA.set(cbProjectDataList.getSelectionModel().getSelectedIndex());

            txtName.textProperty().bindBidirectional(projectData.nameProperty());
            txtDir.textProperty().bindBidirectional(projectData.destDirProperty());
        }
    }

    private void initCont() {

        final Button btnDestDir = new Button();
        btnDestDir.setOnAction(event -> {
            if (!new PAlert().showAlert_yes_no("Pfad ändern", "Projekt verschieben?", "Soll das Projekt von:\n" +
                    txtDir.getText() + "\n\n" +
                    "verschoben werden?").equals(PAlert.BUTTON.YES)) {
                return;
            }
            moveProject();
        });
        btnDestDir.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        final Button btnDestDirHelp = new Button("");
        btnDestDirHelp.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnDestDirHelp.setOnAction(a -> new PAlert().showHelpAlert("Projektpfad", HelpText.PROJECT_PATH));

        // make Grid
        int row = 0;
        GridPane gridPaneDest = new GridPane();
        gridPaneDest.setPadding(new Insets(0));
        gridPaneDest.setVgap(15);
        gridPaneDest.setHgap(15);

        Label lblProject = new Label("Projekt auswählen");
        lblProject.getStyleClass().add("headerLabel");
        lblProject.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lblProject, Priority.ALWAYS);

        Label lblCont = new Label("Einstellungen des gewählten Projekts");
        lblCont.getStyleClass().add("headerLabel");
        lblCont.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lblCont, Priority.ALWAYS);

        Label lblPath = new Label("Ordner in dem das Mosaik erstellt wird");
        lblPath.getStyleClass().add("headerLabel");
        lblPath.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lblPath, Priority.ALWAYS);

        txtDir.setEditable(false);
        txtDir.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(txtDir, Priority.ALWAYS);
        txtName.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(txtName, Priority.ALWAYS);

        cbProjectDataList.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(cbProjectDataList, Priority.ALWAYS);

        gridPaneDest.add(lblProject, 0, row, 4, 1);
        gridPaneDest.add(new Label("Projekt:"), 0, ++row);
        gridPaneDest.add(cbProjectDataList, 1, row);

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(btnNew, btnDel, btnMov);

        gridPaneDest.add(hBox, 1, ++row);

        gridPaneDest.add(new Label(" "), 0, ++row);
        gridPaneDest.add(lblCont, 0, ++row, 4, 1);

        gridPaneDest.add(new Label("Projektname:"), 0, ++row);
        gridPaneDest.add(txtName, 1, row);

        gridPaneDest.add(new Label(""), 0, ++row);
        gridPaneDest.add(lblPath, 0, ++row, 4, 1);

        gridPaneDest.add(new Label("Projektpfad:"), 0, ++row);
        gridPaneDest.add(txtDir, 1, row);
        gridPaneDest.add(btnDestDir, 2, row);
        gridPaneDest.add(btnDestDirHelp, 3, row);

        vBox.getChildren().add(gridPaneDest);
    }

}
