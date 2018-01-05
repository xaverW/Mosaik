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
import de.p2tools.controller.config.ProgData;
import de.p2tools.controller.config.ProgInfos;
import de.p2tools.mLib.configFile.ConfFile;

import java.nio.file.Path;

public class ProgSave {
    final ProgData progData;
    private boolean alreadyMadeBackup = false;
    private boolean open = true;

    public ProgSave() {
        progData = ProgData.getInstance();
    }


    public void save() {
        final Path xmlFilePath = new ProgInfos().getXmlFilePath();
        ConfFile confFile = new ConfFile(xmlFilePath);
        confFile.addConfigs(ProgConfig.getConfigsDate());
        confFile.addConfigs(progData.mosaikData);
        confFile.addConfigs(progData.wallpaperData);
        confFile.addConfigs(progData.thumbCollectionList);
        confFile.addConfigs(progData.projectDataList);
        confFile.writeConfigFile();
    }

//    public void allesSpeichern() {
//        konfigCopy();
//
//        try (IoXmlSchreiben writer = new IoXmlSchreiben(progData)) {
//            writer.datenSchreiben();
//        } catch (final Exception ex) {
//            ex.printStackTrace();
//        }
//
//        if (ProgData.reset) {
//            // das Programm soll beim nächsten Start mit den Standardeinstellungen gestartet werden
//            // dazu wird den Ordner mit den Einstellungen umbenannt
//            String dir1 = ProgInfos.getSettingsDirectory_String();
//            if (dir1.endsWith(File.separator)) {
//                dir1 = dir1.substring(0, dir1.length() - 1);
//            }
//
//            try {
//                final Path path1 = Paths.get(dir1);
//                final String dir2 = dir1 + "--" + FastDateFormat.getInstance("yyyy.MM.dd__HH.mm.ss").format(new Date());
//
//                Files.move(path1, Paths.get(dir2), StandardCopyOption.REPLACE_EXISTING);
//                Files.deleteIfExists(path1);
//            } catch (final IOException e) {
//                SysMsg.sysMsg("Die Einstellungen konnten nicht zurückgesetzt werden.");
//                Platform.runLater(() -> {
//                    new MTAlert().showErrorAlert("Fehler", "Einstellungen zurückgesetzen",
//                            "Die Einstellungen konnten nicht zurückgesetzt werden.\n\n"
//                                    + "Sie müssen jetzt das Programm beenden, dann den Ordner:\n\n"
//                                    + ProgInfos.getSettingsDirectory_String()
//                                    + "\n\n"
//                                    + "von Hand löschen und das Programm wieder starten.");
//                    open = false;
//                });
//                while (open) {
//                    try {
//                        wait(100);
//                    } catch (final Exception ignored) {
//                    }
//                }
//                Log.errorLog(465690123, e);
//            }
//        }
//    }

}
