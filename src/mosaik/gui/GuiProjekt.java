/*
 * PanelMosaik.java
 *
 * Created on 10. September 2008, 15:52
 */

package mosaik.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import mosaik.BeobDatei;
import mosaik.Funktionen;
import mosaik.daten.Daten;
import mosaik.daten.Konstanten;

public class GuiProjekt extends PanelVorlage {

    private Daten daten;

    public GuiProjekt(Daten ddaten) {
        super(ddaten);
        daten = ddaten;
        initComponents();
        initText();
        BeobDocumentPfad doc = new BeobDocumentPfad();
        jButtonPfadBilder.addActionListener(new BeobDatei(daten, jTextFieldPfadBilder, false));
        jButtonBilderHinzufuegen.addActionListener(new BeobBilderHinzufuegen());
        jButtonEinlesen.addActionListener(new BeobEinlesen());
        jButtonLoeschen.addActionListener(new BeobBilderLoeschen());
        jTextFieldName.getDocument().addDocumentListener(new BeobDocumentName());
        jTextFieldPfadBilder.getDocument().addDocumentListener(doc);
        jCheckBoxRechteck.addActionListener(new BeobRadio());
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
        initText();
    }

    @Override
    void neuLadenInTime() {
        jTextFieldAnzahl.setText(String.valueOf(daten.listeFarben.size()));
    }
    //////////////////////////////////
    // private
    //////////////////////////////////
    private void initText() {
        daten.beobStop = true;
        if (daten.datenProjekt != null) {
            jTextFieldPfadBilder.setText(daten.datenProjekt.arr[Konstanten.PROJEKT_PFAD_BILDER_NR]);
            jTextFieldName.setText(daten.datenProjekt.arr[Konstanten.PROJEKT_NAME_NR]);
            jTextFieldAnzahl.setText(String.valueOf(daten.listeFarben.size()));
            jTextFieldBreite.setText(daten.datenProjekt.arr[Konstanten.PROJEKT_AUFLOESUNG_ZIEL_NR]);
            jTextFieldArt.setText(daten.datenProjekt.arr[Konstanten.PROJEKT_ARCHIV_FORMAT_NR]);
        }
        jCheckBoxRechteck.setSelected(Boolean.parseBoolean(daten.datenProjekt.arr[Konstanten.PROJEKT_ARCHIV_RECHTECK_NR]));
        daten.beobStop = false;
    }

    private void bilderLoeschen() {
        daten.listeFarben.clear();
        daten.setGeaendert();
        initText();
    }

    private void bilderEinlesen() {
        bilderLoeschen();
        String dest = Funktionen.getPfadProjektOrdner(daten);
        if (dest.endsWith(File.separator)) {
            dest += Konstanten.KONST_ARCHIV;
        } else {
            dest += File.separator + Konstanten.KONST_ARCHIV;
        }
        daten.bildArchiv.einlesen(dest);
    }

