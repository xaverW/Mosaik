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

package de.p2tools.mosaik.gui.wallpaper;

import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.controller.data.mosaikData.WallpaperData;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.image.ImgTools;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GuiWallpaperController extends AnchorPane {

    private final ProgData progData;
    private final VBox vBox = new VBox();
    private final TabPane tabPane = new TabPane();

    private final GuiWallpaperPane guiWallpaperPane = new GuiWallpaperPane();
    private final GuiWallpaperBorderPane guiWallpaperBorderPane = new GuiWallpaperBorderPane();

    private final Button btnCreate = new Button("Fototapete erstellen");

    WallpaperData wallpaperData = null;

    public GuiWallpaperController() {
        this.progData = ProgData.getInstance();
        if (progData.selectedProjectData != null) {
            this.wallpaperData = progData.selectedProjectData.getWallpaperData();
        }

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
        guiWallpaperPane.isShown();

        if (!progData.selectedProjectData.getWallpaperData().equals(wallpaperData)) {
            wallpaperData = progData.selectedProjectData.getWallpaperData();
        }
    }


    private void initCont() {
        Tab tab = new Tab("Fototapete");
        tab.setClosable(false);
        tab.setContent(guiWallpaperPane);
        tabPane.getTabs().add(tab);

        tab = new Tab("Rahmen");
        tab.setClosable(false);
        tab.setContent(guiWallpaperBorderPane);
        tabPane.getTabs().add(tab);

        btnCreate.setOnAction(a -> {
            if (wallpaperData.getNumberThumbsWidth() * wallpaperData.getThumbSize() >= ImgTools.JPEG_MAX_DIMENSION) {
                PAlert.showErrorAlert("Mosaik erstellen", "Die Maximale Größe des Mosaiks ist überschritten.\n" +
                        "(Es darf maximal eine Kantenlänge von " + ImgTools.JPEG_MAX_DIMENSION + " Pixeln haben.");
                return;
            }

            if (!wallpaperData.getFotoDestDir().isEmpty() && !wallpaperData.getFotoDestName().isEmpty()) {
                progData.worker.createWallpaper(progData.selectedProjectData.getThumbCollection(),
                        progData.selectedProjectData.getWallpaperData());
            } else {
                PAlert.showErrorAlert("Fototapete anlegen", "Es ist kein Speicherziel angegeben.");
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
