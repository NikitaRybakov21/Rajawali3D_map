package com.example.rajawali3d_map.customModel3D;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.random;

import android.graphics.PointF;

import org.rajawali3d.Object3D;
import org.rajawali3d.materials.Material;

import java.util.ArrayList;

public class Object3DSVGPolygon extends Object3D {

    private  float[] colors;
    private final ColorARGB colorARGB;
    public Object3DSVGPolygon(ColorARGB colorARGB,ArrayList<PointF> pathPoints, float base, float height, boolean enableTransparency,boolean isVisibleTriangulatePolygons) {
        this.colorARGB = colorARGB;

        drawPolygon(pathPoints,base,height,enableTransparency,isVisibleTriangulatePolygons);
    }

    private void drawPolygon(ArrayList<PointF> pathPoints, float base, float height, boolean enableTransparency, boolean isVisibleTriangulatePolygons) {

        ArrayList<Triangle> triangles = triangulatePolygon(pathPoints);

        int NUM_VERTICES_TRIANGLE = 3;
        int NUM_VERTICES = triangles.size() * NUM_VERTICES_TRIANGLE * 2  + pathPoints.size() * NUM_VERTICES_TRIANGLE * 2;

        float[] vertices = new float[NUM_VERTICES * NUM_VERTICES_TRIANGLE];
        colors = new float[NUM_VERTICES * 4];

        createVerticesLowerAndUpperBase(triangles,vertices,base,height);
        createVerticesSideWalls(triangles,pathPoints,vertices,base,height);

        if(isVisibleTriangulatePolygons) {
            visibleTriangulatePolygons(NUM_VERTICES);
        }

        drawObject3D(vertices,createIndices(NUM_VERTICES),colors,enableTransparency);
    }

    private ArrayList<Triangle> triangulatePolygon(ArrayList<PointF> arrayList) {
        TriangulatePolygon triangulatePolygon = new TriangulatePolygon();
        triangulatePolygon.setPoints((ArrayList<PointF>) arrayList.clone());
        triangulatePolygon.triangulatePolygon();

        return triangulatePolygon.getTriangles();
    }

    private void drawObject3D(float[] vertices, int[] indices, float[] colors, boolean enableTransparency) {
        setData(vertices, null, null, colors, indices, true);

        isContainer(false);
        setDoubleSided(true);
        setTransparent(enableTransparency);

        Material material = new Material();
        material.useVertexColors(true);

        setMaterial(material);
    }

    private void createWallsColors(int index, PointF pointF, PointF pointF2) {

        float deltaX = abs(pointF2.x - pointF.x);
        float deltaY = abs(pointF2.y - pointF.y);

        float lightK = 0.1f;
        float min = -0.1f;

        float lightPower;

        if(deltaX != 0) {
            float tg = deltaY / deltaX;
            float radian = (float) Math.atan(tg);

            lightPower = (float) ((radian / (PI/2f)) * lightK + min);
        } else {
            lightPower = lightK * 1 + min;
        }

        colors[index] = colorARGB.r + lightPower;
        colors[index + 1] = colorARGB.g + lightPower;
        colors[index + 2] = colorARGB.b + lightPower;
        colors[index + 3] = colorARGB.a;
    }

    private void createBaseColors(int index, boolean isUp) {
        float r1,g1,b1;

        float alpha = colorARGB.a;

        if(isUp) {
            r1 = colorARGB.r + 0.1f; g1 = colorARGB.g + 0.1f; b1 = colorARGB.b + 0.1f;
        } else  {
            r1 = colorARGB.r - 0.3f; g1 = colorARGB.g - 0.3f; b1 = colorARGB.b - 0.3f;
        }

        colors[index] = r1;
        colors[index + 1] = g1;
        colors[index + 2] = b1;
        colors[index + 3] = alpha;
    }