    private void bilderHinzufuegen() {
        String dest = Funktionen.getPfadProjektOrdner(daten);
        if (dest.endsWith(File.separator)) {
            dest += Konstanten.KONST_ARCHIV;
        } else {
            dest += File.separator + Konstanten.KONST_ARCHIV;
        }
        daten.bildArchiv.erstellen(jTextFieldPfadBilder.getText(), dest, jCheckBoxUnterordner.isSelected());
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jTextFieldName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldAnzahl = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        jTextFieldPfadBilder = new javax.swing.JTextField();
        jButtonPfadBilder = new javax.swing.JButton();
        jCheckBoxUnterordner = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jCheckBoxRechteck = new javax.swing.JCheckBox();
        jTextFieldBreite = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldArt = new javax.swing.JTextField();
        jButtonEinlesen = new javax.swing.JButton();
        jButtonBilderHinzufuegen = new javax.swing.JButton();
        jButtonLoeschen = new javax.swing.JButton();

        jLabel3.setText("jLabel3");

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setBackground(new java.awt.Color(204, 204, 204));
        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText(" Projektname");
        jLabel1.setOpaque(true);

        jLabel2.setBackground(new java.awt.Color(204, 204, 204));
        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setText(" Bilder zum Projekt hinzufügen");
        jLabel2.setOpaque(true);

        jSeparator3.setBackground(new java.awt.Color(204, 204, 255));

        jLabel5.setText("Anzahl Bilder:");

        jTextFieldAnzahl.setEditable(false);
        jTextFieldAnzahl.setBorder(null);

        jSeparator4.setBackground(new java.awt.Color(204, 204, 255));

        jButtonPfadBilder.setText(":::");

        jCheckBoxUnterordner.setSelected(true);
        jCheckBoxUnterordner.setText("Unterordner mit hinzufügen");

        jLabel4.setText("Breite und Höhe der Einzelbilder [Pixel] :");

        jCheckBoxRechteck.setText("Seitenverhältnis beachten und Bilder erst quadratisch zuschneiden");

        jTextFieldBreite.setEditable(false);
        jTextFieldBreite.setBorder(null);

        jLabel6.setText("Archivbilder erzeugen als:");

        jTextFieldArt.setEditable(false);
        jTextFieldArt.setBorder(null);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldName, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(175, 175, 175)
                                .addComponent(jTextFieldAnzahl, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldArt, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                                    .addComponent(jTextFieldBreite, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)))
                            .addComponent(jCheckBoxUnterordner)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jTextFieldPfadBilder, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonPfadBilder))
                            .addComponent(jCheckBoxRechteck))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextFieldAnzahl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldBreite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextFieldArt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonPfadBilder)
                    .addComponent(jTextFieldPfadBilder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxUnterordner)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxRechteck)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonPfadBilder, jTextFieldPfadBilder});

        jButtonEinlesen.setText("Projekt neu einlesen");

        jButtonBilderHinzufuegen.setText("Bilder hinzufügen");

        jButtonLoeschen.setText("Bilderliste Löschen");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonBilderHinzufuegen, javax.swing.GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonEinlesen, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 295, Short.MAX_VALUE)
                        .addComponent(jButtonLoeschen, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButtonEinlesen, jButtonLoeschen});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 192, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonEinlesen)
                    .addComponent(jButtonLoeschen))
                .addGap(18, 18, 18)
                .addComponent(jButtonBilderHinzufuegen)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonBilderHinzufuegen;
    private javax.swing.JButton jButtonEinlesen;
    private javax.swing.JButton jButtonLoeschen;
    private javax.swing.JButton jButtonPfadBilder;
    private javax.swing.JCheckBox jCheckBoxRechteck;
    private javax.swing.JCheckBox jCheckBoxUnterordner;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTextField jTextFieldAnzahl;
    private javax.swing.JTextField jTextFieldArt;
    private javax.swing.JTextField jTextFieldBreite;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldPfadBilder;
    // End of variables declaration//GEN-END:variables
    private class BeobEinlesen implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            bilderEinlesen();
        }

    }

    private class BeobBilderHinzufuegen implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            bilderHinzufuegen();
        }

    }

    private class BeobBilderLoeschen implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            bilderLoeschen();
        }

    }

    private class BeobDocumentName implements DocumentListener {

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
                    daten.datenProjekt.arr[Konstanten.PROJEKT_NAME_NR] = jTextFieldName.getText();
                    daten.system[Konstanten.SYSTEM_PROJECT_NAME_NR] = jTextFieldName.getText();
                    daten.mosaik.setTitel();
                    daten.setGeaendert();
                }
            }
        }

    }

    private class BeobDocumentPfad implements DocumentListener {

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
                    daten.datenProjekt.arr[Konstanten.PROJEKT_PFAD_BILDER_NR] = jTextFieldPfadBilder.getText();
                    daten.setGeaendert();
                }
            }
        }

    }

    private class BeobRadio implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            daten.datenProjekt.arr[Konstanten.PROJEKT_ARCHIV_RECHTECK_NR] = Boolean.toString(jCheckBoxRechteck.isSelected());
            daten.setGeaendert();
        }

    }

}