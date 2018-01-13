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

import de.p2tools.controller.RunEvent;
import de.p2tools.controller.RunListener;
import de.p2tools.controller.config.ProgConfig;
import de.p2tools.controller.config.ProgData;
import de.p2tools.controller.config.ProgInfos;
import de.p2tools.controller.data.Icons;
import de.p2tools.controller.data.thumb.Thumb;
import de.p2tools.controller.data.thumb.ThumbCollection;
import de.p2tools.controller.worker.genThumbList.GenThumbList;
import de.p2tools.gui.dialog.MTAlert;
import de.p2tools.mLib.tools.DirFileChooser;
import de.p2tools.mLib.tools.Log;
import de.p2tools.mLib.tools.MLAlert;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.apache.commons.io.FileUtils;
import org.controlsfx.control.ToggleSwitch;

import java.io.File;

public class ThumbGuiController extends AnchorPane {
    SplitPane splitPane = new SplitPane();
    ScrollPane scrollPane = new ScrollPane();
    AnchorPane contPane = new AnchorPane();
    AnchorPane collectPane = new AnchorPane();
    FlowPane flowPane = new FlowPane();


    ThumbCollection thumbCollection = null;
    TextField txtDir = new TextField("");
    ToggleSwitch tglRecursive = new ToggleSwitch("Ordner rekursiv durchsuchen");
    Button btnLod = new Button("Fotos hinzufügen");
    Button btnReload = new Button("Liste neu einlesen");
    Button btnClear = new Button("Liste Löschen");

    private final ProgData progData;
    DoubleProperty splitPaneProperty = ProgConfig.THUMB_GUI_DIVIDER.getDoubleProperty();

    public ThumbGuiController() {
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
        scrollPane.setContent(flowPane);
        SplitPane.setResizableWithParent(scrollPane, Boolean.FALSE);

        initCont();
        selectThumbCollection();

        VBox vBox = new VBox();
        vBox.setSpacing(10);

        vBox.getChildren().addAll(collectPane, contPane);
        splitPane.getItems().addAll(vBox, scrollPane);
        splitPane.getDividers().get(0).positionProperty().bindBidirectional(splitPaneProperty);

        progData.worker.addAdListener(new RunListener() {
            @Override
            public void ping(RunEvent runEvent) {
                if (runEvent.nixLos() && runEvent.getSource().getClass().equals(GenThumbList.class)) {
                    setFlowPane();
                }
            }
        });
    }

    public void isShown() {
        selectThumbCollection();
    }

    private void selectThumbCollection() {
        flowPane.getChildren().clear();

        if (thumbCollection != null) {
            txtDir.textProperty().unbindBidirectional(thumbCollection.fotoSrcDirProperty());
            tglRecursive.selectedProperty().unbindBidirectional(thumbCollection.recursiveProperty());
        }

        if (progData.selectedProjectData != null) {
            thumbCollection = progData.selectedProjectData.getThumbCollection();
        }

        if (thumbCollection == null) {
            contPane.setDisable(true);
        } else {
            contPane.setDisable(false);

            txtDir.textProperty().bindBidirectional(thumbCollection.fotoSrcDirProperty());
            tglRecursive.selectedProperty().bindBidirectional(thumbCollection.recursiveProperty());

            setFlowPane();
        }
    }

    private void setFlowPane() {
        flowPane.getChildren().clear();
        for (Thumb thumb : thumbCollection.getThumbList()) {
            Label lbl = new Label("  ");
            lbl.setPrefHeight(30);
            lbl.setPrefWidth(30);

            lbl.setTooltip(new Tooltip(thumb.getFileName()));

            Color col = thumb.getColor();
            CornerRadii corn = new CornerRadii(20);
            Background background = new Background(new BackgroundFill(col, corn, Insets.EMPTY));
            lbl.setBackground(background);

            flowPane.getChildren().add(lbl);
        }
    }

    private void initCont() {
        Label lblDir = new Label("Ordner mit Fotos auswählen");

        txtDir.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(txtDir, Priority.ALWAYS);

        final Button btnDir = new Button();
        btnDir.setOnAction(event -> DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, txtDir));
        btnDir.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        final Button btnHelp = new Button("");
        btnHelp.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelp.setOnAction(event -> new MTAlert().showHelpAlert("Dateimanager", HelpText.FILEMANAGER));


        btnLod.setOnAction(a -> {
            if (txtDir.getText().isEmpty()) {
                new MLAlert().showErrorAlert("Verzeichnis für die Vorschaubilder", "Zum Laden der Bilder wurde " +
                        "kein Verzeichnis angegeben");
                return;
            }

            if (!setDestDir()) {
                return;
            }

            progData.worker.createThumbList(thumbCollection);
            setFlowPane();
        });
        btnReload.setOnAction(a -> {
            if (!setDestDir()) {
                return;
            }

            progData.worker.readThumbList(thumbCollection);
        });
        btnClear.setOnAction(a -> {
            try {
                FileUtils.deleteDirectory(new File(thumbCollection.getThumbDir()));
            } catch (Exception ex) {
                Log.errorLog(945121254, ex);
            }
            thumbCollection.getThumbList().clear();
            setFlowPane();
        });

        HBox hBoxDir = new HBox();
        hBoxDir.setSpacing(10);
        hBoxDir.getChildren().addAll(txtDir, btnDir, btnHelp);

        HBox hBoxButon = new HBox(10);
        hBoxButon.getChildren().addAll(btnLod, btnReload, btnClear);

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        vBox.getChildren().addAll(lblDir, hBoxDir, tglRecursive, hBoxButon);

        AnchorPane.setTopAnchor(vBox, 5.0);
        AnchorPane.setLeftAnchor(vBox, 5.0);
        AnchorPane.setBottomAnchor(vBox, 5.0);
        AnchorPane.setRightAnchor(vBox, 5.0);
        vBox.setStyle("-fx-border-color: black;");

        contPane.getChildren().add(vBox);
    }

    private boolean setDestDir() {
        String destDir = ProgInfos.getFotoCollectionsDirectory_String();
        if (destDir.isEmpty()) {
            new MLAlert().showErrorAlert("Verzeichnis für die Vorschaubilder", "Für das Projekt wurde " +
                    "kein Verzeichnis angegeben");
            return false;
        }
        thumbCollection.setThumbDir(destDir);
        return true;
    }

}