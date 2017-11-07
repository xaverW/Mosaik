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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.Collections;

public class ThumbList extends SimpleListProperty<Thumb> {

    private int nr = 1;
    private BooleanProperty fotoListChanged = new SimpleBooleanProperty(true);


    public ThumbList() {
        super(FXCollections.observableArrayList());
    }

    public boolean getFotoListChanged() {
        return fotoListChanged.get();
    }

    public void setDownloadsChanged() {
        fotoListChanged.set(!fotoListChanged.get());
    }

    public BooleanProperty fotoListChangedProperty() {
        return fotoListChanged;
    }


    public void sort() {
        Collections.sort(this);
    }


    public synchronized boolean add(Thumb d) {
        d.setNr(nr++);
        return super.add(d);
    }


    public synchronized boolean addAll(ArrayList<Thumb> d) {
        d.forEach(foto -> foto.setNr(nr++));
        return super.addAll(d);
    }

}
