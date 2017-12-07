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
import javafx.collections.ObservableList;

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
    private final ArrayList<ConfigsData> configsDataArr;

    private final ArrayList<String> tagList;

    LoadConfigFile(String filePath, ArrayList<ObservableList<? extends ConfigsData>> configsListList, ArrayList<ConfigsData> configsDataArr) {
        this.filePath = filePath;
        this.configsDataArr = configsDataArr;
        inFactory = XMLInputFactory.newInstance();
        inFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);

        tagList = new ArrayList<>();
        for (ConfigsData configsData : configsDataArr) {
            tagList.add(configsData.getTagName());
        }
    }

    boolean readConfiguration() {
        Duration.counterStart("Konfig lesen");
        boolean ret = false;
        String xmlTag = "system";

        xmlFilePath = Paths.get(filePath);
        if (Files.exists(xmlFilePath)) {
            XMLStreamReader parser = null;
            try (InputStream is = Files.newInputStream(xmlFilePath);
                 InputStreamReader in = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                parser = inFactory.createXMLStreamReader(in);
                while (parser.hasNext()) {
                    final int event = parser.next();
                    if (event == XMLStreamConstants.START_ELEMENT) {
                        String xmlElem = parser.getLocalName();
                        if (tagList.contains(xmlElem)) {
                            getConfig(parser);
                        }
                    }
                }
                ret = true;
            } catch (final Exception ex) {
                ret = false;
                Log.errorLog(392840096, ex);
            } finally {
                try {
                    if (parser != null) {
                        parser.close();
                    }
                } catch (final Exception ignored) {
                }
            }


        }

        Duration.counterStop("Konfig lesen");
        return ret;
    }


    private boolean getConfig(XMLStreamReader parser) {
        boolean ret = false;
        String xmlElem = parser.getLocalName();
        ConfigsData configsData = null;
        Configs[] cArr;
        for (ConfigsData cd : configsDataArr) {
            if (cd.getTagName().equals(xmlElem)) {
                configsData = cd;
                break;
            }
        }
        if (configsData == null) {
            return false;
        }
        cArr = configsData.getConfigsArr();

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
                    for (Configs configs : configsData.getConfigsArr()) {
                        if (configs.getKey().equals(s)) {
                            configs.
                        }
                    }

                    Configs configs = new ConfigsString(s, n, n);
                    configsData.getConfigsArr()
                    configsArr.add(configs);
                }
            }
            configsData.setConfigsArr(configsArr.toArray(new Configs[]{}));

        } catch (final Exception ex) {
            configsData = null;
            Log.errorLog(945120367, ex);
        }

        return configsData;
    }

    @Override
    public void close() throws Exception {

    }

}
