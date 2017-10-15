/*
 * PanelList.java
 *
 * Created on 27. Oktober 2007, 20:01
 *
 *
 *    Emma, Vereinsverwaltungsprorgamm
 *    Copyright (C) 2007
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
 *
 */

package mosaik.daten;

import java.util.LinkedList;
import java.util.ListIterator;
import mosaik.gui.PanelVorlage;

/**
 *
 * @author emil
 */
public class PanelListe {

    Daten daten;
    LinkedList<PanelVorlage> liste;

    public PanelListe(Daten d) {
        daten = d;
        liste = new LinkedList<PanelVorlage>();
    }

    public void addPanel(PanelVorlage p) {
        liste.add(p);
    }

    public void panelAendern() {
        ListIterator<PanelVorlage> it = liste.listIterator(0);
        while (it.hasNext()) {
            it.next().geaendert = true;
        }
    }

}
