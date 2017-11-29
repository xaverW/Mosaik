/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://sourceforge.net/projects/mtplayer/
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */

package de.p2tools.controller;

import de.p2tools.controller.config.Config;
import de.p2tools.controller.config.Const;
import de.p2tools.controller.config.ProgData;
import de.p2tools.controller.config.ProgInfos;
import de.p2tools.controller.data.mosaikData.MosaikData;
import de.p2tools.controller.data.thumb.Thumb;
import de.p2tools.controller.data.thumb.ThumbCollection;
import de.p2tools.controller.data.thumb.ThumbList;
import de.p2tools.mLib.tools.Log;
import de.p2tools.mLib.tools.SysMsg;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class IoXmlSchreiben implements AutoCloseable {

    private XMLStreamWriter writer = null;
    private OutputStreamWriter out = null;
    private Path xmlFilePath = null;
    private OutputStream os = null;
    private ProgData progData = null;

    public IoXmlSchreiben(ProgData progData) {
        this.progData = progData;
    }

    public synchronized void datenSchreiben() {
        xmlFilePath = new ProgInfos().getXmlFilePath();
        SysMsg.sysMsg("ProgData Schreiben nach: " + xmlFilePath.toString());
        xmlDatenSchreiben();
    }

    private void xmlDatenSchreiben() {
        try {
            xmlSchreibenStart();

            writer.writeCharacters("\n\n");
            writer.writeComment("Programmeinstellungen");
            writer.writeCharacters("\n");
            xmlSchreibenConfig(Config.SYSTEM, Config.getAll());
            writer.writeCharacters("\n");

            writer.writeCharacters("\n\n");
            progData.mosaikData.setXmlFromProps();
            xmlSchreibenDaten(MosaikData.TAG, MosaikData.XML_NAMES, progData.mosaikData.arr, true);

            xmlWriteThumbCollection();

            writer.writeCharacters("\n\n");
            xmlSchreibenEnde();
        } catch (final Exception ex) {
            Log.errorLog(656328109, ex);
        }
    }

    private void xmlSchreibenStart() throws IOException, XMLStreamException {
        SysMsg.sysMsg("Start Schreiben nach: " + xmlFilePath.toAbsolutePath());
        os = Files.newOutputStream(xmlFilePath);
        out = new OutputStreamWriter(os, StandardCharsets.UTF_8);

        final XMLOutputFactory outFactory = XMLOutputFactory.newInstance();
        writer = outFactory.createXMLStreamWriter(out);
        writer.writeStartDocument(StandardCharsets.UTF_8.name(), "1.0");
        writer.writeCharacters("\n");// neue Zeile
        writer.writeStartElement(Const.XML_START);
        writer.writeCharacters("\n");// neue Zeile
    }


    private void xmlWriteThumbCollection() throws XMLStreamException {
        for (ThumbCollection thumbCollection : progData.thumbCollectionList) {
            thumbCollection.setXmlFromProps();
            writer.writeComment("ThumbCollection: " + thumbCollection.getName());
            writer.writeCharacters("\n");

            try {
                writer.writeStartElement(ThumbCollection.TAG);
                writer.writeCharacters("\n"); // neue Zeile
                for (int i = 0; i < ThumbCollection.MAX_ELEM; ++i) {
                    if (!thumbCollection.arr[i].isEmpty()) {
                        writer.writeCharacters("\t"); // Tab
                        writer.writeStartElement(ThumbCollection.XML_NAMES[i]);
                        writer.writeCharacters(thumbCollection.arr[i]);
                        writer.writeEndElement();
                        writer.writeCharacters("\n"); // neue Zeile
                    }
                }
                writer.writeCharacters("\n"); // neue Zeile
                ThumbList thumbList = thumbCollection.getThumbList();
                for (Thumb thumb : thumbList) {
                    thumb.setXmlFromProps();
                    xmlSchreibenDaten(Thumb.TAG, Thumb.XML_NAMES, thumb.arr, false, 1);
                }

                writer.writeEndElement();
                writer.writeCharacters("\n"); // neue Zeile
            } catch (final Exception ex) {
                Log.errorLog(198325017, ex);
            }


        }
    }


    private void xmlSchreibenDaten(String xmlName, String[] xmlSpalten, String[] datenArray, boolean newLine) {
        xmlSchreibenDaten(xmlName, xmlSpalten, datenArray, newLine, 0);
    }

    private void xmlSchreibenDaten(String xmlName, String[] xmlSpalten, String[] datenArray, boolean newLine, int tab) {
        final int xmlMax = datenArray.length;
        try {
            for (int t = 0; t < tab; ++t) {
                writer.writeCharacters("\t"); // Tab
            }
            writer.writeStartElement(xmlName);
            if (newLine) {
                writer.writeCharacters("\n"); // neue Zeile
            }
            for (int i = 0; i < xmlMax; ++i) {
                if (!datenArray[i].isEmpty()) {
                    if (newLine) {
                        writer.writeCharacters("\t"); // Tab
                    }
                    writer.writeStartElement(xmlSpalten[i]);
                    writer.writeCharacters(datenArray[i]);
                    writer.writeEndElement();
                    if (newLine) {
                        writer.writeCharacters("\n"); // neue Zeile
                    }
                }
            }
            writer.writeEndElement();
            writer.writeCharacters("\n"); // neue Zeile
        } catch (final Exception ex) {
            Log.errorLog(198325017, ex);
        }
    }


    private void xmlSchreibenConfig(String xmlName, String[][] xmlSpalten) {
        try {
            writer.writeStartElement(xmlName);
            writer.writeCharacters("\n"); // neue Zeile

            for (final String[] xmlSpalte : xmlSpalten) {
                writer.writeCharacters("\t"); // Tab
                writer.writeStartElement(xmlSpalte[0]);
                writer.writeCharacters(xmlSpalte[1]);
                writer.writeEndElement();
                writer.writeCharacters("\n"); // neue Zeile
            }
            writer.writeEndElement();
            writer.writeCharacters("\n"); // neue Zeile
        } catch (final Exception ex) {
            Log.errorLog(951230478, ex);
        }
    }

    private void xmlSchreibenEnde() throws Exception {
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.flush();

        SysMsg.sysMsg("geschrieben!");
    }

    @Override
    public void close() throws Exception {
        writer.close();
        out.close();
        os.close();
    }
}
