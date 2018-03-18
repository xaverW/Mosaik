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

import de.p2tools.mosaik.controller.config.ProgConfig;
import de.p2tools.mosaik.controller.config.ProgConst;
import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.controller.data.Icons;
import de.p2tools.mosaik.controller.data.mosaikData.WallpaperData;
import de.p2tools.mosaik.gui.HelpText;
import de.p2tools.mosaik.gui.tools.GuiTools;
import de.p2tools.p2Lib.dialog.DirFileChooser;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.dialog.PComboBox;
import de.p2tools.p2Lib.image.ImgFile;
import de.p2tools.p2Lib.image.ImgTools;
import de.p2tools.p2Lib.tools.FileUtils;
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
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Locale;

public class GuiWallpaperPane extends AnchorPane {

    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox contPane = new VBox(10);
    private final PComboBox cbDest = new PComboBox();
    private final Button btnDest = new Button("");
    private final Label lblSize = new Label("Die Fototapete wird die Größe haben von:");
    private final Label lblDest = new Label("Datei:");

    private final Slider sliderSize = new Slider();
    private final Label lblSlider = new Label("");
    private final Slider sliderCount = new Slider();
    private final Label lblSliderCount = new Label("");
    private final IntegerProperty iProp = new SimpleIntegerProperty();
    private final IntegerProperty iPropCount = new SimpleIntegerProperty();

    private final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
    BooleanBinding dirBinding;

    private final ProgData progData;
    private WallpaperData wallpaperData = null;

    public GuiWallpaperPane() {
        this.progData = ProgData.getInstance();
        if (progData.selectedProjectData != null) {
            this.wallpaperData = progData.selectedProjectData.getWallpaperData();
        }

        contPane.setPadding(new Insets(0, 0, 10, 0));
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
        if (!wallpaperData.equals(progData.selectedProjectData.getWallpaperData())) {
            unbind();
            wallpaperData = progData.selectedProjectData.getWallpaperData();
            bind();
        }

        if (cbDest.getSel().isEmpty()) {
            String suff;
            if (wallpaperData.getFormat().equals(ImgFile.IMAGE_FORMAT_PNG)) {
                suff = ImgFile.IMAGE_FORMAT_PNG;
            } else {
                suff = ImgFile.IMAGE_FORMAT_JPG;
            }

            Path p = Paths.get(progData.selectedProjectData.getDestDir(),
                    FileUtils.getNextFileName(progData.selectedProjectData.getDestDir(),
                            ProgConst.WALLPAPER_STD_NAME, suff));
            cbDest.selectElement(p.toString());
        }

        setSize(); // falls sich die Liste der Thumbs geändert hat
    }

    private void initCont() {
        // DEST
        btnDest.setOnAction(event -> {
            DirFileChooser.FileChooserSave(ProgData.getInstance().primaryStage, cbDest,
                    progData.selectedProjectData.getDestDir(),
                    "");
        });
        btnDest.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        cbDest.init(ProgConfig.CONFIG_DEST_WALLPAPER_PATH_LIST, ProgConfig.CONFIG_DEST_WALLPAPER_PATH_SEL);

        final Button btnHelpDest = new Button("");
        btnHelpDest.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpDest.setOnAction(a -> new PAlert().showHelpAlert("Mosaik", HelpText.WALLPAPER_DEST));


        // Thumbsize
        final Button btnHelpSlider = new Button("");
        btnHelpSlider.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSlider.setOnAction(a -> new PAlert().showHelpAlert("Pixelgröße", HelpText.WALLPAPER_PIXEL_SIZE));

        sliderSize.setMin(5);
        sliderSize.setMax(25);
        sliderCount.setMin(1);
        sliderCount.setMax(100);


        int row = 0;
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(0));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        cbDest.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(cbDest, Priority.ALWAYS);
        GridPane.setHgrow(sliderSize, Priority.ALWAYS);
        GridPane.setHgrow(sliderCount, Priority.ALWAYS);

