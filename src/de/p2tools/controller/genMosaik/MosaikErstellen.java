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

package de.p2tools.controller.genMosaik;

import de.p2tools.controller.config.Const;
import de.p2tools.controller.config.ProgData;
import de.p2tools.controller.data.createMosaik.CreateMosaik;
import de.p2tools.controller.data.thumb.Thumb;
import de.p2tools.controller.data.thumb.ThumbCollection;
import de.p2tools.mLib.tools.Duration;
import de.p2tools.mLib.tools.Log;
import de.p2tools.mLib.tools.MLAlert;
import mosaik.BildEvent;
import mosaik.BildListener;
import mosaik.Funktionen;
import mosaik.daten.Konstanten;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.util.Locale;

public class MosaikErstellen {

    String dest;
    String src;
    private int anz = 5;
    private EventListenerList listeners = new EventListenerList();
    private int progress = 0;
    private boolean stopAll = false;

    private final ProgData progData;
    private final CreateMosaik createMosaik;
    private final ThumbCollection thumbCollection;

    public MosaikErstellen(CreateMosaik createMosaik) {
        progData = ProgData.getInstance();
        this.createMosaik = createMosaik;
        this.thumbCollection = progData.thumbCollectionList.getThumbCollection(createMosaik.getThumbCollectionId());
    }

    /**
     *
     */
    public void setStop() {
        stopAll = true;
    }

    /**
     * @param listener
     */
    public void addAdListener(BildListener listener) {
        listeners.add(BildListener.class, listener);
    }

    /**
     */
    public void erstellen() {
        dest = createMosaik.getFotoDest();
        src = createMosaik.getFotoSrc();
        anz = createMosaik.getThumbCount();
        progress = 0;
        stopAll = false;

        if (dest.equals("")) {
            Log.errorLog(945120365, "Keine Zieldatei angegeben!");
            return;
        }

        if (createMosaik.getFormat().equals(Konstanten.IMAGE_FORMAT_PNG)) {
            if (!dest.endsWith("." + Const.IMAGE_FORMAT_JPG)) {
                dest += "." + Const.IMAGE_FORMAT_PNG;
            }
        } else {
            if (!dest.endsWith("." + Const.IMAGE_FORMAT_JPG)) {
                dest += "." + Const.IMAGE_FORMAT_JPG;
            }
        }

        boolean weiter = true;
        if (new File(dest).exists()) {
            if (!new MLAlert().showAlert_yes_no("Ziel existiert", dest,
                    "Soll die bereits vorhandene Datei überschrieben werden?").equals(MLAlert.BUTTON.YES)) {
                weiter = false;
            }
        }

        int len = thumbCollection.getThumbList().getSize();
        if (!weiter || len <= 0) {
            return;
        }

        Tus tus = new Tus();
        Thread startenThread = new Thread(tus);
        startenThread.setDaemon(true);
        startenThread.start();
    }

    private void notifyEvent(int max, int progress, String text) {
        BildEvent event;
        event = new BildEvent(this, progress, max, text, 1);
        for (BildListener l : listeners.getListeners(BildListener.class)) {
            l.tus(event);
        }
    }

    private class Tus implements Runnable {

