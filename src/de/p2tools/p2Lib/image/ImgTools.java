/*
 * MTPlayer Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2Lib.image;

import de.p2tools.controller.Funktionen;
import de.p2tools.p2Lib.tools.Log;

import javax.imageio.*;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.Iterator;
import java.util.Locale;

public class ImgTools {

    public static final String IMAGE_FORMAT_JPG = "jpg";
    public static final String IMAGE_FORMAT_PNG = "png";

    public static Color getColor(BufferedImage img) {
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

    public static final BufferedImage cloneImage(BufferedImage image) {
        BufferedImage clone = new BufferedImage(image.getWidth(),
                image.getHeight(), image.getType());
        Graphics2D g2d = clone.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return clone;
    }


    public static BufferedImage getBufferedImage(File source) {
        BufferedImage img = null;
        ImageReader reader = getReader(source);
        try {
            img = reader.read(0);
        } catch (Exception e) {
        }
        reader.dispose();
        return img;
    }

    public static RenderedImage getRenderedImage(File file) {
        RenderedImage img;
        ImageReader reader = getReader(file);
        try {
            img = reader.readAsRenderedImage(0, null);
        } catch (Exception e) {
            System.out.println(e.getMessage() + "BildArchiv_ - getRenderedImage");
            return null;
        }
        reader.dispose();
        return img;
    }

    private static ImageReader getReader(File source) {
        try {
            Iterator readers = ImageIO.getImageReadersByFormatName(Funktionen.fileType(source));
            ImageReader reader = (ImageReader) readers.next();
            ImageInputStream iis = ImageIO.createImageInputStream(source);
            reader.setInput(iis, true);
            return reader;
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n" + "Funktionen - getReader");
            return null;
        }
    }

    public static void writeImage(BufferedImage img, String dest, String suffix) {
        ImageOutputStream ios = null;
        ImageWriter writer = null;
        try {
            if (suffix.equals(IMAGE_FORMAT_PNG)) {
                writer = ImageIO.getImageWritersBySuffix(IMAGE_FORMAT_PNG).next();
                ios = ImageIO.createImageOutputStream(new File(dest));
                writer.setOutput(ios);

                writer.write(new IIOImage(img, null, null));
                ios.flush();

            } else {
                writer = ImageIO.getImageWritersBySuffix(IMAGE_FORMAT_JPG).next();
                ios = ImageIO.createImageOutputStream(new File(dest));
                writer.setOutput(ios);

                ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());
                iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                iwparam.setCompressionQuality(1f);

                writer.write(null, new IIOImage(img, null, null), iwparam);
                ios.flush();
            }
        } catch (Exception e) {
            Log.errorLog(784520369, e, ImgTools.class.toString());
        } finally {
            try {
                ios.close();
                writer.dispose();
            } catch (Exception ex) {
            }
        }
    }

    public static void changeToGrayscale(BufferedImage img) {
        //get image width and height
        int width = img.getWidth();
        int height = img.getHeight();
        try {
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
        } catch (Exception e) {
            Log.errorLog(698741254, e, ImgTools.class.toString());
        }
    }
}
