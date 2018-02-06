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

package de.p2tools.gui;

import de.p2tools.controller.config.ProgData;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

public class GuiThumb extends AnchorPane {

    private final ProgData progData;

    public GuiThumb() {
        progData = ProgData.getInstance();
        initCont();
    }

    public void isShown() {
        progData.guiThumbController.isShown();
        progData.guiThumbChangeController.isShown();
        reload();
    }


    private void initCont() {

        TabPane tabPane = new TabPane();
        Tab tab = new Tab("Miniaturbilder");
        tab.setClosable(false);
        tab.setContent(progData.guiThumbController);
        tabPane.getTabs().add(tab);

        tab = new Tab("Miniaturbilder bearbeiten");
        tab.setClosable(false);
        tab.setContent(progData.guiThumbChangeController);
        tabPane.getTabs().add(tab);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPadding(new Insets(10));
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tabPane);

        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setTopAnchor(scrollPane, 0.0);

        this.getChildren().add(scrollPane);
    }

    private void reload() {
        if (progData.selectedProjectData == null) {
            return;
        }

        if (!progData.selectedProjectData.getThumbCollection().getThumbList().isEmpty()) {
            return;
        }

        String thumbDir = progData.selectedProjectData.getThumbDirString();
        if (thumbDir.isEmpty()) {
            return;
        }

        progData.worker.readThumbList(progData.selectedProjectData.getThumbCollection(), thumbDir);
    }
}
