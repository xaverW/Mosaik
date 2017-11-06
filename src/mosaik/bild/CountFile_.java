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

package mosaik.bild;

import mosaik.daten.Konstanten;

import java.io.File;

public class CountFile_ {

    public int countFile(File pfad) {
        int fileCount = 0;
        File[] liste = null;
        if (pfad.isDirectory()) {
            liste = pfad.listFiles();
            for (int i = 0; i < liste.length; i++) {
                if (liste[i].isFile()) {
                    if (checkSuffix(liste[i])) {
                        ++fileCount;
                    }
                } else if (liste[i].isDirectory()) {
                    fileCount += countFile(liste[i]);
                }
            }
        }
        return fileCount;
    }

    public boolean checkSuffix(File file) {
        boolean ret = false;
        if (file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1).equalsIgnoreCase(Konstanten.IMAGE_FORMAT_JPG) ||
                file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1).equalsIgnoreCase("jpeg") ||
                file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1).equalsIgnoreCase(Konstanten.IMAGE_FORMAT_PNG)) {
            ret = true;
        }
        return ret;
    }

}
