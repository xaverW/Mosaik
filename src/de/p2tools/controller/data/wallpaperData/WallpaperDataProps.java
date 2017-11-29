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

import de.p2tools.controller.config.Const;
import javafx.beans.property.*;

public class WallpaperDataProps extends WallpaperDataXml {
    private final StringProperty format = new SimpleStringProperty(Const.IMAGE_FORMAT_JPG); // Fotoformat: jpg,png
    private final StringProperty fotoDest = new SimpleStringProperty(""); // File dest
    private final IntegerProperty thumbSize = new SimpleIntegerProperty(50); // Größe des Thumbs Width==Height
    private final IntegerProperty numberThumbsWidth = new SimpleIntegerProperty(50); // Anzahl Thumbs in der Breite des Dest
    private final IntegerProperty thumbCollectionId = new SimpleIntegerProperty(0); // ID der ThumbCollection

    public final Property[] properties = {format, fotoDest, thumbSize, numberThumbsWidth, thumbCollectionId};


    public String getFormat() {
        return format.get();
    }

    public StringProperty formatProperty() {
        return format;
    }

    public void setFormat(String format) {
        this.format.set(format);
    }


    public String getFotoDest() {
        return fotoDest.get();
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
        setFotoDest(arr[FOTO_DEST]);
        setThumbSize(Integer.parseInt(arr[THUMB_SIZE]));
        setNumberThumbsWidth(Integer.parseInt(arr[DEST_SIZE_W]));
        setThumbCollectionId(Integer.parseInt(arr[THUMB_COLLECTION_ID]));
    }


    public void setXmlFromProps() {
        arr[FORMAT] = getFormat();
        arr[FOTO_DEST] = getFotoDest();
        arr[THUMB_SIZE] = String.valueOf(getThumbSize());
        arr[DEST_SIZE_W] = String.valueOf(getNumberThumbsWidth());
        arr[THUMB_COLLECTION_ID] = String.valueOf(getThumbCollectionId());
    }
}
