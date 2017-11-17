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

import de.p2tools.controller.config.ProgData;
import de.p2tools.controller.data.Icons;
import de.p2tools.controller.data.createMosaik.CreateMosaik;
import de.p2tools.controller.genMosaik.MosaikErstellen;
import de.p2tools.gui.dialog.MTAlert;
import de.p2tools.mLib.tools.DirFileChooser;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MosaikGuiController extends AnchorPane {

    private final ProgData progData;
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox vBoxCont = new VBox();
    Label lblSrc = new Label("Foto zum Erstellen des Mosaik");
    TextField txtSrc = new TextField();
    Button btnSrc = new Button("");
    Label lblDesst = new Label("Mosaik speichern");
    TextField txtDest = new TextField();
    Button btnDest = new Button("");
    Button btnCreate = new Button("Mosaik erstellen");
    StringProperty srcProp;
    StringProperty destProp;
    CreateMosaik createMosaik;

    public MosaikGuiController() {
        progData = ProgData.getInstance();
        if (progData.createMosaikList.isEmpty()) {
            progData.createMosaikList.add(new CreateMosaik());
        }

        createMosaik = progData.createMosaikList.get(0);

        srcProp = createMosaik.fotoSrcProperty();
        destProp = createMosaik.fotoDestProperty();

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(vBoxCont);

        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setTopAnchor(scrollPane, 0.0);

        initCont();
        getChildren().addAll(scrollPane);
    }

    public void isShown() {

    }


    private void initCont() {
        btnSrc.setOnAction(event -> {
            DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtSrc);
        });
        btnSrc.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        final Button btnHelpSrc = new Button("");
        btnHelpSrc.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSrc.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.FILEMANAGER));

        HBox hBoxSrc = new HBox(10);
        hBoxSrc.getChildren().addAll(txtSrc, btnSrc, btnHelpSrc);

        btnDest.setOnAction(event -> {
            DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtDest);
        });
        btnDest.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        final Button btnHelpDest = new Button("");
        btnHelpDest.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpDest.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.FILEMANAGER));

        btnCreate.setOnAction(a -> {
            if (!txtSrc.getText().isEmpty() && !txtDest.getText().isEmpty()) {
                new MosaikErstellen(createMosaik).erstellen();
            }
        });

        txtSrc.textProperty().bindBidirectional(srcProp);
        txtSrc.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(txtSrc, Priority.ALWAYS);

        txtDest.textProperty().bindBidirectional(destProp);
        txtDest.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(txtDest, Priority.ALWAYS);
        HBox hBoxDest = new HBox(10);
        hBoxDest.getChildren().addAll(txtDest, btnDest, btnHelpDest);

        vBoxCont.setSpacing(10);
        vBoxCont.setPadding(new Insets(10));
        vBoxCont.getChildren().addAll(lblSrc, hBoxSrc, lblDesst, hBoxDest, btnCreate);

    }

}
