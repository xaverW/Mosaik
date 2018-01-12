/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * http://zdfmediathk.sourceforge.net/
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


package de.p2tools.controller.worker.genWallpaper;

import de.p2tools.controller.RunEvent;
import de.p2tools.controller.RunListener;
import de.p2tools.controller.data.thumb.Thumb;
import de.p2tools.controller.data.thumb.ThumbCollection;
import de.p2tools.controller.data.wallpaperData.WallpaperData;
import de.p2tools.controller.worker.genThumbList.ScaleImage;
import de.p2tools.mLib.tools.Duration;
import de.p2tools.mLib.tools.Log;
import de.p2tools.mLib.tools.MLAlert;

import javax.imageio.*;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.event.EventListenerList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.Locale;

public class GenWallpaper {
    private EventListenerList listeners = new EventListenerList();
    private ThumbCollection thumbCollection;
    private String dest;
    private int numThumbWidth;
    private int thumbSize;


    public GenWallpaper() {
    }

    /**
     * @param listener
     */
    public void addAdListener(RunListener listener) {
        listeners.add(RunListener.class, listener);
    }

    public void create(ThumbCollection thumbCollection, WallpaperData wallpaperData) {
        this.thumbCollection = thumbCollection;
        this.dest = wallpaperData.getFotoDest();
        this.numThumbWidth = wallpaperData.getNumberThumbsWidth();
        this.thumbSize = wallpaperData.getThumbSize();

        if (dest.isEmpty()) {
            Log.errorLog(945120364, "Keine Zieldatei angegeben!");
            return;
        }

        if (new File(dest).exists() &&
                !new MLAlert().showAlert_yes_no("Ziel existiert", dest,
                        "Soll die bereits vorhandene Datei überschrieben werden?").equals(MLAlert.BUTTON.YES)) {
            return;
        }

        final int thumbListSize = thumbCollection.getThumbList().getSize();
        if (thumbListSize <= 0) {
            return;
        }

        Tus tus = new Tus();
        Thread startenThread = new Thread(tus);
        startenThread.setDaemon(true);
        startenThread.start();
    }

    private class Tus implements Runnable {

        public synchronized void run() {

            Duration.counterStart("Mosaik erstellen");
            try {
                final int thumbListSize = thumbCollection.getThumbList().getSize();
                notifyEvent(thumbListSize, 0, "");

                int height = (thumbListSize / numThumbWidth) * thumbSize;
                int width = numThumbWidth * thumbSize;

                if (thumbListSize % numThumbWidth != 0) {
                    height += thumbSize;
                }

                BufferedImage imgOut = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                int hh = 0, ww = 0;
                for (int i = 0; i < thumbListSize; ++i) {
                    notifyEvent(thumbListSize, i, "");

                    Thumb thumb = thumbCollection.getThumbList().get(i);
                    BufferedImage img = getBufferedImage(new File(thumb.getFileName()));

                    if (img.getWidth() != thumbSize) {
                        img = ScaleImage.scaleBufferedImage(img, thumbSize, thumbSize);
                    }

                    imgOut.getRaster().setRect(ww * thumbSize, hh * thumbSize, img.getData());

                    ++ww;
                    if (ww >= numThumbWidth) {
                        ww = 0;
                        ++hh;
                    }

                }

                notifyEvent(thumbListSize, thumbListSize, "Datei schreiben");
                writeImage(imgOut);
                notifyEvent(0, 0, "");

            } catch (Exception ex) {
                Log.errorLog(654102025, ex);
            }
        }
    }

    private BufferedImage getBufferedImage(File source) {
        BufferedImage img = null;
        ImageReader reader = getReader(source);
        try {
            img = reader.read(0);
        } catch (Exception e) {
        }
        reader.dispose();
        return img;
    }

    private ImageReader getReader(File source) {
        ImageReader reader = null;
        try {
            Iterator readers = ImageIO.getImageReadersByFormatName("jpeg");
            reader = (ImageReader) readers.next();
            ImageInputStream iis = ImageIO.createImageInputStream(source);
            reader.setInput(iis, true);
        } catch (Exception e) {
        }
        return reader;
    }

    private void writeImage(BufferedImage img) {
        ImageOutputStream ios = null;
        try {
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            ios = ImageIO.createImageOutputStream(new File(dest));
            writer.setOutput(ios);
            ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());
            iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwparam.setCompressionQuality(1f);
            writer.write(null, new IIOImage(img, null, null), iwparam);
            ios.flush();
            writer.dispose();
        } catch (Exception e) {
        } finally {
            try {
                ios.close();
            } catch (Exception ex) {
            }
        }
    }

    private void notifyEvent(int max, int progress, String text) {
        RunEvent event;
        event = new RunEvent(this, progress, max, text);
        for (RunListener l : listeners.getListeners(RunListener.class)) {
            l.notify(event);
        }
    }
}
