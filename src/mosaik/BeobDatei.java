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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import mosaik.daten.Daten;

public class BeobDatei implements ActionListener {

    private Daten daten;
    private JTextField textfeld;
    private boolean file;
    private String text = "";
    private String[] suff = null;

    public BeobDatei(Daten ddaten, JTextField t, boolean ffile) {
        daten = ddaten;
        textfeld = t;
        file = ffile;
    }

    public BeobDatei(Daten ddaten, JTextField t, boolean ffile, String ttext, String[] ssuff) {
        daten = ddaten;
        textfeld = t;
        file = ffile;
        text = ttext;
        suff = ssuff;
    }

    public void actionPerformed(ActionEvent e) {
        if (!daten.beobStop) {
            int returnVal;
            JFileChooser chooser = new JFileChooser();
            chooser.setFileHidingEnabled(false);
            if (!textfeld.getText().equals("")) {
                chooser.setCurrentDirectory(new File(textfeld.getText()));
            }
            if (file) {
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            } else {
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            }
            if (suff != null) {
                chooser.setFileFilter(new FileNameExtensionFilter(text, suff));
            }
            returnVal = chooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    String str = chooser.getSelectedFile().getPath();
                    textfeld.setText(str);
                } catch (Exception ex) {
                    daten.fehler.fehlermeldung(ex, "GuiMosaik.BeobachterDateiDialog");
                }
            }
        }
    }

}
