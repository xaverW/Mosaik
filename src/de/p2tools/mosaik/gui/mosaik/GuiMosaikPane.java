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
import de.p2tools.mosaik.controller.config.ProgConst;
import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.controller.data.Icons;
import de.p2tools.mosaik.controller.data.mosaikData.MosaikData;
import de.p2tools.mosaik.gui.HelpText;
import de.p2tools.mosaik.gui.tools.GuiTools;
import de.p2tools.p2Lib.dialog.DirFileChooser;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.dialog.PComboBox;
import de.p2tools.p2Lib.image.ImgFile;
import de.p2tools.p2Lib.image.ImgTools;
import de.p2tools.p2Lib.tools.FileUtils;
import de.p2tools.p2Lib.tools.Log;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Locale;

public class GuiMosaikPane extends AnchorPane {

    private final ProgData progData;
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox contPane = new VBox();

    private final Label lblSize = new Label("Das Mosaik wird eine Größe haben von:");
    private final Button btnSrc = new Button("");
    private final Slider sliderSize = new Slider();
    private final Label lblSlider = new Label("");
    private final Slider sliderCount = new Slider();
    private final Label lblSliderCount = new Label("");
    private final PComboBox cbSrcPhoto = new PComboBox();

    private final PComboBox cbDestDir = new PComboBox();
    private final Button btnDest = new Button("");

    private final Label lblSrcFile = new Label("Datei:");
    private final Label lblDestDir = new Label("Datei:");

    private int srcHeight = 0;
    private int srcWidth = 0;


