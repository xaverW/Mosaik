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
import de.p2tools.controller.ProgStart;
import de.p2tools.controller.config.Config;
import de.p2tools.controller.config.Const;
import de.p2tools.controller.config.Daten;
import de.p2tools.gui.tools.GuiSize;
import de.p2tools.gui.tools.Listener;
import de.p2tools.mLib.tools.Duration;
import de.p2tools.mLib.tools.SysMsg;
import de.p2tools.res.GetIcon;
import de.p2tools.tools.update.CheckUpdate;
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
    private static final String ICON_NAME = "Mosaik.png";
    private static final String ICON_PATH = "/de/p2tools/res/";
    private static final int ICON_WIDTH = 58;
    private static final int ICON_HEIGHT = 58;

    private static final String LOG_TEXT_PROGRAMMSTART = "***Programmstart***";
    private static final String ARGUMENT_PREFIX = "-";
    private static final String TITLE_TEXT_PROGRAMMVERSION_IST_AKTUELL = "Programmversion ist aktuell";
    private static final String TITLE_TEXT_EIN_PROGRAMMUPDATE_IST_VERFUEGBAR = "Ein Programmupdate ist verfügbar";
    private static final String LOG_TEXT_CHECK_UPDATE = "CheckUpdate";

    protected Daten daten;
    ProgStart progStart;
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
        daten = Daten.getInstance(pfad);
        daten.primaryStage = primaryStage;
        progStart = new ProgStart(daten);

        loadData();
        initRootLayout();
        losGehts();
    }

    private void initRootLayout() {
        try {
            root = new MosaikController();
            daten.mosaikController = root;
            scene = new Scene(root,
                    GuiSize.getWidth(Config.SYSTEM_GROESSE_GUI),
                    GuiSize.getHeight(Config.SYSTEM_GROESSE_GUI));

            String css = this.getClass().getResource(Const.CSS_FILE).toExternalForm();
            scene.getStylesheets().add(css);

            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(e -> {
                e.consume();
                new ProgQuitt().beenden(true, false);
            });

            GuiSize.setPos(Config.SYSTEM_GROESSE_GUI, primaryStage);
            primaryStage.show();

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void losGehts() {
        Duration.counterStop(LOG_TEXT_PROGRAMMSTART);
        primaryStage.getIcons().add(GetIcon.getImage(ICON_NAME, ICON_PATH, ICON_WIDTH, ICON_HEIGHT));

        progStart.startMeldungen();

        Duration.staticPing("Erster Start");
        setOrgTitel();
        initProg();

        addListener();

        Duration.staticPing("Gui steht!");
        progStart.loadDataProgStart();
    }

    private void loadData() {

        if (!progStart.allesLaden()) {

            // konnte nicht geladen werden
            Duration.staticPing("Erster Start");

            Config.loadSystemParameter();
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
        SysMsg.sysMsg("");
        SysMsg.sysMsg(TEXT_LINE);
        for (final String argument : aArguments) {
            SysMsg.sysMsg(String.format(LOG_TEXT_STARTPARAMETER_PATTERN, argument));
        }
        SysMsg.sysMsg(TEXT_LINE);
        SysMsg.sysMsg("");
    }

    private void addListener() {
        Listener.addListener(new Listener(Listener.EREIGNIS_GUI_ORG_TITEL, Mosaik.class.getSimpleName()) {
            @Override
            public void ping() {
                setOrgTitel();
            }
        });
        Listener.addListener(new Listener(Listener.EREIGNIS_GUI_PROGRAMM_AKTUELL, Mosaik.class.getSimpleName()) {
            @Override
            public void ping() {
                primaryStage.setTitle(TITLE_TEXT_PROGRAMMVERSION_IST_AKTUELL);
            }
        });
        Listener.addListener(new Listener(Listener.EREIGNIS_GUI_UPDATE_VERFUEGBAR, Mosaik.class.getSimpleName()) {
            @Override
            public void ping() {
                primaryStage.setTitle(TITLE_TEXT_EIN_PROGRAMMUPDATE_IST_VERFUEGBAR);
            }
        });
    }

    private void setOrgTitel() {
        primaryStage.setTitle(Const.PROGRAMMNAME);
    }

    private void initProg() {
                // Prüfen obs ein Programmupdate gibt
                Duration.staticPing(LOG_TEXT_CHECK_UPDATE);
                new CheckUpdate(daten).checkProgUpdate();
    }
}
