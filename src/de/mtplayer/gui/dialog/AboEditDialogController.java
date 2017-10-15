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

package de.mtplayer.gui.dialog;

import de.mtplayer.controller.config.Config;
import de.mtplayer.controller.config.Daten;
import de.mtplayer.controller.data.MTColor;
import de.mtplayer.controller.data.abo.Abo;
import de.mtplayer.controller.data.abo.AboXml;
import de.mtplayer.tools.storedFilter.SelectedFilter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.controlsfx.control.RangeSlider;

import java.util.ArrayList;

public class AboEditDialogController extends MTDialogExtra {

    final GridPane gridPane = new GridPane();
    private final ObservableList<Abo> lAbo;
    private final Abo aboCopy;
    Button btnOk = new Button("Ok");
    Button btnCancel = new Button("Abbrechen");
    ComboBox<String> cbPset = new ComboBox<>();
    ComboBox<String> cbSender = new ComboBox<>();
    ComboBox<String> cbZiel = new ComboBox<>();
    RangeSlider slTime = new RangeSlider();
    Label lblTimeMin = new Label();
    Label lblTimeMax = new Label();
    CheckBox cbxEin = new CheckBox();
    Label[] lbl = new Label[AboXml.MAX_ELEM];
    TextField[] txt = new TextField[AboXml.MAX_ELEM];
    CheckBox[] cbx = new CheckBox[AboXml.MAX_ELEM];
    CheckBox[] cbxForAll = new CheckBox[AboXml.MAX_ELEM];
    final String ALLE = "Alles";
    private boolean ok = false;
    private Daten daten;

    public AboEditDialogController(Daten daten, Abo abo) {
        super(null, Config.ABO_EDIT_DIALOG_GROESSE,
                "Abo ändern", true);

        this.daten = daten;
        lAbo = FXCollections.observableArrayList();
        lAbo.add(abo);

        aboCopy = lAbo.get(0).getCopy();

        initDialog();
    }

    public AboEditDialogController(Daten daten, ObservableList<Abo> lAbo) {
        super(null, Config.ABO_EDIT_DIALOG_GROESSE,
                "Abo ändern", true);
        this.lAbo = lAbo;
        this.daten = daten;
        aboCopy = lAbo.get(0).getCopy();

        initDialog();
    }

    private void initDialog() {
        getVboxCont().getChildren().add(gridPane);

        btnOk.setMaxWidth(Double.MAX_VALUE);
        btnCancel.setMaxWidth(Double.MAX_VALUE);
        getTilePaneOk().getChildren().addAll(btnOk, btnCancel);

        init(getvBoxDialog(), true);
    }

    private void quitt() {

        ok = true; //Änderungen übernehmen
        if (lAbo.size() == 1) {
            lAbo.get(0).aufMichKopieren(aboCopy);

        } else {
            updateAboList();
        }

        close();
    }

    private void updateAboList() {
        for (final Abo abo : lAbo) {

            for (int i = 0; i < cbxForAll.length; ++i) {
                if (cbxForAll[i] == null || !cbxForAll[i].isSelected()) {
                    continue;
                }
                abo.properties[i].setValue(aboCopy.properties[i].getValue());
            }

        }
    }

    public boolean getOk() {
        return ok;
    }

    @Override
    public void make() {
        btnOk.setOnAction(a -> quitt());
        btnOk.disableProperty().bind(aboCopy.nameProperty().isEmpty());
        btnCancel.setOnAction(a -> close());

        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setMinWidth(Control.USE_PREF_SIZE);
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        final ColumnConstraints ccLabel = new ColumnConstraints();
        ccLabel.setFillWidth(true);
        ccLabel.setMinWidth(Region.USE_COMPUTED_SIZE);
        ccLabel.setHgrow(Priority.NEVER);

        final ColumnConstraints ccTxt = new ColumnConstraints();
        ccTxt.setFillWidth(true);
        ccTxt.setMinWidth(Region.USE_COMPUTED_SIZE);
        ccTxt.setHgrow(Priority.ALWAYS);

        final ColumnConstraints ccCbx = new ColumnConstraints();
        ccCbx.setHalignment(HPos.CENTER);
        ccCbx.setFillWidth(true);
        ccCbx.setMinWidth(Region.USE_COMPUTED_SIZE);
        ccCbx.setHgrow(Priority.NEVER);

        gridPane.getColumnConstraints().add(ccLabel);
        gridPane.getColumnConstraints().add(ccTxt);
        gridPane.getColumnConstraints().add(ccCbx);

        if (lAbo.size() > 1) {
            Label l = new Label("bei allen\nändern");
            l.setMinHeight(Region.USE_COMPUTED_SIZE);
            l.setMinWidth(Region.USE_COMPUTED_SIZE);
            l.setPrefHeight(Region.USE_COMPUTED_SIZE);
            l.setPrefWidth(Region.USE_COMPUTED_SIZE);
            l.setWrapText(true);
            GridPane.setHgrow(l, Priority.NEVER);
            gridPane.add(l, 2, 0);
        }

        for (int i = 0; i < AboXml.MAX_ELEM; ++i) {

            if (i == AboXml.ABO_NAME && lAbo.size() > 1) {
                continue;
            }

            initControls(i);

            addLabel(i, i + 1);
            addTextField(i, i + 1);
            if (lAbo.size() > 1) {
                // nur dann brauchts das
                addCheckBox(i, i + 1);
            }
        }

    }

