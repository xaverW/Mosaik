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

package de.p2tools.mosaik.gui.wallpaper;

import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.controller.data.Icons;
import de.p2tools.mosaik.controller.data.mosaikData.MosaikData;
import de.p2tools.mosaik.controller.data.mosaikData.WallpaperData;
import de.p2tools.mosaik.gui.HelpText;
import de.p2tools.p2Lib.dialog.PAlert;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class GuiWallpaperBorderPane extends AnchorPane {

    private final ProgData progData;
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox contPane = new VBox();

    private final CheckBox chkBorder = new CheckBox("Rahmen einbauen");

    private final Slider sliderBorder = new Slider();
    private final Label lblSlider = new Label("");

    WallpaperData wallpaperData = null;

    public GuiWallpaperBorderPane() {
        progData = ProgData.getInstance();

        if (progData.selectedProjectData != null) {
            wallpaperData = progData.selectedProjectData.getWallpaperData();
        }

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(contPane);

        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setTopAnchor(scrollPane, 0.0);

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
    }


    private void initCont() {
        // make Grid
        final Button btnHelpSize = new Button("");
        btnHelpSize.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSize.setOnAction(a -> new PAlert().showHelpAlert("Größe der Miniaturbilder", HelpText.THUMB_BORDER));


        chkBorder.setOnAction(e -> {
            if (chkBorder.isSelected()) {
                wallpaperData.setResizeThumb(MosaikData.THUMB_RESIZE.ALL.toString());
            } else {
                wallpaperData.setResizeThumb(MosaikData.THUMB_RESIZE.NON.toString());
            }
        });

        // Resize
        final Button btnHelpSlider = new Button("");
        btnHelpSlider.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSlider.setOnAction(a -> new PAlert().showHelpAlert("Pixelgröße", HelpText.THUMB_BORDER_SIZE));

        sliderBorder.setMin(1);
        sliderBorder.setMax(50);

        final ColorPicker colorPicker = new ColorPicker();
        colorPicker.getStyleClass().add("split-button");
        try {
            colorPicker.setValue(Color.web(wallpaperData.getBorderColor()));
        } catch (Exception ex) {
            colorPicker.setValue(Color.BLACK);
            wallpaperData.setBorderColor(Color.BLACK.toString());
        }
        colorPicker.setOnAction(a -> {
            Color colorPickerValue = colorPicker.getValue();
            wallpaperData.setBorderColor(colorPickerValue.toString());
        });

        int row = 0;
        GridPane gridPaneDest = new GridPane();
        gridPaneDest.setPadding(new Insets(0));
        gridPaneDest.setVgap(10);
        gridPaneDest.setHgap(10);

        Label lbl = new Label("Alle Miniaturbilder mit einem Rahmen versehen");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lbl, Priority.ALWAYS);
        GridPane.setHgrow(chkBorder, Priority.ALWAYS);
        GridPane.setHgrow(sliderBorder, Priority.ALWAYS);

        gridPaneDest.add(lbl, 0, row, 4, 1);
        gridPaneDest.add(chkBorder, 0, ++row, 2, 1);
        gridPaneDest.add(btnHelpSize, 3, row);

        gridPaneDest.add(new Label("Breite des Rahmen [Pixel]:"), 0, ++row);
        gridPaneDest.add(sliderBorder, 1, row);
        gridPaneDest.add(lblSlider, 2, row);
        gridPaneDest.add(btnHelpSlider, 3, row);

        gridPaneDest.add(new Label("Farbe des Rahmen:"), 0, ++row);
        gridPaneDest.add(colorPicker, 1, row);

        // import all
        contPane.setSpacing(25);
        contPane.setPadding(new Insets(10));
        contPane.getChildren().addAll(gridPaneDest);
    }

    private void unbind() {
        if (wallpaperData == null) {
            return;
        }

        // Resize
        wallpaperData.reduceSizeProperty().unbind();
        lblSlider.textProperty().unbind();
    }

    private void bind() {
        if (wallpaperData == null) {
            return;
        }

        chkBorder.setSelected(wallpaperData.getResizeThumb().equals(MosaikData.THUMB_RESIZE.ALL.toString()));

        // Resize
        sliderBorder.setValue(wallpaperData.getReduceSize());
        wallpaperData.reduceSizeProperty().bind(sliderBorder.valueProperty());

        lblSlider.textProperty().bind(Bindings.format("%d", wallpaperData.reduceSizeProperty()));
    }
}
