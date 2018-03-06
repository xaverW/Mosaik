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

package de.p2tools.mosaik.gui.dialog;

import de.p2tools.mosaik.controller.config.ProgConfig;
import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.controller.data.Icons;
import de.p2tools.mosaik.controller.data.projectData.ProjectData;
import de.p2tools.mosaik.gui.HelpText;
import de.p2tools.mosaik.gui.tools.GuiTools;
import de.p2tools.p2Lib.dialog.DirFileChooser;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class AddMosaikDialogController extends MTDialogExtra {

    private final ProgData progData;
    private final Button btnOk = new Button("Ok");
    private final Button btnCancel = new Button("Abbrechen");

    private final TextField txtDir = new TextField();
    private final TextField txtName = new TextField();

    private final Button btnDestDir = new Button();
    private final Button btnDestDirHelp = new Button();

    private ProjectData retProjectData = null;
    private ProjectData projectData;


    public AddMosaikDialogController() {
        super(null, ProgConfig.DIALOG_ADD_MOSAIK, "Neues Mosaik anlegen", true);

        this.projectData = new ProjectData();
        this.progData = ProgData.getInstance();

        getTilePaneOk().getChildren().addAll(btnOk, btnCancel);
        init(getvBoxDialog(), true);
    }

    public ProjectData getProjectData() {
        return retProjectData;
    }

    @Override
    public void make() {
        makeButton();

        int row = 0;
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        txtDir.setMaxWidth(Double.MAX_VALUE);
        txtName.setMaxWidth(Double.MAX_VALUE);

        GridPane.setHgrow(txtDir, Priority.ALWAYS);
        GridPane.setHgrow(txtName, Priority.ALWAYS);

        gridPane.add(new Label("Ein Name für das Mosaik"), 0, row, 2, 1);
        gridPane.add(new Label("Name: "), 0, ++row);
        gridPane.add(txtName, 1, row);
        gridPane.add(new Label(""), 0, ++row);

        gridPane.add(new Label("Ordner in dem das Mosaik erstellt wird"), 0, ++row, 2, 1);
        gridPane.add(new Label("Pfad:"), 0, ++row);
        gridPane.add(txtDir, 1, row);
        gridPane.add(btnDestDir, 2, row);
        gridPane.add(btnDestDirHelp, 3, row);

        getVboxCont().getChildren().add(gridPane);

    }

    private void makeButton() {
        btnCancel.setOnAction(a -> {
            close();
        });
        btnOk.setOnAction(a -> {
            retProjectData = projectData;
            close();
        });

        txtName.setText(projectData.getName());
        txtDir.setText(projectData.getDestDir());
        projectData.destDirProperty().bind(txtDir.textProperty());
        projectData.nameProperty().bind(txtName.textProperty());

        btnDestDir.setOnAction(event -> {
            DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, txtDir);
        });
        btnDestDir.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        btnDestDirHelp.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnDestDirHelp.setOnAction(a -> new MTAlert().showHelpAlert("Projektpfad", HelpText.PROJECT_PATH));

        initColor();
    }

    private void initColor() {
        BooleanBinding dirBinding = Bindings.createBooleanBinding(() -> txtDir.getText().trim().isEmpty(), txtDir.textProperty());
        BooleanBinding nameBinding = Bindings.createBooleanBinding(() -> txtName.getText().trim().isEmpty(), txtName.textProperty());

        btnOk.disableProperty().bind(dirBinding.or(nameBinding));
        GuiTools.setColor(txtDir, dirBinding.get());
        GuiTools.setColor(txtName, nameBinding.get());

        dirBinding.addListener(l -> GuiTools.setColor(txtDir, dirBinding.get()));
        nameBinding.addListener(l -> GuiTools.setColor(txtName, nameBinding.get()));
    }

}
