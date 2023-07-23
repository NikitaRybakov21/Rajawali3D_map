package com.example.rajawali3d_map.customModel3D;

import android.graphics.PointF;

public class Triangle {

    public PointF point1;
    public PointF point2;
    public PointF point3;

    public void addPoint1(PointF point) {
        this.point1 = point;
    }
    public void addPoint2(PointF point) {
        this.point2 = point;
    }
    public void addPoint3(PointF point) {
        this.point3 = point;
    }

    private float sign (PointF p1, PointF p2, PointF p3) {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    public boolean contains(PointF point) {
        boolean b1, b2, b3;

        b1 = sign(point, point1, point2) < 0.0f;
        b2 = sign(point, point2, point3) < 0.0f;
        b3 = sign(point, point3, point1) < 0.0f;

        return ((b1 == b2) && (b2 == b3));
    }

}
