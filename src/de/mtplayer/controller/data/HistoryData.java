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

import de.mtplayer.mLib.tools.DatumFilm;
import de.mtplayer.mLib.tools.Functions;
import de.mtplayer.mLib.tools.GermanStringSorter;
import de.mtplayer.mLib.tools.Log;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.lang3.time.FastDateFormat;

public class HistoryData implements Comparable<HistoryData> {

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getTheme() {
        return theme.get();
    }

    public SimpleStringProperty themeProperty() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme.set(theme);
    }

    public String getUrl() {
        return url.get();
    }

    public void setUrl(String fName) {
        url.set(fName);
    }

    public SimpleStringProperty urlProperty() {
        return url;
    }

    public DatumFilm getDate() {
        return date.get();
    }

    public void setDate(DatumFilm fName) {
        date.set(fName);
    }

    public ObjectProperty<DatumFilm> dateProperty() {
        return date;
    }

    public final SimpleStringProperty title = new SimpleStringProperty(""); //todo müssen das Property sein??
    public final SimpleStringProperty theme = new SimpleStringProperty("");
    public final SimpleStringProperty url = new SimpleStringProperty("");
    public final ObjectProperty<DatumFilm> date = new SimpleObjectProperty<>();

    public static final String[] titleNames = {"Datum", "Thema", "Titel", "Url"};
    public static final int HISTORY_DATE = 0;
    public static final int HISTORY_THEME = 1;
    public static final int HISTORY_TITLE = 2;

    public static final int HISTORY_URL = 3;
    private static final GermanStringSorter sorter = GermanStringSorter.getInstance();
    private final static String TRENNER = "  |###|  ";
    private final static String PAUSE = " |#| ";

    private static final FastDateFormat sdf_datum = FastDateFormat.getInstance("dd.MM.yyyy");


    public HistoryData(String date, String thema, String title, String url) {
        setTitle(title);
        setTheme(thema);
        setUrl(url);
        try {
            setDate(new DatumFilm(sdf_datum.parse(date).getTime()));
        } catch (final Exception ignore) {
            setDate(new DatumFilm(0));
        }
    }


    public static String getLine(String date, String thema, String title, String url) {
        return date + PAUSE
                + Functions.textLaenge(25, putzen(thema), false /* mitte */, false /*addVorne*/) + PAUSE
                + Functions.textLaenge(40, putzen(title), false /* mitte */, false /*addVorne*/) + TRENNER
                + url + '\n';
    }

    public String getLine() {
        return getDate() + PAUSE
                + Functions.textLaenge(25, putzen(getTheme()), false /* mitte */, false /*addVorne*/) + PAUSE
                + Functions.textLaenge(40, putzen(getTitle()), false /* mitte */, false /*addVorne*/) + TRENNER
                + getUrl() + '\n';
    }

    public static HistoryData getUrlAusZeile(String zeile) {
        // 29.05.2014 |#| Abendschau                |#| Patenkind trifft Groß                     |###|  http://cdn-storage.br.de/iLCpbHJGNLT6NK9HsLo6s61luK4C_2rc5U1S/_-OS/5-8y9-NP/5bb33365-038d-46f7-914b-eb83fab91448_E.mp4
        String url = "", thema = "", titel = "", datum = "";
        int a1;
        try {
            if (zeile.contains(TRENNER)) {
                //neues Logfile-Format
                a1 = zeile.lastIndexOf(TRENNER);
                a1 += TRENNER.length();
                url = zeile.substring(a1).trim();
                // titel
                titel = zeile.substring(zeile.lastIndexOf(PAUSE) + PAUSE.length(), zeile.lastIndexOf(TRENNER)).trim();
                datum = zeile.substring(0, zeile.indexOf(PAUSE)).trim();
                thema = zeile.substring(zeile.indexOf(PAUSE) + PAUSE.length(), zeile.lastIndexOf(PAUSE)).trim();
            } else {
                url = zeile;
            }
        } catch (final Exception ex) {
            Log.errorLog(398853224, ex);
        }
        return new HistoryData(datum, thema, titel, url);
    }

    public String getString() {
        return Functions.textLaenge(40, getTitle(), false /* mitte */, false /*addVorne*/)
                + "    " + Functions.textLaenge(25, getUrl(), false /* mitte */, false /*addVorne*/)
                + "    " + (getDate().toString().isEmpty() ? "          " : getDate())
                + "    " + getUrl();
    }


    @Override
    public int compareTo(HistoryData arg0) {
        return sorter.compare(getTitle(), arg0.getTitle());
    }

    private static String putzen(String s) {
        s = s.replace("\n", "");
        s = s.replace("|", "");
        s = s.replace(TRENNER, "");
        return s;
    }
}
