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


package de.p2tools.mosaik.controller.worker.genWallpaper;

import de.p2tools.mosaik.controller.RunEvent;
import de.p2tools.mosaik.controller.RunListener;
import de.p2tools.mosaik.controller.data.thumb.Thumb;
import de.p2tools.mosaik.controller.data.thumb.ThumbCollection;
import de.p2tools.mosaik.controller.data.wallpaperData.WallpaperData;
import de.p2tools.mosaik.controller.worker.genThumbList.ScaleImage;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.image.ImgFile;
import de.p2tools.p2Lib.tools.Duration;
import de.p2tools.p2Lib.tools.Log;
import javafx.application.Platform;

import javax.imageio.*;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.event.EventListenerList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Locale;

public class GenWallpaper {
    private EventListenerList listeners = new EventListenerList();
    private ThumbCollection thumbCollection;
    private String destDir;
    private String destName;
    private boolean stopAll = false;
    private int numThumbWidth;
    private int thumbSize;
    private Path destPath = null;


    public GenWallpaper() {
    }

    /**
     * @param listener
     */
    public void addAdListener(RunListener listener) {
        listeners.add(RunListener.class, listener);
    }

    public void setStop() {
        stopAll = true;
    }

    public void create(ThumbCollection thumbCollection, WallpaperData wallpaperData) {
        this.thumbCollection = thumbCollection;
        this.destDir = wallpaperData.getFotoDestDir();
        this.destName = wallpaperData.getFotoDestName();
        this.numThumbWidth = wallpaperData.getNumberThumbsWidth();
        this.thumbSize = wallpaperData.getThumbSize();

        if (destDir.isEmpty() || destName.isEmpty()) {
            Log.errorLog(945120364, "Keine Zieldatei angegeben!");
            return;
        }

        if (wallpaperData.getFormat().equals(ImgFile.IMAGE_FORMAT_PNG) &&
                !destName.endsWith("." + ImgFile.IMAGE_FORMAT_PNG)) {
            destName += "." + ImgFile.IMAGE_FORMAT_PNG;

        } else if (!destName.endsWith("." + ImgFile.IMAGE_FORMAT_JPG)) {
            destName += "." + ImgFile.IMAGE_FORMAT_JPG;
        }


        destPath = Paths.get(destDir, destName);

        if (destPath.toFile().exists() &&
                !new PAlert().showAlert_yes_no("Ziel existiert", destPath.toString(),
                        "Soll die bereits vorhandene Datei überschrieben werden?").equals(PAlert.BUTTON.YES)) {
            return;
        }

        final int thumbListSize = thumbCollection.getThumbList().getSize();
        if (thumbListSize <= 0) {
            return;
        }

        stopAll = false;
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

                if (thumbListSize < numThumbWidth) {
                    numThumbWidth = thumbListSize;
                }

                int height = (thumbListSize / numThumbWidth) * thumbSize;
                int width = numThumbWidth * thumbSize;
                if (thumbListSize % numThumbWidth != 0) {
                    height += thumbSize;
                }
                int hh = 0, ww = 0;
                boolean lineEnd = false;

                BufferedImage imgOut = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                for (int i = 0; i < thumbListSize && !stopAll; ++i) {
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

                        if (lineEnd) {
                            break;
                        }
                    }

                    if (i == thumbListSize - 1 && ww < numThumbWidth) {
                        // dann sind wir in der letzten Zeile und nicht am Zeilenende
                        i = -1;
                        lineEnd = true;
                    }

                }

                if (!stopAll) {
                    notifyEvent(thumbListSize, thumbListSize, "Datei schreiben");
                    writeImage(imgOut);
                }
                notifyEvent(0, 0, "");

            } catch (Exception ex) {
                Log.errorLog(654102025, ex);
            } catch (OutOfMemoryError E) {
                Platform.runLater(() ->
                        PAlert.showErrorAlert("Mosaik erstellen", "Das Mosaik kann nicht erstellt werden, das Programm " +
                                "hat zu wenig Arbeitsspeicher!")
                );
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
            ios = ImageIO.createImageOutputStream(destPath.toFile());
            writer.setOutput(ios);
            ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());
            iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwparam.setCompressionQuality(1f);
            writer.write(null, new IIOImage(img, null, null), iwparam);
            ios.flush();
            writer.dispose();
        } catch (Exception e) {
            Log.errorLog(945123659, e.getMessage());
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
