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
import de.p2tools.controller.config.ProgData;
import de.p2tools.controller.data.createMosaik.CreateMosaik;
import de.p2tools.controller.data.thumb.Thumb;
import de.p2tools.controller.data.thumb.ThumbCollection;
import de.p2tools.controller.data.thumb.ThumbCollectionXml;
import de.p2tools.mLib.tools.Duration;
import de.p2tools.mLib.tools.Log;
import de.p2tools.mLib.tools.MLConfigs;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class IoXmlLesen implements AutoCloseable {

    private XMLInputFactory inFactory = null;
    private ProgData progData = null;

    public IoXmlLesen(ProgData progData) {
        this.progData = progData;

        inFactory = XMLInputFactory.newInstance();
        inFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
    }

    public boolean readConfiguration(Path xmlFilePath) {
        Duration.counterStart("Konfig lesen");
        boolean ret = false;

        if (Files.exists(xmlFilePath)) {
            XMLStreamReader parser = null;
            try (InputStream is = Files.newInputStream(xmlFilePath);
                 InputStreamReader in = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                parser = inFactory.createXMLStreamReader(in);
                while (parser.hasNext()) {
                    final int event = parser.next();
                    if (event == XMLStreamConstants.START_ELEMENT) {
                        switch (parser.getLocalName()) {
                            case Config.SYSTEM:
                                // System
                                getConfig(parser, Config.SYSTEM);
                                break;
                            case CreateMosaik.TAG:
                                if (get(parser, CreateMosaik.TAG, CreateMosaik.XML_NAMES, progData.createMosaik.arr)) {
                                    progData.createMosaik.setPropsFromXml();
                                }
                                break;
                            case ThumbCollection.TAG:
                                getThumbCollection(parser);
                                break;
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

            Config.loadSystemParameter();

        }

        Duration.counterStop("Konfig lesen");
        return ret;
    }


    private boolean get(XMLStreamReader parser, String xmlElem, String[] xmlNames, String[] strRet) {
        boolean ret = true;
        final int maxElem = strRet.length;
        for (int i = 0; i < maxElem; ++i) {
            if (strRet[i] == null) {
                // damit Vorgaben nicht verschwinden!
                strRet[i] = "";
            }
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
                    for (int i = 0; i < maxElem; ++i) {
                        if (parser.getLocalName().equals(xmlNames[i])) {
                            strRet[i] = parser.getElementText();
                            break;
                        }
                    }
                }
            }
        } catch (final Exception ex) {
            ret = false;
            Log.errorLog(739530149, ex);
        }
        return ret;
    }

    private boolean getThumbCollection(XMLStreamReader parser) {
        final ThumbCollection thumbCollection = new ThumbCollection();

        boolean ret = true;
        final int maxElem = thumbCollection.arr.length;
        for (int i = 0; i < maxElem; ++i) {
            if (thumbCollection.arr[i] == null) {
                // damit Vorgaben nicht verschwinden!
                thumbCollection.arr[i] = "";
            }
        }
        try {
            while (parser.hasNext()) {
                final int event = parser.next();

                if (event == XMLStreamConstants.START_ELEMENT && parser.getLocalName() == Thumb.TAG) {
                    final Thumb thumb = new Thumb();
                    if (get(parser, Thumb.TAG, Thumb.XML_NAMES, thumb.arr)) {
                        thumb.setPropsFromXml();
                        thumbCollection.getThumbList().add(thumb);
                    }
                    continue;
                }

                if (event == XMLStreamConstants.END_ELEMENT) {
                    if (parser.getLocalName().equals(ThumbCollectionXml.TAG)) {
                        break;
                    }
                }

                if (event == XMLStreamConstants.START_ELEMENT) {
                    for (int i = 0; i < maxElem; ++i) {
                        if (parser.getLocalName().equals(ThumbCollectionXml.XML_NAMES[i])) {
                            thumbCollection.arr[i] = parser.getElementText();
                            break;
                        }
                    }
                }
            }

            thumbCollection.setPropsFromXml();
            progData.thumbCollectionList.add(thumbCollection);

        } catch (final Exception ex) {
            ret = false;
            Log.errorLog(739530149, ex);
        }
        return ret;
    }

    private boolean getConfig(XMLStreamReader parser, String xmlElem) {
        boolean ret = true;
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
                    MLConfigs mlConfigs = Config.get(s);
                    if (mlConfigs != null) {
                        mlConfigs.setValue(n);
                    }
                }
            }
        } catch (final Exception ex) {
            ret = false;
            Log.errorLog(945120369, ex);
        }
        return ret;
    }

    @Override
    public void close() throws Exception {

    }

}
