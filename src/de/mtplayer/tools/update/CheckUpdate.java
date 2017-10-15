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

package de.mtplayer.tools.update;

import de.mtplayer.controller.config.Config;
import de.mtplayer.controller.config.Daten;
import de.mtplayer.gui.tools.Listener;
import de.mtplayer.mLib.tools.Functions;
import de.mtplayer.mLib.tools.Log;
import de.mtplayer.mLib.tools.StringFormatters;

import java.util.Date;

import static java.lang.Thread.sleep;

public class CheckUpdate {

    private static boolean updateCheckAlreadyPerformed = false;
    private final Daten daten;

    public CheckUpdate(Daten daten) {
        this.daten = daten;
    }

    public void checkProgUpdate() {
        new Thread(this::prog).start();
    }

    private synchronized void prog() {
        checkForPsetUpdates();


        try {
            if (!Boolean.parseBoolean(Config.SYSTEM_UPDATE_SEARCH.get())) {
                // will der User nicht
                return;
            }

            if (Config.SYSTEM_BUILD_NR.get().equals(Functions.getProgVersion())
                    && Config.SYSTEM_UPDATE_DATE.get().equals(StringFormatters.FORMATTER_yyyyMMdd.format(new Date()))) {
                // keine neue Version und heute schon gemacht
                return;
            }

            // damit geänderte Sets gleich gemeldet werden und nicht erst morgen
            final ProgrammUpdateSuchen pgrUpdate = new ProgrammUpdateSuchen();
            if (pgrUpdate.checkVersion(false /* bei aktuell anzeigen */, true /* Hinweis */,
                    false /* hinweiseAlleAnzeigen */)) {
                Listener.notify(Listener.EREIGNIS_GUI_UPDATE_VERFUEGBAR, CheckUpdate.class.getSimpleName());
            } else {
                Listener.notify(Listener.EREIGNIS_GUI_PROGRAMM_AKTUELL, CheckUpdate.class.getSimpleName());
            }

            // ==============================================
            // Sets auf Update prüfen
            checkForPsetUpdates();

            try {
                sleep(10_000);
            } catch (final InterruptedException ignored) {
            }
            Listener.notify(Listener.EREIGNIS_GUI_ORG_TITEL, CheckUpdate.class.getSimpleName());

        } catch (final Exception ex) {
            Log.errorLog(794612801, ex);
        }
    }

    private void checkForPsetUpdates() {
    }
}
