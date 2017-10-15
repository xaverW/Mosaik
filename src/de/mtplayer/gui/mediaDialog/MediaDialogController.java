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

package de.mtplayer.gui.mediaDialog;

import de.mtplayer.controller.config.Config;
import de.mtplayer.controller.config.Daten;
import de.mtplayer.controller.data.Icons;
import de.mtplayer.gui.HelpText;
import de.mtplayer.gui.dialog.MTAlert;
import de.mtplayer.gui.dialog.MTDialog;
import de.mtplayer.gui.tools.Listener;
import de.mtplayer.tools.storedFilter.Filter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;

public class MediaDialogController extends MTDialog {

    private final AnchorPane rootPane = new AnchorPane();
    private final VBox vBoxDialog = new VBox();
    private final VBox vBoxCont = new VBox();

    private final TextField txtSuchen = new TextField();


    private final Button btnOk = new Button("Ok");
    private final Button btnHilfe = new Button();
    private final Button btnReset = new Button("");

    private final RadioButton rbMedien = new RadioButton("Mediensammlung");
    private final RadioButton rbAbos = new RadioButton("erledigte Abos");

    private final StackPane stackPane = new StackPane();

    private final MediaDialogMediaPane mediaDialogMediaPane = new MediaDialogMediaPane();
    private final MediaDialogAboPane mediaDialogAboPane = new MediaDialogAboPane();
    private final Daten daten = Daten.getInstance();
    private final String searchStr;


    public MediaDialogController(String searchStr) {
        super(Config.MEDIA_DIALOG_SIZE, "Mediensammlung", true);
        this.searchStr = searchStr;
        txtSuchen.setText(searchStr);

        init(rootPane, true);
    }

    public void close() {
        daten.erledigteAbos.filterdListClearPred();
        daten.mediaDbList.filterdListClearPred();
        super.close();
    }

    private void initPanel() {
        try {
            vBoxDialog.setPadding(new Insets(10));
            vBoxDialog.setSpacing(20);

            vBoxCont.getStyleClass().add("dialog-border");
            vBoxCont.setSpacing(10);
            VBox.setVgrow(vBoxCont, Priority.ALWAYS);

            rootPane.getChildren().addAll(vBoxDialog);
            AnchorPane.setLeftAnchor(vBoxDialog, 0.0);
            AnchorPane.setBottomAnchor(vBoxDialog, 0.0);
            AnchorPane.setRightAnchor(vBoxDialog, 0.0);
            AnchorPane.setTopAnchor(vBoxDialog, 0.0);


            HBox hBox = new HBox();
            hBox.setSpacing(10);
            HBox.setHgrow(txtSuchen, Priority.ALWAYS);
            hBox.getChildren().addAll(txtSuchen, btnReset);
            vBoxCont.getChildren().add(hBox);

            final ToggleGroup group = new ToggleGroup();
            rbMedien.setToggleGroup(group);
            rbAbos.setToggleGroup(group);
            hBox = new HBox();
            hBox.setSpacing(10);
            hBox.getChildren().addAll(rbMedien, rbAbos);
            vBoxCont.getChildren().add(hBox);

            // Stackpane
            mediaDialogMediaPane.setFitToHeight(true);
            mediaDialogMediaPane.setFitToWidth(true);
            mediaDialogAboPane.setFitToHeight(true);
            mediaDialogAboPane.setFitToWidth(true);

            stackPane.getChildren().addAll(mediaDialogMediaPane, mediaDialogAboPane);
            VBox.setVgrow(stackPane, Priority.ALWAYS);
            vBoxCont.getChildren().add(stackPane);
            mediaDialogMediaPane.toFront();

            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.getChildren().addAll(btnHilfe, region, btnOk);

            vBoxDialog.getChildren().addAll(vBoxCont, hBox);
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void make() {
        initPanel();
        mediaDialogMediaPane.make();
        mediaDialogAboPane.make();

        final ToggleGroup tg = new ToggleGroup();
        rbMedien.setToggleGroup(tg);
        rbAbos.setToggleGroup(tg);

        Listener.addListener(new Listener(Listener.EREIGNIS_MEDIA_DB_START, MediaDialogController.class.getSimpleName()) {
            @Override
            public void ping() {
                // neue DB suchen
                txtSuchen.setDisable(true);
            }
        });
        Listener.addListener(new Listener(Listener.EREIGNIS_MEDIA_DB_STOP, MediaDialogController.class.getSimpleName()) {
            @Override
            public void ping() {
                // neue DB liegt vor
                txtSuchen.setDisable(false);
            }
        });

        txtSuchen.textProperty().addListener((observable, oldValue, newValue) -> {
            Filter.checkPattern1(txtSuchen);
            filter();
        });

        txtSuchen.setOnMouseClicked(event -> {
            if (event.getClickCount() > 1) {
                String sel = txtSuchen.getSelectedText();
                System.out.println(sel);
                txtSuchen.setText(sel);
            }
        });

        btnReset.setGraphic(new Icons().ICON_BUTTON_RESET);
        btnReset.setOnAction(a -> txtSuchen.setText(searchStr));
        btnOk.setOnAction(a -> close());
        btnHilfe.setText("");
        btnHilfe.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHilfe.setOnAction(a -> new MTAlert().showHelpAlert("Suche in der Mediensammlung", HelpText.SEARCH_MEDIA_DIALOG));

        rbMedien.setSelected(true);
        rbMedien.setOnAction(a -> {
            mediaDialogMediaPane.toFront();
            filter();
        });
        rbAbos.setOnAction(a -> {
            mediaDialogAboPane.toFront();
            filter();
        });

        filter();
    }

    private void filter() {
        final String searchStr = txtSuchen.getText().toLowerCase().trim();
        if (rbMedien.isSelected()) {
            mediaDialogMediaPane.filter(searchStr);
        } else {
            mediaDialogAboPane.filter(searchStr);
        }
    }
}
