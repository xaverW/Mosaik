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

package de.p2tools.controller.data.fotos;

import de.p2tools.controller.config.Const;
import javafx.beans.property.*;

import java.text.Format;

public class FotoCollectionProps extends FotoCollectionXml {

    private final IntegerProperty nr = new SimpleIntegerProperty(0);
    private final StringProperty name = new SimpleStringProperty("");
    private final IntegerProperty resolution = new SimpleIntegerProperty(600);
    private final StringProperty format = new SimpleStringProperty(Const.IMAGE_FORMAT_JPG);


    public final Property[] properties = {nr, name, resolution, format};

    public int getNr() {
        return nr.get();
    }

    public IntegerProperty nrProperty() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr.set(nr);
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

    public int getResolution() {
        return resolution.get();
    }

    public IntegerProperty resolutionProperty() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution.set(resolution);
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

    public void setPropsFromXml() {
        setName(arr[NAME]);
        setResolution(Integer.parseInt(arr[RESOLUTION]));
        setFormat(arr[FOTO_FORMAT]);
    }


    public void setXmlFromProps() {
        arr[NAME] =getName();
        arr[RESOLUTION] =String.valueOf(getResolution());
        arr[FOTO_FORMAT] =getFormat();
    }


    @Override
    public int compareTo(FotoCollection fotoCollection) {
        return getName().compareTo(fotoCollection.getName());
    }

}
