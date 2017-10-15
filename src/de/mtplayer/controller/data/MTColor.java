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


package de.mtplayer.controller.data;

import de.mtplayer.controller.config.Config;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class MTColor {

    // Tabelle Filme
    public static final MTC FILM_LIVESTREAM = new MTC(Config.FARBE__FILM_LIVESTREAM, Color.rgb(130, 0, 0), "Filme, Livestreams");
    public static final MTC FILM_HISTORY = new MTC(Config.FARBE__FILM_HISTORY, Color.rgb(223, 223, 223), "Filme, gesehen");
    public static final MTC FILM_NEU = new MTC(Config.FARBE__FILM_NEU, Color.rgb(0, 0, 240), "Filme, neue");
    public static final MTC FILM_GEOBLOCK = new MTC(Config.FARBE__FILM_GEOBLOCK_BACKGROUND, Color.rgb(255, 168, 0), "Film, geogeblockt");
    public static final MTC FILM_GEOBLOCK_BACKGROUND = new MTC(Config.FARBE__FILM_GEOBLOCK_BACKGROUND, Color.rgb(255, 254, 230), "Film, geogeblockt");
    public static final MTC FILM_GEOBLOCK_BACKGROUND_SEL = new MTC(Config.FARBE__FILM_GEOBLOCK_BACKGROUND_SEL, Color.rgb(255, 251, 179), "Film, geogeblockt, selektiert");

    // Tabelle Downloads
    public static final MTC DOWNLOAD_IST_ABO = new MTC(Config.FARBE__DOWNLOAD_IST_ABO, Color.rgb(138, 67, 0), "Download ist ein Abo");
    public static final MTC DOWNLOAD_IST_DIREKTER_DOWNLOAD = new MTC(Config.FARBE__DOWNLOAD_IST_DIREKTER_DOWNLOAD, Color.rgb(0, 72, 138), "Download ist ein direkter DownloadXml");
    public static final MTC DOWNLOAD_ANSEHEN = new MTC(Config.FARBE__DOWNLOAD_ANSEHEN, Color.rgb(0, 125, 0), "Download kann schon angesehen werden");
    // status Downloads
    public static final MTC DOWNLOAD_WAIT = new MTC(Config.FARBE__DOWNLOAD_WAIT, Color.rgb(239, 244, 255), "Download, noch nicht gestartet");
    public static final MTC DOWNLOAD_WAIT_SEL = new MTC(Config.FARBE__DOWNLOAD_WAIT_SEL, Color.rgb(199, 206, 222), "Download, noch nicht gestartet, selektiert");
    public static final MTC DOWNLOAD_RUN = new MTC(Config.FARBE__DOWNLOAD_RUN, Color.rgb(255, 245, 176), "Download, läuft");
    public static final MTC DOWNLOAD_RUN_SEL = new MTC(Config.FARBE__DOWNLOAD_RUN_SEL, Color.rgb(206, 178, 92), "Download, läuft, selektiert");
    public static final MTC DOWNLOAD_FERTIG = new MTC(Config.FARBE__DOWNLOAD_FERTIG, Color.rgb(206, 255, 202), "Download, fertig");
    public static final MTC DOWNLOAD_FERTIG_SEL = new MTC(Config.FARBE__DOWNLOAD_FERTIG_SEL, Color.rgb(115, 206, 92), "Download, fertig, selektiert");
    public static final MTC DOWNLOAD_FEHLER = new MTC(Config.FARBE__DOWNLOAD_FEHLER, Color.rgb(255, 233, 233), "Download, fehlerhaft");
    public static final MTC DOWNLOAD_FEHLER_SEL = new MTC(Config.FARBE__DOWNLOAD_FEHLER_SEL, Color.rgb(206, 92, 128), "Download, fehlerhaft, selektiert");

    // Tabelle Abos
    public static final MTC ABO_FEHLER = new MTC(Config.FARBE__ABO_FEHLER, Color.rgb(255, 233, 233), "Download, fehlerhaft");
    public static final MTC ABO_FEHLER_SEL = new MTC(Config.FARBE__ABO_FEHLER_SEL, Color.rgb(206, 92, 128), "Download, fehlerhaft, selektiert");
    public static final MTC ABO_AUSGESCHALTET = new MTC(Config.FARBE__ABO_AUSGESCHALTET, Color.rgb(225, 225, 225), "Abo, ausgeschaltet");
    public static final MTC ABO_AUSGESCHALTET_SEL = new MTC(Config.FARBE__ABO_AUSGESCHALTET_SEL, Color.rgb(190, 190, 190), "Abo, ausgeschaltet, selektiert");

    // Filter wenn RegEx
    public static final MTC FILTER_REGEX = new MTC(Config.FARBE__FILTER_REGEX, Color.rgb(153, 214, 255), "Filter ist RegEx");
    public static final MTC FILTER_REGEX_FEHLER = new MTC(Config.FARBE__FILTER_REGEX_FEHLER, Color.RED, "Filter ist Regex, fehlerhaft");

    // ProgrammGui
    public static final MTC BUTTON_SET_ABSPIELEN = new MTC(Config.FARBE__BUTTON_SET_ABSPIELEN, Color.rgb(205, 255, 191), "Einstellungen Sets, Button Abspielen");
    public static final MTC FILMLISTE_LADEN_AKTIV = new MTC(Config.FARBE__FILMLISTE_LADEN_AKTIV, Color.rgb(205, 255, 191), "Einstellungen Filmliste, Auto-Manuell");

    // DialogDownload
    public static final MTC DOWNLOAD_DATEINAME_EXISTIERT = new MTC(Config.FARBE__DOWNLOAD_DATEINAME_EXISTIERT, Color.rgb(190, 0, 0), "Download, Dateiname existiert schon");
    public static final MTC DOWNLOAD_DATEINAME_NEU = new MTC(Config.FARBE__DOWNLOAD_DATEINAME_NEU, Color.rgb(0, 140, 0), "Download, Dateiname ist neu");
    public static final MTC DOWNLOAD_DATEINAME_ALT = new MTC(Config.FARBE__DOWNLOAD_DATEINAME_ALT, Color.rgb(0, 0, 200), "Download, Dateiname ist der alte");

    public static final MTC DATEINAME_FEHLER = new MTC(Config.FARBE__DOWNLOAD_FEHLER, Color.rgb(255, 233, 233), "Download, fehlerhaft");

    private static ObservableList<MTC> colorList = FXCollections.observableArrayList();
    public static final int MVC_TEXT = 0;
    public static final int MVC_COLOR = 1;
    public static final int MVC_MAX = 2;

    public MTColor() {
        colorList.add(FILM_LIVESTREAM);
        colorList.add(FILM_HISTORY);
        colorList.add(FILM_NEU);
        colorList.add(FILM_GEOBLOCK_BACKGROUND);
        colorList.add(FILM_GEOBLOCK_BACKGROUND_SEL);
        colorList.add(DOWNLOAD_IST_ABO);
        colorList.add(DOWNLOAD_IST_DIREKTER_DOWNLOAD);
        colorList.add(DOWNLOAD_ANSEHEN);
        colorList.add(DOWNLOAD_WAIT);
        colorList.add(DOWNLOAD_WAIT_SEL);
        colorList.add(DOWNLOAD_RUN);
        colorList.add(DOWNLOAD_RUN_SEL);
        colorList.add(DOWNLOAD_FERTIG);
        colorList.add(DOWNLOAD_FERTIG_SEL);
        colorList.add(DOWNLOAD_FEHLER);
        colorList.add(DOWNLOAD_FEHLER_SEL);
        colorList.add(ABO_AUSGESCHALTET);
        colorList.add(ABO_AUSGESCHALTET_SEL);
        colorList.add(FILTER_REGEX);
        colorList.add(FILTER_REGEX_FEHLER);
        colorList.add(BUTTON_SET_ABSPIELEN);
        colorList.add(FILMLISTE_LADEN_AKTIV);
        colorList.add(DOWNLOAD_DATEINAME_EXISTIERT);
        colorList.add(DOWNLOAD_DATEINAME_NEU);
        colorList.add(DOWNLOAD_DATEINAME_ALT);
    }

    public static synchronized ObservableList<MTC> getColorList() {
        return colorList;
    }


    public final void load() {
        colorList.stream().filter(MTC -> !MTC.getMlConfigs().get().isEmpty()).forEach(MTC -> {
            try {
                MTC.setColorFromHex(MTC.getMlConfigs().get());
            } catch (final Exception ignored) {
                MTC.resetColor();
            }
        });
    }

    public final void save() {
        for (final MTC mtc : colorList) {
            mtc.getMlConfigs().setValue(String.valueOf(mtc.getColorToHex()));
        }
    }

    public void reset() {
        colorList.forEach(MTC::resetColor);
        save();
    }
}
