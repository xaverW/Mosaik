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

package de.p2tools.mosaic.controller.data.thumb;

import de.p2tools.p2Lib.configFile.pData.PDataList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.Collections;

public class ThumbDataList extends SimpleListProperty<Thumb> implements PDataList<Thumb> {

    public static final String TAG = "Thumblist";
    private int nr = 1;
    private BooleanProperty fotoListChanged = new SimpleBooleanProperty(true);


    public ThumbDataList() {
        super(FXCollections.observableArrayList());
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String getComment() {
        return "the list of the thumbs";
    }

    @Override
    public Thumb getNewItem() {
        return new Thumb();
    }

    @Override
    public void addNewItem(Object obj) {
        if (obj != null && obj.getClass().equals(Thumb.class)) {
            add((Thumb) obj);
        }
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

    @Override
    public void clear() {
        nr = 1;
        super.clear();
    }

    public void sort() {
        Collections.sort(this);
        nr = 1;
        stream().forEach(foto -> foto.setNr(nr++));
    }


    @Override
    public synchronized boolean add(Thumb thumb) {
        thumb.setNr(nr++);
        return super.add(thumb);
    }


    public synchronized boolean addAll(ArrayList<Thumb> d) {
        d.forEach(foto -> foto.setNr(nr++));
        return super.addAll(d);
    }

    public void resetAnz() {
        this.parallelStream().forEach(thumb -> thumb.setAnz(0));
    }

    public Thumb getThumbWithColor(int red, int green, int blue, int anz) {
//        if (anz == 0) {
        return this.parallelStream().filter(thumb ->
                thumb.getRed() == red &&
                        thumb.getGreen() == green &&
                        thumb.getBlue() == blue).findAny().orElse(null);
//        } else {
//            return this.parallelStream().filter(thumb ->
//                    thumb.getRed() == red &&
//                            thumb.getGreen() == green &&
//                            thumb.getBlue() == blue &&
//                            thumb.getAnz() <= anz).findFirst().orElse(null);
//        }
    }

    public Thumb getThumbWithGrayValue(int avg, int anz) {
        return this.parallelStream().filter(thumb ->
                thumb.getAvg() == avg).findAny().orElse(null);
    }
}
