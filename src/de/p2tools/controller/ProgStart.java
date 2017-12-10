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

import de.p2tools.controller.config.ProgConfig;
import de.p2tools.controller.config.ProgConst;
import de.p2tools.controller.config.ProgData;
import de.p2tools.controller.config.ProgInfos;
import de.p2tools.gui.dialog.MTAlert;
import de.p2tools.mLib.MLInit;
import de.p2tools.mLib.configFile.ConfFile;
import de.p2tools.mLib.tools.Log;
import de.p2tools.mLib.tools.MLAlert;
import de.p2tools.mLib.tools.SysMsg;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import static de.p2tools.mLib.tools.Log.LILNE;

public class ProgStart {
    ProgData progData;

    public ProgStart(ProgData progData) {
        this.progData = progData;
    }

    // #########################################################
    // Filmliste beim Programmstart!! laden
    // #########################################################
    public void loadDataProgStart() {
    }

    public static void startMeldungen() {
        Log.versionMsg(ProgConst.PROGRAMMNAME);
        SysMsg.sysMsg("Programmpfad: " + ProgInfos.getPathJar());
        SysMsg.sysMsg("Verzeichnis Einstellungen: " + ProgInfos.getSettingsDirectory_String());
        SysMsg.sysMsg("");
        SysMsg.sysMsg(LILNE);
        SysMsg.sysMsg("");
        SysMsg.sysMsg("");
    }


    /**
     * ProgConfig beim  Programmstart laden
     *
     * @return
     */
    public boolean allesLaden() {
        if (!load()) {
            SysMsg.sysMsg("Weder Konfig noch Backup konnte geladen werden!");
            // teils geladene Reste entfernen
            clearKonfig();
            return false;
        }
        SysMsg.sysMsg("Konfig wurde gelesen!");
        MLInit.initLib(ProgData.debug, ProgConst.PROGRAMMNAME, ProgInfos.getUserAgent());
        return true;
    }

    private void clearKonfig() {
        ProgData progData = ProgData.getInstance();
    }

    private boolean load() {
        ProgData progData = ProgData.getInstance();

        ConfFile confFile = new ConfFile("/tmp/usb/test");
        confFile.readConfigFile(
                new ArrayList<>(Arrays.asList(progData.thumbCollectionList, progData.thumbCollectionList)),
                new ArrayList<>(Arrays.asList(ProgConfig.getConfigsDate(), progData.mosaikData, progData.wallpaperData)));

        boolean ret = false;
        final Path xmlFilePath = new ProgInfos().getXmlFilePath();

        try (IoXmlLesen reader = new IoXmlLesen(progData)) {
            if (Files.exists(xmlFilePath)) {
                if (reader.readConfiguration(xmlFilePath)) {
                    return true;
                } else {
                    // dann hat das Laden nicht geklappt
                    SysMsg.sysMsg("Konfig konnte nicht gelesen werden!");
                }
            } else {
                // dann hat das Laden nicht geklappt
                SysMsg.sysMsg("Konfig existiert nicht!");
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }

        // versuchen das Backup zu laden
        if (loadBackup()) {
            ret = true;
        }

        return ret;
    }

    private boolean loadBackup() {
        ProgData progData = ProgData.getInstance();
        boolean ret = false;
        final ArrayList<Path> path = new ArrayList<>();
        new ProgInfos().getMTPlayerXmlCopyFilePath(path);
        if (path.isEmpty()) {
            SysMsg.sysMsg("Es gibt kein Backup");
            return false;
        }

        // dann gibts ein Backup
        SysMsg.sysMsg("Es gibt ein Backup");


        if (MLAlert.BUTTON.YES != new MTAlert().showAlert_yes_no("Gesicherte Einstellungen laden?",
                "Die Einstellungen sind beschädigt\n" +
                        "und können nicht geladen werden.",
                "Soll versucht werden, mit gesicherten\n"
                        + "Einstellungen zu starten?\n\n"
                        + "(ansonsten startet das Programm mit\n"
                        + "Standardeinstellungen)")) {

            SysMsg.sysMsg("User will kein Backup laden.");
            return false;
        }

        for (final Path p : path) {
            // teils geladene Reste entfernen
            clearKonfig();
            SysMsg.sysMsg(new String[]{"Versuch Backup zu laden:", p.toString()});
            try (IoXmlLesen reader = new IoXmlLesen(progData)) {
                if (reader.readConfiguration(p)) {
                    SysMsg.sysMsg(new String[]{"Backup hat geklappt:", p.toString()});
                    ret = true;
                    break;
                }
            } catch (final Exception ex) {
                ex.printStackTrace();
            }

        }
        return ret;
    }
}
