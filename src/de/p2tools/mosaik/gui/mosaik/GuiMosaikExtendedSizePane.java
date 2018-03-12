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

import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.controller.data.Icons;
import de.p2tools.mosaik.controller.data.mosaikData.MosaikData;
import de.p2tools.mosaik.gui.HelpText;
import de.p2tools.p2Lib.dialog.PAlert;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GuiMosaikExtendedSizePane extends AnchorPane {

    private final ProgData progData;
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox contPane = new VBox();

    private final RadioButton rbNon = new RadioButton("Miniaturbilder nicht verkleinern");
    private final RadioButton rbAll = new RadioButton("alle Miniaturbilder verkleinern");
    private final RadioButton rbDark = new RadioButton("nur dunkle Miniaturbilder verkleinern");
    private final RadioButton rbLight = new RadioButton("nur helle Miniaturbilder verkleinern");
    private final ToggleGroup tg = new ToggleGroup();

    private final Slider sliderSize = new Slider();
    private final Label lblSlider = new Label("");
    private final IntegerProperty iPropSize = new SimpleIntegerProperty();

    MosaikData mosaikData = null;

    public GuiMosaikExtendedSizePane() {
        progData = ProgData.getInstance();

        if (progData.selectedProjectData != null) {
            mosaikData = progData.selectedProjectData.getMosaikData();
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

        if (!mosaikData.equals(progData.selectedProjectData.getMosaikData())) {
            unbind();
            mosaikData = progData.selectedProjectData.getMosaikData();

            bind();
        }
    }


    private void initCont() {
        // make Grid
        final Button btnHelpSize = new Button("");
        btnHelpSize.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSize.setOnAction(a -> new PAlert().showHelpAlert("Größe der Miniaturbilder", HelpText.THUMB_SIZE));


        rbNon.setToggleGroup(tg);
        rbAll.setToggleGroup(tg);
        rbDark.setToggleGroup(tg);
        rbLight.setToggleGroup(tg);

        rbNon.setOnAction(e -> mosaikData.setResizeThumb(MosaikData.THUMB_RESIZE.NON.toString()));
        rbAll.setOnAction(e -> mosaikData.setResizeThumb(MosaikData.THUMB_RESIZE.ALL.toString()));
        rbDark.setOnAction(e -> mosaikData.setResizeThumb(MosaikData.THUMB_RESIZE.DARK.toString()));
        rbLight.setOnAction(e -> mosaikData.setResizeThumb(MosaikData.THUMB_RESIZE.LIGHT.toString()));

        // Resize
        final Button btnHelpSlider = new Button("");
        btnHelpSlider.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSlider.setOnAction(a -> new PAlert().showHelpAlert("Pixelgröße", HelpText.THUMB_RESIZE));

        sliderSize.setMin(1);
        sliderSize.setMax(50);

        int row = 0;
        GridPane gridPaneDest = new GridPane();
        gridPaneDest.setPadding(new Insets(0));
        gridPaneDest.setVgap(10);
        gridPaneDest.setHgap(10);

        Label lbl = new Label("Miniaturbilder verkleinern");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lbl, Priority.ALWAYS);
        rbDark.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(rbDark, Priority.ALWAYS);

        gridPaneDest.add(lbl, 0, row, 3, 1);
        gridPaneDest.add(rbNon, 0, ++row);
        gridPaneDest.add(btnHelpSize, 2, row);
        gridPaneDest.add(rbAll, 0, ++row);
        gridPaneDest.add(rbDark, 0, ++row);
        gridPaneDest.add(rbLight, 0, ++row);

        gridPaneDest.add(new Label(" "), 0, ++row);

        gridPaneDest.add(new Label("Miniaturbild um den Wert verkleinern"), 0, ++row);
        gridPaneDest.add(sliderSize, 0, ++row);
        gridPaneDest.add(lblSlider, 1, row);
        gridPaneDest.add(btnHelpSlider, 2, row);

        // import all
        contPane.setSpacing(25);
        contPane.setPadding(new Insets(10));
        contPane.getChildren().addAll(gridPaneDest);
    }

    private void unbind() {
        if (mosaikData == null) {
            return;
        }

        // Resize
        iPropSize.unbind();
        mosaikData.borderSizeProperty().unbind();
        lblSlider.textProperty().unbind();
    }

    private void bind() {
        if (mosaikData == null) {
            return;
        }

        if (mosaikData.getResizeThumb().equals(MosaikData.THUMB_RESIZE.ALL.toString())) {
            rbAll.setSelected(true);
        } else if (mosaikData.getResizeThumb().equals(MosaikData.THUMB_RESIZE.DARK.toString())) {
            rbDark.setSelected(true);
        } else if (mosaikData.getResizeThumb().equals(MosaikData.THUMB_RESIZE.LIGHT.toString())) {
            rbLight.setSelected(true);
        } else {
            rbNon.setSelected(true);
        }

        // Resize
        sliderSize.setValue(mosaikData.getBorderSize() / 2);
        iPropSize.bind(sliderSize.valueProperty());

        NumberBinding nb = Bindings.multiply(iPropSize, 2);
        mosaikData.borderSizeProperty().bind(nb);

        lblSlider.textProperty().bind(Bindings.format("%d", mosaikData.borderSizeProperty()));
    }
}
