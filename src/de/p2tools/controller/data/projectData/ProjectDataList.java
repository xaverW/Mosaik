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

package de.p2tools.controller.data.projectData;

import de.p2tools.p2Lib.configFile.ConfigsList;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;

public class ProjectDataList extends SimpleListProperty<ProjectData> implements ConfigsList<ProjectData> {

    public static final String TAG = "ProjectDataList";
    private BooleanProperty listChanged = new SimpleBooleanProperty(true);


    public ProjectDataList() {
        super(FXCollections.observableArrayList(callback ->
                new Observable[]{callback.nameProperty()}));
    }

    public void initList() {
        if (this.isEmpty()) {
            this.add(new ProjectData());
        }
    }

    public String getTag() {
        return TAG;
    }

    public String getComment() {
        return "list of the project data";
    }


    public ProjectData getNewItem() {
        return new ProjectData();
    }

    public void addNewItem(Object obj) {
        if (obj.getClass().equals(ProjectData.class)) {
            add((ProjectData) obj);
        }
    }

    public boolean isListChanged() {
        return listChanged.get();
    }

    public BooleanProperty listChangedProperty() {
        return listChanged;
    }

    public void setListChanged() {
        this.listChanged.set(!listChanged.get());
    }

    public ProjectData containProjectName(String name) {
        return this.stream().filter(projectData -> projectData.getName().equals(name)).findFirst().orElse(null);
    }

    public synchronized boolean add(ProjectData projectData) {
        boolean ret = super.add(projectData);
        sort();
        setListChanged();
        return ret;
    }


    public synchronized boolean addAll(ArrayList<ProjectData> projectData) {
        boolean ret = super.addAll(projectData);
        sort();
        setListChanged();
        return ret;
    }

    public ProjectData getProjectDate(String name) {
        ProjectData projectData;

        projectData = this.stream().filter(data -> data.getName() == name).findFirst().orElse(null);

        if (projectData == null && !this.isEmpty()) {
            projectData = this.get(0);
        }
        return projectData;
    }

    public ObservableList<String> getListNames() {
        ObservableList<String> list = FXCollections.observableArrayList();
        this.stream().forEach(projectData -> list.add(projectData.getName()));
        return list;
    }

    public void sort() {
        Collections.sort(this);
    }

}
