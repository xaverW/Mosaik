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

package de.mtplayer.controller.data.film;

import de.mtplayer.mLib.tools.Datum;
import de.mtplayer.mLib.tools.DatumFilm;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class FilmProps extends FilmXml {

    public int nr;
    public FilmSize filmSize = new FilmSize(); // Dateigröße in MByte
    public DatumFilm datumFilm = new DatumFilm(0);
    public long dauerL = 0; // Sekunden
    public int filmtime = 0; // Zeit -> Minuten ab 0:00 Uhr
    // todo?? die Property brauchts nicht alle aber dann müssen die checkboxen in der Tabelle
    // ersetzt werden
    private boolean hd = false; // Film hat HD-URL
    private boolean small = false; // Film hat "small"-URL
    private boolean ut = false;
    private final BooleanProperty newFilm = new SimpleBooleanProperty(false);
    private final BooleanProperty shown = new SimpleBooleanProperty(false);
    private boolean doubleUrl = false;
    private final BooleanProperty actHist = new SimpleBooleanProperty(false);

    public int getFilmtime() {
        return filmtime;
    }

    public void setFilmtime(int filmtime) {
        this.filmtime = filmtime;
    }

    public boolean getActHist() {
        return actHist.get();
    }

    public BooleanProperty actHistProperty() {
        return actHist;
    }

    public void setActHist(boolean actHist) {
        this.actHist.set(actHist);
    }


    public boolean isDoubleUrl() {
        return doubleUrl;
    }

//    public BooleanProperty doubleUrlProperty() {
//        return doubleUrl;
//    }

    public void setDoubleUrl(boolean doubleUrl) {
        this.doubleUrl = doubleUrl;
    }

    public boolean isShown() {
        return shown.get();
    }

    public BooleanProperty shownProperty() {
        return shown;
    }

    public void setShown(boolean shown) {
        if (!arr[FilmXml.FILM_THEMA].equals(FilmTools.THEMA_LIVE)) {
            this.shown.set(shown);
        }
    }

    public int getNr() {
        return nr;
    }

    public String getSender() {
        return arr[FILM_SENDER];
    }

    public String getThema() {
        return arr[FILM_THEMA];
    }

    public String getTitel() {
        return arr[FILM_TITEL];
    }

    public Datum getDate() {
        return datumFilm;
    }

    public String getTime() {
        return arr[FILM_ZEIT];
    }

    public String getDauer() {
        return arr[FILM_DAUER];
    }

    public FilmSize getFilmSize() {
        return filmSize;
    }

    public boolean isHd() {
        return hd;
    }

    public void setHd(boolean b) {
        hd = b;
    }

//    public BooleanProperty hdProperty() {
//        return hd;
//    }

    public boolean isSmall() {
        return small;
    }

    public void setSmall(boolean b) {
        small = b;
    }

    public boolean isUt() {
        return ut;
    }

    public void setUt(boolean b) {
        ut = b;
    }

//    public BooleanProperty utProperty() {
//        return ut;
//    }

    public String getDescription() {
        return arr[FILM_BESCHREIBUNG];
    }

    public String getGeo() {
        return arr[FILM_GEO];
    }

    public String getUrl() {
        return arr[FILM_URL];
    }

    public String getWebsite() {
        return arr[FILM_WEBSEITE];
    }

    public String getAboName() {
        return arr[FILM_ABO_NAME];
    }

    public String getUrlSubtitle() {
        return arr[FILM_URL_SUBTITLE];
    }

    public String getUrlRtmp() {
        return arr[FILM_URL_RTMP];
    }

    public String getUrlAuth() {
        return arr[FILM_URL_AUTH];
    }

//    das macht keinen Sinn da, da die URLs erst "zusammengebaut" werden müssen
//    public String getUrlKlein() {
//        return arr[FILM_URL_KLEIN];
//    }
//
//    public String getUrlRtmpKlein() {
//        return arr[FILM_URL_RTMP_KLEIN];
//    }
//
//    public String getUrlHd() {
//        return arr[FILM_URL_HD];
//    }
//
//    public String getUrlRtmpHd() {
//        return arr[FILM_URL_RTMP_HD];
//    }

    public String getUrlHistory() {
        if (arr[FilmXml.FILM_URL_HISTORY].isEmpty()) {
            return arr[FilmXml.FILM_URL];
        } else {
            return arr[FilmXml.FILM_URL_HISTORY];
        }
    }

    public boolean isNewFilm() {
        return newFilm.get();
    }

    public void setNewFilm(final boolean newFilm) {
        this.newFilm.setValue(newFilm);
    }

    public BooleanProperty newFilmProperty() {
        return newFilm;
    }

    public String getFilmDateLong() {
        // beschleunigt etwas das Laden der Filmliste
        return arr[FILM_DATUM_LONG];
    }
}
