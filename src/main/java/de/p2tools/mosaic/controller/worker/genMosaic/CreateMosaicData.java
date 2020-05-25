/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
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


package de.p2tools.mosaic.controller.worker.genMosaic;

import de.p2tools.mosaic.controller.data.thumb.ThumbCollection;

import java.awt.image.BufferedImage;
import java.util.List;

class CreateMosaicData {
    BufferedImage imgOut;
    BufferedImage srcImg;
    BufferedImage srcImgSmall;
    ColorCollection colorCollection;
    List<Integer> listForColoredThumbs;
    ThumbCollection thumbCollection;
    int sizeThumb;
    int yy;
    int numThumbsWidth;
    int numThumbsHeight;
    int numPixelProThumb;
    boolean addBorder;
    int borderSize;

    public CreateMosaicData(BufferedImage imgOut, BufferedImage srcImg,
                            BufferedImage srcImgSmall, ColorCollection colorCollection,
                            List<Integer> listForColoredThumbs, ThumbCollection thumbCollection,
                            int sizeThumb, int yy, int numThumbsWidth, int numThumbsHeight, int numPixelProThumb,
                            int borderSize, boolean addBorder) {
        this.imgOut = imgOut;
        this.srcImg = srcImg;
        this.srcImgSmall = srcImgSmall;
        this.colorCollection = colorCollection;
        this.listForColoredThumbs = listForColoredThumbs;
        this.thumbCollection = thumbCollection;
        this.sizeThumb = sizeThumb;
        this.yy = yy;
        this.numThumbsWidth = numThumbsWidth;
        this.numThumbsHeight = numThumbsHeight;
        this.numPixelProThumb = numPixelProThumb;
        this.borderSize = borderSize;
        this.addBorder = addBorder;
    }
}
