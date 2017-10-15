/*
 * PanelMosaik.java
 *
 * Created on 10. September 2008, 15:52
 */

package mosaik.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import mosaik.BeobDatei;
import mosaik.daten.Daten;
import mosaik.bild.TapeteErstellen;
import mosaik.daten.Konstanten;

public class GuiTapete extends PanelVorlage {

    private Daten daten;

    public GuiTapete(Daten ddaten) {
        super(ddaten);
        daten = ddaten;
        initComponents();
        initBeobachter();
        groesse();
    }
    ///////////////////////////////////
    // public
    ///////////////////////////////////
    @Override
    void isShown() {
        super.isShown();
    }

    @Override
    void neuLaden() {
        laden();
    }

    @Override
    void neuLadenInTime() {
        jTextFieldAnzahl.setText(String.valueOf(daten.listeFarben.size()));
        groesse();
    }
    //////////////////////////////////
    // private
    //////////////////////////////////
    private void laden() {
        daten.beobStop = true;
        if (daten.datenProjekt != null) {
            jTextFieldZiel.setText(daten.datenProjekt.arr[Konstanten.PROJEKT_PFAD_TAPETE_ZIEL_NR]);
        }
        jTextFieldAnzahl.setText(String.valueOf(daten.listeFarben.size()));
        if (daten.listeFarben.size() > 10) {
            jSpinnerAnzahlBreite.setModel(new javax.swing.SpinnerNumberModel(10, 1, daten.listeFarben.size(), 1));
        } else {
            jSpinnerAnzahlBreite.setModel(new javax.swing.SpinnerNumberModel(1, 1, 5, 1));
        }
        groesse();
        daten.beobStop = false;
    }

    private void initBeobachter() {
        BeobDocument doc = new BeobDocument();
        jButtonZiel.addActionListener(new BeobDatei(daten, jTextFieldZiel, true));
        jButtonErstellen.addActionListener(new BeobTus());
        jSpinnerAnzahlBreite.addChangeListener(new BeobSpinnerBreite());
        jTextFieldZiel.getDocument().addDocumentListener(doc);
    }

    private void tus() {
        new TapeteErstellen(daten, jTextFieldZiel.getText()).tus((Integer) jSpinnerAnzahlBreite.getModel().getValue());
    }

    private void groesse() {
        if (daten.datenProjekt != null) {
            int h = daten.listeFarben.size() / (Integer) jSpinnerAnzahlBreite.getModel().getValue();
            if (daten.listeFarben.size() % (Integer) jSpinnerAnzahlBreite.getModel().getValue() != 0 || h == 0) {
                ++h;
            }
            jTextFieldHoehe.setText(String.valueOf(h));
            float x = 0, y = 0, fakt = 1;
            final int GROESSE = 100;
            int intX, intY;
            x = (Integer) jSpinnerAnzahlBreite.getModel().getValue() *
                Integer.parseInt(daten.datenProjekt.arr[Konstanten.PROJEKT_AUFLOESUNG_ZIEL_NR]);
            y = h * Integer.parseInt(daten.datenProjekt.arr[Konstanten.PROJEKT_AUFLOESUNG_ZIEL_NR]);
            intX = Math.round(x);
            intY = Math.round(y);
            if (x > y) {
                if (x > GROESSE) {
                    fakt = x / GROESSE;
                }
            } else if (y > GROESSE) {
                fakt = y / GROESSE;
            }
            x /= fakt;
            y /= fakt;
            final int MIN = 5;
            if (x < MIN) {
                x = MIN;
            }
            if (y < MIN) {
                y = MIN;
            }
            jPanelGroesse.setMaximumSize(new Dimension(Math.round(x), Math.round(y)));
            jPanelGroesse.setPreferredSize(new Dimension(Math.round(x), Math.round(y)));
            jTextFieldGroesse.setText(intX + " x " + intY + " Pixel");
            jPanelGroesse.updateUI();
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonErstellen = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldGroesse = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jTextFieldZiel = new javax.swing.JTextField();
        jButtonZiel = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldAnzahl = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jSpinnerAnzahlBreite = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldHoehe = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        jPanelGroesse = new javax.swing.JPanel();

        jButtonErstellen.setText("Fototapete erstellen");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setBackground(new java.awt.Color(204, 204, 204));
        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setText(" Zielbild");
        jLabel3.setOpaque(true);

        jLabel6.setBackground(new java.awt.Color(204, 204, 204));
        jLabel6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel6.setText(" Fototapete");
        jLabel6.setOpaque(true);

        jTextFieldGroesse.setEditable(false);

        jLabel2.setText("Größe:");

        jLabel7.setBackground(new java.awt.Color(204, 204, 204));
        jLabel7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel7.setText(" Einstellungen");
        jLabel7.setOpaque(true);

        jSeparator4.setBackground(new java.awt.Color(204, 204, 255));

        jButtonZiel.setText(":::");

        jSeparator5.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setText("Anzahl Bilder:");

        jTextFieldAnzahl.setEditable(false);

        jLabel4.setText("Anzahl Bilder in einer Reihe:");

        jSpinnerAnzahlBreite.setModel(new javax.swing.SpinnerNumberModel(10, 1, 100, 1));

        jLabel5.setText("Anzahl Reihen:");

        jTextFieldHoehe.setEditable(false);

        jSeparator6.setBackground(new java.awt.Color(204, 204, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldGroesse, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextFieldAnzahl))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel5))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jSpinnerAnzahlBreite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextFieldHoehe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextFieldZiel, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonZiel)))))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jSpinnerAnzahlBreite, jTextFieldHoehe});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonZiel)
                    .addComponent(jTextFieldZiel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldAnzahl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jSpinnerAnzahlBreite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextFieldHoehe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldGroesse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jSpinnerAnzahlBreite, jTextFieldHoehe});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonZiel, jTextFieldZiel});

        jPanelGroesse.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelGroesseLayout = new javax.swing.GroupLayout(jPanelGroesse);
        jPanelGroesse.setLayout(jPanelGroesseLayout);
        jPanelGroesseLayout.setHorizontalGroup(
            jPanelGroesseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );
        jPanelGroesseLayout.setVerticalGroup(
            jPanelGroesseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonErstellen, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(jPanelGroesse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanelGroesse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 138, Short.MAX_VALUE)
                .addComponent(jButtonErstellen)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonErstellen;
    private javax.swing.JButton jButtonZiel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelGroesse;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSpinner jSpinnerAnzahlBreite;
    private javax.swing.JTextField jTextFieldAnzahl;
    private javax.swing.JTextField jTextFieldGroesse;
    private javax.swing.JTextField jTextFieldHoehe;
    private javax.swing.JTextField jTextFieldZiel;
    // End of variables declaration//GEN-END:variables
    private class BeobTus implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            tus();
        }

    }

    private class BeobSpinnerBreite implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent arg0) {
            groesse();
        }

    }

    private class BeobDocument implements DocumentListener {

        public void changedUpdate(DocumentEvent e) {
            tus();
        }

        public void insertUpdate(DocumentEvent e) {
            tus();
        }

        public void removeUpdate(DocumentEvent e) {
            tus();
        }

        private void tus() {
            if (!daten.beobStop) {
                if (daten.datenProjekt != null) {
                    daten.datenProjekt.arr[Konstanten.PROJEKT_PFAD_TAPETE_ZIEL_NR] = jTextFieldZiel.getText();
                }
            }
        }

    }

}