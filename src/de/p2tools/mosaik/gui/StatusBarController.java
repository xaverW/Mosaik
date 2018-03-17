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

import de.p2tools.mosaik.controller.RunEvent;
import de.p2tools.mosaik.controller.RunListener;
import de.p2tools.mosaik.controller.config.ProgConfig;
import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.controller.data.Icons;
import de.p2tools.p2Lib.guiTools.Listener;
import de.p2tools.p2Lib.guiTools.PProgressBar;
import de.p2tools.p2Lib.tools.Functions;
import de.p2tools.p2Lib.tools.Log;
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

    // memory
    private final Runtime rt = Runtime.getRuntime();
    private static final int MEGABYTE = 1000 * 1000;
    private final PProgressBar pProgressBar1 = new PProgressBar();
    private final PProgressBar pProgressBar2 = new PProgressBar();

    // textPane
    private final AnchorPane textPane = new AnchorPane();
    private final Label lblLeft = new Label();
    private final Label lblRight = new Label();

    // workerPane
    private final AnchorPane workerPane = new AnchorPane();
    private final Label lblProgress = new Label();
    private final ProgressBar progressBar = new ProgressBar();
    private final Button btnStop = new Button("");

    private boolean running = false;
    private final ProgData progData;

    public enum StatusbarIndex {
        Start, Thumb, Mosaik

    }

    private StatusbarIndex statusbarIndex = StatusbarIndex.Start;

    public StatusBarController(ProgData progData) {
        this.progData = progData;

        getChildren().addAll(stackPane);
        AnchorPane.setLeftAnchor(stackPane, 0.0);
        AnchorPane.setBottomAnchor(stackPane, 0.0);
        AnchorPane.setRightAnchor(stackPane, 0.0);
        AnchorPane.setTopAnchor(stackPane, 0.0);

        // textPane
        HBox hBox = getHbox();
        pProgressBar1.setMaxWidth(Double.MAX_VALUE);
        pProgressBar1.setAlignment(Pos.CENTER);
        HBox.setHgrow(pProgressBar1, Priority.ALWAYS);

        hBox.getChildren().addAll(lblLeft, pProgressBar1, lblRight);
        textPane.getChildren().add(hBox);
        textPane.setStyle("-fx-background-color: -fx-background;");

        // workerPane
        hBox = getHbox();
        pProgressBar2.setMaxWidth(Double.MAX_VALUE);
        pProgressBar2.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(pProgressBar2, Priority.ALWAYS);

        hBox.getChildren().addAll(pProgressBar2, lblProgress, progressBar, btnStop);
        progressBar.setPrefWidth(200);
        workerPane.getChildren().add(hBox);
        workerPane.setStyle("-fx-background-color: -fx-background;");

        stackPane.getChildren().addAll(textPane, workerPane);
        textPane.toFront();

        make();
    }

    private HBox getHbox() {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(2, 5, 2, 5));
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        AnchorPane.setLeftAnchor(hBox, 0.0);
        AnchorPane.setBottomAnchor(hBox, 0.0);
        AnchorPane.setRightAnchor(hBox, 0.0);
        AnchorPane.setTopAnchor(hBox, 0.0);
        return hBox;
    }


    private void make() {
        btnStop.setGraphic(new Icons().ICON_BUTTON_STOP);
        btnStop.setOnAction(e -> progData.worker.setStop());
        pProgressBar1.visibleProperty().bind(ProgConfig.START_SHOW_MEM_DATA);
        pProgressBar2.visibleProperty().bind(ProgConfig.START_SHOW_MEM_DATA);

        progData.worker.addAdListener(new RunListener() {
            @Override
            public void ping(RunEvent runEvent) {
                if (runEvent.nixLos()) {
                    running = false;
                } else {
                    running = true;
                }
                updateProgressBar(runEvent);
                setStatusbar();
            }
        });

        Listener.addListener(new Listener(Listener.EREIGNIS_TIMER, StatusBarController.class.getSimpleName()) {
            @Override
            public void ping() {
                try {
                    getMem();
                    if (!running) {
                        setStatusbar();
                    }
                } catch (final Exception ex) {
                    Log.errorLog(936251087, ex);
                }
            }
        });
    }

    private synchronized void updateProgressBar(RunEvent event) {
        int max = event.getMax();
        int progress = event.getProgress();
        double prog = 1.0;
        if (max > 0) {
            prog = 1.0 * progress / max;
        }

        lblProgress.setText(event.getText());
        progressBar.setProgress(prog);
    }

    private void setStatusbar() {
        setStatusbarIndex(statusbarIndex);
    }

    public void setStatusbarIndex(StatusbarIndex statusbarIndex) {
        this.statusbarIndex = statusbarIndex;
        if (running) {
            workerPane.toFront();
            return;
        }

        textPane.toFront();
        switch (statusbarIndex) {
            case Start:
                setTextNone();
                break;
            case Thumb:
            case Mosaik:
                setInfoMosaik();
                break;
            default:
                setTextNone();
        }
    }


    private void setTextNone() {
        setInfoMosaik();
    }

    private void setInfoMosaik() {
        if (progData.selectedProjectData == null) {
            lblLeft.setText("");
            lblRight.setText("");
            return;
        }

        String textLinks = (progData.selectedProjectData.getThumbCollection() != null ?
                progData.selectedProjectData.getName() : "");
        lblLeft.setText(textLinks);

        String strText = progData.selectedProjectData.getThumbCollection() != null ?
                progData.selectedProjectData.getThumbCollection().getThumbList().size() + " Miniaturbilder" : "";
        lblRight.setText(strText);
    }

    private void getMem() {
        if (!ProgConfig.START_SHOW_MEM_DATA.get()) {
            return;
        }

        final long maxMem;
        if (Functions.getOs() == Functions.OperatingSystemType.LINUX) {
            maxMem = rt.totalMemory();
        } else {
            maxMem = rt.maxMemory();
        }
        final long totalMemory = rt.totalMemory();
        final long freeMemory = rt.freeMemory();
        final long usedMem = totalMemory - freeMemory;

        final long used = usedMem / MEGABYTE;
        final long total = maxMem / MEGABYTE;

        double usedD = 1.0 * usedMem / totalMemory;

        final String info = "Speicher: " + used + " von " + total + " MB";
        pProgressBar1.setProgress(usedD, info);
        pProgressBar2.setProgress(usedD, info);
    }

}
