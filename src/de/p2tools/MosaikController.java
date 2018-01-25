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

package de.p2tools;

import de.p2tools.controller.ProgQuitt;
import de.p2tools.controller.config.ProgData;
import de.p2tools.controller.data.Icons;
import de.p2tools.gui.*;
import de.p2tools.gui.configDialog.ConfigDialogController;
import de.p2tools.gui.dialog.AboutDialogController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.controlsfx.control.MaskerPane;

public class MosaikController extends StackPane {

    Button btnStart = new Button("Start");
    Button btnThumbNail = new Button("Miniaturbilder\n" +
            "erstellen");
    Button btnChangeThumbNail = new Button("Miniaturbilder\n" +
            "bearbeiten");
    Button btnMosaik = new Button("Mosaik\n" +
            "erstellen");
    Button btnWallpaper = new Button("Fototapete\n" +
            "erstellen");
    Button btnPrev = new Button("");
    Button btnNext = new Button("");

    MenuButton menuButton = new MenuButton("");

    BorderPane borderPane = new BorderPane();
    StackPane stackPaneCont = new StackPane();
    private MaskerPane maskerPane = new MaskerPane();
    private StatusBarController statusBarController;

    private AnchorPane guiStartController;
    private AnchorPane guiThumbController;
    private AnchorPane guiChangeThumbController;
    private AnchorPane guiMosaikController;
    private AnchorPane guiWallpaperController;

    private final ProgData progData;


    public MosaikController() {
        progData = ProgData.getInstance();
        init();
    }

