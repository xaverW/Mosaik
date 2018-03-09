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
import de.p2tools.mosaik.gui.mosaik.GuiMosaikController;
import de.p2tools.mosaik.gui.wallpaper.GuiWallpaperController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

public class GuiMosaik extends AnchorPane {

    private final VBox vBox = new VBox(0);
    private final StackPane stackPane = new StackPane();
    private final ProgData progData;
    private final ComboBox<OPTIONS> comboBox = new ComboBox();

    private GuiMosaikController guiMosaikController = null;
    private GuiWallpaperController guiWallpaperController = null;


    public enum OPTIONS {

        MOSAIK("Mosaik"), WALLPAPER("Fototapete");
        private final String name;

        OPTIONS(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public GuiMosaik() {
        progData = ProgData.getInstance();
        guiMosaikController = new GuiMosaikController();
        guiWallpaperController = new GuiWallpaperController();

        initCont();
    }

    public void isShown() {
        guiMosaikController.isShown();
        guiWallpaperController.isShown();
    }


    private void initCont() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(10));
        hBox.getChildren().add(comboBox);


        stackPane.getChildren().addAll(guiMosaikController, guiWallpaperController);

        comboBox.getItems().addAll(OPTIONS.MOSAIK, OPTIONS.WALLPAPER);
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    switch (newValue) {
                        case MOSAIK:
                            guiMosaikController.toFront();
                            break;
                        case WALLPAPER:
                            guiWallpaperController.toFront();
                            break;
                        default:
                            guiMosaikController.toFront();
                    }
                }
        );
        comboBox.getSelectionModel().selectFirst();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setTopAnchor(scrollPane, 0.0);

        VBox.setVgrow(stackPane, Priority.ALWAYS);
        vBox.getChildren().addAll(hBox, stackPane);

        scrollPane.setContent(vBox);

        this.getChildren().add(scrollPane);
    }
}
