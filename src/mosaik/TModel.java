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

import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;
import mosaik.daten.Daten;

public class TModel extends DefaultTableModel {

    /** Creates a new instance of TModel */
    public TModel() {
    }

    public Object[][] object;
    Object[] columns;

    public TModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
        object = data;
        columns = columnNames;
    }

    public TModel copy() {
        return new TModel(object, columns);
    }

    @Override
    public boolean isCellEditable(int i, int j) {
        return false;
    }

    public void filterModel(String str, int feld, boolean exact) {
        if (str != null) {
            if (!str.equals("")) {
                for (int i = 0; i < this.getRowCount(); ++i) {
                    String tmp;
                    tmp = (String) this.getValueAt(i, feld);
                    if (exact && tmp.length() != str.length()) {
                        this.removeRow(i);
                        --i;
                        continue;
                    }
                    if (!tmp.toLowerCase().contains(str.toLowerCase())) {
                        this.removeRow(i);
                        --i;
                    }
                }
            }
        }
    }

    public String[] getModelOfField(Daten daten, int feld, boolean leer) {
        /* erstellt ein StringArray mit den ProgData des Feldes
        lee: immer ein leeres Feld am Anfang */
        LinkedList<String> list = new LinkedList<String>();
        String[] ret;
        String str = new String();
        for (int i = 0; i < this.getRowCount(); ++i) {
            str = (String) this.getValueAt(i, feld);
            if (str.equals("")) {
                leer = true;
            } else if (!list.contains(str)) {
                list.add(str);
            }
        }
        if (leer) {
            ret = new String[list.size() + 1];
            ret[0] = "";
            for (int i = 0; i < list.size(); ++i) {
                ret[i + 1] = list.get(i);
            }
        } else {
            ret = new String[list.size()];
            for (int i = 0; i < list.size(); ++i) {
                ret[i] = list.get(i);
            }
        }
        return ret;
    }

}
