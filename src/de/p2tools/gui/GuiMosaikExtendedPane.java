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
import de.p2tools.controller.data.mosaikData.MosaikData;
import de.p2tools.gui.dialog.MTAlert;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.controlsfx.control.ToggleSwitch;

public class GuiMosaikExtendedPane extends AnchorPane {

    private final ProgData progData;
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox contPane = new VBox();

    private final RadioButton rbThumb = new RadioButton("Miniaturbilder für das Mosaik verwenden");
    private final RadioButton rbSelf = new RadioButton("Vorlagenfoto für das Mosaik verwenden");
    private final ToggleGroup tg = new ToggleGroup();
    private final ToggleSwitch tglBlackWhite = new ToggleSwitch("Schwarz/Weiß Mosaik erstellen");

    MosaikData mosaikData = null;

    public GuiMosaikExtendedPane() {
        progData = ProgData.getInstance();

        if (progData.selectedProjectData != null) {
            mosaikData = progData.selectedProjectData.getMosaikData();
        }

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(contPane);

        AnchorPane.setLeftAnchor(scrollPane, 0.0);
//        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setTopAnchor(scrollPane, 0.0);

        scrollPane.getStyleClass().add("layoutBackground");
        
        initCont();
        bind();
        getChildren().addAll(scrollPane);
    }

    public void isShown() {
        if (progData.selectedProjectData == null) {
            contPane.setDisable(true);
            return;
        }

        contPane.setDisable(false);

        if (!mosaikData.equals(progData.selectedProjectData.getMosaikData())) {
            unbind();
            mosaikData = progData.selectedProjectData.getMosaikData();

            bind();
        }
    }


    private void initCont() {
        // make Grid
        final Button btnHelpSrcImage = new Button("");
        btnHelpSrcImage.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSrcImage.setOnAction(a -> new MTAlert().showHelpAlert("Fotos für das Mosaik", HelpText.FILEMANAGER));

        final Button btnHelpBlackWhite = new Button("");
        btnHelpBlackWhite.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpBlackWhite.setOnAction(a -> new MTAlert().showHelpAlert("Schwarz/Weiß Mosaik", HelpText.FILEMANAGER));

        rbThumb.setToggleGroup(tg);
        rbSelf.setToggleGroup(tg);

        rbThumb.setOnAction(e -> mosaikData.setThumbSrc(MosaikData.THUMB_SRC.THUMBS.toString()));
        rbSelf.setOnAction(e -> mosaikData.setThumbSrc(MosaikData.THUMB_SRC.SRC_FOTO.toString()));

        int row = 0;
        GridPane gridPaneDest = new GridPane();
        gridPaneDest.setPadding(new Insets(0));
        gridPaneDest.setVgap(10);
        gridPaneDest.setHgap(10);

        GridPane.setHgrow(rbThumb, Priority.ALWAYS);
        rbThumb.setMaxWidth(Double.MAX_VALUE);
        rbSelf.setMaxWidth(Double.MAX_VALUE);

        Label lbl = new Label("Fotos aus denen das Mosaik gebaut wird festlegen");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lbl, Priority.ALWAYS);
        gridPaneDest.add(lbl, 0, row, 2, 1);
        gridPaneDest.add(rbThumb, 0, ++row);
        gridPaneDest.add(btnHelpSrcImage, 1, row);
        gridPaneDest.add(rbSelf, 0, ++row);

        lbl = new Label("Mosaik aus Schwarz/Weiß Fotos bauen");
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lbl, Priority.ALWAYS);
        gridPaneDest.add(new Label(" "), 1, ++row);
        gridPaneDest.add(lbl, 0, ++row, 2, 1);
        gridPaneDest.add(tglBlackWhite, 0, ++row);
        gridPaneDest.add(btnHelpBlackWhite, 1, row);

        // import all
        contPane.setSpacing(25);
        contPane.setPadding(new Insets(10));
        contPane.getChildren().addAll(gridPaneDest);


    }

    private void unbind() {
        if (mosaikData == null) {
            return;
        }

        mosaikData.blackWhiteProperty().unbind();

    }

    private void bind() {
        if (mosaikData == null) {
            return;
        }

        if (mosaikData.getThumbSrc().equals(MosaikData.THUMB_SRC.THUMBS.toString())) {
            rbThumb.setSelected(true);
        } else {
            rbSelf.setSelected(true);
        }

        tglBlackWhite.setSelected(mosaikData.isBlackWhite());
        mosaikData.blackWhiteProperty().bind(tglBlackWhite.selectedProperty());


    }
}
