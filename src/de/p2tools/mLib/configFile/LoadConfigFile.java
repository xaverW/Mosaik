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

import de.p2tools.mLib.tools.Duration;
import de.p2tools.mLib.tools.Log;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

class LoadConfigFile implements AutoCloseable {

    private XMLInputFactory inFactory;
    private Path xmlFilePath = null;

    private final String filePath;
    private final ArrayList<ConfigsList<ConfigsData>> configsListList;
    private final ArrayList<ConfigsData> configsDataArr;

    LoadConfigFile(String filePath, ArrayList<ConfigsList<ConfigsData>> configsListList, ArrayList<ConfigsData> configsDataArr) {
        this.filePath = filePath;
        this.configsListList = configsListList;
        this.configsDataArr = configsDataArr;

        inFactory = XMLInputFactory.newInstance();
        inFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
    }

    boolean readConfiguration() {
        Duration.counterStart("Konfig lesen");
        boolean ret = false;

        xmlFilePath = Paths.get(filePath);
        if (!Files.exists(xmlFilePath)) {
            Duration.counterStop("Konfig lesen");
            return ret;
        }

        XMLStreamReader parser = null;
        try (InputStream is = Files.newInputStream(xmlFilePath);
             InputStreamReader in = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            parser = inFactory.createXMLStreamReader(in);
            nextTag:
            while (parser.hasNext()) {
                final int event = parser.next();
                if (event != XMLStreamConstants.START_ELEMENT) {
                    continue nextTag;
                }

                String xmlElem = parser.getLocalName();
                for (ConfigsList<ConfigsData> list : configsListList) {

                    if (list.getTagName().equals(xmlElem)) {
                        getConfigList(parser, list);
                        continue nextTag;
                    }
                }
                for (ConfigsData configsData : configsDataArr) {
                    if (configsData.getTagName().equals(xmlElem)) {
                        getConfig(parser, configsData);
                        continue nextTag;
                    }
                }

            }
            ret = true;

        } catch (final Exception ex) {
            ret = false;
            Log.errorLog(732160795, ex);
        } finally {
            try {
                if (parser != null) {
                    parser.close();
                }
            } catch (final Exception ignored) {
            }
        }

        Duration.counterStop("Konfig lesen");
        return ret;
    }


    private boolean getConfigList(XMLStreamReader parser, ConfigsList<ConfigsData> list) {
        boolean ret = false;
        String xmlElem = parser.getLocalName();

        if (!list.getTagName().equals(xmlElem)) {
            return false;
        }

        try {
            while (parser.hasNext()) {
                final int event = parser.next();
                if (event == XMLStreamConstants.END_ELEMENT) {
                    if (parser.getLocalName().equals(xmlElem)) {
                        break;
                    }
                }
                if (event == XMLStreamConstants.START_ELEMENT) {
                    ConfigsData configsData = list.getNewItem();
                    final String s = parser.getLocalName();
                    final String n = parser.getElementText();
                    ArrayList<Configs> aList = configsData.getConfigsArr();
                    for (Configs configs : configsData.getConfigsArr()) {
                        if (configs.getKey().equals(s)) {
                            System.out.println(n + " - Configs " + configs.getActValueToString());
                            configs.setActValue(n);
                            System.out.println(n + " - Configs " + configs.getActValueToString());
                        }
                    }
                }
            }
            ret = true;

        } catch (final Exception ex) {
            Log.errorLog(203641970, ex);
        }

        return ret;
    }

    private boolean getConfig(XMLStreamReader parser, ConfigsData configsData) {
        boolean ret = false;
        String xmlElem = parser.getLocalName();

        try {
            while (parser.hasNext()) {
                final int event = parser.next();
                if (event == XMLStreamConstants.END_ELEMENT) {
                    if (parser.getLocalName().equals(xmlElem)) {
                        break;
                    }
                }
                if (event == XMLStreamConstants.START_ELEMENT) {
                    final String s = parser.getLocalName();
                    final String n = parser.getElementText();
                    ArrayList<Configs> aList = configsData.getConfigsArr();
                    for (Configs configs : configsData.getConfigsArr()) {
                        if (configs.getKey().equals(s)) {
                            System.out.println(n + " - Configs " + configs.getActValueToString());
                            configs.setActValue(n);
                            System.out.println(n + " - Configs " + configs.getActValueToString());
                        }
                    }
                }
            }
            ret = true;

        } catch (final Exception ex) {
            Log.errorLog(302104541, ex);
        }

        return ret;
    }

    @Override
    public void close() throws Exception {
    }

}
