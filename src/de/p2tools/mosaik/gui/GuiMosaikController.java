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

package de.p2tools.mosaik.gui;

import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.controller.data.mosaikData.MosaikData;
import de.p2tools.p2Lib.dialog.PAlert;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GuiMosaikController extends AnchorPane {

    private final ProgData progData;
    private final VBox vBox = new VBox();
    private final TabPane tabPane = new TabPane();

    private final GuiMosaikPane guiMosaikPane = new GuiMosaikPane();
    private final GuiMosaikExtendedThumbPane guiMosaikExtendedThumbPane = new GuiMosaikExtendedThumbPane();
    private final GuiMosaikExtendedSizePane guiMosaikExtendedSizePane = new GuiMosaikExtendedSizePane();
    private final Button btnCreate = new Button("Mosaik erstellen");

    MosaikData mosaikData = null;

    public GuiMosaikController() {
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
        guiMosaikPane.isShown();
        guiMosaikExtendedThumbPane.isShown();
        guiMosaikExtendedSizePane.isShown();

        if (!progData.selectedProjectData.getMosaikData().equals(mosaikData)) {
            mosaikData = progData.selectedProjectData.getMosaikData();
        }
    }


    private void initCont() {
        Tab tab = new Tab("Mosaik");
        tab.setClosable(false);
        tab.setContent(guiMosaikPane);
        tabPane.getTabs().add(tab);

        tab = new Tab("Miniaturbilder verändern");
        tab.setClosable(false);
        tab.setContent(guiMosaikExtendedThumbPane);
        tabPane.getTabs().add(tab);

        tab = new Tab("Größe verändern");
        tab.setClosable(false);
        tab.setContent(guiMosaikExtendedSizePane);
        tabPane.getTabs().add(tab);

        btnCreate.setOnAction(a -> {
            if (mosaikData != null &&
                    !mosaikData.getFotoSrc().isEmpty() &&
                    !mosaikData.getFotoDestDir().isEmpty() &&
                    !mosaikData.getFotoDestName().isEmpty()) {
                progData.worker.createMosaik(mosaikData, progData.selectedProjectData.getThumbCollection());

            } else if (mosaikData.getFotoSrc().isEmpty()) {
                PAlert.showErrorAlert("Mosaik erstellen", "Es ist kein Vorlagenbild für das Mosaik angegeben.");

            } else if (mosaikData.getFotoDestDir().isEmpty()) {
                PAlert.showErrorAlert("Mosaik erstellen", "Es ist kein Speicherziel angegeben.");

            } else if (mosaikData.getFotoDestName().isEmpty()) {
                PAlert.showErrorAlert("Mosaik erstellen", "Es ist kein Dateiname für das erstellte Mosaik angegeben.");
            }
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
