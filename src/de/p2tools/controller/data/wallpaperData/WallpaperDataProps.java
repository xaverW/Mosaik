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


package de.p2tools.controller.data.wallpaperData;

import de.p2tools.controller.config.ProgConst;
import de.p2tools.p2Lib.configFile.ConfigsData;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigIntProp;
import de.p2tools.p2Lib.configFile.config.ConfigStringProp;
import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.Arrays;

public class WallpaperDataProps extends WallpaperDataXml implements ConfigsData {
    private final StringProperty format = new SimpleStringProperty(ProgConst.IMAGE_FORMAT_JPG); // Fotoformat: jpg,png
    private final StringProperty fotoDestDir = new SimpleStringProperty(""); // File dest
    private final StringProperty fotoDestName = new SimpleStringProperty(""); // File dest
    private final IntegerProperty thumbSize = new SimpleIntegerProperty(50); // Größe des Thumbs Width==Height
    private final IntegerProperty numberThumbsWidth = new SimpleIntegerProperty(50); // Anzahl Thumbs in der Breite des Dest
    private final IntegerProperty thumbCollectionId = new SimpleIntegerProperty(0); // ID der ThumbCollection

    public final Property[] properties = {format, fotoDestDir, thumbSize, numberThumbsWidth, thumbCollectionId};

    public String getTag() {
        return TAG;
    }

    public String getComment() {
        return "settings of the wallpaper";
    }

    public ArrayList<Config> getConfigsArr() {
        return new ArrayList<Config>(Arrays.asList(
                new ConfigStringProp("format", ProgConst.IMAGE_FORMAT_JPG, format),
                new ConfigStringProp("foto-dest-dir", "", fotoDestDir),
                new ConfigStringProp("foto-dest-name", "", fotoDestName),
                new ConfigIntProp("thumb-size", 50, thumbSize),
                new ConfigIntProp("number-thumbs-width", 50, numberThumbsWidth),
                new ConfigIntProp("thumb-collection-id", 0, thumbCollectionId)));
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


    public String getFotoDestDir() {
        return fotoDestDir.get();
    }

    public StringProperty fotoDestDirProperty() {
        return fotoDestDir;
    }

    public void setFotoDestDir(String fotoDestDir) {
        this.fotoDestDir.set(fotoDestDir);
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


    public int getThumbCollectionId() {
        return thumbCollectionId.get();
    }

    public IntegerProperty thumbCollectionIdProperty() {
        return thumbCollectionId;
    }

    public void setThumbCollectionId(int thumbCollectionId) {
        this.thumbCollectionId.set(thumbCollectionId);
    }

    public void setPropsFromXml() {
        setFormat(arr[FORMAT]);
        setFotoDestDir(arr[FOTO_DEST]);
        setInt();
    }

    private void setInt() {
        try {
            setThumbSize(Integer.parseInt(arr[THUMB_SIZE]));
            setNumberThumbsWidth(Integer.parseInt(arr[NUMBER_THUMBS_W]));
            setThumbCollectionId(Integer.parseInt(arr[THUMB_COLLECTION_ID]));
        } catch (Exception ex) {
            setThumbSize(50);
            setNumberThumbsWidth(50);
            setThumbCollectionId(0);
        }
    }

    public void setXmlFromProps() {
        arr[FORMAT] = getFormat();
        arr[FOTO_DEST] = getFotoDestDir();
        arr[THUMB_SIZE] = String.valueOf(getThumbSize());
        arr[NUMBER_THUMBS_W] = String.valueOf(getNumberThumbsWidth());
        arr[THUMB_COLLECTION_ID] = String.valueOf(getThumbCollectionId());
    }
}
