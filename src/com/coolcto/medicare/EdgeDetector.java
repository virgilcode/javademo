/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolcto.medicare;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Virgil
 */
public class EdgeDetector {

    int width;
    int height;
    int[] grayData;
    int size;
    int gradientThreshold = -1;
    List<Point> edegePoints;

    public EdgeDetector(int threshold) {
        this.gradientThreshold = threshold;
    }

    public List<Point> getEdgePointsGroup(BufferedImage bufferedImage) throws IOException {
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();
        this.size = (this.width * this.height);
        int[] imageData = bufferedImage.getRGB(0, 0, this.width, this.height, null, 0, this.width);
        this.grayData = new int[this.width * this.height];
        for (int i = 0; i < imageData.length; i++) {
            this.grayData[i] = ((imageData[i] & 0xFF0000) >> 16);
        }
        float[] gradient = gradientM();
        float maxGradient = gradient[0];
        for (int i = 1; i < gradient.length; i++) {
            if (gradient[i] > maxGradient) {
                maxGradient = gradient[i];
            }
        }
        float scaleFactor = 255.0F / maxGradient;
        if (this.gradientThreshold >= 0) {
            this.edegePoints = new ArrayList();
            for (int y = 1; y < this.height - 1; y++) {
                for (int x = 1; x < this.width - 1; x++) {
                    if (Math.round(scaleFactor * gradient[(y * this.width + x)]) >= this.gradientThreshold) {
                        Point point = new Point();
                        point.setX(x);
                        point.setY(y);
                        this.edegePoints.add(point);
                    }
                }
            }

        }

        return this.edegePoints;
    }

    public int getGrayPoint(int x, int y) {
        return this.grayData[(y * this.width + x)];
    }

    protected float[] gradientM() {
        float[] mag = new float[this.size];

        for (int y = 1; y < this.height - 1; y++) {
            for (int x = 1; x < this.width - 1; x++) {
                int gx = GradientX(x, y);
                mag[(y * this.width + x)] = Math.abs(gx);
            }
        }
        return mag;
    }

    protected final int GradientX(int x, int y) {
        return getGrayPoint(x, y) - getGrayPoint(x + 1, y + 1) + getGrayPoint(x + 1, y) - getGrayPoint(x, y + 1);
    }
}
