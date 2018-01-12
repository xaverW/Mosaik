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


package de.p2tools.controller.worker.genThumbList;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageInCorrectOrientation {

    public static BufferedImage getImageInCorrectOrientation(File file) throws ImageProcessingException, IOException, MetadataException {
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        if (metadata != null) {
            if (metadata.containsDirectoryOfType(ExifIFD0Directory.class)) {
                // Get the current orientation of the image
                Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
                int orientation = 1;
                if (directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                    orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
                }

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

}
