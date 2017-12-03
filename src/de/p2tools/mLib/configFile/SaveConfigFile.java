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

package de.p2tools.mLib.configFile;

import de.p2tools.controller.config.Const;
import de.p2tools.controller.config.ProgData;
import de.p2tools.mLib.tools.Log;
import de.p2tools.mLib.tools.SysMsg;
import javafx.collections.ObservableList;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

class SaveConfigFile implements AutoCloseable {

    private XMLStreamWriter writer = null;
    private OutputStreamWriter out = null;
    private Path xmlFilePath = null;
    private OutputStream os = null;
    private ProgData progData = null;

    private final String filePath;
    private final ArrayList<ObservableList<? extends ConfigsData>> configsListList;
    private final ArrayList<ConfigsData> arrayList;

    SaveConfigFile(String filePath, ArrayList<ObservableList<? extends ConfigsData>> configsListList, ArrayList<ConfigsData> arrayList) {
        this.filePath = filePath;
        this.configsListList = configsListList;
        this.arrayList = arrayList;
    }

    synchronized void write() {
        xmlFilePath = Paths.get(filePath);
        SysMsg.sysMsg("ProgData Schreiben nach: " + xmlFilePath.toString());
        xmlDatenSchreiben();
    }


    private void xmlDatenSchreiben() {
        try {
            xmlSchreibenStart();

            for (ObservableList<? extends ConfigsData> observableLists : configsListList) {
                writer.writeCharacters("\n\n");
                for (ConfigsData configsData : observableLists) {
                    writer.writeCharacters("\n\n");
                    xmlSchreibenDaten(configsData, true, 0);
                }
            }

            for (ConfigsData configsData : arrayList) {
                writer.writeCharacters("\n\n");
                xmlSchreibenDaten(configsData, true, 0);
            }

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


    private void xmlSchreibenDaten(ConfigsData configsData, boolean newLine, int tab) {
        //String xmlName, String[] xmlSpalten, String[] datenArray, boolean newLine, int tab

        String xmlName = configsData.getTagName();

        String[] datenArray = new String[configsData.getConfigsArr().length];
        String[] xmlSpalten = new String[configsData.getConfigsArr().length];

        for (int i = 0; i < configsData.getConfigsArr().length; ++i) {
            datenArray[i] = configsData.getConfigsArr()[i].getActValueToString();
            xmlSpalten[i] = configsData.getConfigsArr()[i].getKey();
        }

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
