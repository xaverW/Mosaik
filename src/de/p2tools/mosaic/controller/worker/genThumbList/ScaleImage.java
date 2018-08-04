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

package de.p2tools.mosaic.controller.worker.genThumbList;

import de.p2tools.mosaic.controller.config.ProgConfig;
import de.p2tools.mosaic.controller.config.ProgConst;
import de.p2tools.mosaic.controller.data.thumb.Thumb;
import de.p2tools.mosaic.controller.data.thumb.ThumbDataList;
import de.p2tools.p2Lib.PConst;
import de.p2tools.p2Lib.image.ImgFile;
import de.p2tools.p2Lib.image.ImgTools;
import de.p2tools.p2Lib.tools.log.PLog;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
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
    public static void getScaledThumb(File source, File dest, ThumbDataList thumbDataList) {
        try {
            BufferedImage imgSrc = ImageInCorrectOrientation.getImageInCorrectOrientation(source);

            if (imgSrc == null) {
                PLog.errorLog(465323107, "Image==null");
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
            Thumb thumb = getThumb(rasterDest, dest);
            if (thumb != null) {
                thumbDataList.add(thumb);
            }


            BufferedImage outImg = ImgTools.scaleBufferedImage(imgRect, ProgConst.THUMB_RESOLUTION, ProgConst.THUMB_RESOLUTION);
            ImageIO.write(outImg, ProgConfig.FOTO_FORMAT.get(), dest);


//            Image scaledImage = imgRect.getScaledInstance(ProgConst.THUMB_RESOLUTION, ProgConst.THUMB_RESOLUTION, Image.SCALE_SMOOTH);
//            BufferedImage outImg = new BufferedImage(ProgConst.THUMB_RESOLUTION, ProgConst.THUMB_RESOLUTION, BufferedImage.TYPE_INT_RGB);
//
//            Graphics2D g = outImg.createGraphics();
//            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//            g.drawImage(scaledImage, 0, 0, null);
//            g.dispose();

        } catch (Exception ex) {
            PLog.errorLog(701402586, ex);
            System.out.println(ex.getMessage() + "MakeThumb.thumb");
            System.out.println("----------------------------------");
            System.out.println("Fehler - Src: " + source.getAbsolutePath());
            System.out.println("Fehler - Dest: " + dest.getAbsolutePath());
        }
    }

    /**
     * @param source
     * @param rechts
     */

    public static void rotate(File source, boolean rechts) {
        try {
            BufferedImage img = ImgFile.getBufferedImage(source);
            BufferedImage rImg = rotateImage(img, (rechts) ? 1 : -1);
            ImageIO.write(rImg, ImgTools.fileType(source), source);
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + PConst.LINE_SEPARATOR + "ScaleImage_.drehen");
        }
    }

    private static BufferedImage rotateImage(BufferedImage src, int degrees) {
        AffineTransform affineTransform = AffineTransform.getQuadrantRotateInstance(
                degrees, src.getWidth() / 2, src.getHeight() / 2);
        BufferedImage rotatedImage = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) rotatedImage.getGraphics();
        g.setTransform(affineTransform);
        g.drawImage(src, 0, 0, null);
        return rotatedImage;
    }

    /**
     * @param img
     */
    public static Thumb getThumb(File img) {
        Raster rast = ImgFile.getRenderedImage(img).getData();
        return getThumb(rast, img);
    }

    private static Thumb getThumb(Raster rast, File img) {
        Thumb ret = null;
        long r = 0, g = 0, b = 0;
        long count = 0;
        if (rast != null) {

            try {
                for (int x = rast.getMinX(); x < (rast.getMinX() + rast.getWidth()); x++) {
                    for (int y = rast.getMinY(); y < (rast.getMinY() + rast.getHeight()); y++) {
                        if (rast.getNumBands() < 3) {
                            r += rast.getSample(x, y, 0);
                            g = r;
                            b = r;
                        } else {
                            r += rast.getSample(x, y, 0);
                            g += rast.getSample(x, y, 1);
                            b += rast.getSample(x, y, 2);
                        }
                        ++count;
                    }
                }

                r = r / count;
                g = g / count;
                b = b / count;

//                if (!Thumb.checkC(r) || !Thumb.checkC(g) || !Thumb.checkC(b)) {
//                    Log.errorLog(942103478, "getThumb");
//                }

                long avg = (r + g + b) / 3;
                ret = new Thumb((int) r, (int) g, (int) b, (int) avg, img.getAbsolutePath());

            } catch (Exception ex) {
                throw ex;
            }
        }
        return ret;
    }
}
