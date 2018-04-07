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
import de.p2tools.mosaik.controller.data.thumb.ThumbCollection;
import de.p2tools.p2Lib.dialog.DirFileChooser;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.dialog.PComboBox;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class GuiThumbController extends AnchorPane {
    private final ProgData progData;
    VBox vBoxCont = new VBox(10);
    ThumbCollection thumbCollection = null;
    PComboBox cbDir = new PComboBox();
    CheckBox tglRecursive = new CheckBox("Auch Unterordner durchsuchen");

    public GuiThumbController() {
        progData = ProgData.getInstance();

        AnchorPane.setLeftAnchor(vBoxCont, 0.0);
        AnchorPane.setBottomAnchor(vBoxCont, 0.0);
        AnchorPane.setRightAnchor(vBoxCont, 0.0);
        AnchorPane.setTopAnchor(vBoxCont, 0.0);
        getChildren().addAll(vBoxCont);


        vBoxCont.setPadding(new Insets(10));
        initCont();
        selectThumbCollection();
    }

    public void isShown() {
        if (progData.selectedProjectData == null) {
            vBoxCont.setDisable(true);
            return;
        }

        vBoxCont.setDisable(false);
        selectThumbCollection();
    }

    private void initCont() {

        final Button btnDir = new Button();
        btnDir.setOnAction(event -> DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, cbDir));
        btnDir.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        final Button btnHelp = new Button("");
        btnHelp.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelp.setOnAction(event -> new PAlert().showHelpAlert("Fotos hinzufügen", HelpText.GET_THUMB_DIR));

        final Button btnLod = new Button("Fotos hinzufügen");
        final Button btnReload = new Button("Gespeicherte Liste neu einlesen");
        final Button btnClear = new Button("Gespeicherte Liste Löschen");

        btnLod.setOnAction(a -> {
            if (cbDir.getSelectionModel().getSelectedItem().isEmpty()) {
                new PAlert().showErrorAlert("Verzeichnis für die Vorschaubilder", "Zum Laden der Bilder wurde " +
                        "kein Verzeichnis angegeben");
                return;
            }

            String thumbDir = progData.selectedProjectData.getThumbDirString();
            if (thumbDir.isEmpty()) {
                return;
            }

            progData.worker.createThumbList(thumbCollection, thumbDir);
        });
        btnReload.setOnAction(a -> {
            String thumbDir = progData.selectedProjectData.getThumbDirString();
            if (thumbDir.isEmpty()) {
                return;
            }

            progData.worker.readThumbList(thumbCollection, thumbDir);
        });
        btnClear.setOnAction(a -> {
            try {
                FileUtils.deleteDirectory(new File(progData.selectedProjectData.getThumbDirString()));
            } catch (Exception ex) {
                PLog.errorLog(945121254, ex);
            }
            thumbCollection.getThumbList().clear();
        });

        btnLod.disableProperty().bind(progData.worker.workingProperty());
        btnReload.disableProperty().bind(progData.worker.workingProperty());
        btnClear.disableProperty().bind(progData.worker.workingProperty());

        int row = 0;
        GridPane gridPaneDest = new GridPane();
        gridPaneDest.setPadding(new Insets(0));
        gridPaneDest.setVgap(10);
        gridPaneDest.setHgap(10);

        Label lblTitle = new Label("Miniaturbilder hinzufügen");
        lblTitle.getStyleClass().add("headerLabel");
        lblTitle.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lblTitle, Priority.ALWAYS);

        Label lblDir = new Label("Verzeichnis:");
        cbDir.setMaxWidth(Double.MAX_VALUE);
        cbDir.init(ProgConfig.CONFIG_ADD_PHOTO_PATH_LIST, ProgConfig.CONFIG_ADD_PHOTO_PATH_SEL);
        GridPane.setHgrow(cbDir, Priority.ALWAYS);
        GridPane.setHalignment(btnLod, HPos.RIGHT);
        GridPane.setHalignment(tglRecursive, HPos.RIGHT);

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(tglRecursive, btnLod);
        tglRecursive.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(tglRecursive, Priority.ALWAYS);

        gridPaneDest.add(lblTitle, 0, row, 4, 1);
        gridPaneDest.add(lblDir, 0, ++row);
        gridPaneDest.add(cbDir, 1, row);
        gridPaneDest.add(btnDir, 2, row);
        gridPaneDest.add(btnHelp, 3, row);
        gridPaneDest.add(hBox, 0, ++row, 4, 1);


        lblTitle = new Label("Gespeicherte Liste");
        lblTitle.getStyleClass().add("headerLabel");
        lblTitle.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lblTitle, Priority.ALWAYS);

        TilePane tilePane = new TilePane(Orientation.VERTICAL);
        tilePane.setPrefRows(2);
        tilePane.setAlignment(Pos.TOP_RIGHT);
        tilePane.setVgap(10);
        tilePane.getChildren().addAll(btnReload, btnClear);
        btnClear.setMaxWidth(Double.MAX_VALUE);
        btnReload.setMaxWidth(Double.MAX_VALUE);

        gridPaneDest.add(new Label(" "), 0, ++row);
        gridPaneDest.add(new Label(" "), 0, ++row);
        gridPaneDest.add(lblTitle, 0, ++row, 4, 1);
        gridPaneDest.add(tilePane, 0, ++row, 4, 1);

        ColumnConstraints c0 = new ColumnConstraints();
        gridPaneDest.getColumnConstraints().addAll(c0);
        c0.setMinWidth(GridPane.USE_PREF_SIZE);

        vBoxCont.getChildren().addAll(gridPaneDest);
    }

    private void selectThumbCollection() {
        if (progData.selectedProjectData != null &&
                thumbCollection != null &&
                thumbCollection.equals(progData.selectedProjectData.getThumbCollection())) {
            // dann hat sich nichts geänert
            return;
        }

        if (thumbCollection != null) {
            thumbCollection.fotoSrcDirProperty().unbind();
            tglRecursive.selectedProperty().unbindBidirectional(thumbCollection.recursiveProperty());
        }

        if (progData.selectedProjectData != null) {
            thumbCollection = progData.selectedProjectData.getThumbCollection();
        }

        if (thumbCollection == null) {
            vBoxCont.setDisable(true);
        } else {
            vBoxCont.setDisable(false);

            cbDir.selectElement(thumbCollection.getFotoSrcDir());
            thumbCollection.fotoSrcDirProperty().bind(cbDir.getSelectionModel().selectedItemProperty());
            tglRecursive.selectedProperty().bindBidirectional(thumbCollection.recursiveProperty());
        }
    }

}