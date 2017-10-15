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

package de.mtplayer.mLib.tools;

public class SizeTools {

    public static String getGroesse(long l) {
        if (l > 1000 * 1000) {
            // größer als 1MB sonst kann ich mirs sparen
            return String.valueOf(l / (1000 * 1000));
        } else if (l > 0) {
            return "1";
        }
        return "";
    }

    /**
     * Convert a byte count into a human readable string.
     *
     * @param bytes The number of bytes to convert.
     * @param si    Use International System of Units (SI)?
     * @return The string representation
     */
    public static String humanReadableByteCount(final long bytes, final boolean si) {
        final int unit = si ? 1000 : 1024;
        if (bytes < unit) {
            return bytes + " B";
        }

        final int exp = (int) (Math.log(bytes) / Math.log(unit));

        final String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String humanReadableBandwidth(long b) {
        if (b > 1000 * 1000) {
            String s = String.valueOf(b / 1000);
            if (s.length() >= 4) {
                s = s.substring(0, s.length() - 3) + "," + s.substring(s.length() - 3);
            }
            return s + " MB/s";
        } else if (b > 1000) {
            return (b / 1000) + " kB/s";
        } else if (b > 1) {
            return b + " B/s";
        } else {
            return "";
        }
    }

}
