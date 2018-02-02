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
import de.p2tools.controller.data.mosaikBwData.MosaikDataBw;
import de.p2tools.gui.dialog.MTAlert;
import de.p2tools.p2Lib.tools.DirFileChooser;
import de.p2tools.p2Lib.tools.FileUtils;
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

public class GuiMosaikBWController extends AnchorPane {

    private final ProgData progData;
    private final ScrollPane scrollPane = new ScrollPane();
    //    private final VBox vBoxCont = new VBox();
    private final VBox contPane = new VBox();

    private final TextField txtSrc = new TextField();
    private final Button btnSrc = new Button("");
    private final TextField txtDestName = new TextField();
    private final Slider sliderSize = new Slider();
    private final Label lblSlider = new Label("");
    private final Slider sliderCount = new Slider();
    private final Label lblSliderCount = new Label("");

    private final TextField txtDestDir = new TextField();
    private final Button btnDest = new Button("");
    private final Button btnCreate = new Button("Mosaik erstellen");

    private final CheckBox cbxBlackWhite = new CheckBox("Mosaik als Schwarz-Weiß-Bild erstellen");

    private final IntegerProperty iPropSize = new SimpleIntegerProperty();
    private final IntegerProperty iPropCount = new SimpleIntegerProperty();

    MosaikDataBw mosaikDataBw = null;