        Label lbl = new Label("Fototapete speichern");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);

        gridPane.add(lbl, 0, row, 4, 1);
        gridPane.add(lblDest, 0, ++row);
        gridPane.add(cbDest, 1, row);
        gridPane.add(btnDest, 2, row);
        gridPane.add(btnHelpDest, 3, row);

        lbl = new Label("Größe und Anzahl der Miniaturbilder");
        Label lblSize = new Label("Größe (Pixel):");
        Label lblSum = new Label("Anzahl pro Zeile:");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lbl, Priority.ALWAYS);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(lbl, 0, ++row, 4, 1);

        gridPane.add(lblSize, 0, ++row);
        gridPane.add(sliderSize, 1, row);
        gridPane.add(lblSlider, 2, row);
        gridPane.add(btnHelpSlider, 3, row);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(lblSum, 0, ++row);
        gridPane.add(sliderCount, 1, row);
        gridPane.add(lblSliderCount, 2, row);

        ColumnConstraints c0 = new ColumnConstraints();
        gridPane.getColumnConstraints().addAll(c0);
        c0.setMinWidth(GridPane.USE_PREF_SIZE);

        contPane.setPadding(new Insets(10));
        contPane.getChildren().addAll(gridPane);
    }

    private void initSizePane() {
        lblSize.getStyleClass().add("headerLabel");
        lblSize.setMaxWidth(Double.MAX_VALUE);
        lblSize.setWrapText(true);

        sliderSize.valueProperty().addListener((observable, oldValue, newValue) -> setSize());
        sliderCount.valueProperty().addListener((observable, oldValue, newValue) -> setSize());

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(lblSize);
        VBox.setVgrow(vBox, Priority.ALWAYS);
        vBox.setAlignment(Pos.BOTTOM_LEFT);

        contPane.getChildren().add(vBox);
    }

    private void initColor() {
        dirBinding = Bindings.createBooleanBinding(() -> cbDest.getSel().trim().isEmpty(), cbDest.getSelProperty());
        GuiTools.setColor(cbDest, dirBinding.get());

        dirBinding.addListener(l -> GuiTools.setColor(cbDest, dirBinding.get()));
        dirBinding.addListener(l -> GuiTools.setColor(lblDest, dirBinding.get()));
    }

    private void bind() {
        if (wallpaperData == null) {
            return;
        }

        // DEST
        cbDest.selectElement(wallpaperData.getFotoDest());
        wallpaperData.fotoDestProperty().bind(cbDest.getSelProperty());

        // Thumbsize
        sliderSize.setValue(wallpaperData.getThumbSize() / 10);
        iProp.bind(sliderSize.valueProperty());
        NumberBinding nb = Bindings.multiply(iProp, 10);
        wallpaperData.thumbSizeProperty().bind(nb);
        lblSlider.textProperty().bind(Bindings.format("%d", wallpaperData.thumbSizeProperty()));

        // Anzahl Thumbs
        sliderCount.setValue(wallpaperData.getNumberThumbsWidth() / 10);
        iPropCount.bind(sliderCount.valueProperty());
        NumberBinding nbCount = Bindings.multiply(iPropCount, 10);
        wallpaperData.numberThumbsWidthProperty().bind(nbCount);
        lblSliderCount.textProperty().bind(Bindings.format("%d", wallpaperData.numberThumbsWidthProperty()));
    }

    private void unbind() {
        if (wallpaperData == null) {
            return;
        }

        // DEST
        wallpaperData.fotoDestProperty().unbind();

        // Thumbsize
        iProp.bind(sliderSize.valueProperty());
        wallpaperData.thumbSizeProperty().unbind();
        lblSlider.textProperty().unbind();

        // Anzahl Thumbs
        iPropCount.unbind();
        wallpaperData.numberThumbsWidthProperty().unbind();
        lblSliderCount.textProperty().unbind();
    }

    private void setSize() {
        if (progData.selectedProjectData == null) {
            lblSize.setText("");
            return;
        }

        Text text = new Text();
        int picW, picH;
        int thumbs = progData.selectedProjectData.getThumbCollection().getThumbList().getSize();

        if (thumbs == 0) {
            text.setText("Es sind keine Miniaturbilder in der Sammlung.");
            lblSize.setText(text.getText());

        } else {
            if (10 * (int) sliderCount.getValue() >= thumbs) {
                picW = thumbs;
                picH = 1;
            } else {
                picW = 10 * (int) sliderCount.getValue();
                picH = thumbs / picW;
                if (thumbs % picW > 0) {
                    picH += 1;
                }
            }

            int pixelW = 10 * (int) sliderSize.getValue() * picW;
            int pixelH = 10 * (int) sliderSize.getValue() * picH;

            String fileSize = ImgTools.getImgFileSizeStr(pixelW, pixelH);
            text.setText("Die Fototapete hat eine Breite und Höhe von " +
                    numberFormat.format(pixelW) + " * " +
                    numberFormat.format(pixelH) + " Pixeln." +
                    "\n" +
                    "Die Dateigröße wird etwa " + fileSize + " betragen.");

            lblSize.setText(text.getText());
        }
    }

}
