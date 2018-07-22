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
import de.p2tools.mosaic.controller.data.mosaikData.MosaicDataBase;
import de.p2tools.mosaic.gui.HelpText;
import de.p2tools.p2Lib.dialog.PAlert;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class GuiMosaicChangePane extends AnchorPane {

    private final ProgData progData;
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox contPane = new VBox();

    private final RadioButton rbThumb = new RadioButton("Miniaturbilder für das Mosaik verwenden");
    private final RadioButton rbThumbColor = new RadioButton("Miniaturbilder verwenden und farblich anpassen");
    private final RadioButton rbSelf = new RadioButton("Vorlagenfoto für das Mosaik verwenden");
    private final CheckBox chkColorComplete = new CheckBox("Mosaik gleichmäßig einfärben");
    private final CheckBox chkBlackWhite = new CheckBox("Schwarz/Weiß Mosaik erstellen");
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
        rbThumbColor.setToggleGroup(tg);
        rbSelf.setToggleGroup(tg);

        rbThumb.setOnAction(e -> setMosaicType());
        rbThumbColor.setOnAction(e -> setMosaicType());
        rbSelf.setOnAction(e -> setMosaicType());

        chkColorComplete.setOnAction(a -> setMosaicType());
        chkBlackWhite.setOnAction(a -> setMosaicType());
        chkColorComplete.disableProperty().bind(rbThumbColor.selectedProperty().not());
        chkBlackWhite.disableProperty().bind(rbThumb.selectedProperty().not());

        int row = 0;
        GridPane gridPaneDest = new GridPane();
        gridPaneDest.setPadding(new Insets(0));
        gridPaneDest.setVgap(10);
        gridPaneDest.setHgap(10);

        GridPane.setHgrow(rbThumb, Priority.ALWAYS);
        rbThumb.setMaxWidth(Double.MAX_VALUE);
        rbThumbColor.setMaxWidth(Double.MAX_VALUE);
        rbSelf.setMaxWidth(Double.MAX_VALUE);

        HBox hBoxColor = new HBox();
        hBoxColor.setPadding(new Insets(0, 0, 0, 25));
        hBoxColor.getChildren().add(chkColorComplete);
        VBox vBoxColor = new VBox(10);
        vBoxColor.getChildren().addAll(rbThumbColor, hBoxColor);

        HBox hBoxBlack = new HBox();
        hBoxBlack.setPadding(new Insets(0, 0, 0, 25));
        hBoxBlack.getChildren().add(chkBlackWhite);
        VBox vBoxBlack = new VBox(10);
        vBoxBlack.getChildren().addAll(rbThumb, hBoxBlack);


        Label lbl = new Label("Fotos aus denen das Mosaik gebaut wird");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lbl, Priority.ALWAYS);
        GridPane.setHgrow(vBoxBlack, Priority.ALWAYS);

        gridPaneDest.add(lbl, 0, row, 2, 1);

        gridPaneDest.add(vBoxBlack, 0, ++row);
        gridPaneDest.add(btnHelpSrcImage, 1, row);
        gridPaneDest.add(vBoxColor, 0, ++row);
        gridPaneDest.add(rbSelf, 0, ++row);

        gridPaneDest.setVgap(25);

        // import all
        contPane.setSpacing(25);
        contPane.setPadding(new Insets(10));
        contPane.getChildren().addAll(gridPaneDest);
    }

    private void setMosaicType() {
        if (rbThumb.isSelected()) {
            if (chkBlackWhite.isSelected()) {
                mosaikData.setMosaicType(MosaicDataBase.MOSAIC_TYPE.THUMBS_GRAY.toString());
            } else {
                mosaikData.setMosaicType(MosaicDataBase.MOSAIC_TYPE.THUMBS.toString());
            }
        } else if (rbThumbColor.isSelected()) {
            if (chkColorComplete.isSelected()) {
                mosaikData.setMosaicType(MosaicDataBase.MOSAIC_TYPE.THUMBS_ALL_PIXEL_COLORED.toString());
            } else {
                mosaikData.setMosaicType(MosaicDataBase.MOSAIC_TYPE.THUMBS_COLORED.toString());
            }
        } else {
            mosaikData.setMosaicType(MosaicDataBase.MOSAIC_TYPE.FROM_SRC_IMG.toString());
        }
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

        if (mosaikData.getMosaicType().equals(MosaicDataBase.MOSAIC_TYPE.THUMBS.toString())) {
            rbThumb.setSelected(true);
            chkBlackWhite.setSelected(false);
        } else if (mosaikData.getMosaicType().equals(MosaicDataBase.MOSAIC_TYPE.THUMBS_GRAY.toString())) {
            rbThumb.setSelected(true);
            chkBlackWhite.setSelected(true);
        } else if (mosaikData.getMosaicType().equals(MosaicDataBase.MOSAIC_TYPE.THUMBS_COLORED.toString())) {
            rbThumbColor.setSelected(true);
            chkColorComplete.setSelected(false);
        } else if (mosaikData.getMosaicType().equals(MosaicDataBase.MOSAIC_TYPE.THUMBS_ALL_PIXEL_COLORED.toString())) {
            rbThumbColor.setSelected(true);
            chkColorComplete.setSelected(true);
        } else {
            rbSelf.setSelected(true);
        }
    }
}
