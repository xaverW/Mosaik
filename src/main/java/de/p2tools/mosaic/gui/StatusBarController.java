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

package de.p2tools.mosaic.gui;

import de.p2tools.mosaic.controller.RunEvent;
import de.p2tools.mosaic.controller.RunListener;
import de.p2tools.mosaic.controller.config.ProgConfig;
import de.p2tools.mosaic.controller.config.ProgData;
import de.p2tools.mosaic.controller.data.Icons;
import de.p2tools.p2Lib.guiTools.Listener;
import de.p2tools.p2Lib.guiTools.PProgressBar;
import de.p2tools.p2Lib.tools.ProgramTools;
import de.p2tools.p2Lib.tools.log.PLog;
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

    // memory
    private final Runtime rt = Runtime.getRuntime();
    private static final int MEGABYTE = 1000 * 1000;
    private final PProgressBar pProgressBar = new PProgressBar();

    // textPane
    private final Label lblLeft = new Label();
    private final Label lblRight = new Label();

    // workerPane
    private final Label lblProgress = new Label();
    private final ProgressBar progressBar = new ProgressBar();
    private final Button btnStop = new Button("");

    private boolean running = false;
    private final ProgData progData;
    private final HBox hBoxAll = new HBox(10);

    public enum StatusbarIndex {
        Start, Thumb, Mosaic

    }

    private StatusbarIndex statusbarIndex = StatusbarIndex.Start;

    public StatusBarController(ProgData progData) {
        this.progData = progData;

        getChildren().addAll(hBoxAll);
        AnchorPane.setLeftAnchor(hBoxAll, 0.0);
        AnchorPane.setBottomAnchor(hBoxAll, 0.0);
        AnchorPane.setRightAnchor(hBoxAll, 0.0);
        AnchorPane.setTopAnchor(hBoxAll, 0.0);
        hBoxAll.setAlignment(Pos.CENTER_RIGHT);
        hBoxAll.setPadding(new Insets(2, 5, 2, 5));

        pProgressBar.setPrefWidth(200);

        setInfo();
        make();
    }

    private void make() {
        btnStop.setGraphic(new Icons().ICON_BUTTON_STOP);
        btnStop.setOnAction(e -> progData.worker.setStop());
        pProgressBar.visibleProperty().bind(ProgConfig.START_SHOW_MEM_DATA);

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
                    PLog.errorLog(936251087, ex);
                }
            }
        });
    }

    private synchronized void updateProgressBar(RunEvent event) {
        int max = event.getMax();
        int progress = event.getProgress();
        double prog = 1.0;
        if (max > 0) {
            prog = (1.0 * progress) / max;
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
            setWorker();
            return;
        }

        setInfo();

//        switch (statusbarIndex) {
//            case Start:
//                setTextNone();
//                break;
//            case Thumb:
//            case Mosaic:
//                setInfoMosaic();
//                break;
//            default:
//                setTextNone();
//        }
    }

    private void setWorker(){
        hBoxAll.getChildren().clear();

        HBox hBProg = new HBox();
        hBProg.setAlignment(Pos.CENTER_LEFT);
        hBProg.getChildren().add(pProgressBar);
        HBox.setHgrow(hBProg, Priority.ALWAYS);

        hBoxAll.getChildren().addAll(hBProg, lblProgress, progressBar, btnStop);
    }
    private void setInfo(){
        hBoxAll.getChildren().clear();

        HBox hBProg = new HBox(10);
        hBProg.setAlignment(Pos.CENTER_LEFT);
        hBProg.getChildren().addAll(lblLeft, lblRight);
        HBox.setHgrow(hBProg, Priority.ALWAYS);

        hBoxAll.getChildren().addAll(hBProg,  pProgressBar);
        setInfoMosaic();
    }

    private void setInfoMosaic() {
        if (progData.selectedProjectData == null) {
            lblLeft.setText("");
            lblRight.setText("");
            return;
        }

        String textLinks = (progData.selectedProjectData.getThumbCollection() != null ?
                progData.selectedProjectData.getName() : "");
        lblLeft.setText(textLinks);

        String strText = progData.selectedProjectData.getThumbCollection() != null ?
                " ( " + progData.selectedProjectData.getThumbCollection().getThumbList().size() + " Miniaturbilder )" : "";
        lblRight.setText(strText);
    }

    private void getMem() {
        if (!ProgConfig.START_SHOW_MEM_DATA.get()) {
            return;
        }

        final long maxMem;
        if (ProgramTools.getOs() == ProgramTools.OperatingSystemType.LINUX) {
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
        pProgressBar.setProgress(usedD, info);
    }

}