    private void init() {
        try {
            // Menübutton
            HBox hBoxMenueButton = new HBox();
            hBoxMenueButton.setPadding(new Insets(10));
            hBoxMenueButton.setSpacing(20);
            hBoxMenueButton.setAlignment(Pos.CENTER);
            HBox.setHgrow(hBoxMenueButton, Priority.ALWAYS);

            TilePane tilePane = new TilePane();
            tilePane.setHgap(20);
            tilePane.setAlignment(Pos.CENTER);
            HBox.setHgrow(tilePane, Priority.ALWAYS);

            tilePane.getChildren().addAll(btnStart, btnThumbNail, btnChangeThumbNail, btnMosaik, btnWallpaper);
            hBoxMenueButton.getChildren().addAll(tilePane, menuButton);

            btnStart.setOnAction(e -> selPanelStart());
            btnStart.setMaxWidth(Double.MAX_VALUE);

            btnThumbNail.setOnAction(e -> selPanelTumb());
            btnThumbNail.setMaxWidth(Double.MAX_VALUE);

            btnChangeThumbNail.setOnAction(e -> selPanelChangeTumb());
            btnChangeThumbNail.setMaxWidth(Double.MAX_VALUE);

            btnMosaik.setOnAction(e -> selPanelMosaik());
            btnMosaik.setMaxWidth(Double.MAX_VALUE);

            btnWallpaper.setOnAction(e -> selPanelWallpaper());
            btnWallpaper.setMaxWidth(Double.MAX_VALUE);

            final MenuItem miConfig = new MenuItem("Einstellungen");
            miConfig.setOnAction(e -> new ConfigDialogController());

            final MenuItem miQuitt = new MenuItem("Beenden");
            miQuitt.setOnAction(e -> new ProgQuitt().beenden(true));

            final MenuItem miAbout = new MenuItem("Über dieses Programm");
            miAbout.setOnAction(event -> new AboutDialogController(progData));

            final Menu mHelp = new Menu("Hilfe");
            mHelp.getItems().addAll(miAbout);

            menuButton.getStyleClass().add("btnFunction");
            menuButton.setText("");
            javafx.scene.image.ImageView iv = new Icons().FX_ICON_TOOLBAR_MENUE_TOP;
            menuButton.setGraphic(new Icons().FX_ICON_TOOLBAR_MENUE_TOP);
            menuButton.getItems().addAll(miConfig, mHelp, new SeparatorMenuItem(), miQuitt);


            // Panes
            progData.guiStartController = new GuiStartController();
            guiStartController = progData.guiStartController;

            progData.guiThumbController = new GuiThumbController();
            guiThumbController = progData.guiThumbController;

            progData.guiChangeThumbController = new GuiChangeThumbController();
            guiChangeThumbController = progData.guiChangeThumbController;

            progData.guiMosaikController = new GuiMosaikController();
            guiMosaikController = progData.guiMosaikController;

            progData.guiWallpaperController = new GuiWallpaperController();
            guiWallpaperController = progData.guiWallpaperController;

            stackPaneCont.getChildren().addAll(guiStartController, guiThumbController,
                    guiChangeThumbController, guiMosaikController, guiWallpaperController);


            // Statusbar
            statusBarController = new StatusBarController(progData);


            // MaskerPane
            maskerPane.setPadding(new Insets(3, 1, 1, 1));
            StackPane.setAlignment(maskerPane, Pos.CENTER);
            maskerPane.setVisible(false);

            // Button NEXT-PREV
            HBox hBoxPrev = new HBox();
            hBoxPrev.getChildren().addAll(btnPrev);
            hBoxPrev.setAlignment(Pos.CENTER);
            btnPrev.setMaxHeight(Double.MAX_VALUE);
            btnPrev.setGraphic(new Icons().ICON_BUTTON_GUI_PREV);
            btnPrev.setOnAction(a -> setPrev());
            HBox.setHgrow(btnPrev, Priority.ALWAYS);

            HBox hBoxNext = new HBox();
            hBoxNext.getChildren().addAll(btnNext);
            hBoxNext.setAlignment(Pos.CENTER);
            btnNext.setMaxHeight(Double.MAX_VALUE);
            btnNext.setGraphic(new Icons().ICON_BUTTON_GUI_NEXT);
            btnNext.setOnAction(a -> setNext());
            HBox.setHgrow(btnNext, Priority.ALWAYS);

            // ProgGUI
            borderPane.setTop(hBoxMenueButton);
            borderPane.setCenter(stackPaneCont);
            borderPane.setBottom(statusBarController);
            borderPane.setLeft(hBoxPrev);
            borderPane.setRight(hBoxNext);

            this.setPadding(new Insets(0));
            this.getChildren().addAll(borderPane, maskerPane);

            selPanelStart();
            addStartGuiBinding();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void addStartGuiBinding() {
        btnStart.disableProperty().bind(progData.guiStartController.allOkProperty().not());
        btnThumbNail.disableProperty().bind(progData.guiStartController.allOkProperty().not());
        btnChangeThumbNail.disableProperty().bind(progData.guiStartController.allOkProperty().not());
        btnMosaik.disableProperty().bind(progData.guiStartController.allOkProperty().not());
        btnWallpaper.disableProperty().bind(progData.guiStartController.allOkProperty().not());

        if (!progData.guiStartController.isAllOk()) {
            btnNext.setDisable(true);
            btnPrev.setDisable(true);
        }

        progData.guiStartController.allOkProperty().addListener(l -> {
            if (!progData.guiStartController.isAllOk()) {
                btnNext.setDisable(true);
                btnPrev.setDisable(true);
            } else {
                selPanelStart();
            }
        });
    }

    private void setPrev() {
        Node front = stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1);
        if (front.equals(guiStartController)) {

        } else if (front.equals(guiThumbController)) {
            selPanelStart();
        } else if (front.equals(guiChangeThumbController)) {
            selPanelTumb();
        } else if (front.equals(guiMosaikController)) {
            selPanelChangeTumb();
        } else if (front.equals(guiWallpaperController)) {
            selPanelMosaik();
        }
    }

    private void setNext() {
        Node front = stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1);
        if (front.equals(guiStartController)) {
            selPanelTumb();
        } else if (front.equals(guiThumbController)) {
            selPanelChangeTumb();
        } else if (front.equals(guiChangeThumbController)) {
            selPanelMosaik();
        } else if (front.equals(guiMosaikController)) {
            selPanelWallpaper();
        } else if (front.equals(guiWallpaperController)) {

        }
    }

