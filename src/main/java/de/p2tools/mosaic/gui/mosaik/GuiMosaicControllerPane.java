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

package de.p2tools.mosaic.gui.mosaik;

import de.p2tools.mosaic.controller.config.ProgData;
import de.p2tools.mosaic.controller.data.mosaikData.MosaicData;
import de.p2tools.p2Lib.alert.PAlert;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GuiMosaicControllerPane extends AnchorPane {

    private final ProgData progData;
    private final VBox vBox = new VBox();
    private final TabPane tabPane = new TabPane();

    private final GuiMosaicPane guiMosaicPane = new GuiMosaicPane();
    private final GuiMosaicChangePane guiMosaicChangePane = new GuiMosaicChangePane();
    private final GuiChangeBorderPane guiChangeBorderPane = new GuiChangeBorderPane();
    private final Button btnCreate = new Button("Mosaik erstellen");

    MosaicData mosaicData = null;

    public GuiMosaicControllerPane() {
        progData = ProgData.getInstance();

        AnchorPane.setLeftAnchor(vBox, 0.0);
        AnchorPane.setBottomAnchor(vBox, 0.0);
        AnchorPane.setRightAnchor(vBox, 0.0);
        AnchorPane.setTopAnchor(vBox, 0.0);

        this.setStyle("-fx-background-color: -fx-background ;");
//        this.setStyle("-fx-border-color: red;");
        initCont();
        getChildren().addAll(vBox);
    }

    public void isShown() {
        if (progData.selectedProjectData == null) {
            vBox.setDisable(true);
            return;
        }

        vBox.setDisable(false);
        guiMosaicPane.isShown();
        guiMosaicChangePane.isShown();
        guiChangeBorderPane.setMosaicData(progData.selectedProjectData.getMosaicData());

        if (!progData.selectedProjectData.getMosaicData().equals(mosaicData)) {
            mosaicData = progData.selectedProjectData.getMosaicData();
        }
    }


    private void initCont() {
        Tab tab = new Tab("Mosaik");
        tab.setClosable(false);
        tab.setContent(guiMosaicPane);
        tabPane.getTabs().add(tab);

        tab = new Tab("Miniaturbilder verändern");
        tab.setClosable(false);
        tab.setContent(guiMosaicChangePane);
        tabPane.getTabs().add(tab);

        tab = new Tab("Rahmen");
        tab.setClosable(false);
        tab.setContent(guiChangeBorderPane);
        tabPane.getTabs().add(tab);

        btnCreate.setOnAction(a -> {
            if (progData.selectedProjectData.getThumbCollection().getThumbList().isEmpty()) {
                PAlert.showErrorAlert("Mosaik erstellen", "Es sind keine Miniaturbilder in der Sammlung.");
                return;
            }
            if (mosaicData == null) {
                //todo kann das sein?
                return;
            }

            if (mosaicData.getFotoSrc().isEmpty()) {
                PAlert.showErrorAlert("Mosaik erstellen", "Es ist kein Vorlagenbild für das Mosaik angegeben.");
                return;
            }

            if (mosaicData.getFotoDest().isEmpty()) {
                PAlert.showErrorAlert("Mosaik erstellen", "Es ist kein Speicherziel angegeben.");
                return;
            }

            progData.worker.createMosaic(mosaicData, progData.selectedProjectData.getThumbCollection());
        });
        btnCreate.disableProperty().bind(progData.worker.workingProperty());
        btnCreate.getStyleClass().add("btnCreate");

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().add(btnCreate);

        vBox.setPadding(new Insets(10));
        vBox.setSpacing(20);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
        vBox.getChildren().addAll(tabPane, hBox);
    }

}
