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

package de.mtplayer.controller.data.download;

import de.mtplayer.controller.data.Data;
import de.mtplayer.mLib.tools.Datum;
import javafx.beans.property.*;

public class DownloadProps extends DownloadXml {

    private final IntegerProperty nr = new SimpleIntegerProperty(0);
    private final StringProperty sender = new SimpleStringProperty("");
    private final StringProperty thema = new SimpleStringProperty("");
    private final StringProperty titel = new SimpleStringProperty("");

    public final Property[] properties = {nr,   sender, thema, titel};



    public int getNr() {
        return nr.get();
    }

    public IntegerProperty nrProperty() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr.set(nr);
    }


    public String getSender() {
        return sender.get();
    }

    public StringProperty senderProperty() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender.set(sender);
    }

    public String getThema() {
        return thema.get();
    }

    public StringProperty themaProperty() {
        return thema;
    }

    public void setThema(String thema) {
        this.thema.set(thema);
    }

    public String getTitel() {
        return titel.get();
    }

    public StringProperty titelProperty() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel.set(titel);
    }


    public void setPropsFromXml() {

        setSender(arr[DOWNLOAD_SENDER]);
        setThema(arr[DOWNLOAD_THEMA]);
        setTitel(arr[DOWNLOAD_TITEL]);

    }


    public void setXmlFromProps() {
        arr[DOWNLOAD_SENDER] = getSender();
        arr[DOWNLOAD_THEMA] = getThema();
        arr[DOWNLOAD_TITEL] = getTitel();
    }


    public int compareTo(DownloadProps arg0) {
        return Data.sorter.compare(getSender(), arg0.getSender());
    }

}
