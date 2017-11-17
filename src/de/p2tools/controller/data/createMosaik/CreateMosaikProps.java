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


package de.p2tools.controller.data.createMosaik;

import de.p2tools.controller.config.Const;
import javafx.beans.property.*;

public class CreateMosaikProps extends CreateMosaikXml {
    private final StringProperty format = new SimpleStringProperty(Const.IMAGE_FORMAT_JPG); // Fotoformat: jpg,png
    private final StringProperty fotoSrc = new SimpleStringProperty(""); // File SRC
    private final StringProperty fotoDest = new SimpleStringProperty(""); // File dest
    private final IntegerProperty thumbSize = new SimpleIntegerProperty(50); // Größe des Thumbs Width==Height
    private final IntegerProperty destSizeW = new SimpleIntegerProperty(1000); // Anzahl Thumbs in der Breite des Dest
    private final IntegerProperty thumbCount = new SimpleIntegerProperty(5); // Anzahl wie oft ein Thumbs verwendet werden kann
    private final StringProperty thumbCollectionId = new SimpleStringProperty(""); // ID der ThumbCollection

    public final Property[] properties = {format, fotoSrc, fotoDest, thumbSize, destSizeW, thumbCount, thumbCollectionId};


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

    public int getDestSizeW() {
        return destSizeW.get();
    }

    public IntegerProperty destSizeWProperty() {
        return destSizeW;
    }

    public void setDestSizeW(int destSizeW) {
        this.destSizeW.set(destSizeW);
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

    public String getThumbCollectionId() {
        return thumbCollectionId.get();
    }

    public StringProperty thumbCollectionIdProperty() {
        return thumbCollectionId;
    }

    public void setThumbCollectionId(String thumbCollectionId) {
        this.thumbCollectionId.set(thumbCollectionId);
    }

    public void setPropsFromXml() {
        setFormat(arr[FORMAT]);
        setFotoSrc(arr[FOTO_SRC]);
        setFotoDest(arr[FOTO_DEST]);
        setThumbSize(Integer.parseInt(arr[THUMB_SIZE]));
        setDestSizeW(Integer.parseInt(arr[DEST_SIZE_W]));
        setThumbCount(Integer.parseInt(arr[THUMB_COUNT]));
        setThumbCollectionId(arr[THUMB_COLLECTION_ID]);
    }


    public void setXmlFromProps() {
        arr[FORMAT] = getFormat();
        arr[FOTO_SRC] = getFotoSrc();
        arr[FOTO_DEST] = getFotoDest();
        arr[THUMB_SIZE] = String.valueOf(getThumbSize());
        arr[DEST_SIZE_W] = String.valueOf(getDestSizeW());
        arr[THUMB_COUNT] = String.valueOf(getThumbCount());
        arr[THUMB_COLLECTION_ID] = getThumbCollectionId();
    }
}
