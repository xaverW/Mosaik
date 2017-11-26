/*
 *    Copyright (C) 2008
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.p2tools.controller;

import java.util.EventObject;

public class FotoEvent extends EventObject {
    // meldet eine Änderung
    private int progress;
    private int max;
    private int threads;
    private String text;

    public FotoEvent(Object source, int progress, int max, String text, int threads) {
        super(source);
        this.progress = progress;
        this.max = max;
        this.text = text;
        this.threads = threads;
    }

    public String getText() {
        return text;
    }

    public int getProgress() {
        return progress;
    }

    public int getMax() {
        return max;
    }

    public int getThreads() {
        return threads;
    }

    public boolean nixLos() {
        return max == 0;
    }

}
