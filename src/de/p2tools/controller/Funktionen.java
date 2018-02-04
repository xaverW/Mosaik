/*
 *    Copyright (C) 2008
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.p2tools.controller;

import de.p2tools.p2Lib.image.ImgTools;

import java.io.File;

public class Funktionen {

    public static String fileType(File f) {
        return fileType(f.getName());
    }

    public static String fileType(String f) {
        String n = f;
        String suffix = null;
        int i = n.lastIndexOf('.');
        if (i > 0 && i < n.length() - 1) {
            suffix = n.substring(i + 1).toLowerCase();
        }
        if (suffix.equals("jpeg") || suffix.equals(ImgTools.IMAGE_FORMAT_JPG)) {
            return ImgTools.IMAGE_FORMAT_JPG;
        }
        if (suffix.equals(ImgTools.IMAGE_FORMAT_PNG)) {
            return ImgTools.IMAGE_FORMAT_PNG;
        }
        return "";
    }

}
