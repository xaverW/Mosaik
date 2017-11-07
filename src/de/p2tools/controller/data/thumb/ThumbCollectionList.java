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

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;

public class ThumbCollectionList extends SimpleListProperty<ThumbCollection> {

    private int nr = 1;
    private BooleanProperty listChanged = new SimpleBooleanProperty(true);


    public ThumbCollectionList() {
        super(FXCollections.observableArrayList(callback ->
                new Observable[]{callback.nameProperty()}));
    }

    public boolean isListChanged() {
        return listChanged.get();
    }

    public BooleanProperty listChangedProperty() {
        return listChanged;
    }

    public void setListChanged() {
        this.listChanged.set(!listChanged.get());
    }


    public synchronized boolean add(ThumbCollection element) {
        element.setNr(nr++);
        return super.add(element);
    }


    public synchronized boolean addAll(ArrayList<ThumbCollection> thumbCollections) {
        thumbCollections.forEach(fotoCollection -> fotoCollection.setNr(nr++));
        return super.addAll(thumbCollections);
    }

    public ObservableList<String> getListNames() {
        ObservableList<String> list = FXCollections.observableArrayList();
        this.stream().forEach(fotoCollection -> list.add(fotoCollection.getName()));
        return list;
    }

    public void sort() {
        Collections.sort(this);
    }

}
