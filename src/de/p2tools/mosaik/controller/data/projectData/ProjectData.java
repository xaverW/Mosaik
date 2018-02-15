/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * http://zdfmediathk.sourceforge.net/
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


package de.p2tools.mosaik.controller.data.projectData;

import de.p2tools.mosaik.controller.config.ProgConst;
import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.p2Lib.tools.PAlert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProjectData extends ProjectDataProps {
    private int nr = 1;

    public ProjectData() {
        String[] nArr = getNextProjectNameDirString();
        setName(nArr[0]);
        setDestDir(nArr[1]);
    }

    public String[] getNextProjectNameDirString() {
        Path baseDirectoryPath;
        baseDirectoryPath = Paths.get(System.getProperty("user.home"), ProgConst.DIR_STANDARD_PROJECT);

        String name = ProgConst.DIR_STANDARD_PROJECT;
        while (Files.exists(baseDirectoryPath) ||
                ProgData.getInstance().projectDataList.containProjectName(name) != null) {

            name = ProgConst.DIR_STANDARD_PROJECT + "_" + nr++;
            baseDirectoryPath = Paths.get(System.getProperty("user.home"), name);
        }

        return new String[]{name, baseDirectoryPath.toString()};
    }

    public String getThumbDirString() {
        final Path baseDirectoryPath;
        if (ProgData.getInstance().selectedProjectData == null || ProgData.getInstance().selectedProjectData.getDestDir().isEmpty()) {
            new PAlert().showErrorAlert("Verzeichnis für die Vorschaubilder", "Für das Projekt wurde " +
                    "kein Verzeichnis angegeben");
            return "";
        } else {
            baseDirectoryPath = Paths.get(ProgData.getInstance().selectedProjectData.getDestDir(), ProgConst.VERZEICHNIS_THUMBS);
        }

        if (Files.notExists(baseDirectoryPath)) {
            try {
                Files.createDirectories(baseDirectoryPath);
            } catch (final IOException ioException) {
                new PAlert().showErrorAlert("Verzeichnis für die Vorschaubilder", "Das Verzeinis der Vorschaubilder " +
                        "kann nicht angelegt werden: \n" +
                        baseDirectoryPath.toString());
            }
        }
        return baseDirectoryPath.toString();
    }

}
