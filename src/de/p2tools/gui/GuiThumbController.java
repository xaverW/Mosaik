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
import de.p2tools.controller.data.Icons;
import de.p2tools.controller.data.thumb.Thumb;
import de.p2tools.controller.data.thumb.ThumbCollection;
import de.p2tools.controller.worker.genThumbList.GenThumbList;
import de.p2tools.gui.dialog.MTAlert;
import de.p2tools.p2Lib.tools.DirFileChooser;
import de.p2tools.p2Lib.tools.Log;
import de.p2tools.p2Lib.tools.PAlert;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.apache.commons.io.FileUtils;
import org.controlsfx.control.ToggleSwitch;

import java.io.File;

public class GuiThumbController extends AnchorPane {
    SplitPane splitPane = new SplitPane();

    VBox vBoxCont = new VBox(10);
    VBox vBoxFlowPane = new VBox(10);
    ScrollPane scrollPane = new ScrollPane();
    FlowPane flowPane = new FlowPane();

    ThumbCollection thumbCollection = null;
    TextField txtDir = new TextField("");
    ToggleSwitch tglRecursive = new ToggleSwitch("Auch Unterordner durchsuchen");
    Button btnLod = new Button("Fotos hinzufügen");
    Button btnReload = new Button("Liste neu einlesen");
    Button btnClear = new Button("Liste Löschen");

    private final ProgData progData;
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


        flowPane.setStyle("-fx-border-color: red;");
        scrollPane.setStyle("-fx-border-color: blue;");
        vBoxFlowPane.setStyle("-fx-border-color: green;");


        VBox.setVgrow(scrollPane, Priority.ALWAYS);
//        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
//        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
//        scrollPane.setMaxHeight(Double.MAX_VALUE);
//        scrollPane.setMaxWidth(Double.MAX_VALUE);
//        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(flowPane);

//        flowPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);


        vBoxFlowPane.setPadding(new Insets(10));
        vBoxFlowPane.getChildren().add(scrollPane);

        vBoxCont.setPadding(new Insets(10));
        initCont();
        selectThumbCollection();

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

        flowPane.getChildren().clear();

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

            setFlowPane();
        }
    }

    private void setFlowPane() {
        flowPane.getChildren().clear();
        int i = 1;
        for (Thumb thumb : thumbCollection.getThumbList()) {
            // todo muss wieder weg
            Label lbl = new Label("" + i++);
            lbl.setTextFill(Color.WHITE);
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
        btnHelp.setOnAction(event -> new MTAlert().showHelpAlert("Dateimanager", HelpText.GET_THUMB_DIR));


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
            setFlowPane();
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
                Log.errorLog(945121254, ex);
            }
            thumbCollection.getThumbList().clear();
            setFlowPane();
        });

        HBox hBoxDir = new HBox(10);
        hBoxDir.getChildren().addAll(txtDir, btnDir, btnHelp);

        HBox hBoxButon = new HBox(10);
        hBoxButon.getChildren().addAll(btnLod, btnReload, btnClear);

        vBoxCont.getChildren().addAll(lblDir, hBoxDir, tglRecursive, hBoxButon);
    }

}