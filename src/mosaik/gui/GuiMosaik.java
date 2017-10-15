/*
 * PanelMosaik.java
 *
 * Created on 10. September 2008, 15:52
 */

package mosaik.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import mosaik.BeobDatei;
import mosaik.daten.Daten;
import mosaik.daten.Konstanten;

public class GuiMosaik extends PanelVorlage {

    private Daten daten;

    public GuiMosaik(Daten ddaten) {
        super(ddaten);
        daten = ddaten;
        initComponents();
        initBeobachter();
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
    }
    //////////////////////////////////
    // private
    //////////////////////////////////
    private void laden() {
        daten.beobStop = true;
        if (daten.datenProjekt != null) {
            jTextFieldSrc.setText(daten.datenProjekt.arr[Konstanten.PROJEKT_MOSAIK_QUELLE_NR]);
            jTextFieldDest.setText(daten.datenProjekt.arr[Konstanten.PROJEKT_MOSAIK_ZIEL_NR]);
            jRadioButtonJpg.setSelected(true);
            jRadioButtonPng.setSelected(daten.datenProjekt.arr[Konstanten.PROJEKT_MOSAIK_FORMAT_NR].endsWith(Konstanten.IMAGE_FORMAT_PNG));
            jSpinnerWiederholung.setValue(Integer.parseInt(daten.datenProjekt.arr[Konstanten.PROJEKT_ANZAHL_BILD_NR]));
            jSpinnerHoehe.setValue(Integer.parseInt(daten.datenProjekt.arr[Konstanten.PROJEKT_BILDER_HOEHE_NR]));
        }
        jTextFieldAnzahl.setText(String.valueOf(daten.listeFarben.size()));
        daten.beobStop = false;
    }

    private void initBeobachter() {
        BeobDocument doc = new BeobDocument();
        jButtonSrc.addActionListener(new BeobDatei(daten, jTextFieldSrc, true, "jpg", new String[]{"jpg", "jpeg"}));
        jButtonDest.addActionListener(new BeobDatei(daten, jTextFieldDest, true));
        jButtonErstellen.addActionListener(new BeobTus());
        jTextFieldDest.getDocument().addDocumentListener(doc);
        jTextFieldSrc.getDocument().addDocumentListener(doc);
        jRadioButtonJpg.addActionListener(new BeobRadio());
        jRadioButtonPng.addActionListener(new BeobRadio());
        jSpinnerWiederholung.addChangeListener(new BeobSpinner());
        jSpinnerHoehe.addChangeListener(new BeobSpinner());
    }

    private void tus() {
        daten.mosaikErstellen.erstellen(jTextFieldSrc.getText(), jTextFieldDest.getText(), (Integer) jSpinnerWiederholung.getValue());
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jButtonErstellen = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jTextFieldSrc = new javax.swing.JTextField();
        jButtonSrc = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jTextFieldDest = new javax.swing.JTextField();
        jButtonDest = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldAnzahl = new javax.swing.JTextField();
        jSpinnerWiederholung = new javax.swing.JSpinner();
        jRadioButtonJpg = new javax.swing.JRadioButton();
        jRadioButtonPng = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        jSpinnerHoehe = new javax.swing.JSpinner();

        jButtonErstellen.setText("Mosaik erstellen");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setBackground(new java.awt.Color(204, 204, 204));
        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14));
        jLabel3.setText(" Quellbild");
        jLabel3.setOpaque(true);

        jLabel2.setBackground(new java.awt.Color(204, 204, 204));
        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14));
        jLabel2.setText(" Zielbild");
        jLabel2.setOpaque(true);

        jLabel4.setBackground(new java.awt.Color(204, 204, 204));
        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14));
        jLabel4.setText(" Mosaik");
        jLabel4.setOpaque(true);

        jSeparator1.setBackground(new java.awt.Color(204, 204, 255));

        jButtonSrc.setText(":::");

        jSeparator2.setBackground(new java.awt.Color(204, 204, 255));

        jButtonDest.setText(":::");

        jSeparator3.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setText("Anzahl Bilder im Archiv:");

        jLabel5.setText("Bilder nur n-Mal verwenden (0 für keine Beschränkung) :");

        jTextFieldAnzahl.setEditable(false);

        jSpinnerWiederholung.setModel(new javax.swing.SpinnerNumberModel(0, 0, 99, 1));

        buttonGroup1.add(jRadioButtonJpg);
        jRadioButtonJpg.setText("jpg");

        buttonGroup1.add(jRadioButtonPng);
        jRadioButtonPng.setText("png");

        jLabel6.setText("Höhe Mosaik (Anzahl Einzelbilder) :");

        jSpinnerHoehe.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(10), Integer.valueOf(10), null, Integer.valueOf(1)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldAnzahl, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                            .addComponent(jSpinnerHoehe, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                            .addComponent(jSpinnerWiederholung, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)))
                    .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 857, Short.MAX_VALUE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 857, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jTextFieldSrc, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSrc))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 857, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 857, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 857, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 857, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jRadioButtonJpg)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButtonPng))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextFieldDest, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonDest)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSrc)
                    .addComponent(jTextFieldSrc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonDest)
                    .addComponent(jTextFieldDest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonJpg)
                    .addComponent(jRadioButtonPng))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldAnzahl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jSpinnerWiederholung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jSpinnerHoehe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonDest, jTextFieldDest});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonSrc, jTextFieldSrc});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonErstellen, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 885, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 220, Short.MAX_VALUE)
                .addComponent(jButtonErstellen)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonDest;
    private javax.swing.JButton jButtonErstellen;
    private javax.swing.JButton jButtonSrc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButtonJpg;
    private javax.swing.JRadioButton jRadioButtonPng;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSpinner jSpinnerHoehe;
    private javax.swing.JSpinner jSpinnerWiederholung;
    private javax.swing.JTextField jTextFieldAnzahl;
    private javax.swing.JTextField jTextFieldDest;
    private javax.swing.JTextField jTextFieldSrc;
    // End of variables declaration//GEN-END:variables
    private class BeobTus implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            tus();
        }

    }

    private class BeobRadio implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (jRadioButtonJpg.isSelected()) {
                daten.datenProjekt.arr[Konstanten.PROJEKT_MOSAIK_FORMAT_NR] = Konstanten.IMAGE_FORMAT_JPG;
            } else {
                daten.datenProjekt.arr[Konstanten.PROJEKT_MOSAIK_FORMAT_NR] = Konstanten.IMAGE_FORMAT_PNG;
            }
            daten.setGeaendert();
        }

    }

    private class BeobSpinner implements ChangeListener {

        public void stateChanged(ChangeEvent arg0) {
            if (!daten.beobStop) {
                if (daten.datenProjekt != null) {
                    daten.datenProjekt.arr[Konstanten.PROJEKT_ANZAHL_BILD_NR] = String.valueOf((Number) jSpinnerWiederholung.getValue());
                    daten.datenProjekt.arr[Konstanten.PROJEKT_BILDER_HOEHE_NR] = String.valueOf((Number) jSpinnerHoehe.getValue());
                    daten.setGeaendert();
                }
            }
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
                    daten.datenProjekt.arr[Konstanten.PROJEKT_MOSAIK_QUELLE_NR] = jTextFieldSrc.getText();
                    daten.datenProjekt.arr[Konstanten.PROJEKT_MOSAIK_ZIEL_NR] = jTextFieldDest.getText();
                    daten.setGeaendert();
                }
            }
        }

    }

}