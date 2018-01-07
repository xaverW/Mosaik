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

import de.p2tools.controller.config.ProgConst;
import de.p2tools.controller.config.ProgData;
import de.p2tools.mLib.configFile.config.Config;
import de.p2tools.mLib.configFile.config.ConfigConfigsData;
import de.p2tools.mLib.configFile.config.ConfigConfigsList;
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
import java.util.ArrayList;

class SaveConfigFile implements AutoCloseable {

    private XMLStreamWriter writer = null;
    private OutputStreamWriter out = null;
    private Path xmlFilePath = null;
    private OutputStream os = null;
    private ProgData progData = null;

    private final ArrayList<ConfigsList> configsList;
    private final ArrayList<ConfigsData> configsData;

    SaveConfigFile(Path filePath, ArrayList<ConfigsList> configsList, ArrayList<ConfigsData> configsData) {
        xmlFilePath = filePath;
        this.configsList = configsList;
        this.configsData = configsData;
    }

    synchronized void write() {
        SysMsg.sysMsg("ProgData Schreiben nach: " + xmlFilePath.toString());
        xmlDatenSchreiben();
    }


    private void xmlDatenSchreiben() {
        try {
            xmlSchreibenStart();

            for (ConfigsData configsData : configsData) {
                writer.writeCharacters("\n\n");

                if (configsData.getClass().equals(ConfigConfigsData.class)) {
                    writeConfProp((ConfigConfigsData) configsData, 1);
                } else {
                    writeConfigsData(configsData, 0);
                }

            }

            for (ConfigsList cl : configsList) {
                writer.writeCharacters("\n\n");
                writeConfigsList(cl, 0);
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
        writer.writeStartElement(ProgConst.XML_START);
        writer.writeCharacters("\n");
    }


    private void writeConfigsList(ConfigsList configsList, int tab) throws XMLStreamException {

        String xmlName = configsList.getTagName();

        for (int t = 0; t < tab; ++t) {
            writer.writeCharacters("\t"); // Tab
        }
        writer.writeStartElement(xmlName);
        writer.writeCharacters("\n"); // neue Zeile

        ++tab;
        for (Object configsData : configsList) {
            if (configsData instanceof ConfigsData) {
                writeConfigsData((ConfigsData) configsData, tab);
            }
            if (configsData instanceof ConfigsList) {
                writeConfigsList((ConfigsList) configsData, tab);
            }
        }
        --tab;

        for (int t = 0; t < tab; ++t) {
            writer.writeCharacters("\t"); // Tab
        }
        writer.writeEndElement();
        writer.writeCharacters("\n"); // neue Zeile
    }

    private void writeConfigsData(ConfigsData configsData, int tab) throws XMLStreamException {

        String xmlName = configsData.getTagName();

        for (int t = 0; t < tab; ++t) {
            writer.writeCharacters("\t"); // Tab
        }
        writer.writeStartElement(xmlName);
        writer.writeCharacters("\n"); // neue Zeile

        ++tab;
        for (Config config : configsData.getConfigsArr()) {
            writeConfigs(config, tab);
        }
        --tab;

        for (int t = 0; t < tab; ++t) {
            writer.writeCharacters("\t"); // Tab
        }
        writer.writeEndElement();
        writer.writeCharacters("\n"); // neue Zeile
    }

    private void writeConfigs(Config config, int tab) throws XMLStreamException {
        if (config.getClass().equals(ConfigConfigsList.class)) {
            writeConfList((ConfigConfigsList) config, tab);
        } else {
            writeConf(config, tab);
        }
    }


    private void writeConfProp(ConfigConfigsData configConfigsData, int tab) throws XMLStreamException {
        ArrayList<Config> arrayList = configConfigsData.getActValue();
        for (Config configs : arrayList) {
            writeConf(configs, tab);
        }
    }

    private void writeConfList(ConfigConfigsList configConfigsList, int tab) throws XMLStreamException {
        ConfigsList<? extends ConfigsData> observableList = configConfigsList.getActValue();
        for (ConfigsData configs : observableList) {
            writeConfigsData(configs, tab);
        }
    }

    private void writeConf(Config config, int tab) throws XMLStreamException {
        if (!config.getActValueString().isEmpty()) {
            for (int t = 0; t < tab; ++t) {
                writer.writeCharacters("\t"); // Tab
            }
            writer.writeStartElement(config.getKey());
            writer.writeCharacters(config.getActValueString());
            writer.writeEndElement();
            writer.writeCharacters("\n"); // neue Zeile
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
