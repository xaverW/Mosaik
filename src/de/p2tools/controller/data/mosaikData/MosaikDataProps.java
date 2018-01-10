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


package de.p2tools.controller.data.mosaikData;

import de.p2tools.controller.config.ProgConst;
import de.p2tools.controller.data.Data;
import de.p2tools.mLib.configFile.config.Config;
import de.p2tools.mLib.configFile.config.ConfigIntProp;
import de.p2tools.mLib.configFile.config.ConfigStringProp;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Arrays;

public class MosaikDataProps extends Data<MosaikData> {
    public static final String TAG = "MosaikData";

    private final StringProperty format = new SimpleStringProperty(ProgConst.IMAGE_FORMAT_JPG); // Fotoformat: jpg,png
    private final StringProperty fotoSrc = new SimpleStringProperty(""); // File SRC
    private final StringProperty fotoDestName = new SimpleStringProperty(""); // File dest
    private final StringProperty fotoDestDir = new SimpleStringProperty(""); // File dest
    private final IntegerProperty thumbSize = new SimpleIntegerProperty(50); // Größe des Thumbs Width==Height
    private final IntegerProperty numberThumbsWidth = new SimpleIntegerProperty(50); // Anzahl Thumbs in der Breite des Dest
    private final IntegerProperty thumbCount = new SimpleIntegerProperty(0); // Anzahl wie oft ein Thumbs verwendet werden kann

    public String getTag() {
        return TAG;
    }

    public ArrayList<Config> getConfigsArr() {
        return new ArrayList<>(Arrays.asList(
                new ConfigStringProp("format", ProgConst.IMAGE_FORMAT_JPG, format),
                new ConfigStringProp("foto-src", "", fotoSrc),
                new ConfigStringProp("foto-dest-name", "", fotoDestName),
                new ConfigStringProp("foto-dest-dir", "", fotoDestDir),
                new ConfigIntProp("thumb-size", 50, thumbSize),
                new ConfigIntProp("number-thumbs-width", 50, numberThumbsWidth),
                new ConfigIntProp("thumb-count", 0, thumbCount)));
    }


    public String getFormat() {
        return format.get();
    }

    public StringProperty formatProperty() {
        return format;
    }

    public void setFormat(String format) {
        this.format.set(format);
    }

    public String getFotoSrc() {
        return fotoSrc.get();
    }

    public StringProperty fotoSrcProperty() {
        return fotoSrc;
    }

    public void setFotoSrc(String fotoSrc) {
        this.fotoSrc.set(fotoSrc);
    }

    public String getFotoDestName() {
        return fotoDestName.get();
    }

    public StringProperty fotoDestNameProperty() {
        return fotoDestName;
    }

    public void setFotoDestName(String fotoDestName) {
        this.fotoDestName.set(fotoDestName);
    }

    public String getFotoDestDir() {
        return fotoDestDir.get();
    }

    public StringProperty fotoDestDirProperty() {
        return fotoDestDir;
    }

    public void setFotoDestDir(String fotoDestDir) {
        this.fotoDestDir.set(fotoDestDir);
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

    public int getNumberThumbsWidth() {
        return numberThumbsWidth.get();
    }

    public IntegerProperty numberThumbsWidthProperty() {
        return numberThumbsWidth;
    }

    public void setNumberThumbsWidth(int numberThumbsWidth) {
        this.numberThumbsWidth.set(numberThumbsWidth);
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

}
