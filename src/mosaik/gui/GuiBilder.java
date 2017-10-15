/*
 * PanelMosaik.java
 *
 * Created on 10. September 2008, 15:52
 */

package mosaik.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import mosaik.FarbenCellRenderer;
import mosaik.TModel;
import mosaik.daten.Daten;
import mosaik.daten.Konstanten;

public class GuiBilder extends PanelVorlage {

    private Daten daten;
    private TModel tModel1;

    public GuiBilder(Daten ddaten) {
        super(ddaten);
        daten = ddaten;
        initComponents();
        jButtonAnzeigen.addActionListener(new BeobAnzeigen());
        jTable1.setDefaultRenderer(Object.class, new FarbenCellRenderer(daten));
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
    //////////////////////////////////
    // private
    //////////////////////////////////
    private void laden() {
        tModel1 = new TModel(daten.listeFarben.getObjectData(), Konstanten.FARBEN_COLUMN_NAMES);
        jTable1.setModel(tModel1);
    }

    private void anzeigen() {
        if (daten.listeFarben.size() > 0) {
            int nr = 0;
            if (jTable1.getSelectedRow() >= 0) {
                nr = jTable1.convertRowIndexToModel(jTable1.getSelectedRow());
            }
            new DialogAnzeigeBild(null, true, daten, nr);
            if (daten.isGeaendert()) {
                neuLaden();
            }
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButtonAnzeigen = new javax.swing.JButton();

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButtonAnzeigen.setText("Anzeigen");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                    .addComponent(jButtonAnzeigen))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonAnzeigen)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAnzeigen;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
    private class BeobAnzeigen implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            anzeigen();
        }

    }

}