    private void initControls(int i) {
        lbl[i] = new Label(AboXml.COLUMN_NAMES[i] + ":");
        lbl[i].setMinHeight(Region.USE_COMPUTED_SIZE);
        lbl[i].setMinWidth(Region.USE_COMPUTED_SIZE);
        lbl[i].setPrefHeight(Region.USE_COMPUTED_SIZE);
        lbl[i].setPrefWidth(Region.USE_COMPUTED_SIZE);
        GridPane.setHgrow(lbl[i], Priority.NEVER);

        switch (i) {
            case AboXml.ABO_SENDER_EXAKT:
                cbx[i] = new CheckBox("");
                cbx[i].setSelected(aboCopy.isSenderExact());
                break;
            case AboXml.ABO_THEMA_EXAKT:
                cbx[i] = new CheckBox("");
                cbx[i].setSelected(aboCopy.isThemeExact());
                break;
            default:
                txt[i] = new TextField("");
                txt[i].setMinHeight(Region.USE_COMPUTED_SIZE);
                txt[i].setMinWidth(Region.USE_COMPUTED_SIZE);
                txt[i].setPrefHeight(Region.USE_COMPUTED_SIZE);
                txt[i].setPrefWidth(Region.USE_COMPUTED_SIZE);
                txt[i].setText(aboCopy.getStringOf(i));
                GridPane.setHgrow(txt[i], Priority.ALWAYS);
        }

        cbxForAll[i] = new CheckBox();
        cbxForAll[i].setSelected(false);
        GridPane.setHgrow(cbxForAll[i], Priority.NEVER);
    }

    private void addLabel(int i, int grid) {

        switch (i) {
            case AboXml.ABO_SENDER_EXAKT:
            case AboXml.ABO_THEMA_EXAKT:
                lbl[i].setText("  exakt:");
                gridPane.add(lbl[i], 0, grid);
                break;
            case AboXml.ABO_MINDESTDAUER:
                VBox vBox = new VBox();
                vBox.setSpacing(5);
                vBox.setAlignment(Pos.CENTER_LEFT);

                lbl[i].setText("Dauer [Min]:");
                vBox.getChildren().addAll(lbl[i], new Label("min / max"));
                gridPane.add(vBox, 0, grid);
                break;
            case AboXml.ABO_MAXDESTDAUER:
                break;
            default:
                gridPane.add(lbl[i], 0, grid);
                break;
        }

    }

