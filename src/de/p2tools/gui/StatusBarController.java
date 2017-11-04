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

import de.p2tools.controller.config.Daten;
import de.p2tools.controller.data.Icons;
import de.p2tools.gui.tools.Listener;
import de.p2tools.mLib.tools.Log;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public class StatusBarController extends AnchorPane {

    StackPane stackPane = new StackPane();

    // loadPane
    Label lblProgress = new Label();
    ProgressBar progress = new ProgressBar();
    Button btnStop = new Button("");

    //Film
    Label lblLeftFilm = new Label();
    Label lblRightFilm = new Label();

    //Download
    Label lblLeftDownload = new Label();
    Label lblRightDownload = new Label();

    //nonePane
    Label lblLeftNone = new Label();
    Label lblRightNone = new Label();

    AnchorPane loadPane = new AnchorPane();
    AnchorPane nonePane = new AnchorPane();
    AnchorPane filmPane = new AnchorPane();
    AnchorPane downloadPane = new AnchorPane();


    public enum StatusbarIndex {

        NONE, FILME, DOWNLOAD
    }

    private StatusbarIndex statusbarIndex = StatusbarIndex.NONE;
    private boolean loadList = false;

    private final Daten daten;
    private boolean stopTimer = false;
    private static final String TRENNER = "  ||  ";

    public StatusBarController(Daten daten) {
        this.daten = daten;

        getChildren().addAll(stackPane);
        AnchorPane.setLeftAnchor(stackPane, 0.0);
        AnchorPane.setBottomAnchor(stackPane, 0.0);
        AnchorPane.setRightAnchor(stackPane, 0.0);
        AnchorPane.setTopAnchor(stackPane, 0.0);


        HBox hBox = getHbox();
        lblLeftNone.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblLeftNone, Priority.ALWAYS);
        hBox.getChildren().addAll(lblLeftNone, lblRightNone);
        nonePane.getChildren().add(hBox);
        nonePane.setStyle("-fx-background-color: -fx-background ;");

        hBox = getHbox();
        btnStop.setGraphic(new Icons().ICON_BUTTON_STOP);
        hBox.getChildren().addAll(lblProgress, progress, btnStop);
        progress.setPrefWidth(200);
        loadPane.getChildren().add(hBox);
        loadPane.setStyle("-fx-background-color: -fx-background ;");

        hBox = getHbox();
        lblLeftFilm.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblLeftFilm, Priority.ALWAYS);
        hBox.getChildren().addAll(lblLeftFilm, lblRightFilm);
        filmPane.getChildren().add(hBox);
        filmPane.setStyle("-fx-background-color: -fx-background ;");

        hBox = getHbox();
        lblLeftDownload.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblLeftDownload, Priority.ALWAYS);
        hBox.getChildren().addAll(lblLeftDownload, lblRightDownload);
        downloadPane.getChildren().add(hBox);
        downloadPane.setStyle("-fx-background-color: -fx-background ;");

        make();
    }

    private HBox getHbox() {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(2, 5, 2, 5));
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        AnchorPane.setLeftAnchor(hBox, 0.0);
        AnchorPane.setBottomAnchor(hBox, 0.0);
        AnchorPane.setRightAnchor(hBox, 0.0);
        AnchorPane.setTopAnchor(hBox, 0.0);
        return hBox;
    }


    private void make() {
        stackPane.getChildren().addAll(nonePane, loadPane, filmPane, downloadPane);
        nonePane.toFront();

        Listener.addListener(new Listener(Listener.EREIGNIS_TIMER, StatusBarController.class.getSimpleName()) {
            @Override
            public void ping() {
                try {
                    if (!stopTimer) {
                        setStatusbar();
                    }
                } catch (final Exception ex) {
                    Log.errorLog(936251087, ex);
                }
            }
        });
    }

    public void setStatusbar() {
        setStatusbarIndex(statusbarIndex);
    }

    public void setStatusbarIndex(StatusbarIndex statusbarIndex) {
        this.statusbarIndex = statusbarIndex;
        if (loadList) {
            loadPane.toFront();
            return;
        }

        switch (statusbarIndex) {
            case FILME:
                filmPane.toFront();
                setInfoFilme();
                setTextForRightDisplay();
                break;
            case DOWNLOAD:
                downloadPane.toFront();
                setInfoDownload();
                setTextForRightDisplay();
                break;
            case NONE:
            default:
                nonePane.toFront();
                setTextNone();
                setTextForRightDisplay();
                break;
        }
    }


    private void setTextNone() {
        lblLeftNone.setText("Anzahl Filme: " + 0);
    }

    private void setInfoFilme() {
        String textLinks="Film";
        lblLeftFilm.setText(textLinks);
    }

    private void setInfoDownload() {
        final String textLinks = "Download";
        lblLeftDownload.setText(textLinks);
    }


    private void setTextForRightDisplay() {
        // Text rechts: alter/neuladenIn anzeigen
        String strText = "Filmliste erstellt: ";
        // Infopanel setzen
        lblRightFilm.setText(strText);
        lblRightDownload.setText(strText);
        lblRightNone.setText(strText);
    }


}
