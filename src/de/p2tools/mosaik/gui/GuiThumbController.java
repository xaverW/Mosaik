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

import de.p2tools.mosaik.controller.RunEvent;
import de.p2tools.mosaik.controller.RunListener;
import de.p2tools.mosaik.controller.config.ProgConfig;
import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.controller.data.Icons;
import de.p2tools.mosaik.controller.data.thumb.Thumb;
import de.p2tools.mosaik.controller.data.thumb.ThumbCollection;
import de.p2tools.mosaik.controller.worker.genThumbList.GenThumbList;
import de.p2tools.p2Lib.dialog.DirFileChooser;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.tools.Log;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.apache.commons.io.FileUtils;
import org.controlsfx.control.ToggleSwitch;

import java.io.File;

public class GuiThumbController extends AnchorPane {
    private final ProgData progData;
    SplitPane splitPane = new SplitPane();
    VBox vBoxCont = new VBox(10);
    VBox vBoxFlowPane = new VBox(10);
    ScrollPane scrollPane = new ScrollPane();
    TilePane tilePane = new TilePane();
    ThumbCollection thumbCollection = null;
    TextField txtDir = new TextField("");
    ToggleSwitch tglRecursive = new ToggleSwitch("Auch Unterordner durchsuchen");
    DoubleProperty splitPaneProperty = ProgConfig.THUMB_GUI_DIVIDER.getDoubleProperty();

    public GuiThumbController() {
        progData = ProgData.getInstance();

        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        AnchorPane.setTopAnchor(splitPane, 0.0);
        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        splitPane.getItems().addAll(vBoxCont, vBoxFlowPane);
        splitPane.getDividers().get(0).positionProperty().bindBidirectional(splitPaneProperty);
        SplitPane.setResizableWithParent(vBoxFlowPane, Boolean.FALSE);
        getChildren().addAll(splitPane);

        tilePane.setHgap(5);
        tilePane.setVgap(5);
        tilePane.setPadding(new Insets(5));

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tilePane);

        vBoxFlowPane.setPadding(new Insets(10));
        vBoxFlowPane.getChildren().add(scrollPane);

        vBoxCont.setPadding(new Insets(10));
        initCont();
        selectThumbCollection();

        progData.worker.addAdListener(new RunListener() {
            @Override
            public void ping(RunEvent runEvent) {
                if (runEvent.nixLos() && runEvent.getSource().getClass().equals(GenThumbList.class)) {
                    makeTilePane();
                }
            }
        });

    }

    public void isShown() {
        if (progData.selectedProjectData == null) {
            vBoxCont.setDisable(true);
            return;
        }

        vBoxCont.setDisable(false);
        selectThumbCollection();
    }

    private void selectThumbCollection() {
        if (progData.selectedProjectData != null &&
                thumbCollection != null &&
                thumbCollection.equals(progData.selectedProjectData.getThumbCollection())) {
            // dann hat sich nichts geänert
            return;
        }

        tilePane.getChildren().clear();

        if (thumbCollection != null) {
            txtDir.textProperty().unbindBidirectional(thumbCollection.fotoSrcDirProperty());
            tglRecursive.selectedProperty().unbindBidirectional(thumbCollection.recursiveProperty());
        }

        if (progData.selectedProjectData != null) {
            thumbCollection = progData.selectedProjectData.getThumbCollection();
        }

        if (thumbCollection == null) {
            vBoxCont.setDisable(true);
        } else {
            vBoxCont.setDisable(false);

            txtDir.textProperty().bindBidirectional(thumbCollection.fotoSrcDirProperty());
            tglRecursive.selectedProperty().bindBidirectional(thumbCollection.recursiveProperty());

            makeTilePane();
        }
    }

    private void makeTilePane() {
        tilePane.getChildren().clear();
        int i = 1;
        for (Thumb thumb : thumbCollection.getThumbList()) {
            Label lbl = new Label("" + i++);
            lbl.setAlignment(Pos.CENTER);
            lbl.setPadding(new Insets(2));
            lbl.setTextFill(Color.WHITE);
            lbl.setMinHeight(30);
            lbl.setMinWidth(30);
            lbl.setMaxWidth(Double.MAX_VALUE);
            lbl.setMaxHeight(Double.MAX_VALUE);

            lbl.setTooltip(new Tooltip(thumb.getFileName()));

            Color col = thumb.getColor();
            CornerRadii corn = new CornerRadii(20);
            Background background = new Background(new BackgroundFill(col, corn, Insets.EMPTY));
            lbl.setBackground(background);

            tilePane.getChildren().add(lbl);
        }
    }

    private void initCont() {

        final Button btnDir = new Button();
        btnDir.setOnAction(event -> DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, txtDir));
        btnDir.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        final Button btnHelp = new Button("");
        btnHelp.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelp.setOnAction(event -> new PAlert().showHelpAlert("Fotos hinzufügen", HelpText.GET_THUMB_DIR));

        final Button btnLod = new Button("Fotos hinzufügen");
        final Button btnReload = new Button("Gespeicherte Liste neu einlesen");
        final Button btnClear = new Button("Gespeicherte Liste Löschen");

        btnLod.setOnAction(a -> {
            if (txtDir.getText().isEmpty()) {
                new PAlert().showErrorAlert("Verzeichnis für die Vorschaubilder", "Zum Laden der Bilder wurde " +
                        "kein Verzeichnis angegeben");
                return;
            }

            String thumbDir = progData.selectedProjectData.getThumbDirString();
            if (thumbDir.isEmpty()) {
                return;
            }

            progData.worker.createThumbList(thumbCollection, thumbDir);
            makeTilePane();
        });
        btnReload.setOnAction(a -> {
            String thumbDir = progData.selectedProjectData.getThumbDirString();
            if (thumbDir.isEmpty()) {
                return;
            }

            progData.worker.readThumbList(thumbCollection, thumbDir);
            makeTilePane();
        });
        btnClear.setOnAction(a -> {
            try {
                FileUtils.deleteDirectory(new File(progData.selectedProjectData.getThumbDirString()));
            } catch (Exception ex) {
                Log.errorLog(945121254, ex);
            }
            thumbCollection.getThumbList().clear();
            makeTilePane();
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

        Label lblDir = new Label("Ordner mit Fotos:");

        txtDir.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(txtDir, Priority.ALWAYS);
        GridPane.setHalignment(btnLod, HPos.RIGHT);

        gridPaneDest.add(lblTitle, 0, row, 4, 1);
        gridPaneDest.add(lblDir, 0, ++row);
        gridPaneDest.add(txtDir, 1, row);
        gridPaneDest.add(btnDir, 2, row);
        gridPaneDest.add(btnHelp, 3, row);
        gridPaneDest.add(tglRecursive, 0, ++row, 4, 1);
        gridPaneDest.add(btnLod, 0, ++row, 4, 1);

        lblTitle = new Label("Gespeicherte Liste");
        lblTitle.getStyleClass().add("headerLabel");
        lblTitle.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lblTitle, Priority.ALWAYS);

        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(btnReload, btnClear);

        gridPaneDest.add(new Label(" "), 0, ++row);
        gridPaneDest.add(lblTitle, 0, ++row, 4, 1);
        gridPaneDest.add(hBox, 0, ++row, 4, 1);

        vBoxCont.getChildren().addAll(gridPaneDest);
    }

}