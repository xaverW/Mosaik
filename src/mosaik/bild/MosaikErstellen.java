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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.util.Locale;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;
import mosaik.BildEvent;
import mosaik.BildListener;
import mosaik.Funktionen;
import mosaik.daten.Daten;
import mosaik.daten.DatenFarbe;
import mosaik.daten.Konstanten;

public class MosaikErstellen {

    Daten daten;
    String dest;
    String src;
    ScaleImage scaleImage;
//    Farbraum farbraum;
    private int anz = 5;
    private EventListenerList listeners = new EventListenerList();
    private int progress = 0;
    private boolean stopAll = false;

    public MosaikErstellen(Daten ddaten) {
        daten = ddaten;
    }
    /////////////////////////////////////////
    // public
    /////////////////////////////////////////
    /**
     * 
     */
    public void setStop() {
        stopAll = true;
    }

    /**
     * 
     * @param listener
     */
    public void addAdListener(BildListener listener) {
        listeners.add(BildListener.class, listener);
    }

    /**
     * 
     * @param ssrc
     * @param ddest
     * @param aanz
     */
    public void erstellen(String ssrc, String ddest, int aanz) {
        dest = ddest;
        src = ssrc;
        anz = aanz;
        progress = 0;
        stopAll = false;
//        farbraum = new Farbraum(daten);
        Tus tus = new Tus();
        Thread startenThread = new Thread(tus);
        startenThread.setDaemon(true);
        startenThread.start();
    }
    ///////////////////////////////////////////
    // private
    //////////////////////////////////////////
    private void notifyEvent(int max, int progress, String text) {
        BildEvent event;
        ///////////////////////threads
        event = new BildEvent(this, progress, max, text, 1);
        for (BildListener l : listeners.getListeners(BildListener.class)) {
            l.tus(event);
        }
    }

    private class Tus implements Runnable {

        public synchronized void run() {
            daten.listeFarben.resetAnz();
            BufferedImage imgOut = null;
            if (dest.equals("")) {
                daten.fehler.fehlermeldung("Keine Zieldatei angegeben!");
            } else {
                if (daten.datenProjekt.arr[Konstanten.PROJEKT_MOSAIK_FORMAT_NR].equals(Konstanten.IMAGE_FORMAT_PNG)) {
                    if (!dest.endsWith("." + Konstanten.IMAGE_FORMAT_PNG)) {
                        dest += "." + Konstanten.IMAGE_FORMAT_PNG;
                    }
                } else {
                    if (!dest.endsWith("." + Konstanten.IMAGE_FORMAT_JPG)) {
                        dest += "." + Konstanten.IMAGE_FORMAT_JPG;
                    }
                }
                boolean weiter = true;
                if (new File(dest).exists()) {
                    if (JOptionPane.showConfirmDialog(null, "Trotzdem anlegen und \u00fcberschreiben?",
                                                      "Datei existiert bereits!", JOptionPane.YES_NO_OPTION) != JOptionPane.OK_OPTION) {
                        weiter = false;
                    }
                }
                int len = daten.listeFarben.size();
                if (weiter && len > 0) {
                    int width = Integer.parseInt(daten.datenProjekt.arr[Konstanten.PROJEKT_AUFLOESUNG_ZIEL_NR]);
                    BufferedImage img = Funktionen.getBufferedImage(daten, new File(src));
                    int h = img.getRaster().getHeight();
                    int w = img.getRaster().getWidth();
                    int anzH = Integer.parseInt(daten.datenProjekt.arr[Konstanten.PROJEKT_BILDER_HOEHE_NR]);
                    int anzPixelProBild = h / anzH;
                    int anzW = w / anzPixelProBild;
                    imgOut = new BufferedImage(width * anzW, width * anzH, BufferedImage.TYPE_INT_RGB);
                    DatenFarbe farbe;
                    //Bild zusammenbauen
                    Color c;
                    notifyEvent(anzW * anzH, 0, "");
                    Farbraum farbraum = new Farbraum(daten);
                    File file;
                    BufferedImage buffImg;
                    for (int yy = 0; yy < anzH && !stopAll; ++yy) {
                        for (int xx = 0; xx < anzW && !stopAll; ++xx) {
                            ++progress;
                            notifyEvent(anzW * anzH, progress, "Zeilen: " + yy);
                            c = getColor(img.getSubimage(xx * anzPixelProBild, yy * anzPixelProBild, anzPixelProBild, anzPixelProBild));
                            farbe = farbraum.getFarbe(c, anz);
                            if (farbe != null) {
                                ++farbe.anz;
                                file = new File(farbe.arr[Konstanten.FARBEN_PFAD_NR]);
                                buffImg = Funktionen.getBufferedImage(daten, file);
                                int count = 0;
                                while (buffImg == null && count < 5) {
                                    ++count;
                                    try {
                                        this.wait(500);
                                    } catch (InterruptedException ex) {
                                    }
                                    JOptionPane.showMessageDialog(null, "Kann Bild nicht laden!",
                                                                  "??", JOptionPane.INFORMATION_MESSAGE);
                                    System.out.println("buffImg == null  -  " + farbe.arr[Konstanten.FARBEN_PFAD_NR]);
                                    buffImg = Funktionen.getBufferedImage(daten, file);
                                }
                                imgOut.getRaster().setRect(xx * width, yy * width, buffImg.getData());
                            } else {
                                daten.fehler.fehlermeldung("MosaikErstellen.tus-Farbe fehlt!!");
                            }
                        }
                    }
                    //fertig
                    notifyEvent(anzW * anzH, progress, "Speichern");
                    writeImage(imgOut);
                    notifyEvent(0, 0, "");
                }
            }
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
               int rr =
                
               (int) (r / count), gg = (int) (g / count),bb  =  (  
             
        

            
               int) (b / count);
            Color ret = new Color(rr, gg, bb);
            return ret;
        }

        private void writeImage(BufferedImage img) {
            ImageOutputStream ios = null;
            ImageWriter writer = null;
            try {
                if (daten.datenProjekt.arr[Konstanten.PROJEKT_MOSAIK_FORMAT_NR].equals(Konstanten.IMAGE_FORMAT_PNG)) {
                    writer = ImageIO.getImageWritersBySuffix(Konstanten.IMAGE_FORMAT_PNG).next();
                    ios = ImageIO.createImageOutputStream(new File(dest));
                    writer.setOutput(ios);

                    writer.write(new IIOImage(img, null, null));
                    ios.flush();
                } else {
                    writer = ImageIO.getImageWritersBySuffix(Konstanten.IMAGE_FORMAT_JPG).next();
                    ios = ImageIO.createImageOutputStream(new File(dest));
                    writer.setOutput(ios);

                    ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());
                    iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    iwparam.setCompressionQuality(1f);

                    writer.write(null, new IIOImage(img, null, null), iwparam);
                    ios.flush();
                }
            } catch (Exception e) {
                daten.fehler.fehlermeldung(e, "MosaikErstellen.Tus.writeImage");
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