    private void selPanelStart() {
        if (maskerPane.isVisible()) {
            return;
        }
        btnPrev.setDisable(true);
        btnNext.setDisable(false);

        btnStart.getStyleClass().clear();
        btnThumbNail.getStyleClass().clear();
        btnChangeThumbNail.getStyleClass().clear();
        btnMosaik.getStyleClass().clear();
        btnWallpaper.getStyleClass().clear();

        btnStart.getStyleClass().add("btnTab-sel");
        btnThumbNail.getStyleClass().add("btnTab");
        btnChangeThumbNail.getStyleClass().add("btnTab");
        btnMosaik.getStyleClass().add("btnTab");
        btnWallpaper.getStyleClass().add("btnTab");

        guiStartController.toFront();
        progData.guiStartController.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.Start);
    }

    private void selPanelTumb() {
        if (maskerPane.isVisible()) {
            return;
        }
        btnPrev.setDisable(false);
        btnNext.setDisable(false);

        btnStart.getStyleClass().clear();
        btnThumbNail.getStyleClass().clear();
        btnChangeThumbNail.getStyleClass().clear();
        btnMosaik.getStyleClass().clear();
        btnWallpaper.getStyleClass().clear();

        btnStart.getStyleClass().add("btnTab");
        btnThumbNail.getStyleClass().add("btnTab-sel");
        btnChangeThumbNail.getStyleClass().add("btnTab");
        btnMosaik.getStyleClass().add("btnTab");
        btnWallpaper.getStyleClass().add("btnTab");

        guiThumbController.toFront();
        progData.guiThumbController.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.Thumb);
    }

    private void selPanelChangeTumb() {
        if (maskerPane.isVisible()) {
            return;
        }
        btnPrev.setDisable(false);
        btnNext.setDisable(false);

        btnStart.getStyleClass().clear();
        btnThumbNail.getStyleClass().clear();
        btnChangeThumbNail.getStyleClass().clear();
        btnMosaik.getStyleClass().clear();
        btnWallpaper.getStyleClass().clear();

        btnStart.getStyleClass().add("btnTab");
        btnThumbNail.getStyleClass().add("btnTab");
        btnChangeThumbNail.getStyleClass().add("btnTab-sel");
        btnMosaik.getStyleClass().add("btnTab");
        btnWallpaper.getStyleClass().add("btnTab");

        guiChangeThumbController.toFront();
        progData.guiChangeThumbController.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.Thumb);
    }

    private void selPanelMosaik() {
        if (maskerPane.isVisible()) {
            return;
        }
        btnPrev.setDisable(false);
        btnNext.setDisable(false);

        btnStart.getStyleClass().clear();
        btnThumbNail.getStyleClass().clear();
        btnChangeThumbNail.getStyleClass().clear();
        btnMosaik.getStyleClass().clear();
        btnWallpaper.getStyleClass().clear();

        btnStart.getStyleClass().add("btnTab");
        btnThumbNail.getStyleClass().add("btnTab");
        btnChangeThumbNail.getStyleClass().add("btnTab");
        btnMosaik.getStyleClass().add("btnTab-sel");
        btnWallpaper.getStyleClass().add("btnTab");

        progData.guiMosaikController.isShown();
        guiMosaikController.toFront();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.Mosaik);
    }

    private void selPanelWallpaper() {
        if (maskerPane.isVisible()) {
            return;
        }
        btnPrev.setDisable(false);
        btnNext.setDisable(true);

        btnStart.getStyleClass().clear();
        btnThumbNail.getStyleClass().clear();
        btnChangeThumbNail.getStyleClass().clear();
        btnMosaik.getStyleClass().clear();
        btnWallpaper.getStyleClass().clear();

        btnStart.getStyleClass().add("btnTab");
        btnThumbNail.getStyleClass().add("btnTab");
        btnChangeThumbNail.getStyleClass().add("btnTab");
        btnMosaik.getStyleClass().add("btnTab");
        btnWallpaper.getStyleClass().add("btnTab-sel");

        progData.guiWallpaperController.isShown();
        guiWallpaperController.toFront();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.Wallpaper);
    }

    public void setMasker() {
        maskerPane.setVisible(true);
    }

    public void resetMasker() {
        Platform.runLater(() -> {
            maskerPane.setVisible(false);
        });
    }
}
