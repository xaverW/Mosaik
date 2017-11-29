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

import de.p2tools.controller.FotoEvent;
import de.p2tools.controller.FotoListener;
import de.p2tools.controller.config.ProgData;
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
    ProgressBar progressBar = new ProgressBar();
    Button btnStop = new Button("");

    //Thumb
    Label lblLeftThumb = new Label();
    Label lblRightThumb = new Label();

    //Mosaik
    Label lblLeftMosaik = new Label();
    Label lblRightMosaik = new Label();

    //Wallpaper
    Label lblLeftWallpaper = new Label();
    Label lblRightWallpaper = new Label();

    //nonePane
    Label lblLeftNone = new Label();
    Label lblRightNone = new Label();

    AnchorPane loadPane = new AnchorPane();
    AnchorPane nonePane = new AnchorPane();
    AnchorPane thumbPane = new AnchorPane();
    AnchorPane mosaikPane = new AnchorPane();
    AnchorPane wallpaperPane = new AnchorPane();


    public enum StatusbarIndex {

        NONE, Thumb, Mosaik, Wallpaper
    }

    private StatusbarIndex statusbarIndex = StatusbarIndex.NONE;
    private boolean loadFoto = false;

    private final ProgData progData;

    public StatusBarController(ProgData progData) {
        this.progData = progData;

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
        hBox.getChildren().addAll(lblProgress, progressBar, btnStop);
        progressBar.setPrefWidth(200);
        loadPane.getChildren().add(hBox);
        loadPane.setStyle("-fx-background-color: -fx-background ;");

        hBox = getHbox();
        lblLeftThumb.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblLeftThumb, Priority.ALWAYS);
        hBox.getChildren().addAll(lblLeftThumb, lblRightThumb);
        thumbPane.getChildren().add(hBox);
        thumbPane.setStyle("-fx-background-color: -fx-background ;");

        hBox = getHbox();
        lblLeftMosaik.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblLeftMosaik, Priority.ALWAYS);
        hBox.getChildren().addAll(lblLeftMosaik, lblRightMosaik);
        mosaikPane.getChildren().add(hBox);
        mosaikPane.setStyle("-fx-background-color: -fx-background ;");

        hBox = getHbox();
        lblLeftWallpaper.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblLeftWallpaper, Priority.ALWAYS);
        hBox.getChildren().addAll(lblLeftWallpaper, lblRightWallpaper);
        wallpaperPane.getChildren().add(hBox);
        wallpaperPane.setStyle("-fx-background-color: -fx-background ;");

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
        stackPane.getChildren().addAll(nonePane, loadPane, thumbPane, mosaikPane);
        nonePane.toFront();

        progData.genThumbList.addAdListener(new FotoListener() {
            @Override
            public void ping(FotoEvent fotoEvent) {
                if (fotoEvent.nixLos()) {
                    loadFoto = false;
                } else {
                    loadFoto = true;
                }
                updateProgressBar(fotoEvent);
                setStatusbar();
            }
        });
        Listener.addListener(new Listener(Listener.EREIGNIS_TIMER, StatusBarController.class.getSimpleName()) {
            @Override
            public void ping() {
                try {
                    if (!loadFoto) {
                        setStatusbar();
                    }
                } catch (final Exception ex) {
                    Log.errorLog(936251087, ex);
                }
            }
        });
    }

    private void updateProgressBar(FotoEvent event) {
        int max = event.getMax();
        int progress = event.getProgress();
        double prog = 1.0;
        if (max > 0) {
            prog = 1.0 * progress / max;
        }

        progressBar.setProgress(prog);
        lblProgress.setText(event.getText());
    }

    public void setStatusbar() {
        setStatusbarIndex(statusbarIndex);
    }

    public void setStatusbarIndex(StatusbarIndex statusbarIndex) {
        this.statusbarIndex = statusbarIndex;
        if (loadFoto) {
            loadPane.toFront();
            return;
        }

        switch (statusbarIndex) {
            case Thumb:
                thumbPane.toFront();
                setInfoThumb();
                setTextForRightDisplay();
                break;
            case Mosaik:
                mosaikPane.toFront();
                setInfoMosaik();
                setTextForRightDisplay();
                break;
            case Wallpaper:
                wallpaperPane.toFront();
                setInfoWallpaper();
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

    private void setInfoThumb() {
        String textLinks = "Miniaturbilder: " + progData.selectedThumbCollection.getName();
        lblLeftThumb.setText(textLinks);
    }

    private void setInfoMosaik() {
        String textLinks = "Miniaturbilder: " + progData.selectedThumbCollection.getName();
        lblLeftMosaik.setText(textLinks);
    }

    private void setInfoWallpaper() {
        String textLinks = "Miniaturbilder: " + progData.selectedThumbCollection.getName();
        lblLeftWallpaper.setText(textLinks);
    }

    private void setTextForRightDisplay() {
        // Text rechts: alter/neuladenIn anzeigen
        String strText = progData.selectedThumbCollection.getThumbList().size() + " Bilder";
        // Infopanel setzen
        lblRightThumb.setText(strText);
        lblRightMosaik.setText(strText);
        lblRightNone.setText(strText);
    }


}
