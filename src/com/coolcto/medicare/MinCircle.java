/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolcto.medicare;

import java.util.List;

/**
 *
 * @author Virgil
 */
public class MinCircle {

    public class MPoint {

        int x;
        int y;

        public MPoint() {
            this.x = 0;
            this.y = 0;
        }
    }

    class Line {

        MPoint a;
        MPoint b;

        public Line() {
            this.a = new MPoint();
            this.b = new MPoint();
        }

        public Line(MPoint a, MPoint b) {
            this.a = a;
            this.b = b;
        }

        MPoint intersection(Line u, Line v) {
            MPoint res = u.a;
            double t = ((u.a.x - v.a.x) * (v.b.y - v.a.y) - (u.a.y - v.a.y) * (v.b.x - v.a.x))
                    / ((u.a.x - u.b.x) * (v.b.y - v.a.y) - (u.a.y - u.b.y) * (v.b.x - v.a.x));
            res.x += (u.b.x - u.a.x) * t;
            res.y += (u.b.y - u.a.y) * t;
            return res;
        }

        /*
         * 三角形外接圆
         * @Param 坐标点
         */
        MPoint center(MPoint a, MPoint b, MPoint c) {
            MPoint ret = new MPoint();
            int a1 = b.x - a.x, b1 = b.y - a.y, c1 = (a1 * a1 + b1 * b1) / 2;
            int a2 = c.x - a.x, b2 = c.y - a.y, c2 = (a2 * a2 + b2 * b2) / 2;
            int d = a1 * b2 - a2 * b1;
            ret.x = a.x + (c1 * b2 - c2 * b1) / d;
            ret.y = a.y + (a1 * c2 - a2 * c1) / d;
            return ret;
        }
    }
    int n;
    int x;
    int y;

    public double distance(MPoint p1, MPoint p2) {
        return (Math.hypot((p1.x - p2.x), (p1.y - p2.y)));
    }

    /*
     * @return d[0]坐标x d[1]坐标y d[3]半径r
     */
    public double[] getMinCircle(List<Point> list) {
        int n = list.size();
        MPoint[] mp = new MPoint[n];
        for (int i = 0; i < n; i++) {
            MPoint m = new MPoint();
            m.x = list.get(i).getX();
            m.y = list.get(i).getY();
            // System.out.println(list.get(i).getX() + " " + list.get(i).getY());
            mp[i] = m;
        }
        MPoint circle = mp[0];
        double r = 0;
        for (int i = 0; i < n; i++) {
            double dis = distance(circle, mp[i]);
            if (Double.compare(dis, r) <= 0) {
                continue;
            }
            circle = mp[i];
            r = 0;
            for (int j = 0; j < i; j++) {
                dis = distance(circle, mp[j]);
                if (Double.compare(dis, r) <= 0) {
                    continue;
                }
                circle.x = (mp[j].x + mp[i].x) / 2;
                circle.y = (mp[j].y + mp[i].y) / 2;
                r = distance(circle, mp[j]);
                for (int k = 0; k < j; k++) {
                    dis = distance(circle, mp[k]);
                    if (Double.compare(dis, r) <= 0) {
                        continue;
                    }
                    int a1 = mp[j].x - mp[i].x, b1 = mp[j].y - mp[i].y;
                    int a2 = mp[k].x - mp[i].x, b2 = mp[k].y - mp[i].y;
                    int d = a1 * b2 - a2 * b1;
                    if (d == 0) {
                        continue;
                    }
                    Line line = new Line();
                    circle = line.center(mp[i], mp[j], mp[k]);
                    r = distance(mp[k], circle);
                }
            }
        }
        double[] d = new double[3];
        d[0] = circle.x;
        d[1] = circle.y;
        d[2] = r;
        return d;
    }
}
