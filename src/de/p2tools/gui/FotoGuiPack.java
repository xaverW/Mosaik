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

import de.p2tools.controller.config.Config;
import de.p2tools.controller.config.Daten;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class FotoGuiPack {

    Daten daten;
    private final SplitPane splitPane = new SplitPane();
    private final HBox hBox = new HBox();
    static DoubleProperty doubleProperty;//sonst geht die Ref verloren
    static BooleanProperty boolDivOn;
    private final FotoListPane fotoListPane =new FotoListPane();
    private final FotoGuiController guiController;
    private boolean bound = false;

    public FotoGuiPack() {
        daten = Daten.getInstance();
        this.doubleProperty = Config.FILM_GUI_FILTER_DIVIDER.getDoubleProperty();
        this.boolDivOn = Config.FILM_GUI_FILTER_DIVIDER_ON.getBooleanProperty();
        guiController = new FotoGuiController();
    }

    public void closeSplit() {
        boolDivOn.setValue(!boolDivOn.get());
    }

    private void setSplit() {
        if (boolDivOn.getValue()) {
            splitPane.getItems().clear();
            splitPane.getItems().addAll(guiController,fotoListPane );
            bound = true;
            splitPane.getDividers().get(0).positionProperty().bindBidirectional(doubleProperty);
        } else {
            if (bound) {
                splitPane.getDividers().get(0).positionProperty().unbindBidirectional(doubleProperty);
            }
            splitPane.getItems().clear();
            splitPane.getItems().addAll(guiController);
        }
    }

    public AnchorPane pack() {

        // Menü
        final MenuController menuController = new MenuController(MenuController.StartupMode.Film);
        menuController.setId("film-menu-pane");

        // Gui
        daten.fotoGuiController = guiController;

        splitPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        SplitPane.setResizableWithParent(guiController, Boolean.FALSE);

        hBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(splitPane, Priority.ALWAYS);
        hBox.getChildren().addAll(splitPane, menuController);
        AnchorPane.setTopAnchor(hBox, 0.0);
        AnchorPane.setLeftAnchor(hBox, 0.0);
        AnchorPane.setBottomAnchor(hBox, 0.0);
        AnchorPane.setRightAnchor(hBox, 0.0);
        boolDivOn.addListener((observable, oldValue, newValue) -> setSplit());
        setSplit();
        return new AnchorPane(hBox);
    }

}
