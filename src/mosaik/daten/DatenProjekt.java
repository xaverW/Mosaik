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

package mosaik.daten;

public class DatenProjekt implements Comparable<DatenProjekt> {

    public String[] arr;

    public DatenProjekt() {
        makeArr();
        arr[Konstanten.PROJEKT_AUFLOESUNG_ZIEL_NR] = "50";
        arr[Konstanten.PROJEKT_ARCHIV_FORMAT_NR] = Konstanten.IMAGE_FORMAT_JPG;
        arr[Konstanten.PROJEKT_MOSAIK_FORMAT_NR] = Konstanten.IMAGE_FORMAT_JPG;
        arr[Konstanten.PROJEKT_ANZAHL_BILD_NR] = "0";
        arr[Konstanten.PROJEKT_BILDER_HOEHE_NR] = "10";
    }

    private void makeArr() {
        arr = new String[Konstanten.PROJEKT_MAX_ELEM];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = new String("");
        }
    }

    @Override
    public int compareTo(DatenProjekt arg0) {
        return (arr[Konstanten.PROJEKT_NAME_NR].compareToIgnoreCase(((DatenProjekt) arg0).arr[Konstanten.PROJEKT_NAME_NR]));
    }

}
