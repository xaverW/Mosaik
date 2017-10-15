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

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import mosaik.daten.Daten;
import mosaik.daten.Konstanten;

public class FarbenCellRenderer extends DefaultTableCellRenderer {

    Daten daten;

    public FarbenCellRenderer(Daten ddaten) {
        daten = ddaten;
    }

    @Override
    public Component getTableCellRendererComponent(
        JTable table,
        Object value,
        boolean isSelected,
        boolean hasFocus,
        int row,
        int column) {

        setBackground(null);
        super.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column);
        int r = table.convertRowIndexToModel(row);
        Color c = new Color(Integer.parseInt((String) table.getModel().getValueAt(r, Konstanten.FARBEN_R_NR)),
                            Integer.parseInt((String) table.getModel().getValueAt(r, Konstanten.FARBEN_G_NR)),
                            Integer.parseInt((String) table.getModel().getValueAt(r, Konstanten.FARBEN_B_NR)));
        if (column == Konstanten.FARBEN_FARBE_NR) {
            setBackground(c);
        }
        return this;
    }

}
