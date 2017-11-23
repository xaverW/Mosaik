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

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;
import de.p2tools.controller.config.Config;
import de.p2tools.controller.config.Const;
import de.p2tools.controller.data.thumb.Thumb;
import de.p2tools.controller.data.thumb.ThumbCollection;
import de.p2tools.mLib.tools.Log;
import mosaik.Funktionen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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
            BufferedImage imgSrc = correctOrientation(source);
//            BufferedImage imgSrc = Funktionen.getBufferedImage(source);

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

    public static void printMetaData(File file) throws IOException {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(new File("/mnt/lager/mosaik/2014.12.26_Weihnachten/2014.12.26_Weihnachten_01.jpg"));

            for (Directory directory : metadata.getDirectories()) {
                System.out.println("===" + directory.getName() + "===");
                for (Tag tag : directory.getTags()) {
                    System.out.println(tag.getTagName() + ": " + tag.getDescription());
                }
                if (directory.hasErrors()) {
                    for (String error : directory.getErrors()) {
                        System.out.format("ERROR: %s", error);
                    }
                }
                System.out.println();
            }

            Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);

            int orientation = 1;
            try {
                orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            } catch (MetadataException me) {
            }
            int width = jpegDirectory.getImageWidth();
            int height = jpegDirectory.getImageHeight();


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


    }

    public static BufferedImage correctOrientation(File file) throws ImageProcessingException, IOException, MetadataException {
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        if (metadata != null) {
            if (metadata.containsDirectoryOfType(ExifIFD0Directory.class)) {
                // Get the current orientation of the image
                Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
                int orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);

                // Create a buffered image from the input stream
                BufferedImage bimg = ImageIO.read(file);


                // Get the current width and height of the image
                int[] imageSize = {bimg.getWidth(), bimg.getHeight()};
                int width = imageSize[0];
                int height = imageSize[1];

                // Determine which correction is needed
                AffineTransform t = new AffineTransform();
                switch (orientation) {
                    case 1:
                        // no correction necessary skip and return the image
                        return bimg;
                    case 2: // Flip X
                        t.scale(-1.0, 1.0);
                        t.translate(-width, 0);
                        return transform(bimg, t);
                    case 3: // PI rotation
                        t.translate(width, height);
                        t.rotate(Math.PI);
                        return transform(bimg, t);
                    case 4: // Flip Y
                        t.scale(1.0, -1.0);
                        t.translate(0, -height);
                        return transform(bimg, t);
                    case 5: // - PI/2 and Flip X
                        t.rotate(-Math.PI / 2);
                        t.scale(-1.0, 1.0);
                        return transform(bimg, t);
                    case 6: // -PI/2 and -width
                        t.translate(height, 0);
                        t.rotate(Math.PI / 2);
                        return transform(bimg, t);
                    case 7: // PI/2 and Flip
                        t.scale(-1.0, 1.0);
                        t.translate(height, 0);
                        t.translate(0, width);
                        t.rotate(3 * Math.PI / 2);
                        return transform(bimg, t);
                    case 8: // PI / 2
                        t.translate(0, width);
                        t.rotate(3 * Math.PI / 2);
                        return transform(bimg, t);
                }
            }
        }

        return null;
    }

    /**
     * Performs the tranformation
     *
     * @param bimage
     * @param transform
     * @return
     * @throws IOException
     */
    private static BufferedImage transform(BufferedImage bimage, AffineTransform transform) throws IOException {
        // Create an transformation operation
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);

        // Create an instance of the resulting image, with the same width, height and image type than the referenced one
        BufferedImage destinationImage = new BufferedImage(bimage.getWidth(), bimage.getHeight(), bimage.getType());
        op.filter(bimage, destinationImage);

        return destinationImage;
    }


    /**
     * @param source
     * @param rechts
     */

    public static void rotate(File source, boolean rechts) {
        try {
            BufferedImage img = Funktionen.getBufferedImage(source);
            BufferedImage rImg = rotateImage(img, (rechts) ? 1 : -1);
            ImageIO.write(rImg, Funktionen.fileType(source), source);
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + "\n" + "ScaleImage_.drehen");
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
