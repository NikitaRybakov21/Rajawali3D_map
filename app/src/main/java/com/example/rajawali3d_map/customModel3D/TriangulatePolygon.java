package com.example.rajawali3d_map.customModel3D;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

public class TriangulatePolygon {

    private ArrayList<PointF> points = new ArrayList<>();
    private final ArrayList<Triangle> triangles = new ArrayList<>();

    public void setPoints(ArrayList<PointF> points) {
        this.points = points;
    }

    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }

    public void triangulatePolygon() {

        boolean clockwise = isClockwise(points);
        int index = 0;

        while (points.size() > 2) {

            PointF p1 = points.get((index) % points.size());
            PointF p2 = points.get((index + 1) % points.size());
            PointF p3 = points.get((index + 2) % points.size());

            Vec2 v1 = new Vec2(p2.x - p1.x, p2.y - p1.y);
            Vec2 v2 = new Vec2(p3.x - p1.x, p3.y - p1.y);
            double cross = v1.cross(v2);

            Triangle triangle = new Triangle();
            triangle.addPoint1(new PointF(p1.x, p1.y));
            triangle.addPoint2(new PointF(p2.x, p2.y));
            triangle.addPoint3(new PointF(p3.x, p3.y));

            if (!clockwise && cross >= 0 && validTriangle(triangle, p1, p2, p3, points)) {
                points.remove(p2);
                triangles.add(triangle);
            }
            else if (clockwise && cross <= 0 && validTriangle(triangle, p1, p2, p3, points)) {
                points.remove(p2);
                triangles.add(triangle);
            }
            else {
                index++;
            }
        }

        points.size();
        points.clear();
    }
    private boolean validTriangle(Triangle triangle, PointF p1, PointF p2, PointF p3, List<PointF> points) {
        for (PointF p : points) {
            if (p != p1 && p != p2 && p != p3 && triangle.contains(p)) {
                return false;
            }
        }
        return true;
    }
    private boolean isClockwise(List<PointF> points) {
        int sum = 0;
        for (int i = 0; i < points.size(); i++) {
            PointF p1 = points.get(i);
            PointF p2 = points.get((i + 1) % points.size());
            sum += (p2.x - p1.x) * (p2.y + p1.y);
        }
        return sum >= 0;
    }
}
