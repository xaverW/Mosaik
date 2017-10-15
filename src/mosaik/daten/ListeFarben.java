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

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class ListeFarben extends LinkedList<DatenFarbe> {

    Daten daten;

    public ListeFarben(Daten ddaten) {
        daten = ddaten;
    }

    /**
     * 
     * @param daten
     * @param farbe
     * @return
     */
    @Override
    public synchronized boolean add(DatenFarbe farbe) {
        boolean ret;
        ret = super.add(farbe);
        daten.setGeaendert();
        return ret;
    }

    /**
     * 
     * @param nr
     * @return
     */
    @Override
    public DatenFarbe remove(int nr) {
        daten.setGeaendert();
        return super.remove(nr);
    }

    /**
     * 
     * @param i
     * @return
     */
    @Override
    public DatenFarbe get(int i) {
        return super.get(i);
    }

    public DatenFarbe getFarbe(int red, int green, int blue, int anz) {
        Iterator<DatenFarbe> it = this.iterator();
        DatenFarbe farbe;
        while (it.hasNext()) {
            farbe = it.next();
            if (anz == 0) {
                if (Integer.parseInt(farbe.arr[Konstanten.FARBEN_R_NR]) == red &&
                    Integer.parseInt(farbe.arr[Konstanten.FARBEN_G_NR]) == green &&
                    Integer.parseInt(farbe.arr[Konstanten.FARBEN_B_NR]) == blue) {
                    return farbe;
                }
            } else {
                if (Integer.parseInt(farbe.arr[Konstanten.FARBEN_R_NR]) == red &&
                    Integer.parseInt(farbe.arr[Konstanten.FARBEN_G_NR]) == green &&
                    Integer.parseInt(farbe.arr[Konstanten.FARBEN_B_NR]) == blue) {
                    if (farbe.anz <= anz) {
                        return farbe;
                    }
                }
            }
        }
        return null;
    }

    public void resetAnz() {
        Iterator<DatenFarbe> it = this.iterator();
        DatenFarbe farbe;
        while (it.hasNext()) {
            it.next().anz = 0;
        }
    }

    public void sort() {
        Collections.<DatenFarbe>sort(this);
    }

    public Object[][] getObjectData() {
        this.sort();
        Object[][] object;
        DatenFarbe farbe;
        int i = 0;
        ListIterator<DatenFarbe> iterator = this.listIterator();
        object = new Object[this.size()][Konstanten.FARBEN_MAX_ELEM];
        while (iterator.hasNext()) {
            farbe = iterator.next();
            object[i] = farbe.arr;
            ++i;
        }
        return object;
    }

}
