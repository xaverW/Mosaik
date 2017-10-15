/*
 * QemuStarter.java
 *
 */

package mosaik.gui;

import mosaik.daten.Daten;
import mosaik.BildEvent;
import mosaik.BildListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import mosaik.daten.Konstanten;

public class Mosaik extends javax.swing.JFrame {

    private Daten daten;
    private GuiProjekt guiProjekt;
    private GuiTapete guiTapete;
    private GuiMosaik guiMosaik;
    private GuiBilder guiBilder;

    public Mosaik() {
        initComponents();
        daten = new Daten("", this);
        //Gui's
        guiProjekt = new GuiProjekt(daten);
        guiTapete = new GuiTapete(daten);
        guiMosaik = new GuiMosaik(daten);
        guiBilder = new GuiBilder(daten);
        initBeobachter();
        autoOeffnen();
    }
    ////////////////////////////////////////////////////
    // public
    ////////////////////////////////////////////////////
    public void setTitel() {
        if (daten.datenProjekt != null) {
            this.setTitle(daten.system[Konstanten.SYSTEM_PROJECTDATEI_PFAD_NR]);
        } else {
            this.setTitle("Mosaik");
        }
    }
    ////////////////////////////////////////////////////////
    // private
    ////////////////////////////////////////////////////////
    private void initBeobachter() {
        jMenuItemNeu.addActionListener(new java.awt.event.ActionListener() {

                                       @Override
                                       public void actionPerformed(java.awt.event.ActionEvent evt) {
                                           neu();
                                       }

                                   });
        jMenuItemOeffnen.addActionListener(new java.awt.event.ActionListener() {

                                           @Override
                                           public void actionPerformed(java.awt.event.ActionEvent evt) {
                                               oeffnen();
                                           }

                                       });
        jMenuItemSpeichern.addActionListener(new java.awt.event.ActionListener() {

                                             @Override
                                             public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                 daten.ioXml.speichern();
                                             }

                                         });
        jMenuItemBeenden.addActionListener(new java.awt.event.ActionListener() {

                                           @Override
                                           public void actionPerformed(java.awt.event.ActionEvent evt) {
                                               beenden();
                                           }

                                       });


        addWindowListener(new java.awt.event.WindowAdapter() {

                          @Override
                          public void windowClosing(java.awt.event.WindowEvent evt) {
                              beenden();
                          }

                      });
        daten.bildArchiv.addAdListener(new BeobachterStart());
        daten.mosaikErstellen.addAdListener(new BeobachterStart());
        jButtonStop.addActionListener(new BeobachterStop());
    }

    private void beenden() {
        if (daten.isGeaendert()) {
            daten.ioXml.speichern();
        }
        System.exit(0);
    }

    private void autoOeffnen() {
        //laden
        if (daten.ioXml.datenProjektLesen()) {
            setPanels();
        } else {
            neu();
        }
    }

    private void oeffnen() {
        DialogProjektOeffnen dialog = new DialogProjektOeffnen(null, true, daten);
        dialog.setVisible(true);
        if (dialog.ok) {
            //laden
            daten.listeFarben.clear();
            if (daten.ioXml.datenProjektLesen()) {
                setPanels();
            }
        } else {
            daten.fehler.fehlermeldung("Kann das Projekt nicht öffnen!");
            daten.datenProjekt = null;
            setPanels();
        }
    }

    private void neu() {
        DialogProjektNeu dialog = new DialogProjektNeu(null, true, daten);
        dialog.setVisible(true);
        if (dialog.ok) {
            //laden
            setPanels();
        } else {
            daten.datenProjekt = null;
            setPanels();
        }
    }

    private synchronized void setPanels() {
        jTabbedPane1.removeAll();
        if (daten.datenProjekt != null) {
            this.setTitle(daten.system[Konstanten.SYSTEM_PROJECTDATEI_PFAD_NR]);
            jTabbedPane1.add("Projekt", guiProjekt);
            jTabbedPane1.add("Fototapete", guiTapete);
            jTabbedPane1.add("Mosaik", guiMosaik);
            jTabbedPane1.add("Bilder", guiBilder);
        } else {
            this.setTitle("Mosaik");
        }
        try {
            this.wait(100); //für den Debugger
        } catch (InterruptedException ex) {
        }
    }

    private synchronized void setProgressBar(BildEvent ev) {
        if (ev.nixLos()) {
            jProgressBar1.setMaximum(0);
            jProgressBar1.setValue(0);
            jProgressBar1.setStringPainted(false);
        } else {
            jProgressBar1.setMaximum(ev.getMax());
            int proz = 0;
            if (ev.getProgress() != 0) {
                proz = ev.getProgress() * 100 / ev.getMax();
            }
            String text = "";
            if (ev.getThreads() > 1) {
                text = ev.getThreads() + " Prozesse";
            }
            if (!ev.getText().equals("")) {
                if (text.equals("")) {
                    text += ev.getText();
                } else {
                    text += "  -  " + ev.getText();
                }
            }
            if (text.equals("")) {
                text = proz + "% von " + ev.getMax() + " Bildern";
            } else {
                text += "  -  " + proz + "% von " + ev.getMax() + " Bildern";
            }
            jProgressBar1.setString(text);
            jProgressBar1.setStringPainted(true);
            jProgressBar1.setValue(ev.getProgress());
        }
        jPanelStatus.repaint();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanelStatus = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButtonStop = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuProjekt = new javax.swing.JMenu();
        jMenuItemOeffnen = new javax.swing.JMenuItem();
        jMenuItemNeu = new javax.swing.JMenuItem();
        jMenuItemSpeichern = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItemBeenden = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButtonStop.setText("Stop");

        javax.swing.GroupLayout jPanelStatusLayout = new javax.swing.GroupLayout(jPanelStatus);
        jPanelStatus.setLayout(jPanelStatusLayout);
        jPanelStatusLayout.setHorizontalGroup(
            jPanelStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 814, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonStop)
                .addContainerGap())
        );
        jPanelStatusLayout.setVerticalGroup(
            jPanelStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelStatusLayout.createSequentialGroup()
                .addGroup(jPanelStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonStop, 0, 24, Short.MAX_VALUE)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanelStatusLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonStop, jProgressBar1});

        jMenuProjekt.setText("Projekt");

        jMenuItemOeffnen.setText("Projekt öffnen");
        jMenuProjekt.add(jMenuItemOeffnen);

        jMenuItemNeu.setText("Neues Projekt");
        jMenuProjekt.add(jMenuItemNeu);

        jMenuItemSpeichern.setText("Projekt Speichern");
        jMenuProjekt.add(jMenuItemSpeichern);
        jMenuProjekt.add(jSeparator1);

        jMenuItemBeenden.setText("Beenden");
        jMenuProjekt.add(jMenuItemBeenden);

        jMenuBar1.add(jMenuProjekt);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 893, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonStop;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemBeenden;
    private javax.swing.JMenuItem jMenuItemNeu;
    private javax.swing.JMenuItem jMenuItemOeffnen;
    private javax.swing.JMenuItem jMenuItemSpeichern;
    private javax.swing.JMenu jMenuProjekt;
    private javax.swing.JPanel jPanelStatus;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
    private class BeobachterStart implements BildListener {

        public void tus(BildEvent e) {
            setProgressBar(e);
        }

    }

    private class BeobachterStop implements ActionListener {

        public void actionPerformed(ActionEvent arg0) {
            daten.bildArchiv.setStop();
            daten.mosaikErstellen.setStop();
        }

    }

}