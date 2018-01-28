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

package de.p2tools.controller.worker.genMosaikBw;

import de.p2tools.controller.Funktionen;
import de.p2tools.controller.RunEvent;
import de.p2tools.controller.RunListener;
import de.p2tools.controller.config.ProgConst;
import de.p2tools.controller.config.ProgData;
import de.p2tools.controller.data.mosaikBwData.MosaikDataBw;
import de.p2tools.controller.data.thumb.Thumb;
import de.p2tools.controller.data.thumb.ThumbCollection;
import de.p2tools.controller.worker.genThumbList.ScaleImage;
import de.p2tools.p2Lib.tools.Duration;
import de.p2tools.p2Lib.tools.FileUtils;
import de.p2tools.p2Lib.tools.Log;
import de.p2tools.p2Lib.tools.PAlert;

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

public class GenMosaikBw {

    String dest;
    String src;
    private int anz = 5;
    private EventListenerList listeners = new EventListenerList();
    private int progress = 0;
    private boolean stopAll = false;

    private ProgData progData;
    private MosaikDataBw mosaikDataBw;
    private ThumbCollection thumbCollection;

    public GenMosaikBw(ProgData progData) {
        this.progData = progData;
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

    /**
     * @param mosaikData
     */
    public void create(MosaikDataBw mosaikData) {
        this.mosaikDataBw = mosaikData;
        this.thumbCollection = progData.selectedProjectData.getThumbCollection();

        dest = FileUtils.concatPaths(mosaikData.getFotoDestDir(), mosaikData.getFotoDestName());
        src = mosaikData.getFotoSrc();
        anz = mosaikData.getThumbCount();
        progress = 0;
        stopAll = false;

        if (dest.isEmpty()) {
            Log.errorLog(945120365, "Keine Zieldatei angegeben!");
            return;
        }

        if (mosaikData.getFormat().equals(ProgConst.IMAGE_FORMAT_PNG)) {
            if (!dest.endsWith("." + ProgConst.IMAGE_FORMAT_JPG)) {
                dest += "." + ProgConst.IMAGE_FORMAT_PNG;
            }
        } else {
            if (!dest.endsWith("." + ProgConst.IMAGE_FORMAT_JPG)) {
                dest += "." + ProgConst.IMAGE_FORMAT_JPG;
            }
        }

        if (new File(dest).exists() &&
                !new PAlert().showAlert_yes_no("Ziel existiert", dest,
                        "Soll die bereits vorhandene Datei überschrieben werden?").equals(PAlert.BUTTON.YES)) {
            return;
        }

        int len = thumbCollection.getThumbList().getSize();
        if (len <= 0) {
            return;
        }

        Tus tus = new Tus();
        Thread startenThread = new Thread(tus);
        startenThread.setDaemon(true);
        startenThread.start();
    }

    private void notifyEvent(int max, int progress, String text) {
        RunEvent event;
        event = new RunEvent(this, progress, max, text);
        for (RunListener l : listeners.getListeners(RunListener.class)) {
            l.notify(event);
        }
    }

    private class Tus implements Runnable {

        public synchronized void run() {

            Duration.counterStart("Mosaik erstellen");
            try {

                thumbCollection.getThumbList().resetAnz();
                BufferedImage imgOut, imgSrcSmall;
                BufferedImage srcImg = Funktionen.getBufferedImage(new File(src));

                int srcHeight = srcImg.getRaster().getHeight();
                int srcWidth = srcImg.getRaster().getWidth();
                int sizeThumb = mosaikDataBw.getThumbSize();

                int numThumbsWidth = mosaikDataBw.getNumberThumbsWidth();
                int numPixelProThumb = srcWidth / numThumbsWidth;
                int numThumbsHeight = srcHeight / numPixelProThumb;

                int destWidth = numThumbsWidth * sizeThumb;
                int destHeight = numThumbsHeight * sizeThumb;

                boolean blackWhite = mosaikDataBw.isBlackWhite();

                imgOut = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);

                imgSrcSmall = ScaleImage.scaleBufferedImage(srcImg, sizeThumb, sizeThumb);

                //Bild zusammenbauen
//                Thumb thumb;
                Color c;
                int maxRun = numThumbsHeight * numThumbsWidth;
                notifyEvent(maxRun, 0, "");
//                Farbraum farbraum = new Farbraum(thumbCollection);
                File file;
                BufferedImage buffImg;
                for (int yy = 0; yy < numThumbsHeight && !stopAll; ++yy) {
                    System.out.println("yy " + yy + " von " + numThumbsHeight);

                    for (int xx = 0; xx < numThumbsWidth && !stopAll; ++xx) {

                        ++progress;
                        notifyEvent(maxRun, progress, "Zeilen: " + yy);
                        c = getColor(srcImg.getSubimage(xx * numPixelProThumb, yy * numPixelProThumb,
                                numPixelProThumb, numPixelProThumb));

                        buffImg = getImgBw(imgSrcSmall, c, blackWhite);
                        buffImg = ScaleImage.scaleBufferedImage(buffImg, sizeThumb, sizeThumb);
                        imgOut.getRaster().setRect(xx * sizeThumb, yy * sizeThumb, buffImg.getData());
                    }
                }

                //fertig
                notifyEvent(maxRun, progress, "Speichern");
                writeImage(imgOut);
                notifyEvent(0, 0, "");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

            Duration.counterStop("Mosaik erstellen");

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
                if (mosaikDataBw.getFormat().equals(ProgConst.IMAGE_FORMAT_PNG)) {
                    writer = ImageIO.getImageWritersBySuffix(ProgConst.IMAGE_FORMAT_PNG).next();
                    ios = ImageIO.createImageOutputStream(new File(dest));
                    writer.setOutput(ios);

                    writer.write(new IIOImage(img, null, null));
                    ios.flush();
                } else {
                    writer = ImageIO.getImageWritersBySuffix(ProgConst.IMAGE_FORMAT_JPG).next();
                    ios = ImageIO.createImageOutputStream(new File(dest));
                    writer.setOutput(ios);

                    ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());
                    iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    iwparam.setCompressionQuality(1f);

                    writer.write(null, new IIOImage(img, null, null), iwparam);
                    ios.flush();
                }
            } catch (Exception e) {
                Log.errorLog(784520369, e, "MosaikErstellen.Tus.writeImage");
            } finally {
                try {
                    ios.close();
                    writer.dispose();
                } catch (Exception ex) {
                }
            }
        }

    }

    private BufferedImage getImgBw(Thumb thumb) {
        File file = new File(thumb.getFileName());
        BufferedImage img = clone(Funktionen.getBufferedImage(file));


        int width = img.getWidth();
        int height = img.getHeight();

        //convert to grayscale
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y);

                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                //calculate average
                int avg = (r + g + b) / 3;

                //replace RGB value with avg
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;

                img.setRGB(x, y, p);
            }
        }
        return img;
    }

    private BufferedImage getImgBw(BufferedImage srcImg, Color c, boolean bw) {
        BufferedImage img = clone(srcImg);

        int width = img.getWidth();
        int height = img.getHeight();

        float avgSrc = 1.0f * (c.getRed() + c.getGreen() + c.getBlue()) * 255 / (3 * 255);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y);

                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                if (bw) {
                    //calculate average
                    float avg = 1.0f * (r + g + b) / (3 * 255);
                    int avgI = (int) (avg * avgSrc);

                    p = (a << 24) | (avgI << 16) | (avgI << 8) | avgI;

                } else {
                    //calculate average
                    float avg = 1.0f * (r + g + b) / (3 * 255);

                    p = (a << 24) |
                            ((int) (avg * c.getRed()) << 16) |
                            ((int) (avg * c.getGreen()) << 8) |
                            (int) (avg * c.getBlue());
                }

                img.setRGB(x, y, p);
            }
        }
        return img;
    }

    public static final BufferedImage clone(BufferedImage image) {
        BufferedImage clone = new BufferedImage(image.getWidth(),
                image.getHeight(), image.getType());
        Graphics2D g2d = clone.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return clone;
    }
}