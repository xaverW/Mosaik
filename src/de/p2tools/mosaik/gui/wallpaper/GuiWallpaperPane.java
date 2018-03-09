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

import de.p2tools.mosaik.controller.config.ProgConst;
import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.controller.data.Icons;
import de.p2tools.mosaik.controller.data.mosaikData.WallpaperData;
import de.p2tools.mosaik.gui.HelpText;
import de.p2tools.mosaik.gui.tools.GuiTools;
import de.p2tools.p2Lib.dialog.DirFileChooser;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.tools.FileUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.text.NumberFormat;
import java.util.Locale;

public class GuiWallpaperPane extends AnchorPane {

    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox contPane = new VBox(10);
    private final TextField txtDestName = new TextField();
    private final TextField txtDestDir = new TextField();
    private final Button btnDest = new Button("");
    private final Label lblSize = new Label("Die Fototapete wird die Größe haben von:");
    private final Label lblDestName = new Label("Dateiname: ");
    private final Label lblDestDir = new Label("Verzeichnis:");

    private final Slider sliderSize = new Slider();
    private final Label lblSlider = new Label("");
    private final Slider sliderCount = new Slider();
    private final Label lblSliderCount = new Label("");
    private final IntegerProperty iProp = new SimpleIntegerProperty();
    private final IntegerProperty iPropCount = new SimpleIntegerProperty();
    BooleanBinding dirBinding;
    BooleanBinding nameBinding;

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

        if (txtDestDir.getText().isEmpty()) {
            txtDestDir.setText(progData.selectedProjectData.getDestDir());
        }
        if (txtDestName.getText().isEmpty()) {
            txtDestName.setText(FileUtils.getNextFileName(txtDestDir.getText(), ProgConst.WALLPAPER_STD_NAME));
        }

    }

    private void initColor() {
        dirBinding = Bindings.createBooleanBinding(() -> txtDestDir.getText().trim().isEmpty(), txtDestDir.textProperty());
        nameBinding = Bindings.createBooleanBinding(() -> txtDestName.getText().trim().isEmpty(), txtDestName.textProperty());

        GuiTools.setColor(txtDestDir, dirBinding.get());
        GuiTools.setColor(txtDestName, nameBinding.get());

        dirBinding.addListener(l -> GuiTools.setColor(txtDestDir, dirBinding.get()));
        dirBinding.addListener(l -> GuiTools.setColor(lblDestDir, dirBinding.get()));
        nameBinding.addListener(l -> GuiTools.setColor(txtDestName, nameBinding.get()));
        nameBinding.addListener(l -> GuiTools.setColor(lblDestName, nameBinding.get()));
    }

    private void initCont() {
        // DEST
        btnDest.setOnAction(event -> {
            DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, txtDestDir);
        });
        btnDest.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        final Button btnHelpDest = new Button("");
        btnHelpDest.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpDest.setOnAction(a -> new PAlert().showHelpAlert("Mosaik", HelpText.WALLPAPER_DEST));


        // Thumbsize
        final Button btnHelpSlider = new Button("");
        btnHelpSlider.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSlider.setOnAction(a -> new PAlert().showHelpAlert("Pixelgröße", HelpText.WALLPAPER_PIXEL_SIZE));

        sliderSize.setMin(5);
        sliderSize.setMax(25);


        // Anzahl Thumbs
        final Button btnHelpSliderCount = new Button("");
        btnHelpSliderCount.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSliderCount.setOnAction(a -> new PAlert().showHelpAlert("Mosaikgröße", HelpText.WALLPAPER_PIXEL_COUNT));

        sliderCount.setMin(1);
        sliderCount.setMax(100);


        int row = 0;
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(0));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        GridPane.setHgrow(txtDestDir, Priority.ALWAYS);
        GridPane.setHgrow(txtDestName, Priority.ALWAYS);
        GridPane.setHgrow(sliderSize, Priority.ALWAYS);
        GridPane.setHgrow(sliderCount, Priority.ALWAYS);

        Label lbl = new Label("Fototapete speichern");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);

        gridPane.add(lbl, 0, row, 4, 1);
        gridPane.add(lblDestDir, 0, ++row);
        gridPane.add(txtDestDir, 1, row);
        gridPane.add(btnDest, 2, row);
        gridPane.add(btnHelpDest, 3, row);

        gridPane.add(lblDestName, 0, ++row);
        gridPane.add(txtDestName, 1, row);

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
        gridPane.add(btnHelpSliderCount, 3, row);


        setSize();
        sliderSize.valueProperty().addListener((observable, oldValue, newValue) -> setSize());
        sliderCount.valueProperty().addListener((observable, oldValue, newValue) -> setSize());

        contPane.setPadding(new Insets(10));
        contPane.getChildren().addAll(gridPane);
    }

    private void unbind() {
        if (wallpaperData == null) {
            return;
        }

        // DEST
        txtDestDir.textProperty().unbindBidirectional(wallpaperData.fotoDestDirProperty());
        txtDestName.textProperty().unbindBidirectional(wallpaperData.fotoDestNameProperty());

        // Thumbsize
        iProp.bind(sliderSize.valueProperty());
        wallpaperData.thumbSizeProperty().unbind();
        lblSlider.textProperty().unbind();

        // Anzahl Thumbs
        iPropCount.unbind();
        wallpaperData.numberThumbsWidthProperty().unbind();
        lblSliderCount.textProperty().unbind();
    }

    private void bind() {
        if (wallpaperData == null) {
            return;
        }

        // DEST
        txtDestDir.textProperty().bindBidirectional(wallpaperData.fotoDestDirProperty());
        txtDestName.textProperty().bindBidirectional(wallpaperData.fotoDestNameProperty());

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

    private void setSize() {
        Text text = new Text();
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
        if (progData.selectedProjectData == null) {
            lblSize.setText("");
        }
        int picW, picH;

        int thumbs = progData.selectedProjectData.getThumbCollection().getThumbList().getSize();

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
        long fSize = (long) (16.0 * pixelW * pixelH / 10.0 / 1024.0); // filesize kB and with jpg-compression

        String fileSize = numberFormat.format(fSize) + " KByte";
        if (fSize > 1024) {
            fSize /= 1024;
            fileSize = numberFormat.format(fSize) + " MByte";
        }
        if (fSize > 1024) {
            fSize /= 1024;
            fileSize = numberFormat.format(fSize) + " GByte";
        }

        text.setText("Die Fototapete hat eine Breite und Höhe von " + pixelW + " * " + pixelH + " Pixeln." +
                "\n" +
                "Die Dateigröße wird etwa " + fileSize + " haben.");

        lblSize.setText(text.getText());
    }

}
