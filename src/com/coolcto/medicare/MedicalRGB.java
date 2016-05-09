/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolcto.medicare;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author Virgil
 */
public class MedicalRGB {

    int width = 0;
    int height = 0;

    public int[] getColorRGB(String srcpath)
            throws Exception {
        ImageToColor process = new ImageToColor();
        BufferedImage fbi = ImageFilter.imageFiltering(srcpath);
        BufferedImage bi = ImageIO.read(new File(srcpath));
        this.height = bi.getHeight();
        this.width = bi.getWidth();
        EdgeDetector edge = new EdgeDetector(120);
        List list = edge.getEdgePointsGroup(fbi);
        MinCircle minC = new MinCircle();
        double[] d = minC.getMinCircle(list);
        int[] na = new int[4];
        if ((d[0] > 50.0D) && (d[0] < this.width - 50) && (d[1] > 50.0D) && (d[1] < this.height - 50)) {
            na[0] = ((int) d[0] - 50);
            na[1] = ((int) d[1] - 50);
            na[2] = ((int) d[0] + 50);
            na[3] = ((int) d[1] + 50);
            return process.getLevelByAverage(bi, na);
        }
        throw new RuntimeException("image edge detector error");
    }

    public int[] getMinCircle(String srcpath) throws IOException {
        int[] res = new int[3];
        BufferedImage fbi = ImageFilter.imageFiltering(srcpath);
        this.height = fbi.getHeight();
        this.width = fbi.getWidth();
        EdgeDetector edge = new EdgeDetector(120);
        List list = edge.getEdgePointsGroup(fbi);
        MinCircle minC = new MinCircle();
        double[] d = minC.getMinCircle(list);
        if (d.length == 0) {
            return res;
        } else {
            res[0] = ((int) d[0]);
            res[1] = ((int) d[1]);
            res[2] = ((int) d[2]);
            return res;
        }
    }

    public int[] getColorRGB(int x, int y, int r, File file) throws IOException {
        ImageToColor process = new ImageToColor();
        return process.getRGB(x, y, r, file);
    }

    public int getColorLevel(int[] rgb) throws Exception {
        Color c = new Color();
        return c.process(rgb);
    }

    public static void main(String[] args) throws Exception {
        MedicalRGB medicalRGB = new MedicalRGB();
        String file = "G:\\test\\1.jpg";
        int[] a = medicalRGB.getMinCircle(file);
        System.out.println(Arrays.toString(a));
        int[] b = medicalRGB.getColorRGB(a[0], a[1], a[2], new File(file));
        System.out.println(Arrays.toString(b));
        System.out.println(medicalRGB.getColorLevel(b));
    }
}
