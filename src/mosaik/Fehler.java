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

package mosaik;

import mosaik.daten.Daten;
import mosaik.gui.DialogFehler;

public class Fehler {

    public boolean fehlerAnzeigen = true;

    public Fehler(Daten ddaten) {
        daten = ddaten;
    }

    private Daten daten;

    public void fehlermeldung(Exception ex, String text) {
        meldung(new String[]{ex.getMessage(), text});
    }

    public void fehlermeldung(String text) {
        meldung(new String[]{text});
    }

    public void fehlermeldung(String text1, String text2) {
        meldung(new String[]{text1, text2});
    }

    private void meldung(String[] texte) {
        if (fehlerAnzeigen) {
            DialogFehler dialog = new DialogFehler(null, daten, texte);
            dialog.setVisible(true);
            fehlerAnzeigen = dialog.fehlerAnzeigen;
        }
    }

}
