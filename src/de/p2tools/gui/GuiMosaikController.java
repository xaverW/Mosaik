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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

public class GuiMosaikController extends AnchorPane {

    private final ProgData progData;
    private final TabPane contPane = new TabPane();

    private final GuiMosaikPane guiMosaikPane = new GuiMosaikPane();
    private final GuiMosaikExtendedPane guiMosaikExtendedPane = new GuiMosaikExtendedPane();

    public GuiMosaikController() {
        progData = ProgData.getInstance();

        AnchorPane.setLeftAnchor(contPane, 0.0);
        AnchorPane.setBottomAnchor(contPane, 0.0);
        AnchorPane.setRightAnchor(contPane, 0.0);
        AnchorPane.setTopAnchor(contPane, 0.0);

        initCont();
        getChildren().addAll(contPane);
    }

    public void isShown() {
        if (progData.selectedProjectData == null) {
            contPane.setDisable(true);
            return;
        }

        contPane.setDisable(false);
        guiMosaikPane.isShown();
        guiMosaikExtendedPane.isShown();
    }


    private void initCont() {
        Tab tab = new Tab("Mosaik");
        tab.setClosable(false);
        tab.setContent(guiMosaikPane);
        contPane.getTabs().add(tab);

        tab = new Tab("erweiterte Einstellungen");
        tab.setClosable(false);
        tab.setContent(guiMosaikExtendedPane);
        contPane.getTabs().add(tab);
    }
}
