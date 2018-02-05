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

import de.p2tools.controller.config.ProgConfig;
import de.p2tools.controller.config.ProgConst;
import de.p2tools.controller.config.ProgData;
import de.p2tools.controller.data.Icons;
import de.p2tools.controller.data.mosaikData.MosaikData;
import de.p2tools.gui.dialog.MTAlert;
import de.p2tools.p2Lib.tools.DirFileChooser;
import de.p2tools.p2Lib.tools.FileUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class GuiMosaikPane extends AnchorPane {

    private final ProgData progData;
    private final ScrollPane scrollPane = new ScrollPane();
    //    private final VBox vBoxCont = new VBox();
    private final VBox contPane = new VBox();

    private final Button btnSrc = new Button("");
    private final TextField txtDestName = new TextField();
    private final Slider sliderSize = new Slider();
    private final Label lblSlider = new Label("");
    private final Slider sliderCount = new Slider();
    private final Label lblSliderCount = new Label("");
    private final ComboBox<String> cbSrcPhoto = new ComboBox<>();

    private final TextField txtDestDir = new TextField();
    private final Button btnDest = new Button("");


    private final IntegerProperty iPropSize = new SimpleIntegerProperty();
    private final IntegerProperty iPropCount = new SimpleIntegerProperty();

    MosaikData mosaikData = null;

    public GuiMosaikPane() {
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
//        this.setStyle("-fx-border-color: red;");

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
        if (txtDestDir.getText().isEmpty()) {
            txtDestDir.setText(progData.selectedProjectData.getDestDir());
        }
        if (txtDestName.getText().isEmpty()) {
            txtDestName.setText(FileUtils.getNextFileName(txtDestDir.getText(), ProgConst.MOSAIK_STD_NAME));
        }
    }


    private void initCont() {
        // SRC
        btnSrc.setOnAction(event -> {
            String foto = DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, cbSrcPhoto);
        });
        btnSrc.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        final Button btnHelpSrc = new Button("");
        btnHelpSrc.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSrc.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.FILEMANAGER));

        cbSrcPhoto.setMaxWidth(Double.MAX_VALUE);
        cbSrcPhoto.getItems().addListener((ListChangeListener<String>) c ->
                ProgConfig.CONFIG_DIR_SRC_PHOTO_PATH.setValue(saveComboPfad(cbSrcPhoto))
        );

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
        gridPaneDest.setVgap(10);
        gridPaneDest.setHgap(10);

        GridPane.setHgrow(cbSrcPhoto, Priority.ALWAYS);
        GridPane.setHgrow(txtDestName, Priority.ALWAYS);
        GridPane.setHgrow(txtDestDir, Priority.ALWAYS);
        GridPane.setHgrow(sliderSize, Priority.ALWAYS);
        GridPane.setHgrow(sliderCount, Priority.ALWAYS);

        Label lbl = new Label("Foto als Vorlage zum Erstellen des Mosaik");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lbl, Priority.ALWAYS);
        gridPaneDest.add(lbl, 0, row, 4, 1);
        gridPaneDest.add(new Label("Datei:"), 0, ++row);
        gridPaneDest.add(cbSrcPhoto, 1, row);
        gridPaneDest.add(btnSrc, 2, row);
        gridPaneDest.add(btnHelpSrc, 3, row);

        lbl = new Label("Mosaik speichern");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lbl, Priority.ALWAYS);
        gridPaneDest.add(new Label(" "), 0, ++row);
        gridPaneDest.add(lbl, 0, ++row, 4, 1);

        gridPaneDest.add(new Label("Verzeichnis:"), 0, ++row);
        gridPaneDest.add(txtDestDir, 1, row);
        gridPaneDest.add(btnDest, 2, row);
        gridPaneDest.add(btnHelpDest, 3, row);

        gridPaneDest.add(new Label("Dateiname:"), 0, ++row);
        gridPaneDest.add(txtDestName, 1, row);

        lbl = new Label("Größe der Miniaturbilder (Pixel):");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lbl, Priority.ALWAYS);
        gridPaneDest.add(new Label(" "), 0, ++row);
        gridPaneDest.add(lbl, 0, ++row, 4, 1);
        gridPaneDest.add(sliderSize, 0, ++row, 2, 1);
        gridPaneDest.add(lblSlider, 2, row);
        gridPaneDest.add(btnHelpSlider, 3, row);

        lbl = new Label("Anzahl Miniaturbilder im Mosaik pro Zeile");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lbl, Priority.ALWAYS);
        gridPaneDest.add(new Label(" "), 0, ++row);
        gridPaneDest.add(lbl, 0, ++row, 4, 1);
        gridPaneDest.add(sliderCount, 0, ++row, 2, 1);
        gridPaneDest.add(lblSliderCount, 2, row);
        gridPaneDest.add(btnHelpSliderCount, 3, row);

        // import all
        contPane.setSpacing(25);
        contPane.setPadding(new Insets(10));
        contPane.getChildren().addAll(gridPaneDest);


    }

    private void unbind() {
        if (mosaikData == null) {
            return;
        }

        // SRC
        mosaikData.fotoSrcProperty().unbind();

        // DEST
        txtDestName.textProperty().unbindBidirectional(mosaikData.fotoDestNameProperty());
        txtDestDir.textProperty().unbindBidirectional(mosaikData.fotoDestDirProperty());

        // Thumbsize

        iPropSize.unbind();
        mosaikData.thumbSizeProperty().unbind();
        lblSlider.textProperty().unbind();

        // Anzahl Thumbs
        iPropCount.unbind();
        mosaikData.numberThumbsWidthProperty().unbind();
        lblSliderCount.textProperty().unbind();
    }

    private void bind() {
        if (mosaikData == null) {
            return;
        }

        // SRC
        String[] storedPhotoPath = {""};
        storedPhotoPath = ProgConfig.CONFIG_DIR_SRC_PHOTO_PATH.get().split(ProgConst.DIR_SEPARATOR);
        cbSrcPhoto.getItems().addAll(storedPhotoPath);
        if (!mosaikData.getFotoSrc().isEmpty() && !cbSrcPhoto.getItems().contains(mosaikData.getFotoSrc())) {
            cbSrcPhoto.getItems().add(mosaikData.getFotoSrc());
        }
        cbSrcPhoto.getSelectionModel().select(mosaikData.getFotoSrc());
        mosaikData.fotoSrcProperty().bind(cbSrcPhoto.getSelectionModel().selectedItemProperty());

        // DEST
        txtDestName.textProperty().bindBidirectional(mosaikData.fotoDestNameProperty());
        txtDestDir.textProperty().bindBidirectional(mosaikData.fotoDestDirProperty());

        // Thumbsize
        sliderSize.setValue(mosaikData.getThumbSize() / 10);
        iPropSize.bind(sliderSize.valueProperty());

        NumberBinding nb = Bindings.multiply(iPropSize, 10);
        mosaikData.thumbSizeProperty().bind(nb);

        lblSlider.textProperty().bind(Bindings.format("%d", mosaikData.thumbSizeProperty()));

        // Anzahl Thumbs
        sliderCount.setValue(mosaikData.getNumberThumbsWidth() / 10);
        iPropCount.bind(sliderCount.valueProperty());

        NumberBinding nbCount = Bindings.multiply(iPropCount, 10);
        mosaikData.numberThumbsWidthProperty().bind(nbCount);

        lblSliderCount.textProperty().bind(Bindings.format("%d", mosaikData.numberThumbsWidthProperty()));
    }

    private String saveComboPfad(ComboBox<String> comboBox) {
        final ArrayList<String> pfade = new ArrayList<>(comboBox.getItems());

        final ArrayList<String> pfade2 = new ArrayList<>();
        String sel = comboBox.getEditor().getText();
        if (sel != null && !sel.isEmpty()) {
            System.out.println(sel);
            pfade2.add(sel);
        }

        pfade.stream().forEach(s1 -> {
            // um doppelte auszusortieren

            if (!s1.isEmpty() && !pfade2.contains(s1)) {
                pfade2.add(s1);
            }
        });

        String s = "";
        if (!pfade2.isEmpty()) {
            s = pfade2.get(0);
            for (int i = 1; i < ProgConst.MAX_PFADE_SRC_PHOTO && i < pfade2.size(); ++i) {
                if (!pfade2.get(i).isEmpty()) {
                    s += ProgConst.DIR_SEPARATOR + pfade2.get(i);
                }
            }
        }

        return s;
    }
}