    private final IntegerProperty iPropSize = new SimpleIntegerProperty();
    private final IntegerProperty iPropCount = new SimpleIntegerProperty();
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
    BooleanBinding dirBinding;

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
        initSizePane();
        initColor();
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
        if (cbDestDir.getSel().isEmpty()) {
            String suff;
            if (mosaikData.getFormat().equals(ImgFile.IMAGE_FORMAT_PNG)) {
                suff = ImgFile.IMAGE_FORMAT_PNG;
            } else {
                suff = ImgFile.IMAGE_FORMAT_JPG;
            }
            Path p = Paths.get(progData.selectedProjectData.getDestDir(),
                    FileUtils.getNextFileName(progData.selectedProjectData.getDestDir(),
                            ProgConst.MOSAIK_STD_NAME, suff));
            cbDestDir.selectElement(p.toString());
        }
    }

    private void initColor() {
        dirBinding = Bindings.createBooleanBinding(() -> cbDestDir.getSel().trim().isEmpty(),
                cbDestDir.getSelProperty());

        //todo
        cbSrcPhoto.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    GuiTools.setColor(lblSrcFile, newValue == null || newValue.trim().isEmpty());
                    getSrcSize();
                }
        );

        GuiTools.setColor(cbDestDir, dirBinding.get());

        GuiTools.setColor(lblSrcFile, cbSrcPhoto.getSelectionModel().getSelectedItem() == null ||
                cbSrcPhoto.getSelectionModel().getSelectedItem().trim().isEmpty());

        dirBinding.addListener(l -> GuiTools.setColor(cbDestDir, dirBinding.get()));
        dirBinding.addListener(l -> GuiTools.setColor(lblDestDir, dirBinding.get()));
    }


    private void initCont() {
        // SRC
        btnSrc.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, cbSrcPhoto));
        btnSrc.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        cbSrcPhoto.init(ProgConfig.CONFIG_SRC_PHOTO_PATH_LIST, ProgConfig.CONFIG_SRC_PHOTO_PATH_SEL);
        getSrcSize();

        final Button btnHelpSrc = new Button("");
        btnHelpSrc.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSrc.setOnAction(a -> new PAlert().showHelpAlert("Mosaikvorlage", HelpText.IMAGE_TAMPLATE));


        // DEST
        btnDest.setOnAction(event -> DirFileChooser.FileChooserSave(ProgData.getInstance().primaryStage, cbDestDir,
                "Mosaik.jpg"));
        btnDest.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        cbDestDir.init(ProgConfig.CONFIG_DEST_PHOTO_PATH_LIST, ProgConfig.CONFIG_DEST_PHOTO_PATH_SEL);

        final Button btnHelpDest = new Button("");
        btnHelpDest.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpDest.setOnAction(a -> new PAlert().showHelpAlert("Mosaik", HelpText.MOSAIK_DEST));


        // Thumbsize
        final Button btnHelpSlider = new Button("");
        btnHelpSlider.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSlider.setOnAction(a -> new PAlert().showHelpAlert("Pixel", HelpText.MOSAIK_PIXEL_SIZE));


        sliderSize.setMin(5);
        sliderSize.setMax(25);
        sliderCount.setMin(1);
        sliderCount.setMax(100);


        // make Grid
        int row = 0;
        GridPane gridPaneDest = new GridPane();
        gridPaneDest.setPadding(new Insets(0));
        gridPaneDest.setVgap(10);
        gridPaneDest.setHgap(10);

        cbSrcPhoto.setMaxWidth(Double.MAX_VALUE);
        cbDestDir.setMaxWidth(Double.MAX_VALUE);

        GridPane.setHgrow(cbSrcPhoto, Priority.ALWAYS);
        GridPane.setHgrow(cbDestDir, Priority.ALWAYS);
        GridPane.setHgrow(sliderSize, Priority.ALWAYS);
        GridPane.setHgrow(sliderCount, Priority.ALWAYS);

        Label lbl = new Label("Foto als Vorlage zum Erstellen des Mosaik");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lbl, Priority.ALWAYS);
        gridPaneDest.add(lbl, 0, row, 4, 1);
        gridPaneDest.add(lblSrcFile, 0, ++row);
        gridPaneDest.add(cbSrcPhoto, 1, row);
        gridPaneDest.add(btnSrc, 2, row);
        gridPaneDest.add(btnHelpSrc, 3, row);

        lbl = new Label("Mosaik speichern");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lbl, Priority.ALWAYS);
        gridPaneDest.add(new Label(" "), 0, ++row);
        gridPaneDest.add(lbl, 0, ++row, 4, 1);

        gridPaneDest.add(lblDestDir, 0, ++row);
        gridPaneDest.add(cbDestDir, 1, row);
        gridPaneDest.add(btnDest, 2, row);
        gridPaneDest.add(btnHelpDest, 3, row);

        lbl = new Label("Größe und Anzahl der Miniaturbilder");
        Label lblSize = new Label("Größe (Pixel):");
        Label lblSum = new Label("Anzahl pro Zeile:");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lbl, Priority.ALWAYS);

        gridPaneDest.add(new Label(" "), 0, ++row);
        gridPaneDest.add(lbl, 0, ++row, 4, 1);

        gridPaneDest.add(lblSize, 0, ++row);
        gridPaneDest.add(sliderSize, 1, row);
        gridPaneDest.add(lblSlider, 2, row);
        gridPaneDest.add(btnHelpSlider, 3, row);

        gridPaneDest.add(new Label(" "), 0, ++row);
        gridPaneDest.add(lblSum, 0, ++row);
        gridPaneDest.add(sliderCount, 1, row);
        gridPaneDest.add(lblSliderCount, 2, row);

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
        mosaikData.fotoDestProperty().unbind();

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
        cbSrcPhoto.selectElement(mosaikData.getFotoSrc());
        mosaikData.fotoSrcProperty().bind(cbSrcPhoto.getSelProperty());

        // DEST
        cbDestDir.selectElement(mosaikData.getFotoDest());
        mosaikData.fotoDestProperty().bind(cbDestDir.getSelProperty());

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

    private void initSizePane() {
        lblSize.getStyleClass().add("headerLabel");
        lblSize.setMaxWidth(Double.MAX_VALUE);
        lblSize.setWrapText(true);

        sliderSize.valueProperty().addListener((observable, oldValue, newValue) -> setSize());
        sliderCount.valueProperty().addListener((observable, oldValue, newValue) -> setSize());
        setSize();
        
        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(lblSize);
        VBox.setVgrow(vBox, Priority.ALWAYS);
        vBox.setAlignment(Pos.BOTTOM_LEFT);

        contPane.getChildren().add(vBox);
    }

    private void getSrcSize() {
        try {
            if (mosaikData.getFotoSrc().isEmpty()) {
                srcHeight = 0;
                srcWidth = 0;
                return;
            }

            BufferedImage srcImg = ImgFile.getBufferedImage(new File(mosaikData.getFotoSrc()));
            srcHeight = srcImg.getRaster().getHeight();
            srcWidth = srcImg.getRaster().getWidth();
        } catch (Exception ex) {
            srcHeight = 0;
            srcWidth = 0;
            Log.errorLog(945123690, ex);
        } finally {
            setSize();
        }
    }

    private Text setSize() {
        Text ret = new Text();

        int pixelW, pixelH;
        pixelW = 100 * (int) sliderSize.getValue() * (int) sliderCount.getValue();

        if (srcHeight > 0 && srcWidth > 0) {
            pixelH = pixelW * srcHeight / srcWidth;
        } else {
            pixelH = pixelW;
        }

        String fileSize = ImgTools.getImgFileSizeStr(pixelW, pixelH);
        ret.setText("Das Mosaik hat eine Breite von " + numberFormat.format(pixelW) +
                " und eine Höhe von " + numberFormat.format(pixelH) + " Pixeln." +
                "\n" +
                "Die Dateigröße wird etwa " + fileSize + " haben.");

        lblSize.setText(ret.getText());
        return ret;
    }


}