    private void addTextField(int i, int grid) {

        switch (i) {
            case AboXml.ABO_NR:
                txt[i].setEditable(false);
                txt[i].setDisable(true);
                txt[i].setText(aboCopy.getNr() + "");
                gridPane.add(txt[i], 1, grid);
                break;

            case AboXml.ABO_NAME:
                setDefaultTxt(i, grid);
                txt[i].textProperty().addListener((observable, oldValue, newValue) -> {
                    if (txt[i].getText().isEmpty()) {
                        txt[i].setStyle(MTColor.DATEINAME_FEHLER.getCssBackground());
                    } else {
                        txt[i].setStyle("");
                    }
                });
                if (txt[i].getText().isEmpty()) {
                    txt[i].setStyle(MTColor.DATEINAME_FEHLER.getCssBackground());
                } else {
                    txt[i].setStyle("");
                }
                break;

            case AboXml.ABO_SENDER_EXAKT:
            case AboXml.ABO_THEMA_EXAKT:
                gridPane.add(cbx[i], 1, grid);
                cbx[i].selectedProperty().bindBidirectional(aboCopy.properties[i]);
                cbx[i].selectedProperty().addListener((observable, oldValue, newValue) -> {
                    cbxForAll[i].setSelected(true);
                });
                break;

            case AboXml.ABO_DOWN_DATUM:
                txt[i].setEditable(false);
                txt[i].setDisable(true);
                break;

            case AboXml.ABO_ON:
                cbxEin.selectedProperty().bindBidirectional(aboCopy.activeProperty());
                cbxEin.setOnAction(a -> {
                    cbxForAll[i].setSelected(true);
                });
                gridPane.add(cbxEin, 1, grid);
                break;

            case AboXml.ABO_PSET:
                cbPset.setItems(FXCollections.observableArrayList(Daten.setList.getListeAbo().generatePsetList()));
                if (aboCopy.getPset().isEmpty()) {
                    cbPset.getSelectionModel().selectFirst();
                    aboCopy.setPset(cbPset.getSelectionModel().getSelectedItem());
                } else {
                    cbPset.getSelectionModel().select(aboCopy.getPset());
                }
                cbPset.valueProperty().bindBidirectional(aboCopy.psetProperty());
                cbPset.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> cbxForAll[i].setSelected(true));
                gridPane.add(cbPset, 1, grid);
                break;

            case AboXml.ABO_SENDER:
                cbSender.setItems(daten.namesList.getObsAllSender());
                cbSender.setEditable(true);
                cbSender.valueProperty().bindBidirectional(aboCopy.senderProperty());
                cbSender.valueProperty().addListener((observable, oldValue, newValue) -> cbxForAll[i].setSelected(true));
                gridPane.add(cbSender, 1, grid);
                break;

            case AboXml.ABO_ZIELPFAD:
                ArrayList<String> pfade = daten.aboList.getPfade();
                if (!pfade.contains(aboCopy.getDest())) {
                    pfade.add(0, aboCopy.getDest());
                }
                cbZiel.setMaxWidth(Double.MAX_VALUE);
                cbZiel.setItems(FXCollections.observableArrayList(pfade));
                cbZiel.setEditable(true);
                cbZiel.valueProperty().bindBidirectional(aboCopy.destProperty());
                cbZiel.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> cbxForAll[i].setSelected(true));
                gridPane.add(cbZiel, 1, grid);
                break;

            case AboXml.ABO_MINDESTDAUER:
                initDur();
                HBox h1 = new HBox();
                HBox h2 = new HBox();
                h1.getChildren().add(lblTimeMin);
                HBox.setHgrow(h1, Priority.ALWAYS);
                h2.getChildren().addAll(h1, lblTimeMax);

                VBox vBox = new VBox();
                vBox.setSpacing(5);
                vBox.getChildren().addAll(slTime, h2);
                slTime.setShowTickLabels(false);
                gridPane.add(vBox, 1, grid);
                break;

            case AboXml.ABO_MAXDESTDAUER:
                break;

            default:
                setDefaultTxt(i, grid);
                break;
        }
    }

    private void setDefaultTxt(int i, int grid) {
        txt[i].textProperty().bindBidirectional(aboCopy.properties[i]);
        txt[i].textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                cbxForAll[i].setSelected(true);
            }
        });
        gridPane.add(txt[i], 1, grid);

    }

    private void addCheckBox(int i, int grid) {

        switch (i) {
            case AboXml.ABO_ON:
            case AboXml.ABO_SENDER:
            case AboXml.ABO_SENDER_EXAKT:
            case AboXml.ABO_THEMA:
            case AboXml.ABO_THEMA_EXAKT:
            case AboXml.ABO_TITEL:
            case AboXml.ABO_THEMA_TITEL:
            case AboXml.ABO_IRGENDWO:
            case AboXml.ABO_MINDESTDAUER:
            case AboXml.ABO_ZIELPFAD:
            case AboXml.ABO_PSET:
                gridPane.add(cbxForAll[i], 2, grid);
        }
    }

    private void initDur() {
        slTime.setMin(0);
        slTime.setMax(SelectedFilter.FILTER_DURATIION_MAX_MIN);
        slTime.setShowTickLabels(true);
        slTime.setMinorTickCount(9);
        slTime.setMajorTickUnit(50);
        slTime.setBlockIncrement(10);
        slTime.setSnapToTicks(true);

        // hightvalue
        slTime.highValueProperty().bindBidirectional(aboCopy.maxProperty());
        slTime.highValueProperty().addListener(l -> {
            setLabelSlider();
            cbxForAll[AboXml.ABO_MAXDESTDAUER].setSelected(true);
        });

        // lowvalue
        slTime.lowValueProperty().bindBidirectional(aboCopy.minProperty());
        slTime.lowValueProperty().addListener(l -> {
            setLabelSlider();
            cbxForAll[AboXml.ABO_MINDESTDAUER].setSelected(true);
        });

        setLabelSlider();
    }

    private void setLabelSlider() {
        int i;
        i = (int) slTime.getLowValue();
        lblTimeMin.setText(i == 0 ? ALLE : i + "");

        i = (int) slTime.getHighValue();
        lblTimeMax.setText(i == SelectedFilter.FILTER_DURATIION_MAX_MIN ? ALLE : i + "");
    }
}
