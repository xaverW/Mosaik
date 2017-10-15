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

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.Locale;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JOptionPane;
import mosaik.Fehler;
import mosaik.daten.Daten;
import mosaik.daten.DatenFarbe;
import mosaik.daten.Konstanten;

public class TapeteErstellen {

    Daten daten;
    String dest;
    ScaleImage scaleImage;

    public TapeteErstellen(Daten ddaten, String ddest) {
        daten = ddaten;
        dest = ddest;
        scaleImage = new ScaleImage(daten);
    }

    public void tus(int spalten) {
        if (dest.equals("")) {
          daten.fehler.fehlermeldung("Keine Zieldatei angegeben!");
        } else {
            final int AUFLOESUNG = Integer.parseInt(daten.datenProjekt.arr[Konstanten.PROJEKT_AUFLOESUNG_ZIEL_NR]);
            boolean weiter = true;
            int l = daten.listeFarben.size();
            if (new File(dest).exists()) {
                if (JOptionPane.showConfirmDialog(null, "Trotzdem anlegen und \u00fcberschreiben?",
                                                  "Datei existiert bereits!", JOptionPane.YES_NO_OPTION) != JOptionPane.OK_OPTION) {
                    weiter = false;
                }
            }
            if (weiter && l > 0) {
                int h = l / spalten * AUFLOESUNG,
                    b = spalten * AUFLOESUNG;
                if (l % spalten != 0) {
                    h += Integer.parseInt(daten.datenProjekt.arr[Konstanten.PROJEKT_AUFLOESUNG_ZIEL_NR]);
                }
                BufferedImage imgOut = new BufferedImage(b, h, BufferedImage.TYPE_INT_RGB);
                int hh = 0, bb = 0;
                for (int i = 0; i < l; ++i) {
                    DatenFarbe farbe = daten.listeFarben.get(i);
                    BufferedImage img = getBufferedImage(new File(farbe.arr[Konstanten.FARBEN_PFAD_NR]));
                    ////        try {
                    ////                if (img.getWidth() != Integer.parseInt(daten.datenProjekt.arr[Konstanten.PROJEKT_AUFLOESUNG_ZIEL_NR])) {
                    ////                    File file = File.createTempFile("mosaik", null);
                    ////                    scaleImage.tus(new File(farbe.arr[Konstanten.FARBEN_PFAD_NR]), file);
                    ////                    img = getBufferedImage(file);
                    ////                }
                    ////        } catch (IOException ex) {
                    ////        }
                    imgOut.getRaster().setRect(bb * AUFLOESUNG, hh * AUFLOESUNG, img.getData());
                    ++bb;
                    if (bb >= spalten) {
                        bb = 0;
                        ++hh;
                    }
                }
                while (bb < spalten ) {
                    for (int i = 0; i < l; ++i) {
                        DatenFarbe farbe = daten.listeFarben.get(i);
                        BufferedImage img = getBufferedImage(new File(farbe.arr[Konstanten.FARBEN_PFAD_NR]));
                        imgOut.getRaster().setRect(bb * AUFLOESUNG, hh * AUFLOESUNG, img.getData());
                        ++bb;
                        if (bb >= spalten) {
                            break;
                        }
                    }
                }
                writeImage(imgOut);
            }
        }
    }

    public BufferedImage getBufferedImage(
        File source) {
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

}
