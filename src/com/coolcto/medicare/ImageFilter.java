/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolcto.medicare;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Virgil
 */
public class ImageFilter {

    public static BufferedImage imageFiltering(String srcPath)throws IOException {
        BufferedImage img = ImageIO.read(new File(srcPath));
        int[] imageData = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
        int w = img.getWidth();
        int h = img.getHeight();
        int[] pix = new int[w * h];
        img.getRGB(0, 0, w, h, pix, 0, w);
        int[] newpix = filtering(pix, w, h);
        img.setRGB(0, 0, w, h, newpix, 0, w);
        return img;
    }

    public static int[] filtering(int[] pix, int w, int h) {
        int[] newpix = new int[w * h];
        int n = 9;

        int[] temp1 = new int[n];
        int[] temp2 = new int[n / 2];
        ColorModel cm = ColorModel.getRGBdefault();
        int r = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if ((x != 0) && (x != w - 1) && (y != 0) && (y != h - 1)) {
                    int sum = 0;
                    temp1[0] = cm.getRed(pix[(x - 1 + (y - 1) * w)]);
                    temp1[1] = cm.getRed(pix[(x + (y - 1) * w)]);
                    temp1[2] = cm.getRed(pix[(x + 1 + (y - 1) * w)]);
                    temp1[3] = cm.getRed(pix[(x - 1 + y * w)]);
                    temp1[4] = cm.getRed(pix[(x + y * w)]);
                    temp1[5] = cm.getRed(pix[(x + 1 + y * w)]);
                    temp1[6] = cm.getRed(pix[(x - 1 + (y + 1) * w)]);
                    temp1[7] = cm.getRed(pix[(x + (y + 1) * w)]);
                    temp1[8] = cm.getRed(pix[(x + 1 + (y + 1) * w)]);
                    for (int k = 0; k < n / 2; k++) {
                        int i1 = Math.abs(temp1[(n / 2)] - temp1[k]);
                        int i2 = Math.abs(temp1[(n / 2)] - temp1[(n - k - 1)]);
                        temp2[k] = (i1 < i2 ? temp1[k] : temp1[(n - k - 1)]);
                        sum += temp2[k];
                    }
                    r = sum / (n / 2);
                    newpix[(y * w + x)] = (0xFF000000 | r << 16 | r << 8 | r);
                } else {
                    newpix[(y * w + x)] = pix[(y * w + x)];
                }
            }
        }
        return newpix;
    }
}