    private void visibleTriangulatePolygons(int NUM_VERTICES) {
        for (int i = 0; i < NUM_VERTICES; i++) {
            int index = i * 4;
            colors[index] = 255;
            colors[index + 1] = (float) random();
            colors[index + 2] = (float) random();
            colors[index + 3] = (float) random();
        }
    }

    private int[] createIndices(int NUM_VERTICES) {
        int[] indices = new int[NUM_VERTICES];
        for (int i = 0; i < NUM_VERTICES; i++) { indices[i] = i; }
        return indices;
    }

    private void createVerticesLowerAndUpperBase(ArrayList<Triangle> triangles, float[] vertices, float base, float height) {
        for (int i = 0; i < triangles.size(); i++) {
            Triangle tri = triangles.get(i);

            int index = i * 18;

            int upIndex = 0;
            float mainBase = base;
            for (int j = 0; j < 2; j++) {
                vertices[index     + upIndex] = tri.point1.x;
                vertices[index + 1 + upIndex] = mainBase;
                vertices[index + 2 + upIndex] = tri.point1.y;
                createBaseColors((index * 4)/3  +j*3*4 , upIndex > 0);

                vertices[index + 3 + upIndex] = tri.point2.x;
                vertices[index + 4 + upIndex] = mainBase;
                vertices[index + 5 + upIndex] = tri.point2.y;
                createBaseColors((index * 4)/3 + 4  +j*3*4, upIndex > 0);

                vertices[index + 6 + upIndex] = tri.point3.x;
                vertices[index + 7 + upIndex] = mainBase;
                vertices[index + 8 + upIndex] = tri.point3.y;
                createBaseColors((index * 4)/3 + 2*4  +j*3*4, upIndex > 0);

                mainBase += height;
                upIndex = 9;
            }
        }
    }

    private void createVerticesSideWalls(ArrayList<Triangle> triangles, ArrayList<PointF> pathPoints, float[] vertices, float base, float height) {
        int startIndex = triangles.size() * 18;

        for (int i = 0; i < pathPoints.size() ; i++) {
            int index = startIndex + i * 18;

            PointF pointF = pathPoints.get(i);
            PointF pointF2;

            if(i + 1 < pathPoints.size()) {
                pointF2 = pathPoints.get(i + 1);
            } else {
                pointF2 = pathPoints.get(0);
            }

            createOnePartTriangle(vertices,pointF,pointF2,base,height,index);
            createTwoPartTriangle(vertices,pointF,pointF2,base,height,index);
        }
    }

    private void createTwoPartTriangle(float[] vertices, PointF pointF, PointF pointF2, float base, float height, int index) {
        vertices[index + 9] = pointF2.x;
        vertices[index + 10] = base;
        vertices[index + 11] = pointF2.y;
        createWallsColors((index * 4)/3 + 3*4, pointF,pointF2);

        vertices[index + 12] = pointF2.x;
        vertices[index + 13] = base + height;
        vertices[index + 14] = pointF2.y;
        createWallsColors((index * 4)/3 + 4*4, pointF,pointF2);

        vertices[index + 15] = pointF.x;
        vertices[index + 16] = base + height;
        vertices[index + 17] = pointF.y;
        createWallsColors((index * 4)/3 + 5*4, pointF,pointF2);
    }

    private void createOnePartTriangle(float[] vertices, PointF pointF, PointF pointF2, float base, float height, int index) {
        vertices[index    ] = pointF.x;
        vertices[index + 1] = base;
        vertices[index + 2] = pointF.y;
        createWallsColors((index * 4) / 3, pointF,pointF2);

        vertices[index + 3] = pointF.x;
        vertices[index + 4] = base + height;
        vertices[index + 5] = pointF.y;
        createWallsColors((index * 4)/3 + 4, pointF,pointF2);

        vertices[index + 6] = pointF2.x;
        vertices[index + 7] = base;
        vertices[index + 8] = pointF2.y;
        createWallsColors((index  * 4)/3 + 2*4, pointF,pointF2);
    }
}