        public synchronized void run() {

            Duration.counterStart("Mosaik erstellen");
            try {
                thumbCollection.getThumbList().resetAnz();
                BufferedImage imgOut;
                BufferedImage srcImg = Funktionen.getBufferedImage(new File(src));

                int srcHeight = srcImg.getRaster().getHeight();
                int srcWidth = srcImg.getRaster().getWidth();
                int sizeThumb = createMosaik.getThumbSize();

                int numThumbsWidth = createMosaik.getNumberThumbsWidth();
                int numPixelProThumb = srcWidth / numThumbsWidth;
                int numThumbsHeight = srcHeight / numPixelProThumb;

                int destWidth = numThumbsWidth * sizeThumb;
                int destHeight = numThumbsHeight * sizeThumb;

                imgOut = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);

                Thumb thumb;
                //Bild zusammenbauen
                Color c;
                notifyEvent(numThumbsWidth * numThumbsHeight, 0, "");
                Farbraum farbraum = new Farbraum(thumbCollection);
                File file;
                BufferedImage buffImg;
                for (int yy = 0; yy < numThumbsHeight && !stopAll; ++yy) {
                    System.out.println("yy " + yy + " von " + numThumbsHeight);

                    for (int xx = 0; xx < numThumbsWidth && !stopAll; ++xx) {

                        ++progress;
                        notifyEvent(yy * xx, progress, "Zeilen: " + yy);
                        c = getColor(srcImg.getSubimage(xx * numPixelProThumb, yy * numPixelProThumb,
                                numPixelProThumb, numPixelProThumb));

                        thumb = farbraum.getThumb(c, anz);
                        if (thumb != null) {
                            thumb.addAnz();
                            file = new File(thumb.getFileName());
                            buffImg = Funktionen.getBufferedImage(file);
                            buffImg = scale(buffImg, sizeThumb, sizeThumb);
                            imgOut.getRaster().setRect(xx * sizeThumb, yy * sizeThumb, buffImg.getData());
                        } else {
                            Log.errorLog(981021036, "MosaikErstellen_.tus-Farbe fehlt!!");
                        }
                    }
                }

                //fertig
                notifyEvent(numThumbsHeight * numThumbsWidth, progress, "Speichern");
                writeImage(imgOut);
                notifyEvent(0, 0, "");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

            Duration.counterStop("Mosaik erstellen");

        }

        BufferedImage scale(BufferedImage src, int w, int h) {
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            int x, y;
            int ww = src.getWidth();
            int hh = src.getHeight();
            for (x = 0; x < w; x++) {
                for (y = 0; y < h; y++) {
                    int col = src.getRGB(x * ww / w, y * hh / h);
                    img.setRGB(x, y, col);
                }
            }
            return img;
        }

        private Color getColor(BufferedImage img) {
            Raster rast = img.getRaster();
            long r = 0, g = 0, b = 0;
            long count = 0;
            try {
                for (int x = rast.getMinX(); x < (rast.getMinX() + rast.getWidth()); x++) {
                    for (int y = rast.getMinY(); y < (rast.getMinY() + rast.getHeight()); y++) {
                        r += rast.getSample(x, y, 0);
                        g += rast.getSample(x, y, 1);
                        b += rast.getSample(x, y, 2);
                        ++count;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            int rr = (int) (r / count), gg = (int) (g / count), bb = (int) (b / count);
            Color ret = new Color(rr, gg, bb);
            return ret;
        }

        private void writeImage(BufferedImage img) {
            ImageOutputStream ios = null;
            ImageWriter writer = null;
            try {
                if (createMosaik.getFormat().equals(Const.IMAGE_FORMAT_PNG)) {
                    writer = ImageIO.getImageWritersBySuffix(Const.IMAGE_FORMAT_PNG).next();
                    ios = ImageIO.createImageOutputStream(new File(dest));
                    writer.setOutput(ios);

                    writer.write(new IIOImage(img, null, null));
                    ios.flush();
                } else {
                    writer = ImageIO.getImageWritersBySuffix(Const.IMAGE_FORMAT_JPG).next();
                    ios = ImageIO.createImageOutputStream(new File(dest));
                    writer.setOutput(ios);

                    ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());
                    iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    iwparam.setCompressionQuality(1f);

                    writer.write(null, new IIOImage(img, null, null), iwparam);
                    ios.flush();
                }
            } catch (Exception e) {
                Log.errorLog(784520369, e, "MosaikErstellen_.Tus.writeImage");
            } finally {
                try {
                    ios.close();
                    writer.dispose();
                } catch (Exception ex) {
                }
            }
        }

    }

}
