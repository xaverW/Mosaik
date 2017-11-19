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

package de.p2tools.controller.genFotoList;

import de.p2tools.controller.config.Config;
import de.p2tools.controller.config.Const;
import de.p2tools.controller.data.thumb.Thumb;
import de.p2tools.controller.data.thumb.ThumbCollection;
import de.p2tools.mLib.tools.Log;
import mosaik.Funktionen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class ScaleImage {

    /**
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void scale(File source, File dest, ThumbCollection thumbCollection) throws IOException {
        try {
            BufferedImage imgSrc = Funktionen.getBufferedImage(source);

            if (imgSrc == null) {
                Log.errorLog(465323107, "Image==null");
                return;
            }

            int h = imgSrc.getHeight(), w = imgSrc.getWidth(), x, y;
            int newSize = (h > w) ? w : h;

            if (w > h) {
                y = 0;
                x = (w - h) / 2;
            } else {
                y = (h - w) / 2;
                x = 0;
            }

            int imgType = imgSrc.getType();
            BufferedImage imgRect = new BufferedImage(newSize, newSize, imgType);

            Raster rasterSrc = imgSrc.getRaster();
            WritableRaster rasterDest = imgRect.getRaster();
            int bands = rasterSrc.getNumBands();

            for (int xx = 0; xx < newSize; xx++) {
                for (int yy = 0; yy < newSize; yy++) {
                    for (int band = 0; band < bands; ++band) {
                        rasterDest.setSample(xx, yy, band, rasterSrc.getSample(xx + x, yy + y, band));
                    }
                }
            }

            Image scaledImage = imgRect.getScaledInstance(Const.THUMB_RESOLUTION, Const.THUMB_RESOLUTION, Image.SCALE_SMOOTH);
            BufferedImage outImg = new BufferedImage(Const.THUMB_RESOLUTION, Const.THUMB_RESOLUTION, BufferedImage.TYPE_INT_RGB);

            Graphics2D g = outImg.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.drawImage(scaledImage, 0, 0, null);
            g.dispose();

            ImageIO.write(outImg, Config.FOTO_FORMAT.get(), dest);
        } catch (Exception ex) {
            Log.errorLog(701402586, ex);
        }
    }

//    /**
//     * @param source
//     * @param rechts
//     */
//
//    public static void rotate(File source, boolean rechts) {
//        try {
//            BufferedImage img = Funktionen.getBufferedImage(source);
//            BufferedImage rImg = rotateImage(img, (rechts) ? 1 : -1);
//            ImageIO.write(rImg, Funktionen.fileType(source), source);
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage() + "\n" + "ScaleImage_.drehen");
//        }
//    }
//
//    private static BufferedImage rotateImage(BufferedImage src, int degrees) {
//        AffineTransform affineTransform = AffineTransform.getQuadrantRotateInstance(
//                degrees, src.getWidth() / 2, src.getHeight() / 2);
//        BufferedImage rotatedImage = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
//        Graphics2D g = (Graphics2D) rotatedImage.getGraphics();
//        g.setTransform(affineTransform);
//        g.drawImage(src, 0, 0, null);
//        return rotatedImage;
//    }

    /**
     * @param img
     */
    public static Thumb getThumb(File img) {
        Thumb ret = null;
        Raster rast = Funktionen.getRenderedImage(img).getData();
        int r = 0, g = 0, b = 0;
        long count = 0;
        if (rast != null) {
            for (int x = rast.getMinX(); x < (rast.getMinX() + rast.getWidth()); x++) {
                for (int y = rast.getMinY(); y < (rast.getMinY() + rast.getHeight()); y++) {
                    r += rast.getSample(x, y, 0);
                    g += rast.getSample(x, y, 1);
                    b += rast.getSample(x, y, 2);
                    ++count;
                }
            }
            ret = new Thumb((int) (r / count), (int) (g / count), (int) (b / count), img.getAbsolutePath());
        }
        return ret;
    }
}
