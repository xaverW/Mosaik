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

package mosaik.io;

import mosaik.daten.Daten;
import mosaik.daten.DatenFarbe_;
import mosaik.daten.DatenProjekt;
import mosaik.daten.Konstanten;

import javax.xml.stream.*;
import java.io.*;
import java.util.Iterator;

public class IoXml {

    private XMLOutputFactory outFactory;
    private XMLStreamWriter writer;
    private OutputStreamWriter out = null;
    private Daten daten;

    public IoXml(Daten d) {
        daten = d;
    }

    public void speichern() {
        daten.ioXml.datenSystemSchreiben();
        daten.ioXml.datenProjektSchreiben();
    }

    public boolean datenSystemLesen() {
        boolean ret = true;
        try {
            int event;
            XMLInputFactory inFactory = XMLInputFactory.newInstance();
            inFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
            XMLStreamReader parser = null;
            parser = inFactory.createXMLStreamReader(
                    new InputStreamReader(new FileInputStream(daten.getBasisVerzeichnis() + Konstanten.KONST_XML_DATEI), Konstanten.KONST_KODIERUNG_UTF));
            while (parser.hasNext()) {
                event = parser.next();
                //System
                if (event == XMLStreamConstants.START_ELEMENT && parser.getLocalName().equals(Konstanten.SYSTEM)) {
                    get(parser, event, Konstanten.SYSTEM, Konstanten.SYSTEM_COLUMN_NAMES, daten.system);
                }
            }
        } catch (Exception ex) {
            ret = false;
        }
        return ret;

    }

    public boolean datenProjektLesen() {
        daten.listeFarben.clear();
        daten.datenProjekt = new DatenProjekt();
        boolean ret = true;
        try {
            int event;
            XMLInputFactory inFactory = XMLInputFactory.newInstance();
            inFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
            XMLStreamReader parser = null;
            parser = inFactory.createXMLStreamReader(
                    new InputStreamReader(new FileInputStream(daten.system[Konstanten.SYSTEM_PROJECTDATEI_PFAD_NR]), Konstanten.KONST_KODIERUNG_UTF));
            while (parser.hasNext()) {
                event = parser.next();
                //Projekt
                if (event == XMLStreamConstants.START_ELEMENT && parser.getLocalName().equals(Konstanten.PROJEKT)) {
                    get(parser, event, Konstanten.PROJEKT, Konstanten.PROJEKT_COLUMN_NAMES, daten.datenProjekt.arr);
                }
                //Farben
                if (event == XMLStreamConstants.START_ELEMENT && parser.getLocalName().equals(Konstanten.FARBEN)) {
                    DatenFarbe_ farbe = new DatenFarbe_();
                    get(parser, event, Konstanten.FARBEN, Konstanten.FARBEN_COLUMN_NAMES, farbe.arr);
                    daten.listeFarben.add(farbe);
                }
            }
        } catch (Exception ex) {
            ret = false;
        }
        daten.beobAenderung.notifyEvent();
        return ret;

    }

    private boolean get(XMLStreamReader parser, int event, String xmlElem, String[] xmlNames, String[] strRet) {
        boolean ret = true;
        int maxElem = strRet.length;
        try {
            while (parser.hasNext()) {
                event = parser.next();
                if (event == XMLStreamConstants.END_ELEMENT && parser.getLocalName().equals(xmlElem)) {
                    break;
                }
                if (event == XMLStreamConstants.START_ELEMENT) {
                    for (int i = 0; i < maxElem; ++i) {
                        if (parser.getLocalName().equals(xmlNames[i])) {
                            strRet[i] = parser.getElementText();
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ret = false;
            daten.fehler.fehlermeldung(ex, "IoXml.get");
        }
        return ret;
    }

    private void datenSystemSchreiben() {
        try {
            //öffnen
            String datei = "";
            daten.basisVerzeichnisAnlegen();
            datei = daten.getBasisVerzeichnis() + Konstanten.KONST_XML_DATEI;
            outFactory = XMLOutputFactory.newInstance();
            out = new OutputStreamWriter(new FileOutputStream(datei), Konstanten.KONST_KODIERUNG_UTF);
            writer = outFactory.createXMLStreamWriter(out);
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeCharacters("\n");//neue Zeile
            writer.writeStartElement(Konstanten.KONST_XML_START);
            writer.writeCharacters("\n");//neue Zeile
            //System schreiben
            xmlSchreibenDaten(Konstanten.SYSTEM, Konstanten.SYSTEM_COLUMN_NAMES, daten.system);
            writer.writeCharacters("\n");//neue Zeile
            //Schließen
            xmlSchreibenEnde();
        } catch (Exception ex) {
        }
    }

    private void datenProjektSchreiben() {
        try {
            if (daten.datenProjekt != null) {
                //öffnen
                String datei = "";
                File basis = new File(daten.system[Konstanten.SYSTEM_PROJECTDATEI_PFAD_NR]);
                if (!basis.exists()) {
                    if (!basis.mkdir()) {
                        daten.fehler.fehlermeldung("Kann den Ordner zum Speichern der Projektdaten nicht anlegen!");
                    }
                }
                datei = daten.system[Konstanten.SYSTEM_PROJECTDATEI_PFAD_NR];
                outFactory = XMLOutputFactory.newInstance();
                out = new OutputStreamWriter(new FileOutputStream(datei), Konstanten.KONST_KODIERUNG_UTF);
                writer = outFactory.createXMLStreamWriter(out);
                writer.writeStartDocument("UTF-8", "1.0");
                writer.writeCharacters("\n");//neue Zeile
                writer.writeStartElement(Konstanten.KONST_XML_START);
                writer.writeCharacters("\n");//neue Zeile
                //Projekt schreiben
                xmlSchreibenDaten(Konstanten.PROJEKT, Konstanten.PROJEKT_COLUMN_NAMES, daten.datenProjekt.arr);
                writer.writeCharacters("\n");//neue Zeile
                //Farben schreiben
                Iterator<DatenFarbe_> it = daten.listeFarben.iterator();
                while (it.hasNext()) {
                    xmlSchreibenDaten(Konstanten.FARBEN, Konstanten.FARBEN_COLUMN_NAMES, it.next().arr);
                    writer.writeCharacters("\n");//neue Zeile
                }
                //Schließen
                xmlSchreibenEnde();
            }
        } catch (Exception ex) {
        }
    }

    private void xmlSchreibenDaten(String xmlName, String[] xmlSpalten, String[] datenArray) {
        int xmlMax = datenArray.length;
        try {
            writer.writeStartElement(xmlName);
            for (int i = 0; i < xmlMax; ++i) {
                writer.writeStartElement(xmlSpalten[i]);
                writer.writeCharacters(datenArray[i]);
                writer.writeEndElement();
            }
            writer.writeEndElement();
        } catch (Exception ex) {
            daten.fehler.fehlermeldung(ex, "IoXml.xmlSchreibenDaten");
        }

    }

    private void xmlSchreibenEnde() throws Exception {
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.flush();
        writer.close();
    }

}
