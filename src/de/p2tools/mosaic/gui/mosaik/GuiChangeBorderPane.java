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

import de.p2tools.mosaic.controller.config.ProgConfig;
import de.p2tools.mosaic.controller.config.ProgData;
import de.p2tools.mosaic.controller.data.Icons;
import de.p2tools.mosaic.controller.data.mosaikData.MosaicData;
import de.p2tools.mosaic.controller.data.mosaikData.MosaicDataBase;
import de.p2tools.mosaic.gui.HelpText;
import de.p2tools.p2Lib.dialog.DirFileChooser;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PComboBox;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class GuiChangeBorderPane extends AnchorPane {

    private final ProgData progData;
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox contPane = new VBox();

    private final CheckBox chkBorder = new CheckBox("Rahmen einbauen");
    private final RadioButton rbColor = new RadioButton("Farbe als Hintergrund");
    private final RadioButton rbBgImg = new RadioButton("Foto als Hintergrund");
    private final ToggleGroup tg = new ToggleGroup();
    private final PComboBox cboBgImg = new PComboBox();
    private final Button btnBgImg = new Button();

    private final Slider sliderBorder = new Slider();
    private final Label lblSlider = new Label("");
    final ColorPicker colorPicker = new ColorPicker();

    MosaicData mosaikData = null;

    public GuiChangeBorderPane() {
        progData = ProgData.getInstance();

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

    public void setMosaicData(MosaicData mosaikData) {
        if (mosaikData == null) {
            contPane.setDisable(true);
            return;
        }

        contPane.setDisable(false);

        if (this.mosaikData == null || !this.mosaikData.equals(mosaikData)) {
            unbind();
            this.mosaikData = mosaikData;
            bind();
        }
    }

    private void initCont() {
        rbBgImg.setToggleGroup(tg);
        rbColor.setToggleGroup(tg);

        rbBgImg.setOnAction(e -> mosaikData.setBackground(MosaicData.BACKGROUND.IMAGE.toString()));
        rbColor.setOnAction(e -> mosaikData.setBackground(MosaicData.BACKGROUND.COLOR.toString()));

        final Button btnHelpSize = new Button("");
        btnHelpSize.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSize.setOnAction(a -> new PAlert().showHelpAlert("Größe des Rahmen", HelpText.THUMB_BORDER));

        btnBgImg.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage,
                cboBgImg,
                progData.selectedProjectData.getDestDir()));
        btnBgImg.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        cboBgImg.init(ProgConfig.CONFIG_SRC_PHOTO_PATH_LIST, ProgConfig.CONFIG_SRC_PHOTO_PATH_SEL);

        chkBorder.setOnAction(e -> {
            if (chkBorder.isSelected()) {
                mosaikData.setResizeThumb(MosaicData.THUMB_RESIZE.ALL.toString());
            } else {
                mosaikData.setResizeThumb(MosaicData.THUMB_RESIZE.NON.toString());
            }
        });

        rbBgImg.disableProperty().bind(chkBorder.selectedProperty().not());
        rbColor.disableProperty().bind(chkBorder.selectedProperty().not());
        cboBgImg.disableProperty().bind(chkBorder.selectedProperty().not());
        btnBgImg.disableProperty().bind(chkBorder.selectedProperty().not());
        colorPicker.disableProperty().bind(chkBorder.selectedProperty().not());
        sliderBorder.disableProperty().bind(chkBorder.selectedProperty().not());

        sliderBorder.setMin(1);
        sliderBorder.setMax(250);

        colorPicker.getStyleClass().add("split-button");
        colorPicker.setOnAction(a -> {
            Color colorPickerValue = colorPicker.getValue();
            mosaikData.setBorderColor(colorPickerValue.toString());
        });

        int row = 0;
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(0));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Label lbl = new Label("Einen Rahmen um die Miniaturbilder ziehen");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lbl, Priority.ALWAYS);

        GridPane.setHgrow(sliderBorder, Priority.ALWAYS);

        cboBgImg.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(cboBgImg, Priority.ALWAYS);

        gridPane.add(lbl, 0, row, 4, 1);
        gridPane.add(chkBorder, 0, ++row);
        gridPane.add(btnHelpSize, 3, row);

        gridPane.add(new Label("Breite des Rahmen [Pixel]:"), 0, ++row);
        gridPane.add(sliderBorder, 1, row);
        gridPane.add(lblSlider, 2, row);

        gridPane.add(new Label("    "), 2, ++row);

        Label lblBg = new Label("Als Hintergrund eine Farbe oder ein Bild wählen");
        lblBg.getStyleClass().add("headerLabel");
        lblBg.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lblBg, Priority.ALWAYS);
        gridPane.add(lblBg, 0, ++row, 4, 1);

        gridPane.add(rbBgImg, 0, ++row);
        gridPane.add(cboBgImg, 1, row);
        gridPane.add(btnBgImg, 3, row);

        gridPane.add(rbColor, 0, ++row);
        gridPane.add(colorPicker, 1, row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSize(),
                PColumnConstraints.getCcPrefSize());

        // import all
        contPane.setSpacing(25);
        contPane.setPadding(new Insets(10));
        contPane.getChildren().addAll(gridPane);
    }

    private void unbind() {
        if (mosaikData == null) {
            return;
        }

        chkBorder.selectedProperty().unbindBidirectional(mosaikData.addBorderProperty());
        mosaikData.borderSizeProperty().unbind();

        lblSlider.textProperty().unbind();
        mosaikData.bgPicProperty().unbind();
    }

    private void bind() {
        if (mosaikData == null) {
            return;
        }

        try {
            colorPicker.setValue(Color.web(mosaikData.getBorderColor()));
        } catch (Exception ex) {
            colorPicker.setValue(Color.BLACK);
            mosaikData.setBorderColor(Color.BLACK.toString());
        }

        if (mosaikData.getBackground().equals(MosaicData.BACKGROUND.COLOR.toString())) {
            rbColor.setSelected(true);
        } else if (mosaikData.getBackground().equals(MosaicData.BACKGROUND.IMAGE.toString())) {
            rbBgImg.setSelected(true);
        } else {
            rbColor.setSelected(true);
            mosaikData.setBackground(MosaicDataBase.BACKGROUND.COLOR.toString());
        }

        chkBorder.selectedProperty().bindBidirectional(mosaikData.addBorderProperty());

        sliderBorder.setValue(mosaikData.getBorderSize());
        mosaikData.borderSizeProperty().bind(sliderBorder.valueProperty());
        lblSlider.textProperty().bind(Bindings.format("%d", mosaikData.borderSizeProperty()));

        cboBgImg.selectElement(mosaikData.getBgPic());
        mosaikData.bgPicProperty().bind(cboBgImg.getSelValueProperty());
    }
}
