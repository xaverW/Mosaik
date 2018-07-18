/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * http://zdfmediathk.sourceforge.net/
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


package de.p2tools.mosaic.controller.data.mosaikData;

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigBoolProp;
import de.p2tools.p2Lib.configFile.config.ConfigIntProp;
import de.p2tools.p2Lib.configFile.config.ConfigStringProp;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import de.p2tools.p2Lib.image.ImgFile;
import javafx.beans.property.*;

public class MosaicDataBase extends PDataSample<MosaicData> {
    public static final String TAG = "MosaicData";

    public enum BACKGROUND {
        COLOR("COLOR"), IMAGE("IMAGE");
        private final String name;

        BACKGROUND(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum THUMB_SRC {
        THUMBS("THUMBS"), THUMBS_COLOR("THUMBS_COLOR"), SRC_FOTO("SRC_FOTO");
        private final String name;

        THUMB_SRC(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum THUMB_RESIZE {
        NON("NON"), ALL("ALL"), DARK("DARK"), LIGHT("LIGHT");
        private final String name;

        THUMB_RESIZE(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private final StringProperty format = new SimpleStringProperty(ImgFile.IMAGE_FORMAT_JPG); // Fotoformat: jpg,png
    private final StringProperty fotoSrc = new SimpleStringProperty(""); // File SRC
    private final StringProperty fotoDest = new SimpleStringProperty(""); // File dest
    private final IntegerProperty thumbSize = new SimpleIntegerProperty(50); // Größe des Thumbs Width==Height
    private final IntegerProperty quantityThumbsWidth = new SimpleIntegerProperty(50); // Anzahl Thumbs in der Breite des Dest
    private final IntegerProperty thumbCount = new SimpleIntegerProperty(0); // Anzahl wie oft ein Thumbs verwendet werden kann
    private final StringProperty thumbSrc = new SimpleStringProperty(THUMB_SRC.THUMBS.toString()); // Miniaturbilder die verwendet werden
    private final BooleanProperty blackWhite = new SimpleBooleanProperty(false); // Mosaik aus S/W-Bildern erstellen
    private final StringProperty resizeThumb = new SimpleStringProperty(THUMB_RESIZE.NON.toString()); // dunkle Thumbs werden etwas verkleinert

    private final BooleanProperty addBorder = new SimpleBooleanProperty(false); // einen Rahmen um die Thumbs zeichnen
    private final IntegerProperty borderSize = new SimpleIntegerProperty(0); // Anzahl Pixel um die ein Thumb verleinert wird
    private final StringProperty background = new SimpleStringProperty(BACKGROUND.COLOR.toString()); // Hintergrund ist eine Farbe/Bild
    private final StringProperty borderColor = new SimpleStringProperty(""); // Farbe des Rahmens zwischen den Miniaturbildern
    private final StringProperty bgPic = new SimpleStringProperty(""); // File Hintergrundfoto

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public Config[] getConfigsArr() {
        return new Config[]{
                new ConfigStringProp("format", format),
                new ConfigStringProp("foto-src", fotoSrc),
                new ConfigStringProp("foto-dest-dir", fotoDest),
                new ConfigIntProp("thumb-size", thumbSize),
                new ConfigIntProp("number-thumbs-width", quantityThumbsWidth),
                new ConfigIntProp("thumb-count", thumbCount),
                new ConfigStringProp("thumb-src", thumbSrc),
                new ConfigBoolProp("black-white", blackWhite),
                new ConfigStringProp("reduce-big", resizeThumb),

                new ConfigBoolProp("add-border", addBorder),
                new ConfigIntProp("reduce-size", borderSize),
                new ConfigStringProp("bg-color", background),
                new ConfigStringProp("border-color", borderColor),
                new ConfigStringProp("bg-pic", bgPic)};
    }


    public String getFormat() {
        return format.getValueSafe();
    }

    public StringProperty formatProperty() {
        return format;
    }

    public void setFormat(String format) {
        this.format.set(format);
    }

    public String getFotoSrc() {
        return fotoSrc.getValueSafe();
    }

    public StringProperty fotoSrcProperty() {
        return fotoSrc;
    }

    public void setFotoSrc(String fotoSrc) {
        this.fotoSrc.set(fotoSrc);
    }

    public String getFotoDest() {
        return fotoDest.getValueSafe();
    }

    public StringProperty fotoDestProperty() {
        return fotoDest;
    }

    public void setFotoDest(String fotoDest) {
        this.fotoDest.set(fotoDest);
    }


    public int getThumbSize() {
        return thumbSize.get();
    }

    public IntegerProperty thumbSizeProperty() {
        return thumbSize;
    }

    public void setThumbSize(int thumbSize) {
        this.thumbSize.set(thumbSize);
    }

    public int getQuantityThumbsWidth() {
        return quantityThumbsWidth.get();
    }

    public IntegerProperty quantityThumbsWidthProperty() {
        return quantityThumbsWidth;
    }

    public void setQuantityThumbsWidth(int quantityThumbsWidth) {
        this.quantityThumbsWidth.set(quantityThumbsWidth);
    }

    public int getThumbCount() {
        return thumbCount.get();
    }

    public IntegerProperty thumbCountProperty() {
        return thumbCount;
    }

    public void setThumbCount(int thumbCount) {
        this.thumbCount.set(thumbCount);
    }

    public String getThumbSrc() {
        return thumbSrc.getValueSafe();
    }

    public StringProperty thumbSrcProperty() {
        return thumbSrc;
    }

    public void setThumbSrc(String thumbSrc) {
        this.thumbSrc.set(thumbSrc);
    }

    public boolean isBlackWhite() {
        return blackWhite.get();
    }

    public BooleanProperty blackWhiteProperty() {
        return blackWhite;
    }

    public void setBlackWhite(boolean blackWhite) {
        this.blackWhite.set(blackWhite);
    }

    public String getResizeThumb() {
        return resizeThumb.getValueSafe();
    }

    public StringProperty resizeThumbProperty() {
        return resizeThumb;
    }

    public void setResizeThumb(String resizeThumb) {
        this.resizeThumb.set(resizeThumb);
    }

    public boolean isAddBorder() {
        return addBorder.get();
    }

    public BooleanProperty addBorderProperty() {
        return addBorder;
    }

    public void setAddBorder(boolean addBorder) {
        this.addBorder.set(addBorder);
    }

    public int getBorderSize() {
        return borderSize.get();
    }

    public IntegerProperty borderSizeProperty() {
        return borderSize;
    }

    public void setBorderSize(int borderSize) {
        this.borderSize.set(borderSize);
    }

    public String getBackground() {
        return background.get();
    }

    public StringProperty backgroundProperty() {
        return background;
    }

    public void setBackground(String background) {
        this.background.set(background);
    }

    public String getBorderColor() {
        return borderColor.getValueSafe();
    }

    public StringProperty borderColorProperty() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor.set(borderColor);
    }

    public String getBgPic() {
        return bgPic.get();
    }

    public StringProperty bgPicProperty() {
        return bgPic;
    }

    public void setBgPic(String bgPic) {
        this.bgPic.set(bgPic);
    }
}
