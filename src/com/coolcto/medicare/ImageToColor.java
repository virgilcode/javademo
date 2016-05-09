/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolcto.medicare;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author Virgil
 */
public class ImageToColor {

    public List<Point> filterErrorPoint(List<Point> originList) {
        List list = new ArrayList();
        for (int i = 0; i < originList.size(); i++) {
            if (counts(((Point) originList.get(i)).getY(), originList) > 8) {
                list.add(originList.get(i));
            }
        }
        return list;
    }

    public int counts(int yValue, List<Point> list) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (((Point) list.get(i)).getY() == yValue) {
                count++;
            }
        }
        return count;
    }

    public int[] getLevelByAverage(BufferedImage bi, int[] a) {
        int[] sum = new int[3];
        int count = 0;
        for (int i = a[0]; i <= a[2]; i++) {
            for (int j = a[1]; j <= a[3]; j++) {
                int pixel = bi.getRGB(i, j);
                int[] rgb = new int[3];
                rgb[0] = ((pixel & 0xFF0000) >> 16);
                rgb[1] = ((pixel & 0xFF00) >> 8);
                rgb[2] = (pixel & 0xFF);
                sum[0] += rgb[0];
                sum[1] += rgb[1];
                sum[2] += rgb[2];
                count++;
            }
        }
        int[] av_rgb = new int[3];
        sum[0] /= count;
        sum[1] /= count;
        sum[2] /= count;
        return av_rgb;
    }

    public int[] getRGB(int x, int y, int r, File file) throws IOException {
        int[] sum = new int[3];
        int count = 0;
        BufferedImage bi = ImageIO.read(file);
//        int pixe = bi.getRGB(x, y);
//        int[] rgb1 = new int[3];
//        rgb1[0] = ((pixe & 0xFF0000) >> 16);
//        rgb1[1] = ((pixe & 0xFF00) >> 8);
//        rgb1[2] = (pixe & 0xFF);
//        System.out.println(Arrays.toString(rgb1));
        if ((x > 50) && (y > 50)) {
            for (int i = x - 50; i <= x + 50; i++) {
                for (int j = y - 50; j <= y + 50; j++) {
                    int pixel = bi.getRGB(i, j);
                    int[] rgb = new int[3];
                    rgb[0] = ((pixel & 0xFF0000) >> 16);
                    rgb[1] = ((pixel & 0xFF00) >> 8);
                    rgb[2] = (pixel & 0xFF);
                    sum[0] += rgb[0];
                    sum[1] += rgb[1];
                    sum[2] += rgb[2];
                    count++;
                }
            }
            int[] av_rgb = new int[3];
            av_rgb[0] = sum[0] / count;
            av_rgb[1] = sum[1] / count;
            av_rgb[2] = sum[2] / count;
            return av_rgb;
        }
        throw new RuntimeException("image edge detector error");
    }
}
