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
package de.p2tools.mosaic;

import de.p2tools.mosaic.controller.ProgQuitt;
import de.p2tools.mosaic.controller.ProgStart;
import de.p2tools.mosaic.controller.config.ProgConfig;
import de.p2tools.mosaic.controller.config.ProgConst;
import de.p2tools.mosaic.controller.config.ProgData;
import de.p2tools.mosaic.icon.GetIcon;
import de.p2tools.p2Lib.P2LibInit;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.tools.duration.PDuration;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Mosaic extends Application {

    private Stage primaryStage;
    private static final String LOG_TEXT_PROGRAMSTART = "***Programmstart***";

    protected ProgData progData;
    Scene scene = null;

    @Override
    public void init() throws Exception {
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        PDuration.counterStart(LOG_TEXT_PROGRAMSTART);
        progData = ProgData.getInstance();
        progData.primaryStage = primaryStage;
        new ProgStart(progData).loadConfigData();

        initP2lib();
        initRootLayout();
        losGehts();
    }
    private void initP2lib() {
        PButton.setHlpImage(GetIcon.getImage("button-help.png", 16, 16));
        P2LibInit.initLib(primaryStage, ProgConst.PROGRAMNAME,
                ProgConst.CSS_FILE, "",
                ProgData.debug, ProgData.duration);
    }
    private void initRootLayout() {
        try {
            addThemeCss(); // damit es für die 2 schon mal stimmt
            progData.mosaicController = new MosaicController();
            scene = new Scene(progData.mosaicController,
                    PGuiSize.getWidth(ProgConfig.SYSTEM_GUI_SIZE),
                    PGuiSize.getHeight(ProgConfig.SYSTEM_GUI_SIZE));

            P2LibInit.addP2LibCssToScene(scene);
            ProgConfig.SYSTEM_DARK_THEME.addListener((u, o, n) -> {
                addThemeCss();
                P2LibInit.addP2LibCssToScene(scene);
                ProgConfig.SYSTEM_THEME_CHANGED.setValue(!ProgConfig.SYSTEM_THEME_CHANGED.get());
            });

            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(e -> {
                e.consume();
                new ProgQuitt().quitt();
            });

            if (!PGuiSize.setPos(ProgConfig.SYSTEM_GUI_SIZE, primaryStage)) {
                primaryStage.centerOnScreen();
            }            primaryStage.show();

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
    private void addThemeCss() {
        if (ProgConfig.SYSTEM_DARK_THEME.get()) {
            P2LibInit.addCssFile(ProgConst.CSS_FILE_DARK_THEME);
        } else {
            P2LibInit.removeCssFile(ProgConst.CSS_FILE_DARK_THEME);
        }
    }

    public void losGehts() {
        PDuration.counterStop(LOG_TEXT_PROGRAMSTART);
        primaryStage.getIcons().add(GetIcon.getImage(ProgConst.P2_ICON_32, ProgConst.P2_ICON_PATH, 32, 32));

        PDuration.onlyPing("Erster Start");
        setTitle();
        ProgConfig.START_GUI_PROJECT_DATA.addListener((observable, oldValue, newValue) -> setTitle());

        PDuration.onlyPing("Gui steht!");
    }

    private void setTitle() {
        if (progData.selectedProjectData == null) {
            primaryStage.setTitle(ProgConst.P2_PROGRAMMNAME);
        } else {
            String prj = progData.selectedProjectData.getName();
            primaryStage.setTitle((prj.isEmpty() ? "" : (prj + " - ")) + " [ " + ProgConst.P2_PROGRAMMNAME + " ]");
        }
    }

}
