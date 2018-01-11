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

import de.p2tools.controller.config.ProgData;
import de.p2tools.controller.data.Icons;
import de.p2tools.controller.data.mosaikData.MosaikData;
import de.p2tools.controller.genMosaik.GenMosaik;
import de.p2tools.gui.dialog.MTAlert;
import de.p2tools.mLib.tools.DirFileChooser;
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

public class MosaikGuiController extends AnchorPane {

    private final ProgData progData;
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox vBoxCont = new VBox();
    private final Label lblSrc = new Label("Foto als Vorlage zum Erstellen des Mosaik");
    private final TextField txtSrc = new TextField();
    private final Button btnSrc = new Button("");
    private final Label lblDestName = new Label("Dateiname des Mosaik");
    private final TextField txtDestName = new TextField();

    private final Label lblDest = new Label("Mosaik im Ordner speichern");
    private final TextField txtDestDir = new TextField();
    private final Button btnDest = new Button("");
    private final Button btnCreate = new Button("Mosaik erstellen");

    MosaikData mosaikData;

    public MosaikGuiController() {
        progData = ProgData.getInstance();
        mosaikData = progData.selectedProjectData.getMosaikData();

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(vBoxCont);

        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setTopAnchor(scrollPane, 0.0);

        initCont();
        getChildren().addAll(scrollPane);
    }

    public void isShown() {

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

        txtSrc.textProperty().bindBidirectional(mosaikData.fotoSrcProperty());
        txtSrc.setMaxWidth(Double.MAX_VALUE);

        // DEST
        txtDestName.textProperty().bindBidirectional(mosaikData.fotoDestNameProperty());
        txtDestName.setMaxWidth(Double.MAX_VALUE);

        btnDest.setOnAction(event -> DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, txtDestDir));
        btnDest.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        final Button btnHelpDest = new Button("");
        btnHelpDest.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpDest.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.FILEMANAGER));

        txtDestDir.textProperty().bindBidirectional(mosaikData.fotoDestDirProperty());
        txtDestDir.setMaxWidth(Double.MAX_VALUE);

        GridPane.setHgrow(txtDestName, Priority.ALWAYS);
        GridPane.setHgrow(txtDestDir, Priority.ALWAYS);

        // Thumbsize
        final Button btnHelpSlider = new Button("");
        btnHelpSlider.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSlider.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.FILEMANAGER));

        Slider sliderSize = new Slider();
        sliderSize.setMin(5);
        sliderSize.setMax(25);
        GridPane.setHgrow(sliderSize, Priority.ALWAYS);

        sliderSize.setValue(mosaikData.getThumbSize() / 10);
        IntegerProperty iProp = new SimpleIntegerProperty();
        iProp.bind(sliderSize.valueProperty());

        NumberBinding nb = Bindings.multiply(iProp, 10);
        mosaikData.thumbSizeProperty().bind(nb);

        Label lblSlider = new Label("");
        lblSlider.textProperty().bind(Bindings.format("%d", mosaikData.thumbSizeProperty()));

        // Anzahl Thumbs
        final Button btnHelpSliderCount = new Button("");
        btnHelpSliderCount.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSliderCount.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.FILEMANAGER));

        Slider sliderCount = new Slider();
        sliderCount.setMin(1);
        sliderCount.setMax(100);
        GridPane.setHgrow(sliderCount, Priority.ALWAYS);

        sliderCount.setValue(mosaikData.getNumberThumbsWidth() / 10);
        IntegerProperty iPropCount = new SimpleIntegerProperty();
        iPropCount.bind(sliderCount.valueProperty());

        NumberBinding nbCount = Bindings.multiply(iPropCount, 10);
        mosaikData.numberThumbsWidthProperty().bind(nbCount);

        Label lblSliderCount = new Label("");
        lblSliderCount.textProperty().bind(Bindings.format("%d", mosaikData.numberThumbsWidthProperty()));


        // make Grid
        int row = 0;
        GridPane gridPaneDest = new GridPane();
        gridPaneDest.setPadding(new Insets(0));
        gridPaneDest.setVgap(5);
        gridPaneDest.setHgap(5);
        gridPaneDest.add(new Label("Foto zum Erstellen des Mosaik"), 0, row, 2, 1);
        gridPaneDest.add(new Label("Datei:"), 0, ++row);
        gridPaneDest.add(txtSrc, 1, row);
        gridPaneDest.add(btnSrc, 2, row);
        gridPaneDest.add(btnHelpSrc, 3, row);

        gridPaneDest.add(new Label(""), 0, ++row);

        gridPaneDest.add(new Label("Mosaik speichern"), 0, ++row, 2, 1);
        gridPaneDest.add(new Label("Dateiname:"), 0, ++row);
        gridPaneDest.add(txtDestName, 1, row);
        gridPaneDest.add(new Label("Verzeichnis:"), 0, ++row);
        gridPaneDest.add(txtDestDir, 1, row);
        gridPaneDest.add(btnDest, 2, row);
        gridPaneDest.add(btnHelpDest, 3, row);

        gridPaneDest.add(new Label(""), 0, ++row);

        gridPaneDest.add(new Label("Größe der Miniaturbilder (Pixel):"), 0, ++row, 2, 1);
        gridPaneDest.add(sliderSize, 0, ++row, 2, 1);
        gridPaneDest.add(lblSlider, 2, row);
        gridPaneDest.add(btnHelpSlider, 3, row);

        gridPaneDest.add(new Label("Anzahl Miniaturbilder im Mosaik (Breite):"), 0, ++row, 2, 1);
        gridPaneDest.add(sliderCount, 0, ++row, 2, 1);
        gridPaneDest.add(lblSliderCount, 2, row);
        gridPaneDest.add(btnHelpSliderCount, 3, row);

        // import all
        vBoxCont.setSpacing(25);
        vBoxCont.setPadding(new Insets(10));
        vBoxCont.getChildren().addAll(gridPaneDest, btnCreate);


        btnCreate.setOnAction(a -> {
            if (!txtSrc.getText().isEmpty() && !txtDestDir.getText().isEmpty()) {
                new GenMosaik(mosaikData).erstellen();
            }
        });

    }
}
