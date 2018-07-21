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

package de.p2tools.mosaic.gui.mosaik;

import de.p2tools.mosaic.controller.config.ProgData;
import de.p2tools.mosaic.controller.data.Icons;
import de.p2tools.mosaic.controller.data.mosaikData.MosaicData;
import de.p2tools.mosaic.gui.HelpText;
import de.p2tools.p2Lib.dialog.PAlert;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GuiMosaicChangePane extends AnchorPane {

    private final ProgData progData;
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox contPane = new VBox();

    private final RadioButton rbThumb = new RadioButton("Miniaturbilder für das Mosaik verwenden");
    private final RadioButton rbGrayThumb = new RadioButton("Miniaturbilder für ein Schwarz/Weiß Mosaik verwenden");
    private final RadioButton rbThumbColor = new RadioButton("Miniaturbilder verwenden und farblich anpassen");
    private final RadioButton rbSelf = new RadioButton("Vorlagenfoto für das Mosaik verwenden");
    private final ToggleGroup tg = new ToggleGroup();

    MosaicData mosaikData = null;

    public GuiMosaicChangePane() {
        progData = ProgData.getInstance();

        if (progData.selectedProjectData != null) {
            mosaikData = progData.selectedProjectData.getMosaicData();
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

        if (!mosaikData.equals(progData.selectedProjectData.getMosaicData())) {
            unbind();
            mosaikData = progData.selectedProjectData.getMosaicData();

            bind();
        }
    }


    private void initCont() {
        // make Grid
        final Button btnHelpSrcImage = new Button("");
        btnHelpSrcImage.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSrcImage.setOnAction(a -> new PAlert().showHelpAlert("Fotos für das Mosaik", HelpText.MOSAIC_PIXEL_FOTO));

        rbThumb.setToggleGroup(tg);
        rbGrayThumb.setToggleGroup(tg);
        rbThumbColor.setToggleGroup(tg);
        rbSelf.setToggleGroup(tg);

        rbThumb.setOnAction(e -> mosaikData.setThumbSrc(MosaicData.THUMB_SRC.THUMBS.toString()));
        rbGrayThumb.setOnAction(e -> mosaikData.setThumbSrc(MosaicData.THUMB_SRC.THUMBS_GRAY.toString()));
        rbThumbColor.setOnAction(e -> mosaikData.setThumbSrc(MosaicData.THUMB_SRC.THUMBS_COLOR.toString()));
        rbSelf.setOnAction(e -> mosaikData.setThumbSrc(MosaicData.THUMB_SRC.SRC_FOTO.toString()));

        int row = 0;
        GridPane gridPaneDest = new GridPane();
        gridPaneDest.setPadding(new Insets(0));
        gridPaneDest.setVgap(10);
        gridPaneDest.setHgap(10);

        GridPane.setHgrow(rbThumb, Priority.ALWAYS);
        rbThumb.setMaxWidth(Double.MAX_VALUE);
        rbGrayThumb.setMaxWidth(Double.MAX_VALUE);
        rbThumbColor.setMaxWidth(Double.MAX_VALUE);
        rbSelf.setMaxWidth(Double.MAX_VALUE);

        Label lbl = new Label("Fotos aus denen das Mosaik gebaut wird");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lbl, Priority.ALWAYS);
        gridPaneDest.add(lbl, 0, row, 2, 1);
        gridPaneDest.add(rbThumb, 0, ++row);
        gridPaneDest.add(btnHelpSrcImage, 1, row);
        gridPaneDest.add(rbGrayThumb, 0, ++row);
        gridPaneDest.add(rbThumbColor, 0, ++row);
        gridPaneDest.add(rbSelf, 0, ++row);

        // import all
        contPane.setSpacing(25);
        contPane.setPadding(new Insets(10));
        contPane.getChildren().addAll(gridPaneDest);
    }

    private void unbind() {
        if (mosaikData == null) {
            return;
        }
    }

    private void bind() {
        if (mosaikData == null) {
            return;
        }

        if (mosaikData.getThumbSrc().equals(MosaicData.THUMB_SRC.THUMBS.toString())) {
            rbThumb.setSelected(true);
        } else if (mosaikData.getThumbSrc().equals(MosaicData.THUMB_SRC.THUMBS_GRAY.toString())) {
            rbGrayThumb.setSelected(true);
        } else if (mosaikData.getThumbSrc().equals(MosaicData.THUMB_SRC.THUMBS_COLOR.toString())) {
            rbThumbColor.setSelected(true);
        } else {
            rbSelf.setSelected(true);
        }
    }
}
