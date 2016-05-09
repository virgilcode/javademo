/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolcto.medicare;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Virgil
 */
public class Color {

    private double PROCESS_LEVEL = 0.1;

    public Color(double process_level) {
        this.PROCESS_LEVEL = process_level;
    }

    public Color() {
        this.PROCESS_LEVEL = 0.1;
    }

    public int[] darkColor(int[] rgb) {
        int[] RGB = new int[3];
        for (int i = 0; i < 3; i++) {
            RGB[i] = (int) Math.floor(rgb[i] * (1 - this.PROCESS_LEVEL));
        }
        return RGB;
    }

    public int[] lightColor(int[] rgb) {
        int[] RGB = new int[3];
        for (int i = 0; i < 3; i++) {
            RGB[i] = (int) Math.floor((255 - rgb[i]) * this.PROCESS_LEVEL + rgb[i]);
        }
        return RGB;
    }

    public String rgbToHex(int[] rgb) {
        return "" + Integer.toHexString(rgb[0]) + Integer.toHexString(rgb[1]) + Integer.toHexString(rgb[2]);
    }

    public int[] hexToRGB(String hex) {
        int[] a = new int[3];
        char[] array = hex.toCharArray();
        for (int i = 0; i < 3; i++) {
            a[i] = Integer.parseInt(("" + array[i * 2] + array[i * 2 + 1]), 16);
        }
        return a;
    }

    public int process(int[] rgb) throws Exception {
        int level = 9;
        String hex = rgbToHex(rgb);
        int main = Integer.parseInt(hex, 16);
        String path = this.getClass().getResource("").getPath() + "color.xml";
        File f = new File(path);
        DocumentBuilder db = null;
        DocumentBuilderFactory dbf = null;
        Element element = null;
        dbf = DocumentBuilderFactory.newInstance();

        try {
            db = dbf.newDocumentBuilder();
            Document dt = db.parse(f);
            element = dt.getDocumentElement();
            NodeList childNodes = element.getChildNodes();
            int positon = 0;//记录当前节点位置
            label1:
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node1 = childNodes.item(i);
                positon = i;
                if ("color".equals(node1.getNodeName())) {
                    NodeList nodeDetail = node1.getChildNodes();
                    for (int j = 0; j < nodeDetail.getLength(); j++) {
                        Node node2 = nodeDetail.item(j);
                        if ("value".equals(node2.getNodeName())) {
                            String hex_color = node2.getTextContent();
                            int color = Integer.parseInt(hex_color, 16);
                            if (color < main) {
                                break label1;
                            }
                        }
                    }
                }
            }
            NodeList list = childNodes.item(positon).getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                if ("level".equals(list.item(i).getNodeName())) {
                    String findLevel = childNodes.item(positon).getChildNodes().item(i).getTextContent();
                    level = Integer.parseInt(findLevel);
                }
            }
            return level;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Color.class.getName()).log(Level.SEVERE, null, ex);
        }
        return level;
    }

}
