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

package de.p2tools.mosaik.controller.data.thumb;

public final class Thumb extends ThumbProps {

    private final int DARK = 125;

    public Thumb() {
    }

    public Thumb(int r, int g, int b, String path) {
        setRed(r);
        setGreen(g);
        setBlue(b);
        setFileName(path);
    }

    public static Thumb getThumb(int r, int g, int b, String path) {
        if (checkC(r) && checkC(g) && checkC(b)) {
            return new Thumb(r, g, b, path);
        }
        return null;
    }


    public boolean isDark() {
        // todo
        return (getRed() < DARK && getGreen() < DARK && getBlue() < DARK);
    }

    public Thumb getCopy() {
        final Thumb ret = new Thumb();
        for (int i = 0; i < properties.length; ++i) {
            ret.properties[i].setValue(this.properties[i].getValue());
        }

        return ret;
    }

    public void copyToMe(Thumb thumb) {
        for (int i = 0; i < properties.length; ++i) {
            properties[i].setValue(thumb.properties[i].getValue());
        }
    }
}
