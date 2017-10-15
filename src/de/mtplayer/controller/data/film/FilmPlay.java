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
import de.mtplayer.gui.dialog.NoSetDialogController;

public class FilmPlay {

    public static void playFilm(Film film, SetData psetData) {
        SetData pset;
        String auflösung = "";

        if (psetData != null) {
            pset = psetData;
        } else {
            pset = Daten.setList.getPsetAbspielen();
        }

        if (pset == null) {
            new NoSetDialogController(Daten.getInstance(), NoSetDialogController.TEXT.PLAY);
            return;
        }

        if (Daten.getInstance().storedFilter.getSelectedFilter().isOnlyHd()) {
            auflösung = Film.AUFLOESUNG_HD;
        }

        // und starten
        Daten.getInstance().starterClass.urlMitProgrammStarten(film, pset, auflösung);
    }


}
