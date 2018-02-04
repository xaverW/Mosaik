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
import javafx.scene.control.Accordion;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

public class GuiThumb extends AnchorPane {

    private final Accordion accordion = new Accordion();
    private final ProgData progData;

    public GuiThumb() {
        progData = ProgData.getInstance();
        getStyleClass().add("layoutBackground");

        initCont();
    }

    public void isShown() {
        progData.guiThumbController.isShown();
        progData.guiChangeThumbController.isShown();
    }


    private void initCont() {

        TitledPane tpThumb = new TitledPane("Miniaturbilder", progData.guiThumbController);
        tpThumb.getStyleClass().add("contPaneAccordion");

        TitledPane tpChangeThumb = new TitledPane("Miniaturbilder bearbeiten", progData.guiChangeThumbController);
        tpChangeThumb.getStyleClass().add("contPaneAccordion");

        accordion.getPanes().addAll(tpThumb, tpChangeThumb);
        accordion.setExpandedPane(tpThumb);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(accordion);

        AnchorPane.setLeftAnchor(scrollPane, 10.0);
        AnchorPane.setBottomAnchor(scrollPane, 10.0);
        AnchorPane.setRightAnchor(scrollPane, 10.0);
        AnchorPane.setTopAnchor(scrollPane, 10.0);

        this.getChildren().add(scrollPane);
    }
}
