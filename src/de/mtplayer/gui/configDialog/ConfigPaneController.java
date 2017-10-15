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

package de.mtplayer.gui.configDialog;

import de.mtplayer.controller.config.Config;
import de.mtplayer.controller.config.Daten;
import de.mtplayer.controller.data.Icons;
import de.mtplayer.gui.HelpText;
import de.mtplayer.gui.dialog.MTAlert;
import de.mtplayer.mLib.tools.DirFileChooser;
import de.mtplayer.tools.update.ProgrammUpdateSuchen;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.controlsfx.control.ToggleSwitch;

import java.util.ArrayList;
import java.util.Collection;

public class ConfigPaneController extends AnchorPane {

    private final Daten daten;
    VBox noaccordion = new VBox();
    private final Accordion accordion = new Accordion();
    private final HBox hBox = new HBox(0);
    private final CheckBox cbxAccordion = new CheckBox("");

    BooleanProperty accordionProp = Config.CONFIG_DIALOG_ACCORDION.getBooleanProperty();
    BooleanProperty updateProp = Config.SYSTEM_UPDATE_SEARCH.getBooleanProperty();

    ScrollPane scrollPane = new ScrollPane();

    public ConfigPaneController() {
        daten = Daten.getInstance();

        cbxAccordion.selectedProperty().bindBidirectional(accordionProp);
        cbxAccordion.selectedProperty().addListener((observable, oldValue, newValue) -> setAccordion());

        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        hBox.getChildren().addAll(cbxAccordion, scrollPane);
        getChildren().addAll(hBox);

        accordion.setPadding(new Insets(1));
        noaccordion.setPadding(new Insets(1));
        noaccordion.setSpacing(1);

        AnchorPane.setLeftAnchor(hBox, 10.0);
        AnchorPane.setBottomAnchor(hBox, 10.0);
        AnchorPane.setRightAnchor(hBox, 10.0);
        AnchorPane.setTopAnchor(hBox, 10.0);

        setAccordion();
    }

    private void setAccordion() {
        if (cbxAccordion.isSelected()) {
            noaccordion.getChildren().clear();
            accordion.getPanes().addAll(createPanes());
            scrollPane.setContent(accordion);
        } else {
            accordion.getPanes().clear();
            noaccordion.getChildren().addAll(createPanes());
            scrollPane.setContent(noaccordion);
        }
    }

    private Collection<TitledPane> createPanes() {
        Collection<TitledPane> result = new ArrayList<TitledPane>();
        makeUpdate(result);
        return result;
    }


    private void makeUpdate(Collection<TitledPane> result) {
        final VBox vBox = new VBox();
        vBox.setFillWidth(true);
        TitledPane tpConfig = new TitledPane("Programmupdate", vBox);
        result.add(tpConfig);

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        vBox.getChildren().add(gridPane);

        //einmal am Tag Update suchen
        final ToggleSwitch tglSearch = new ToggleSwitch("einmal am Tag nach einer neuen Programmversion suchen");
        tglSearch.selectedProperty().bindBidirectional(updateProp);
        gridPane.add(tglSearch, 0, 0);

        final Button btnHelp = new Button("");
        javafx.scene.image.ImageView i1 = new Icons().ICON_BUTTON_HELP;
        btnHelp.setGraphic(i1);
        btnHelp.setOnAction(a -> new MTAlert().showHelpAlert("Programmupdate suchen",
                "Beim Programmstart wird geprüft, ob es eine neue Version des Programms gibt. " +
                        "Ist eine aktualisierte Version vorhanden, wird das dann gemeldet.\n" +
                        "Das Programm wird aber nicht ungefragt ersetzt."));
        GridPane.setHalignment(btnHelp, HPos.RIGHT);
        gridPane.add(btnHelp, 1, 0);

        final ColumnConstraints ccTxt = new ColumnConstraints();
        ccTxt.setFillWidth(true);
        ccTxt.setMinWidth(Region.USE_COMPUTED_SIZE);
        ccTxt.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(new ColumnConstraints(), ccTxt);

        //jetzt suchen
        Button btnNow = new Button("Jetzt suchen");
        btnNow.setMaxWidth(Double.MAX_VALUE);
        btnNow.setOnAction(event -> new ProgrammUpdateSuchen().checkVersion(true /* bei aktuell anzeigen */,
                false /* Hinweis */,
                true /* hinweiseAlleAnzeigen */));

        Button btnInfo = new Button("Programminfos anzeigen");
        btnInfo.setMaxWidth(Double.MAX_VALUE);
        btnInfo.setOnAction(event -> new ProgrammUpdateSuchen().checkVersion(false /* bei aktuell anzeigen */,
                true /* Hinweis */,
                true /* hinweiseAlleAnzeigen */));

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10, 0, 0, 0));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(btnNow, btnInfo);
        gridPane.add(hBox, 0, 1);
    }

}
