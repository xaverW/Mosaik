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
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.text.DecimalFormat;

public class MosaikGuiController extends AnchorPane {

    private final ProgData progData;
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox vBoxCont = new VBox();
    private final Label lblSrc = new Label("Foto zum Erstellen des Mosaik");
    private final TextField txtSrc = new TextField();
    private final Button btnSrc = new Button("");
    private final Label lblDesst = new Label("Mosaik speichern");
    private final TextField txtDest = new TextField();
    private final TextField txtNumThumb = new TextField("25");
    private final Button btnDest = new Button("");
    private final Button btnCreate = new Button("Mosaik erstellen");

    CreateMosaik createMosaik;

    public MosaikGuiController() {
        progData = ProgData.getInstance();
        createMosaik = progData.createMosaik;

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
        // SRC
        btnSrc.setOnAction(event -> {
            DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtSrc);
        });
        btnSrc.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        final Button btnHelpSrc = new Button("");
        btnHelpSrc.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSrc.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.FILEMANAGER));

        txtSrc.textProperty().bindBidirectional(createMosaik.fotoSrcProperty());
        txtSrc.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(txtSrc, Priority.ALWAYS);

        HBox hBoxSrc = new HBox(10);
        hBoxSrc.getChildren().addAll(txtSrc, btnSrc, btnHelpSrc);


        // DEST
        btnDest.setOnAction(event -> {
            DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtDest);
        });
        btnDest.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        final Button btnHelpDest = new Button("");
        btnHelpDest.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpDest.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.FILEMANAGER));

        txtDest.textProperty().bindBidirectional(createMosaik.fotoDestProperty());
        txtDest.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(txtDest, Priority.ALWAYS);

        HBox hBoxDest = new HBox(10);
        hBoxDest.getChildren().addAll(txtDest, btnDest, btnHelpDest);


        // Anzahl Thumbs
        Label lblNum = new Label("Anzahl Thumbs:");

        final StringProperty sp = txtNumThumb.textProperty();
        final IntegerProperty ip = createMosaik.numberThumbsWidthProperty();
        try {
            sp.setValue(String.valueOf(ip.get()));
        } catch (final Exception ex) {
            sp.setValue("25");
            ip.setValue(25);
        }
        final StringConverter<Number> converterNum = new NumberStringConverter(new DecimalFormat("##"));
        Bindings.bindBidirectional(sp, ip, converterNum);

        HBox hBoxNum = new HBox(10);
        hBoxNum.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(txtNumThumb, Priority.ALWAYS);
        hBoxNum.getChildren().addAll(lblNum, txtNumThumb);


        // Thumbsize
        final Button btnHelpSlider = new Button("");
        btnHelpSlider.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSlider.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.FILEMANAGER));

        Slider slider = new Slider();
        slider.setMin(5);
        slider.setMax(25);

        slider.setValue(createMosaik.getThumbSize() / 10);

        IntegerProperty iProp = new SimpleIntegerProperty();
        iProp.bind(slider.valueProperty());

        NumberBinding nb = Bindings.multiply(iProp, 10);
        createMosaik.thumbSizeProperty().bind(nb);

        Label lblSlider = new Label("");
        lblSlider.textProperty().bind(
                Bindings.format("%d", createMosaik.thumbSizeProperty())
        );

        HBox.setHgrow(slider, Priority.ALWAYS);
        HBox hBoxSlider = new HBox(10);
        hBoxSlider.setAlignment(Pos.CENTER_LEFT);
        hBoxSlider.getChildren().addAll(slider, lblSlider, btnHelpSlider);

        // import all
        vBoxCont.setSpacing(10);
        vBoxCont.setPadding(new Insets(10));
        vBoxCont.getChildren().addAll(lblSrc, hBoxSrc, lblDesst, hBoxDest, hBoxSlider, hBoxNum, btnCreate);


        btnCreate.setOnAction(a -> {
            if (!txtSrc.getText().isEmpty() && !txtDest.getText().isEmpty()) {
                createMosaik.setThumbCollectionId(progData.selectedThumbCollection.getId());
                new MosaikErstellen(createMosaik).erstellen();
            }
        });

    }
}
