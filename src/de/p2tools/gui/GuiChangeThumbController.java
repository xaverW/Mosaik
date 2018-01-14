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
import de.p2tools.controller.data.thumb.Thumb;
import de.p2tools.controller.data.thumb.ThumbCollection;
import de.p2tools.gui.dialog.MTAlert;
import de.p2tools.gui.tools.Table;
import de.p2tools.mLib.tools.MLAlert;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class GuiChangeThumbController extends AnchorPane {
    ScrollPane scrollPaneTable = new ScrollPane();
    ScrollPane scrollPaneCont = new ScrollPane();
    TableView table = new TableView<>();
    AnchorPane contPane = new AnchorPane();

    ThumbCollection thumbCollection = null;
    Button btnReload = new Button("Liste neu einlesen");
    Button btnDel = new Button("Bilder löschen");

    private final ProgData progData;

    public GuiChangeThumbController() {
        progData = ProgData.getInstance();

        VBox vBox = new VBox();
        AnchorPane.setLeftAnchor(vBox, 0.0);
        AnchorPane.setBottomAnchor(vBox, 0.0);
        AnchorPane.setRightAnchor(vBox, 0.0);
        AnchorPane.setTopAnchor(vBox, 0.0);
        getChildren().addAll(vBox);

        scrollPaneTable.setFitToHeight(true);
        scrollPaneTable.setFitToWidth(true);
        scrollPaneTable.setContent(table);
        scrollPaneCont.setFitToHeight(true);
        scrollPaneCont.setFitToWidth(true);
        scrollPaneCont.setContent(contPane);
        VBox.setVgrow(scrollPaneTable, Priority.ALWAYS);
        vBox.getChildren().addAll(scrollPaneTable, scrollPaneCont);

        initTable();
        initCont();
        selectThumbCollection();
    }

    public void isShown() {
        selectThumbCollection();
    }


    public void saveTable() {
        new Table().saveTable(table, Table.TABLE.CHANGE_THUMB);
    }

    private void selectThumbCollection() {
//        if (thumbCollection != null &&
//                progData.selectedProjectData.getThumbCollection() != null &&
//                thumbCollection.equals(progData.selectedProjectData.getThumbCollection())) {
//            return;
//        }

        table.setItems(null);

        thumbCollection = progData.selectedProjectData.getThumbCollection();

        if (thumbCollection == null) {
            contPane.setDisable(true);
        } else {
            contPane.setDisable(false);
            table.setItems(thumbCollection.getThumbList());
        }
    }

    private void initCont() {
        btnReload.setOnAction(a -> {
            String thumbDir = progData.selectedProjectData.getThumbDirString();
            if (thumbDir.isEmpty()) {
                return;
            }
            progData.worker.readThumbList(thumbCollection, thumbDir);
            table.refresh();
        });

        btnDel.setOnAction(a -> {
            ArrayList<Thumb> thumbs = new ArrayList<>();
            thumbs.addAll(table.getSelectionModel().getSelectedItems());

            if (thumbs.isEmpty()) {
                new MLAlert().showInfoNoSelection();

            } else if (thumbs.size() == 1 &&
                    !new MTAlert().showAlert("Datei Löschen?", "", "Die Datei löschen:\n\n" + thumbs.get(0).getFileName())) {
                return;

            } else if (thumbs.size() > 1 &&
                    !new MTAlert().showAlert("Dateien Löschen?", thumbs.size() + " Dateien löschen",
                            "Sollen die Datei gelöscht werden?")) {
                return;
            }

            for (Thumb thumb : thumbs) {
                if (de.p2tools.mLib.tools.FileUtils.deleteFileNoMsg(thumb.getFileName())) {
                    thumbCollection.getThumbList().remove(thumb);
                } else {
                    break;
                }
            }

            table.refresh();

        });

        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(10));
        hBox.getChildren().addAll(btnReload, btnDel);

        AnchorPane.setTopAnchor(hBox, 5.0);
        AnchorPane.setLeftAnchor(hBox, 5.0);
        AnchorPane.setBottomAnchor(hBox, 5.0);
        AnchorPane.setRightAnchor(hBox, 5.0);
        contPane.getChildren().add(hBox);
    }

    private void initTable() {
        new Table().setTable(table, Table.TABLE.CHANGE_THUMB);
    }

}
