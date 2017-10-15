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

package de.mtplayer.controller.data.download;

import javafx.beans.property.ObjectPropertyBase;

public class DownloadProgress extends ObjectPropertyBase<DownloadProgressData> implements Comparable<DownloadProgress> {

    private Double progress = 0.0;
    private String text = "";

    private DownloadProgressData stateData = new DownloadProgressData(-1.0, "");

    public DownloadProgress() {
    }

    @Override
    public final DownloadProgressData get() {
        return getDownloadStateData();
    }

    public void set(double progr, String text) {
        // Wert zwischen -1=nix, 0..999 => 0,0 .. 1,0
        this.progress = 1.0 * progr / 1000;
        this.text = text;
        fireValueChangedEvent();
    }

    public void clear() {
        // Wert zwischen -1=nix, 0..999 => 0,0 .. 1,0
        this.progress = -1.0;
        this.text = "";
        fireValueChangedEvent();
    }

    @Override
    public Object getBean() {
        return DownloadProgress.this;
    }

    @Override
    public String getName() {
        return "DownloadProgress";
    }

    @Override
    public int compareTo(DownloadProgress ll) {
        return (progress.compareTo(ll.progress));
    }

    @Override
    public String toString() {
        return text;
    }


    private DownloadProgressData getDownloadStateData() {
        return new DownloadProgressData(progress, text);
    }

}
