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

package de.mtplayer;

import de.mtplayer.controller.ProgQuitt;
import de.mtplayer.controller.config.Config;
import de.mtplayer.controller.config.Daten;
import de.mtplayer.controller.data.Icons;
import de.mtplayer.gui.DownloadGuiPack;
import de.mtplayer.gui.FilmGuiPack;
import de.mtplayer.gui.StatusBarController;
import de.mtplayer.gui.configDialog.ConfigDialogController;
import de.mtplayer.gui.dialog.AboutDialogController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.controlsfx.control.MaskerPane;

public class MTFxController extends StackPane {

    Button btnFilm = new Button("Filme");
    Button btnDownload = new Button("Downloads");

    MenuButton menuButton = new MenuButton("");

    VBox vBoxAll = new VBox();
    BorderPane borderPane = new BorderPane();
    StackPane stackPaneCont = new StackPane();
    private MaskerPane maskerPane = new MaskerPane();
    private StatusBarController statusBarController;

    private SplitPane splitPaneFilm;
    private SplitPane splitPaneDownoad;

    private final Daten daten;
    BooleanProperty msgVisProperty = Config.MSG_VISIBLE.getBooleanProperty();

    FilmGuiPack filmGuiPack ;
    DownloadGuiPack downloadGuiPack;


    public MTFxController() {
        daten = Daten.getInstance();
        filmGuiPack = new FilmGuiPack();
        downloadGuiPack = new DownloadGuiPack();
        init();
    }

    private void init() {
        try {
            // Top
            this.setPadding(new Insets(0));

            HBox hBoxTop = new HBox();
            hBoxTop.setPadding(new Insets(10));
            hBoxTop.setSpacing(20);
            hBoxTop.setAlignment(Pos.CENTER);
            HBox.setHgrow(hBoxTop, Priority.ALWAYS);

            TilePane tilePane = new TilePane();
            tilePane.setHgap(20);
            tilePane.setAlignment(Pos.CENTER);
            HBox.setHgrow(tilePane, Priority.ALWAYS);

            tilePane.getChildren().addAll(btnFilm, btnDownload);
            hBoxTop.getChildren().addAll( tilePane, menuButton);


            splitPaneFilm = filmGuiPack.pack();
            splitPaneDownoad = downloadGuiPack.pack();
            stackPaneCont.getChildren().addAll(splitPaneFilm, splitPaneDownoad);

            statusBarController = new StatusBarController(daten);

            vBoxAll.getChildren().addAll(hBoxTop, stackPaneCont, statusBarController);
            VBox.setVgrow(hBoxTop, Priority.NEVER);
            VBox.setVgrow(statusBarController, Priority.NEVER);

            borderPane.setTop(hBoxTop);
            borderPane.setCenter(stackPaneCont);
            borderPane.setBottom(statusBarController);

            this.setPadding(new Insets(0));
            maskerPane.setPadding(new Insets(3, 1, 1, 1));
            this.getChildren().addAll(borderPane, maskerPane);
            StackPane.setAlignment(maskerPane, Pos.CENTER);
            maskerPane.toFront();
            maskerPane.setVisible(false);

            btnFilm.getStyleClass().add("btnFilm");
            btnFilm.setOnAction(e -> selPanelFilm());
            btnFilm.setMaxWidth(Double.MAX_VALUE);

            btnDownload.getStyleClass().add("btnDownlad");
            btnDownload.setOnAction(e -> selPanelDownload());
            btnDownload.setMaxWidth(Double.MAX_VALUE);

            final MenuItem miConfig = new MenuItem("Einstellungen");
            miConfig.setOnAction(e -> new ConfigDialogController());

            final MenuItem miQuitt = new MenuItem("Beenden");
            miQuitt.setOnAction(e -> new ProgQuitt().beenden(true, false));

            final MenuItem miAbout = new MenuItem("Über dieses Programm");
            miAbout.setOnAction(event -> new AboutDialogController(daten));

            final Menu mHelp = new Menu("Hilfe");
            mHelp.getItems().addAll(miAbout);

            menuButton.getStyleClass().add("btnFunction");
            menuButton.setText("");
            menuButton.setGraphic(new Icons().FX_ICON_TOOLBAR_MENUE_TOP);
            menuButton.getItems().addAll(miConfig, mHelp, new SeparatorMenuItem(), miQuitt);


            selPanelFilm();


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void selPanelFilm() {
        if (maskerPane.isVisible()) {
            return;
        }

        btnFilm.getStyleClass().clear();
        btnDownload.getStyleClass().clear();

        btnFilm.getStyleClass().add("btnTab-sel");
        btnDownload.getStyleClass().add("btnTab");

        splitPaneFilm.toFront();
        daten.filmGuiController.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.FILME);
    }

    private void selPanelDownload() {
        if (maskerPane.isVisible()) {
            return;
        }

        btnFilm.getStyleClass().clear();
        btnDownload.getStyleClass().clear();

        btnFilm.getStyleClass().add("btnTab");
        btnDownload.getStyleClass().add("btnTab-sel");

        daten.downloadGuiController.isShown();
        splitPaneDownoad.toFront();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.DOWNLOAD);
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
