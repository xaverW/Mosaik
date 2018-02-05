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

import de.p2tools.controller.config.ProgConst;
import de.p2tools.controller.config.ProgData;
import de.p2tools.controller.data.Icons;
import de.p2tools.controller.data.wallpaperData.WallpaperData;
import de.p2tools.gui.dialog.MTAlert;
import de.p2tools.p2Lib.tools.DirFileChooser;
import de.p2tools.p2Lib.tools.FileUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GuiWallpaperController extends AnchorPane {

    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox contPane = new VBox(10);
    private final TextField txtDestName = new TextField();
    private final TextField txtDestDir = new TextField();
    private final Button btnDest = new Button("");
    private final Button btnCreate = new Button("Fototapete erstellen");

    private final Slider sliderSize = new Slider();
    private final Label lblSlider = new Label("");
    private final Slider sliderCount = new Slider();
    private final Label lblSliderCount = new Label("");
    private final IntegerProperty iProp = new SimpleIntegerProperty();
    private final IntegerProperty iPropCount = new SimpleIntegerProperty();

    private final ProgData progData;
    private WallpaperData wallpaperData = null;

    public GuiWallpaperController() {
        this.progData = ProgData.getInstance();
        if (progData.selectedProjectData != null) {
            this.wallpaperData = progData.selectedProjectData.getWallpaperData();
        }

        contPane.setPadding(new Insets(10));
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(contPane);

        AnchorPane.setLeftAnchor(scrollPane, 0.0);
//        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        scrollPane.getStyleClass().add("layoutBackground");
        getStyleClass().add("layoutBackground");
        initCont();
        bind();
        getChildren().addAll(scrollPane);
    }

    public void isShown() {
        if (progData.selectedProjectData == null) {
            contPane.setDisable(true);
            return;
        }

        contPane.setDisable(false);
        if (!wallpaperData.equals(progData.selectedProjectData.getWallpaperData())) {
            unbind();
            wallpaperData = progData.selectedProjectData.getWallpaperData();
            bind();
        }

        if (txtDestDir.getText().isEmpty()) {
            txtDestDir.setText(progData.selectedProjectData.getDestDir());
        }
        if (txtDestName.getText().isEmpty()) {
            txtDestName.setText(FileUtils.getNextFileName(txtDestDir.getText(), ProgConst.WALLPAPER_STD_NAME));
        }

    }


    private void initCont() {
        // DEST
        btnDest.setOnAction(event -> {
            DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtDestDir);
        });
        btnDest.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        final Button btnHelpDest = new Button("");
        btnHelpDest.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpDest.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.FILEMANAGER));


        // Thumbsize
        final Button btnHelpSlider = new Button("");
        btnHelpSlider.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSlider.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.FILEMANAGER));

        sliderSize.setMin(5);
        sliderSize.setMax(25);


        // Anzahl Thumbs
        final Button btnHelpSliderCount = new Button("");
        btnHelpSliderCount.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSliderCount.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.FILEMANAGER));

        sliderCount.setMin(1);
        sliderCount.setMax(100);


        int row = 0;
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(0));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        GridPane.setHgrow(txtDestDir, Priority.ALWAYS);
        GridPane.setHgrow(txtDestName, Priority.ALWAYS);
        GridPane.setHgrow(sliderSize, Priority.ALWAYS);
        GridPane.setHgrow(sliderCount, Priority.ALWAYS);

        Label lbl = new Label("Fototapete speichern");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);

        gridPane.add(lbl, 0, row, 4, 1);
        gridPane.add(new Label("Verzeichnis:"), 0, ++row);
        gridPane.add(txtDestDir, 1, row);
        gridPane.add(btnDest, 2, row);
        gridPane.add(btnHelpDest, 3, row);

        gridPane.add(new Label("Dateiname: "), 0, ++row);
        gridPane.add(txtDestName, 1, row);


        lbl = new Label("Größe der Miniaturbilder (Pixel)");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);

        gridPane.add(new Label(""), 0, ++row);
        gridPane.add(lbl, 0, ++row, 4, 1);
        gridPane.add(sliderSize, 0, ++row, 2, 1);
        gridPane.add(lblSlider, 2, row);
        gridPane.add(btnHelpSlider, 3, row);

        lbl = new Label("Anzahl Miniaturbilder im Mosaik (Breite)");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);

        gridPane.add(new Label(""), 0, ++row);
        gridPane.add(lbl, 0, ++row, 4, 1);
        gridPane.add(sliderCount, 0, ++row, 2, 1);
        gridPane.add(lblSliderCount, 2, row);
        gridPane.add(btnHelpSliderCount, 3, row);

        GridPane.setHalignment(btnCreate, HPos.RIGHT);
        gridPane.add(new Label(""), 0, ++row);
        gridPane.add(btnCreate, 0, ++row, 4, 1);

        contPane.getChildren().add(gridPane);

        btnCreate.setOnAction(a -> {
            if (!txtDestDir.getText().isEmpty()) {
                progData.worker.createWallpaper(progData.selectedProjectData.getThumbCollection(),
                        progData.selectedProjectData.getWallpaperData());
            }
        });

    }

    private void unbind() {
        if (wallpaperData == null) {
            return;
        }

        // DEST
        txtDestDir.textProperty().unbindBidirectional(wallpaperData.fotoDestDirProperty());
        txtDestName.textProperty().unbindBidirectional(wallpaperData.fotoDestNameProperty());

        // Thumbsize
        iProp.bind(sliderSize.valueProperty());
        wallpaperData.thumbSizeProperty().unbind();
        lblSlider.textProperty().unbind();

        // Anzahl Thumbs
        iPropCount.unbind();
        wallpaperData.numberThumbsWidthProperty().unbind();
        lblSliderCount.textProperty().unbind();
    }

    private void bind() {
        if (wallpaperData == null) {
            return;
        }

        // DEST
        txtDestDir.textProperty().bindBidirectional(wallpaperData.fotoDestDirProperty());
        txtDestName.textProperty().bindBidirectional(wallpaperData.fotoDestNameProperty());

        // Thumbsize
        sliderSize.setValue(wallpaperData.getThumbSize() / 10);
        iProp.bind(sliderSize.valueProperty());
        NumberBinding nb = Bindings.multiply(iProp, 10);
        wallpaperData.thumbSizeProperty().bind(nb);
        lblSlider.textProperty().bind(Bindings.format("%d", wallpaperData.thumbSizeProperty()));

        // Anzahl Thumbs
        sliderCount.setValue(wallpaperData.getNumberThumbsWidth() / 10);
        iPropCount.bind(sliderCount.valueProperty());
        NumberBinding nbCount = Bindings.multiply(iPropCount, 10);
        wallpaperData.numberThumbsWidthProperty().bind(nbCount);
        lblSliderCount.textProperty().bind(Bindings.format("%d", wallpaperData.numberThumbsWidthProperty()));
    }
}
