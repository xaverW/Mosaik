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
package de.p2tools.mosaik;

import de.p2tools.mosaik.controller.ProgQuitt;
import de.p2tools.mosaik.controller.ProgStart;
import de.p2tools.mosaik.controller.config.ProgConfig;
import de.p2tools.mosaik.controller.config.ProgConst;
import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.res.GetIcon;
import de.p2tools.p2Lib.guiTools.GuiSize;
import de.p2tools.p2Lib.tools.log.Duration;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class Mosaik extends Application {

    private Stage primaryStage;
    private MosaikController root;

    private static final String TEXT_LINE = "==========================================";
    private static final String LOG_TEXT_STARTPARAMETER_PATTERN = "Startparameter: %s";

    private static final String LOG_TEXT_PROGRAMMSTART = "***Programmstart***";
    private static final String ARGUMENT_PREFIX = "-";

    protected ProgData progData;
    Scene scene = null;

    @Override
    public void init() throws Exception {
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        final Parameters parameters = getParameters();
        final List<String> rawArguments = parameters.getRaw();
        final String pfad = readPfadFromArguments(rawArguments.toArray(new String[]{}));

        Duration.counterStart(LOG_TEXT_PROGRAMMSTART);

        progData = ProgData.getInstance(pfad);
        progData.primaryStage = primaryStage;

        new ProgStart(progData).loadConfigData();

        initRootLayout();
        losGehts();
    }

    private void initRootLayout() {
        try {
            root = new MosaikController();
            progData.mosaikController = root;
            scene = new Scene(root,
                    GuiSize.getWidth(ProgConfig.SYSTEM_GROESSE_GUI),
                    GuiSize.getHeight(ProgConfig.SYSTEM_GROESSE_GUI));

            String css = this.getClass().getResource(ProgConst.CSS_FILE).toExternalForm();
            scene.getStylesheets().add(css);

            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(e -> {
                e.consume();
                new ProgQuitt().beenden(true);
            });

            GuiSize.setPos(ProgConfig.SYSTEM_GROESSE_GUI, primaryStage);
            primaryStage.show();

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void losGehts() {
        Duration.counterStop(LOG_TEXT_PROGRAMMSTART);
        primaryStage.getIcons().add(GetIcon.getImage(ProgConst.ICON_NAME, ProgConst.ICON_PATH, 32, 32));

        Duration.staticPing("Erster Start");
        setTitle();
        ProgConfig.START_GUI_PROJECT_DATA.addListener((observable, oldValue, newValue) -> setTitle());

        Duration.staticPing("Gui steht!");
    }

    private void setTitle() {
        if (progData.selectedProjectData == null) {
            primaryStage.setTitle(ProgConst.P2_PROGRAMMNAME);
        } else {
            String prj = progData.selectedProjectData.getName();
            primaryStage.setTitle((prj.isEmpty() ? "" : (prj + " - ")) + " [ " + ProgConst.P2_PROGRAMMNAME + " ]");
        }
    }

    private String readPfadFromArguments(final String[] aArguments) {
        String pfad;
        if (aArguments == null) {
            pfad = "";
        } else {
            printArguments(aArguments);
            if (aArguments.length > 0) {
                if (!aArguments[0].startsWith(ARGUMENT_PREFIX)) {
                    if (!aArguments[0].endsWith(File.separator)) {
                        aArguments[0] += File.separator;
                    }
                    pfad = aArguments[0];
                } else {
                    pfad = "";
                }
            } else {
                pfad = "";
            }
        }
        return pfad;
    }

    private void printArguments(final String[] aArguments) {
        PLog.sysLog("");
        PLog.sysLog(TEXT_LINE);
        for (final String argument : aArguments) {
            PLog.sysLog(String.format(LOG_TEXT_STARTPARAMETER_PATTERN, argument));
        }
        PLog.sysLog(TEXT_LINE);
        PLog.sysLog("");
    }

}
