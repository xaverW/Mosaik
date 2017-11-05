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

package mosaik.bild;

import java.io.IOException;
import mosaik.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import javax.imageio.ImageIO;
import mosaik.daten.Daten;
import mosaik.daten.Konstanten;

public class ScaleImage_ {

    Daten daten;

    public ScaleImage_(Daten ddaten) {
        daten = ddaten;
    }

    /**
     * 
     * @param source
     * @param dest
     * @throws IOException 
     */
    public void tus(File source, File dest) throws IOException {
        BufferedImage img = Funktionen.getBufferedImage(source);
        if (Boolean.parseBoolean(daten.datenProjekt.arr[Konstanten.PROJEKT_ARCHIV_RECHTECK_NR])) {
            int h = img.getHeight(), w = img.getWidth(), x, y;
            int widthNew = (h > w) ? w : h;
            if (w > h) {
                y = 0;
                x = (w - h) / 2;
            } else {
                y = (h - w) / 2;
                x = 0;
            }
            BufferedImage imgOut = new BufferedImage(widthNew, widthNew, BufferedImage.TYPE_INT_RGB);
            Raster raster1 = img.getRaster();
            WritableRaster raster2 = imgOut.getRaster();
            for (int i = 0; i < widthNew; i++) {
                for (int k = 0; k < widthNew; k++) {
                    raster2.setSample(i, k, 0, raster1.getSample(i + x, k + y, 0));
                    raster2.setSample(i, k, 1, raster1.getSample(i + x, k + y, 1));
                    raster2.setSample(i, k, 2, raster1.getSample(i + x, k + y, 2));
                }
            }
            img = imgOut;
        }
        int width = Integer.parseInt(daten.datenProjekt.arr[Konstanten.PROJEKT_AUFLOESUNG_ZIEL_NR]);
        Image scaledImage = img.getScaledInstance(width, width, Image.SCALE_SMOOTH);
        BufferedImage outImg = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
        Graphics g = outImg.getGraphics();
        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();
        ImageIO.write(outImg, daten.datenProjekt.arr[Konstanten.PROJEKT_ARCHIV_FORMAT_NR], dest);
    }

    /**
     * 
     * @param source
     * @param rechts 
     */
    public void drehen(File source, boolean rechts) {
        try {
            BufferedImage img = Funktionen.getBufferedImage(source);
            BufferedImage rImg = rotateImage(img, (rechts) ? 1 : -1);
            ImageIO.write(rImg, Funktionen.fileType(source), source);
        } catch (Exception ex) {
            daten.fehler.fehlermeldung(ex, "ScaleImage_.drehen");
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

}
