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
import de.p2tools.mosaic.res.GetIcon;
import de.p2tools.p2Lib.PInit;
import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.tools.log.PDuration;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Mosaic extends Application {

    private Stage primaryStage;
    private MosaicController root;
    private static final String LOG_TEXT_PROGRAMMSTART = "***Programmstart***";

    protected ProgData progData;
    Scene scene = null;

    @Override
    public void init() throws Exception {
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        PDuration.counterStart(LOG_TEXT_PROGRAMMSTART);
        progData = ProgData.getInstance();
        progData.primaryStage = primaryStage;
        new ProgStart(progData).loadConfigData();

        initRootLayout();
        losGehts();
    }

    private void initRootLayout() {
        try {
            root = new MosaicController();
            progData.mosaicController = root;
            scene = new Scene(root,
                    PGuiSize.getWidth(ProgConfig.SYSTEM_GROESSE_GUI),
                    PGuiSize.getHeight(ProgConfig.SYSTEM_GROESSE_GUI));

            String css = this.getClass().getResource(ProgConst.CSS_FILE).toExternalForm();
            scene.getStylesheets().add(css);

            PInit.addP2LibCss(scene);

            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(e -> {
                e.consume();
                new ProgQuitt().quitt();
            });

            PGuiSize.setPos(ProgConfig.SYSTEM_GROESSE_GUI, primaryStage);
            primaryStage.show();

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void losGehts() {
        PDuration.counterStop(LOG_TEXT_PROGRAMMSTART);
        primaryStage.getIcons().add(GetIcon.getImage(ProgConst.ICON_NAME, ProgConst.ICON_PATH, 32, 32));

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
