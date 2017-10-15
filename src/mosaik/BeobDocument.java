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

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class BeobDocument implements DocumentListener {

    JTextField textfeld;
    String[] text;
    int idx;

    /**
     * 
     * @param ttextfeld
     * @param ttext
     * @param iidx
     */
    public BeobDocument(JTextField ttextfeld, String[] ttext, int iidx) {
        textfeld = ttextfeld;
        text = ttext;
        idx = iidx;
    }

    /**
     * 
     * @param e
     */
    @Override
    public void changedUpdate(DocumentEvent e) {
        tus();
    }

    /**
     * 
     * @param e
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        tus();
    }

    /**
     * 
     * @param e
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        tus();
    }

    private void tus() {
        text[idx] = textfeld.getText();
    }

}
