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

import de.mtplayer.controller.config.Daten;
import de.mtplayer.controller.data.SetData;
import de.mtplayer.controller.data.download.Download;
import de.mtplayer.gui.dialog.DownloadAddDialogController;
import de.mtplayer.gui.dialog.MTAlert;
import de.mtplayer.gui.dialog.NoSetDialogController;

import java.util.ArrayList;


public class FilmSave {


    public FilmSave() {
    }

    public void saveFilm(Film film, SetData pSet) {
        ArrayList<Film> list = new ArrayList<>();
        list.add(film);
        saveFilm(list, pSet);
    }

    public void saveFilm(ArrayList<Film> liste, SetData pSet) {
        if (liste.isEmpty()) {
            return;
        }

        final SetData psetData = pSet;
        Daten daten = Daten.getInstance();
        ArrayList<Film> filmsAddDownloadList = new ArrayList<>();

        if (Daten.setList.getListeSpeichern().isEmpty()) {
            new NoSetDialogController(daten, NoSetDialogController.TEXT.SAVE);
            return;
        }

        String aufloesung = "";
        if (daten.storedFilter.getSelectedFilter().isOnlyHd()) {
            aufloesung = Film.AUFLOESUNG_HD;
        }

        for (final Film datenFilm : liste) {
            // erst mal schauen obs den schon gibt
            Download download = daten.downloadList.getDownloadUrlFilm(datenFilm.arr[Film.FILM_URL]);
            if (download == null) {
                filmsAddDownloadList.add(datenFilm);
            } else {
                // dann ist der Film schon in der Downloadliste

                //todo wenn nur einer in der Liste macht "no" keinen Sinn
                MTAlert.BUTTON antwort = new MTAlert().showAlert_yes_no_cancel("Anlegen?", "Nochmal anlegen?",
                        "Download für den Film existiert bereits.\n" + "Nochmal anlegen?");
                switch (antwort) {
                    case CANCEL:
                        // alles Abbrechen
                        return;
                    case NO:
                        continue;
                    case YES:
                        filmsAddDownloadList.add(datenFilm);
                        break;
                }
            }
        }
        if (!filmsAddDownloadList.isEmpty()) {
            new DownloadAddDialogController(daten, filmsAddDownloadList, psetData, aufloesung);
        }
    }


}