    public GuiMosaikBWController() {
        progData = ProgData.getInstance();
        if (progData.selectedProjectData != null) {
            mosaikDataBw = progData.selectedProjectData.getMosaikDataBw();
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
        if (!mosaikDataBw.equals(progData.selectedProjectData.getMosaikDataBw())) {
            unbind();
            mosaikDataBw = progData.selectedProjectData.getMosaikDataBw();
            bind();
        }
        if (txtDestDir.getText().isEmpty()) {
            txtDestDir.setText(progData.selectedProjectData.getDestDir());
        }
        if (txtDestName.getText().isEmpty()) {
            txtDestName.setText(FileUtils.getNextFileName(txtDestDir.getText(), ProgConst.MOSAIK_BW_STD_NAME));
        }
    }


    private void initCont() {
        // SRC
        btnSrc.setOnAction(event -> {
            DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtSrc);
        });
        btnSrc.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        final Button btnHelpSrc = new Button("");
        btnHelpSrc.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSrc.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.FILEMANAGER));

        txtSrc.setMaxWidth(Double.MAX_VALUE);

        // DEST
        txtDestName.setMaxWidth(Double.MAX_VALUE);

        btnDest.setOnAction(event -> DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, txtDestDir));
        btnDest.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        final Button btnHelpDest = new Button("");
        btnHelpDest.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpDest.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.FILEMANAGER));

        txtDestDir.setMaxWidth(Double.MAX_VALUE);


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


        // make Grid
        int row = 0;
        GridPane gridPaneDest = new GridPane();
        gridPaneDest.setPadding(new Insets(0));
        gridPaneDest.setVgap(5);
        gridPaneDest.setHgap(5);

        GridPane.setHgrow(txtDestName, Priority.ALWAYS);
        GridPane.setHgrow(txtDestDir, Priority.ALWAYS);
        GridPane.setHgrow(sliderSize, Priority.ALWAYS);
        GridPane.setHgrow(sliderCount, Priority.ALWAYS);

        gridPaneDest.add(new Label("Foto zum Erstellen des Mosaik"), 0, row, 2, 1);
        gridPaneDest.add(new Label("Datei:"), 0, ++row);
        gridPaneDest.add(txtSrc, 1, row);
        gridPaneDest.add(btnSrc, 2, row);
        gridPaneDest.add(btnHelpSrc, 3, row);

        gridPaneDest.add(new Label(""), 0, ++row);
        gridPaneDest.add(new Label("Mosaik speichern"), 0, ++row, 2, 1);

        gridPaneDest.add(new Label("Verzeichnis:"), 0, ++row);
        gridPaneDest.add(txtDestDir, 1, row);
        gridPaneDest.add(btnDest, 2, row);
        gridPaneDest.add(btnHelpDest, 3, row);

        gridPaneDest.add(new Label("Dateiname:"), 0, ++row);
        gridPaneDest.add(txtDestName, 1, row);

        gridPaneDest.add(new Label(""), 0, ++row);
        gridPaneDest.add(new Label("Größe der Miniaturbilder (Pixel):"), 0, ++row, 2, 1);
        gridPaneDest.add(sliderSize, 0, ++row, 2, 1);
        gridPaneDest.add(lblSlider, 2, row);
        gridPaneDest.add(btnHelpSlider, 3, row);

        gridPaneDest.add(new Label("Anzahl Miniaturbilder im Mosaik (Breite):"), 0, ++row, 2, 1);
        gridPaneDest.add(sliderCount, 0, ++row, 2, 1);
        gridPaneDest.add(lblSliderCount, 2, row);
        gridPaneDest.add(btnHelpSliderCount, 3, row);

        gridPaneDest.add(new Label(""), 0, ++row);
        gridPaneDest.add(cbxBlackWhite, 0, ++row, 2, 1);

        // import all
        contPane.setSpacing(25);
        contPane.setPadding(new Insets(10));
        contPane.getChildren().addAll(gridPaneDest, btnCreate);

        btnCreate.setOnAction(a -> {
            if (!txtSrc.getText().isEmpty() && !txtDestDir.getText().isEmpty()) {
                progData.worker.createMosaikBw(mosaikDataBw);
            }
        });

    }

    private void unbind() {
        if (mosaikDataBw == null) {
            return;
        }

        // SRC
        txtSrc.textProperty().unbindBidirectional(mosaikDataBw.fotoSrcProperty());

        // DEST
        txtDestName.textProperty().unbindBidirectional(mosaikDataBw.fotoDestNameProperty());
        txtDestDir.textProperty().unbindBidirectional(mosaikDataBw.fotoDestDirProperty());

        // Black/White
        cbxBlackWhite.selectedProperty().unbindBidirectional(mosaikDataBw.blackWhiteProperty());

        // Thumbsize
        iPropSize.unbind();
        mosaikDataBw.thumbSizeProperty().unbind();
        lblSlider.textProperty().unbind();

        // Anzahl Thumbs
        iPropCount.unbind();
        mosaikDataBw.numberThumbsWidthProperty().unbind();
        lblSliderCount.textProperty().unbind();
    }

    private void bind() {
        if (mosaikDataBw == null) {
            return;
        }

        // SRC
        txtSrc.textProperty().bindBidirectional(mosaikDataBw.fotoSrcProperty());

        // DEST
        txtDestName.textProperty().bindBidirectional(mosaikDataBw.fotoDestNameProperty());
        txtDestDir.textProperty().bindBidirectional(mosaikDataBw.fotoDestDirProperty());

        // Black/White
        cbxBlackWhite.selectedProperty().bindBidirectional(mosaikDataBw.blackWhiteProperty());


        // Thumbsize
        sliderSize.setValue(mosaikDataBw.getThumbSize() / 10);
        iPropSize.bind(sliderSize.valueProperty());

        NumberBinding nb = Bindings.multiply(iPropSize, 10);
        mosaikDataBw.thumbSizeProperty().bind(nb);

        lblSlider.textProperty().bind(Bindings.format("%d", mosaikDataBw.thumbSizeProperty()));

        // Anzahl Thumbs
        sliderCount.setValue(mosaikDataBw.getNumberThumbsWidth() / 10);
        iPropCount.bind(sliderCount.valueProperty());

        NumberBinding nbCount = Bindings.multiply(iPropCount, 10);
        mosaikDataBw.numberThumbsWidthProperty().bind(nbCount);

        lblSliderCount.textProperty().bind(Bindings.format("%d", mosaikDataBw.numberThumbsWidthProperty()));
    }
}
