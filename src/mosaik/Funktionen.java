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

package mosaik;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import mosaik.daten.Daten;
import mosaik.daten.Konstanten;

public class Funktionen {

    public static BufferedImage getBufferedImage( File source) {
        BufferedImage img = null;
        ImageReader reader = getReader(source);
        try {
            img = reader.read(0);
        } catch (Exception e) {
        }
        reader.dispose();
        return img;
    }

    public static String getPfadProjektOrdner(Daten daten) {
        String ret = daten.system[Konstanten.SYSTEM_PROJECTDATEI_PFAD_NR];
        ret = new File(ret).getParent();
        if (ret == null) {
            ret = "";
        }
        return ret;
    }

    public static RenderedImage getRenderedImage( File file) {
        RenderedImage img;
        ImageReader reader = getReader( file);
        try {
            img = reader.readAsRenderedImage(0, null);
        } catch (Exception e) {
            System.out.println(e.getMessage()+"BildArchiv - getRenderedImage");
            return null;
        }
        reader.dispose();
        return img;
    }

    private static ImageReader getReader( File source) {
        try {
            Iterator readers = ImageIO.getImageReadersByFormatName(fileType(source));
            ImageReader reader = (ImageReader) readers.next();
            ImageInputStream iis = ImageIO.createImageInputStream(source);
            reader.setInput(iis, true);
            return reader;
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n"+ "Funktionen - getReader");
            return null;
        }
    }

    public static String fileType(File f) {
        return fileType(f.getName());
    }

    public static String fileType(String f) {
        String n = f;
        String suffix = null;
        int i = n.lastIndexOf('.');
        if (i > 0 && i < n.length() - 1) {
            suffix = n.substring(i + 1).toLowerCase();
        }
        if (suffix.equals("jpeg") || suffix.equals(Konstanten.IMAGE_FORMAT_JPG)) {
            return Konstanten.IMAGE_FORMAT_JPG;
        }
        if (suffix.equals(Konstanten.IMAGE_FORMAT_PNG)) {
            return Konstanten.IMAGE_FORMAT_PNG;
        }
        return "";
    }

}
