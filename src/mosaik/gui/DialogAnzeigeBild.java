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
 *
 */

package mosaik.gui;

import com.sun.media.jai.widget.DisplayJAI;
import mosaik.bild.ScaleImage_;
import mosaik.daten.Daten;
import mosaik.daten.DatenFarbe_;
import mosaik.daten.Konstanten;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class DialogAnzeigeBild extends javax.swing.JDialog {

    Daten daten;
    int nr;
    final int MAX = 10;
    final int LABEL_HIGHT = 30;
    DisplayJAI[] display = new DisplayJAI[MAX];
    JLabel label[] = new JLabel[MAX];
    BeobSlider beobSlider = new BeobSlider();

    public DialogAnzeigeBild(java.awt.Frame parent, boolean modal, Daten d, int nnr) {
        super(parent, modal);
        initComponents();
        nr = nnr;
        daten = d;
        jButtonVor.addActionListener(new BeobVor());
        jButtonZurueck.addActionListener(new BeobZurueck());
        jButtonVVor.addActionListener(new BeobVVor());
        jButtonZZurueck.addActionListener(new BeobZZurueck());
        jButtonAnfang.addActionListener(new BeobAnfang());
        jButtonEnde.addActionListener(new BeobEnde());
        jPanelExtra.setLayout(new FlowLayout());
        jPanelExtra2.setLayout(new FlowLayout());
        jSlider1.setMinimum(0);
        jSlider1.setMaximum(daten.listeFarben.size() - 1);
        jSlider1.addChangeListener(beobSlider);
        for (int i = 0; i < MAX; ++i) {
            display[i] = new DisplayJAI();
            display[i].addMouseListener(new BeobMaus(i));
            label[i] = new JLabel();
            label[i].setPreferredSize(new Dimension(Integer.parseInt(daten.datenProjekt.arr[Konstanten.PROJEKT_AUFLOESUNG_ZIEL_NR]),
                    LABEL_HIGHT));
            label[i].setMinimumSize(new Dimension(Integer.parseInt(daten.datenProjekt.arr[Konstanten.PROJEKT_AUFLOESUNG_ZIEL_NR]),
                    LABEL_HIGHT));
            label[i].setMaximumSize(new Dimension(Integer.parseInt(daten.datenProjekt.arr[Konstanten.PROJEKT_AUFLOESUNG_ZIEL_NR]),
                    LABEL_HIGHT));
            label[i].setOpaque(true);
            label[i].setHorizontalAlignment(JLabel.CENTER);
        }
//        jPanelAussen.setLayout(new BorderLayout ());
        init();
        this.doLayout();
        this.pack();
        this.setVisible(true);
    }

    private void init() {
        jPanelExtra.removeAll();
        jPanelExtra2.removeAll();
        for (int i = 0; i < MAX && i < daten.listeFarben.size(); ++i) {
            jPanelExtra.add(display[i]);
            jPanelExtra2.add(label[i]);
        }
        set();
        jPanelExtra.updateUI();
        jPanelExtra2.updateUI();
    }

    private void set() {
        jSlider1.removeChangeListener(beobSlider);
        DatenFarbe_ farbe;
        String file;
        PlanarImage image;
        if (daten.listeFarben.size() <= 10) {
            nr = 0;
        } else if (nr >= daten.listeFarben.size() - 10) {
            nr = daten.listeFarben.size() - 11;
        }
        if (nr < 0) {
            nr = 0;
        }
        if ((nr - MAX / 2) < 0) {
            jSlider1.setValue(0);
        } else {
            jSlider1.setValue(nr - MAX / 2);
        }
        for (int i = 0; i < MAX && (nr + i) < daten.listeFarben.size(); ++i) {
            farbe = daten.listeFarben.get(nr + i);
            file = farbe.arr[Konstanten.FARBEN_PFAD_NR];
            try {
                image = JAI.create("fileload", file);
                if (Boolean.parseBoolean(farbe.arr[Konstanten.FARBEN_BENUTZEN_NR])) {
//                display[i].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
//                label[i].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
                    label[i].setText(String.valueOf(nr + i));
//                    label[i].setText(String.valueOf(farbe.arr[Konstanten.FARBEN_R_NR] + " " +
//                                                    farbe.arr[Konstanten.FARBEN_G_NR] + " " +
//                                                    farbe.arr[Konstanten.FARBEN_B_NR]));
                } else {
//                display[i].setBorder(null);
//                label[i].setBorder(null);
                    label[i].setText(String.valueOf(" -- "));
                }
                display[i].set(image);
                label[i].setBackground(new Color(Integer.parseInt(farbe.arr[Konstanten.FARBEN_R_NR]),
                        Integer.parseInt(farbe.arr[Konstanten.FARBEN_G_NR]),
                        Integer.parseInt(farbe.arr[Konstanten.FARBEN_B_NR])));
                label[i].setForeground(getLabelColor(farbe));
            } catch (Exception e) {
                daten.fehler.fehlermeldung(e, "Kann ein Bild nicht laden, Projekt neu einlesen!");
            }
        }
        jSlider1.addChangeListener(beobSlider);
    }

    private Color getLabelColor(DatenFarbe_ farbe) {
        Color ret = Color.WHITE;
        if ((Integer.parseInt(farbe.arr[Konstanten.FARBEN_R_NR]) +
                Integer.parseInt(farbe.arr[Konstanten.FARBEN_G_NR]) +
                Integer.parseInt(farbe.arr[Konstanten.FARBEN_B_NR])) > 3 * 255 / 2) {
            ret = Color.BLACK;
        }
        return ret;
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPanelExtra = new javax.swing.JPanel();
        jPanelExtra2 = new javax.swing.JPanel();
        jSlider1 = new javax.swing.JSlider();
        jPanel1 = new javax.swing.JPanel();
        jPanelInnen = new javax.swing.JPanel();
        jButtonAnfang = new javax.swing.JButton();
        jButtonZZurueck = new javax.swing.JButton();
        jButtonZurueck = new javax.swing.JButton();
        jButtonVor = new javax.swing.JButton();
        jButtonVVor = new javax.swing.JButton();
        jButtonEnde = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanelExtra.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanelExtraLayout = new javax.swing.GroupLayout(jPanelExtra);
        jPanelExtra.setLayout(jPanelExtraLayout);
        jPanelExtraLayout.setHorizontalGroup(
                jPanelExtraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 565, Short.MAX_VALUE)
        );
        jPanelExtraLayout.setVerticalGroup(
                jPanelExtraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanelExtra2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanelExtra2Layout = new javax.swing.GroupLayout(jPanelExtra2);
        jPanelExtra2.setLayout(jPanelExtra2Layout);
        jPanelExtra2Layout.setHorizontalGroup(
                jPanelExtra2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 565, Short.MAX_VALUE)
        );
        jPanelExtra2Layout.setVerticalGroup(
                jPanelExtra2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jButtonAnfang.setText("|<--");

        jButtonZZurueck.setText("<<-");

        jButtonZurueck.setText("<--");

        jButtonVor.setText("-->");

        jButtonVVor.setText("->>");

        jButtonEnde.setText("-->|");

        javax.swing.GroupLayout jPanelInnenLayout = new javax.swing.GroupLayout(jPanelInnen);
        jPanelInnen.setLayout(jPanelInnenLayout);
        jPanelInnenLayout.setHorizontalGroup(
                jPanelInnenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelInnenLayout.createSequentialGroup()
                                .addComponent(jButtonAnfang, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jButtonZZurueck, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jButtonZurueck, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jButtonVor, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jButtonVVor, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jButtonEnde, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanelInnenLayout.setVerticalGroup(
                jPanelInnenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButtonAnfang)
                        .addComponent(jButtonZZurueck)
                        .addComponent(jButtonZurueck)
                        .addComponent(jButtonVor)
                        .addComponent(jButtonVVor)
                        .addComponent(jButtonEnde)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jPanelInnen, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 569, Short.MAX_VALUE)
                                        .addComponent(jPanelExtra2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanelExtra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jSlider1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 569, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanelExtra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanelExtra2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAnfang;
    private javax.swing.JButton jButtonEnde;
    private javax.swing.JButton jButtonVVor;
    private javax.swing.JButton jButtonVor;
    private javax.swing.JButton jButtonZZurueck;
    private javax.swing.JButton jButtonZurueck;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelExtra;
    private javax.swing.JPanel jPanelExtra2;
    private javax.swing.JPanel jPanelInnen;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JSlider jSlider1;

    // End of variables declaration//GEN-END:variables
    private class BeobVor implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ++nr;
            set();
        }

    }

    private class BeobZurueck implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            --nr;
            set();
        }

    }

    private class BeobVVor implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            nr += 10;
            set();
        }

    }

    private class BeobZZurueck implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            nr -= 10;
            set();
        }

    }

    private class BeobAnfang implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            nr = 0;
            set();
        }

    }

    private class BeobEnde implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            nr = daten.listeFarben.size() - 1;
            set();
        }

    }

    private class BeobSlider implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            nr = jSlider1.getValue();
            set();
        }

    }

    private class BeobMaus implements MouseListener {

        private int idx;

        public BeobMaus(int iidx) {
            idx = iidx;
        }

        public void mouseClicked(MouseEvent arg0) {
            if (arg0.getButton() == MouseEvent.BUTTON1) {
                daten.listeFarben.get(idx + nr).arr[Konstanten.FARBEN_BENUTZEN_NR] =
                        Boolean.toString(!Boolean.parseBoolean(daten.listeFarben.get(idx + nr).arr[Konstanten.FARBEN_BENUTZEN_NR]));
                set();
            } else if (arg0.getButton() == MouseEvent.BUTTON3) {
                showMenu(arg0);
            }
        }

        public void mousePressed(MouseEvent arg0) {
        }

        public void mouseReleased(MouseEvent arg0) {
        }

        public void mouseEntered(MouseEvent arg0) {
        }

        public void mouseExited(MouseEvent arg0) {
        }

        private void showMenu(MouseEvent evt) {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem itemRechts = new JMenuItem("Bild rechts drehen");
            itemRechts.addActionListener(new BeobDrehen(true));
            menu.add(itemRechts);
            JMenuItem itemLinks = new JMenuItem("Bild links drehen");
            itemLinks.addActionListener(new BeobDrehen(false));
            menu.add(itemLinks);
            JMenuItem itemLoeschen = new JMenuItem("Bild Löschen");
            itemLoeschen.addActionListener(new BeobLoeschen());
            menu.add(itemLoeschen);
            menu.show(display[idx], evt.getX(), evt.getY());
        }

        private class BeobDrehen implements ActionListener {

            private boolean rechts;

            public BeobDrehen(boolean rrechts) {
                rechts = rrechts;
            }

            public void actionPerformed(ActionEvent arg0) {
                new ScaleImage_(daten).drehen(new File(daten.listeFarben.get(idx + nr).arr[Konstanten.FARBEN_PFAD_NR]), rechts);
                set();
            }

        }

        private class BeobLoeschen implements ActionListener {

            public void actionPerformed(ActionEvent arg0) {
                File file = new File(daten.listeFarben.get(idx + nr).arr[Konstanten.FARBEN_PFAD_NR]);
                if (!file.delete()) {
                    daten.fehler.fehlermeldung("Konnte das Bild nicht löschen!");
                }
                daten.listeFarben.remove(idx + nr);
                daten.setGeaendert();
                init();
            }

        }

    }

}



