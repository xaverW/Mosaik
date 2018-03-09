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

import de.p2tools.mosaik.controller.data.mosaikData.MosaikData;
import de.p2tools.mosaik.controller.data.mosaikData.WallpaperData;
import de.p2tools.mosaik.controller.data.thumb.ThumbCollection;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigPData;
import de.p2tools.p2Lib.configFile.config.ConfigStringProp;
import de.p2tools.p2Lib.configFile.pData.PDataVault;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Arrays;

public class ProjectDataProps extends PDataVault<ProjectData> {
    public static final String TAG = "ProjectData";

    private final StringProperty name = new SimpleStringProperty("Neues Mosaik");
    private final StringProperty destDir = new SimpleStringProperty(""); // project dir
    private final StringProperty srcPhoto = new SimpleStringProperty(""); // Quellbilder für das Mosaik

    private final MosaikData mosaikData = new MosaikData();
    private final WallpaperData wallpaperData = new WallpaperData();
    private final ThumbCollection thumbCollection = new ThumbCollection();

    public String getTag() {
        return TAG;
    }

    public ArrayList<Config> getConfigsArr() {
        return new ArrayList(Arrays.asList(
                new ConfigStringProp("name", "Neues Mosaik", name),
                new ConfigStringProp("dest-dir", "", destDir),
                new ConfigStringProp("src-photo", "", srcPhoto),
                new ConfigPData(mosaikData),
                new ConfigPData(wallpaperData),
                new ConfigPData(thumbCollection)));
    }

    public MosaikData getMosaikData() {
        return mosaikData;
    }

    public WallpaperData getWallpaperData() {
        return wallpaperData;
    }

    public ThumbCollection getThumbCollection() {
        return thumbCollection;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDestDir() {
        return destDir.get();
    }

    public StringProperty destDirProperty() {
        return destDir;
    }

    public void setDestDir(String destDir) {
        this.destDir.set(destDir);
    }

    public String getSrcPhoto() {
        return srcPhoto.get();
    }

    public StringProperty srcPhotoProperty() {
        return srcPhoto;
    }

    public void setSrcPhoto(String srcPhoto) {
        this.srcPhoto.set(srcPhoto);
    }

    @Override
    public int compareTo(ProjectData arg0) {
        return getName().compareTo(arg0.getName());
    }

}
