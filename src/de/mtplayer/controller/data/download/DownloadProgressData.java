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

public class DownloadProgressData implements Comparable<DownloadProgressData> {

    private Double progress = -1.0;
    private String text = "";

    /**
     * damit wird nach long progress sortiert und der Text text angezeigt
     *
     * @param l
     * @param s
     */
    DownloadProgressData(double l, String s) {
        this.progress = l;
        this.text = s;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isProgress() {
        if (0.0 < progress && progress < 1.0) {
            return true;
        }
        return false;
    }

    public int compareTo(DownloadProgressData d) {
        return d.progress.compareTo(progress);
    }

    @Override
    public String toString() {
        return text;
    }
}
