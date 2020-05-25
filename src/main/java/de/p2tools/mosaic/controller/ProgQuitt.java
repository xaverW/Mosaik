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

package de.p2tools.mosaic.controller;

import de.p2tools.mosaic.controller.config.ProgConfig;
import de.p2tools.mosaic.controller.config.ProgData;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.tools.log.LogMessage;
import javafx.application.Platform;

public class ProgQuitt {
    final ProgData progData;

    public ProgQuitt() {
        progData = ProgData.getInstance();
    }


    private void writeWindowSizes() {
        // Hauptfenster
        PGuiSize.getSizeScene(ProgConfig.SYSTEM_GROESSE_GUI, progData.primaryStage);
    }

    private void writeTabSettings() {
        // Tabelleneinstellungen merken
        progData.guiThumbChangeController.saveTable();
    }

    /**
     * Quit the application
     */
    public void quitt() {
        if (quitt_()) {

            // dann jetzt beenden -> Tschüss
            Platform.runLater(() -> {
                Platform.exit();
                System.exit(0);
            });

        }
    }

    private boolean quitt_() {
        boolean ret = false;

        if (PAlert.BUTTON.YES == PAlert.showAlert_yes_no("Frage", "Programm beenden", "Soll das Programm beendet werden?")) {
            ret = true;

            writeTabSettings();
            writeWindowSizes();
            new ProgSave().save();

            LogMessage.endMsg();
        }


        return ret;
    }

}
