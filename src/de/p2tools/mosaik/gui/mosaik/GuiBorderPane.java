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

package de.p2tools.mosaik.gui.mosaik;

import de.p2tools.mosaik.controller.config.ProgConfig;
import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.controller.data.Icons;
import de.p2tools.mosaik.controller.data.mosaikData.MosaikData;
import de.p2tools.mosaik.controller.data.mosaikData.MosaikDataProps;
import de.p2tools.mosaik.gui.HelpText;
import de.p2tools.p2Lib.dialog.DirFileChooser;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.dialog.PComboBox;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class GuiBorderPane extends AnchorPane {

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

    MosaikData mosaikData = null;

    public GuiBorderPane() {
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

    public void setMosaikData(MosaikData mosaikData) {
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

        rbBgImg.setOnAction(e -> mosaikData.setBackGround(MosaikData.BACKGROUND.IMAGE.toString()));
        rbColor.setOnAction(e -> mosaikData.setBackGround(MosaikData.BACKGROUND.COLOR.toString()));

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
                mosaikData.setResizeThumb(MosaikData.THUMB_RESIZE.ALL.toString());
            } else {
                mosaikData.setResizeThumb(MosaikData.THUMB_RESIZE.NON.toString());
            }
        });

        rbBgImg.disableProperty().bind(chkBorder.selectedProperty().not());
        rbColor.disableProperty().bind(chkBorder.selectedProperty().not());
        cboBgImg.disableProperty().bind(chkBorder.selectedProperty().not());
        btnBgImg.disableProperty().bind(chkBorder.selectedProperty().not());
        colorPicker.disableProperty().bind(chkBorder.selectedProperty().not());
        sliderBorder.disableProperty().bind(chkBorder.selectedProperty().not());
        
        sliderBorder.setMin(1);
        sliderBorder.setMax(50);

        colorPicker.getStyleClass().add("split-button");
        colorPicker.setOnAction(a -> {
            Color colorPickerValue = colorPicker.getValue();
            mosaikData.setBorderColor(colorPickerValue.toString());
        });

        int row = 0;
        GridPane gridPaneDest = new GridPane();
        gridPaneDest.setPadding(new Insets(0));
        gridPaneDest.setVgap(10);
        gridPaneDest.setHgap(10);

        Label lbl = new Label("Einen Rahmen um die Miniaturbilder ziehen");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lbl, Priority.ALWAYS);

        GridPane.setHgrow(sliderBorder, Priority.ALWAYS);

        cboBgImg.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(cboBgImg, Priority.ALWAYS);

        gridPaneDest.add(lbl, 0, row, 4, 1);
        gridPaneDest.add(chkBorder, 0, ++row);
        gridPaneDest.add(btnHelpSize, 3, row);

        gridPaneDest.add(new Label("Breite des Rahmen [Pixel]:"), 0, ++row);
        gridPaneDest.add(sliderBorder, 1, row);
        gridPaneDest.add(lblSlider, 2, row);

        gridPaneDest.add(new Label("    "), 2, ++row);

        Label lblBg = new Label("Als Hintergrund eine Farbe oder ein Bild wählen");
        lblBg.getStyleClass().add("headerLabel");
        lblBg.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lblBg, Priority.ALWAYS);
        gridPaneDest.add(lblBg, 0, ++row, 4, 1);

        gridPaneDest.add(rbBgImg, 0, ++row);
        gridPaneDest.add(cboBgImg, 1, row);
        gridPaneDest.add(btnBgImg, 3, row);

        gridPaneDest.add(rbColor, 0, ++row);
        gridPaneDest.add(colorPicker, 1, row);

        // import all
        contPane.setSpacing(25);
        contPane.setPadding(new Insets(10));
        contPane.getChildren().addAll(gridPaneDest);
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

        if (mosaikData.getBackGround().equals(MosaikData.BACKGROUND.COLOR.toString())) {
            rbColor.setSelected(true);
        } else if (mosaikData.getBackGround().equals(MosaikData.BACKGROUND.IMAGE.toString())) {
            rbBgImg.setSelected(true);
        } else {
            rbColor.setSelected(true);
            mosaikData.setBackGround(MosaikDataProps.BACKGROUND.COLOR.toString());
        }

        chkBorder.selectedProperty().bindBidirectional(mosaikData.addBorderProperty());

        sliderBorder.setValue(mosaikData.getBorderSize());
        mosaikData.borderSizeProperty().bind(sliderBorder.valueProperty());
        lblSlider.textProperty().bind(Bindings.format("%d", mosaikData.borderSizeProperty()));

        cboBgImg.selectElement(mosaikData.getBgPic());
        mosaikData.bgPicProperty().bind(cboBgImg.getSelProperty());
    }
}
