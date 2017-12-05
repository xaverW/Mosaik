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
                for (ConfigsData configsData : observableLists) {
                    writer.writeCharacters("\n\n");
                    writeConfigsData(configsData, 0);
                }
            }

            for (ConfigsData configsData : arrayList) {
                writer.writeCharacters("\n\n");
                writeConfigsData(configsData, 0);
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
        writer.writeCharacters("\n");
        writer.writeStartElement(Const.XML_START);
        writer.writeCharacters("\n");
    }


    private void writeConfigsData(ConfigsData configsData, int tab) throws XMLStreamException {

        String xmlName = configsData.getTagName();

        for (int t = 0; t < tab; ++t) {
            writer.writeCharacters("\t"); // Tab
        }
        writer.writeStartElement(xmlName);
        writer.writeCharacters("\n"); // neue Zeile

        ++tab;
        for (Configs configs : configsData.getConfigsArr()) {
            writeConfigs(configs, tab);
        }
        --tab;

        for (int t = 0; t < tab; ++t) {
            writer.writeCharacters("\t"); // Tab
        }
        writer.writeEndElement();
        writer.writeCharacters("\n"); // neue Zeile
    }

    private void writeConfigs(Configs configs, int tab) throws XMLStreamException {
        if (configs.getClass().equals(ConfigsList.class)) {
            writeConfList((ConfigsList) configs, tab);
        } else {
            writeConf(configs, tab);
        }
    }


    private void writeConf(Configs configs, int tab) throws XMLStreamException {
        if (!configs.getActValueToString().isEmpty()) {
            for (int t = 0; t < tab; ++t) {
                writer.writeCharacters("\t"); // Tab
            }
            writer.writeStartElement(configs.getKey());
            writer.writeCharacters(configs.getActValueToString());
            writer.writeEndElement();
            writer.writeCharacters("\n"); // neue Zeile
        }
    }


    private void writeConfList(ConfigsList configsData, int tab) throws XMLStreamException {
        ObservableList<? extends ConfigsData> observableList = configsData.getActValue();
        for (ConfigsData configs : observableList) {
            writeConfigsData(configs, tab);
        }
    }


    private void xmlSchreibenEnde() throws XMLStreamException {
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.flush();
        SysMsg.sysMsg("geschrieben!");
    }

    @Override
    public void close() throws IOException, XMLStreamException {
        writer.close();
        out.close();
        os.close();
    }
}
