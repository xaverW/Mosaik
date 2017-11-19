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

package de.p2tools.controller.data.thumb;

import de.p2tools.controller.config.Const;
import javafx.beans.property.*;

public class ThumbCollectionProps extends ThumbCollectionXml {

    private final IntegerProperty id = new SimpleIntegerProperty(0);
    private final StringProperty name = new SimpleStringProperty("");
    private final BooleanProperty recursive = new SimpleBooleanProperty(true);
    private final StringProperty format = new SimpleStringProperty(Const.IMAGE_FORMAT_JPG);
    private final StringProperty thumbDir = new SimpleStringProperty("");
    private final StringProperty fotoSrcDir = new SimpleStringProperty("");


    public final Property[] properties = {id, name, format};

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public boolean isRecursive() {
        return recursive.get();
    }

    public BooleanProperty recursiveProperty() {
        return recursive;
    }

    public void setRecursive(boolean recursive) {
        this.recursive.set(recursive);
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

    public String getThumbDir() {
        return thumbDir.get();
    }

    public StringProperty thumbDirProperty() {
        return thumbDir;
    }

    public void setThumbDir(String thumbDir) {
        this.thumbDir.set(thumbDir);
    }

    public String getFotoSrcDir() {
        return fotoSrcDir.get();
    }

    public StringProperty fotoSrcDirProperty() {
        return fotoSrcDir;
    }

    public void setFotoSrcDir(String fotoSrcDir) {
        this.fotoSrcDir.set(fotoSrcDir);
    }

    public void setPropsFromXml() {
        setName(arr[NAME]);
        setRecursive(Boolean.parseBoolean(arr[RECURSIV]));
        setFormat(arr[FOTO_FORMAT]);
        setThumbDir(arr[DIR_THUMB]);
        setFotoSrcDir(arr[DIR_FOTO_SRC]);
    }


    public void setXmlFromProps() {
        arr[NAME] = getName();
        arr[RECURSIV] = String.valueOf(isRecursive());
        arr[FOTO_FORMAT] = getFormat();
        arr[DIR_THUMB] = getThumbDir();
        arr[DIR_FOTO_SRC] = getFotoSrcDir();
    }


    @Override
    public int compareTo(ThumbCollection thumbCollection) {
        return getName().compareTo(thumbCollection.getName());
    }

}
