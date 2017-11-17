/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mosaik.daten;

import mosaik.BeobAenderung;
import mosaik.Fehler;
import mosaik.bild.BildArchiv_;
import mosaik.bild.MosaikErstellen_;
import mosaik.gui.Mosaik;
import mosaik.io.IoXml;

import java.awt.*;
import java.io.File;
import java.util.Random;

/**
 * @author emil
 */
public class Daten {
    //public
    public String[] system;
    public DatenProjekt datenProjekt = new DatenProjekt();
    public ListeFarben listeFarben = new ListeFarben(this);
    //    public Farbraum_ farbraum = new Farbraum_(this);
    public IoXml ioXml = new IoXml(this);
    public BildArchiv_ bildArchiv = new BildArchiv_(this);
    public MosaikErstellen_ mosaikErstellen = new MosaikErstellen_(this);
    public Random random = new Random();
    public Dimension groesseBild;
    public Mosaik mosaik;
    public BeobAenderung beobAenderung = new BeobAenderung();
    public boolean beobStop = false;
    public Fehler fehler = new Fehler(this);
    //private
    private boolean geaendert = false;
    private String basisverzeichnis = "";

    /**
     * @param basis
     * @param mmosaik
     */
    public Daten(String basis, Mosaik mmosaik) {
        basisverzeichnis = basis;
        mosaik = mmosaik;
        system = new String[Konstanten.SYSTEM_MAX_ELEM];
        for (int i = 0; i < system.length; ++i) {
            system[i] = "";
        }
        if (!ioXml.datenSystemLesen()) {
            // Neustart und noch kein File, initialisieren
        }
    }

    public void setGeaendert() {
        beobAenderung.notifyEvent();
//        panelListe.panelAendern();
        geaendert = true;
    }

    public boolean isGeaendert() {
        return geaendert;
    }

    public String getBasisVerzeichnis() {
        String ret;
        if (basisverzeichnis.equals("")) {
            ret = System.getProperty("user.home") + File.separator + Konstanten.KONST_XML_DATEI + File.separator;
        } else {
            ret = basisverzeichnis;
        }
        return ret;
    }

    public void basisVerzeichnisAnlegen() {
        File basis = new File(getBasisVerzeichnis());
        if (!basis.exists()) {
            if (!basis.mkdir()) {
                fehler.fehlermeldung("Kann den Ordner zum Speichern der ProgData nicht anlegen!", "ProgData.getBasisVerzeichnis");
            }
        }
    }

}
