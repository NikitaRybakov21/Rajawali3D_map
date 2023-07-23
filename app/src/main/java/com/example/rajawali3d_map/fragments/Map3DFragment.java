package com.example.rajawali3d_map.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rajawali3d_map.R;
import com.example.rajawali3d_map.customModel3D.ColorARGB;
import com.example.rajawali3d_map.customModel3D.Object3DSVGPolygon;

import org.rajawali3d.debug.DebugVisualizer;
import org.rajawali3d.debug.GridFloor;
import org.rajawali3d.materials.Material;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Line3D;

import java.util.ArrayList;
import java.util.Stack;

public class Map3DFragment extends Base3DFragment implements View.OnTouchListener {
	ColoredLinesRenderer renderClass;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.object_picking_overlay, mLayout, true);
		((View) mRenderSurface).setOnTouchListener(this);

		view.findViewById(R.id.left123).setOnClickListener(view1 -> {

			Vector3 vector3Pos = renderClass.getCurrentCamera().getPosition();
			Vector3 vector3look = renderClass.getCurrentCamera().getLookAt();

			float deltaX = (float) (vector3Pos.x - vector3look.x);
			float deltaY = (float) (vector3Pos.z - vector3look.z);

			float l = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
			float tg = deltaY / deltaX;

			float n = 0f;
			if(deltaX < 0 ) {
				n = (float) - Math.PI;
			}

			float radian = n + (float) Math.atan(tg);

			radian -= 0.04f;

			float x = (float) (vector3Pos.x - l * Math.cos(radian));
			float y = (float) (vector3Pos.z - l * Math.sin(radian));

			renderClass.getCurrentCamera().setLookAt(x,  vector3look.y , y  );
		});

		view.findViewById(R.id.right123).setOnClickListener(view1 -> {
			Vector3 vector3Pos = renderClass.getCurrentCamera().getPosition();
			Vector3 vector3look = renderClass.getCurrentCamera().getLookAt();

			float deltaX = (float) (vector3Pos.x - vector3look.x);
			float deltaY = (float) (vector3Pos.z - vector3look.z);

			float l = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
			float tg = deltaY / deltaX;

			float n = 0f;
			if(deltaX < 0 ) {
				n = (float) - Math.PI;
			}

			float radian = n + (float) Math.atan(tg);

			radian += 0.04f;

			float x = (float) (vector3Pos.x - l * Math.cos(radian));
			float y = (float) (vector3Pos.z - l * Math.sin(radian));

			renderClass.getCurrentCamera().setLookAt(x,  vector3look.y , y  );
		});

		return mLayout;
	}

	@Override
    public AExampleRenderer createRenderer() {
		 renderClass = new ColoredLinesRenderer(getActivity(), this);
		 return renderClass;
	}

	float tempX = 0;
	float tempY = 0;

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {

		if(motionEvent.getAction() == 0) {
			tempX = motionEvent.getX();
			tempY = motionEvent.getY();
		}

		if(motionEvent.getAction() == 2) {

			float deltaX = - (motionEvent.getX() - tempX) / 200f;
			float deltaY = - (motionEvent.getY() - tempY) / 200f;

			tempX = motionEvent.getX();
			tempY = motionEvent.getY();

			Vector3 vector3Pos = renderClass.getCurrentCamera().getPosition();
			Vector3 vector3look = renderClass.getCurrentCamera().getLookAt();

			Vector3 newVector3Pos = new Vector3(vector3Pos.x + deltaX,vector3Pos.y  ,vector3Pos.z + deltaY);
			renderClass.getCurrentCamera().setPosition(newVector3Pos);

			Vector3 newVector3look = new Vector3(vector3look.x + deltaX,vector3look.y,vector3look.z + deltaY);
		//	Vector3 newVector3look= new Vector3(0,0,0);

			renderClass.getCurrentCamera().setLookAt(newVector3look);
		}
		return true;
	}


	private static final class ColoredLinesRenderer extends AExampleRenderer {

		public ColoredLinesRenderer(Context context, @Nullable Base3DFragment fragment) {
			super(context, fragment);
		}

		@Override
		protected void initScene() {
			getCurrentScene().setBackgroundColor(Color.rgb(245, 245, 255));

			getCurrentCamera().setPosition(5, 10, 20);
			getCurrentCamera().setLookAt(0, 0, 0);

			drawLine();
			drawGrid();

			ArrayList<PointF> arrayList = new ArrayList<>();
			arrayList.add(new PointF(0,0));
			arrayList.add(new PointF(0,1));
			arrayList.add(new PointF(1,2));
			arrayList.add(new PointF(2,2));
			arrayList.add(new PointF(3,1));
			arrayList.add(new PointF(3,0));
			arrayList.add(new PointF(1.5f,-2f));

			ArrayList<PointF> arrayList2 = new ArrayList<>();
			arrayList2.add(new PointF(0,0));
			arrayList2.add(new PointF(0,1));
			arrayList2.add(new PointF(1,1));

			Object3DSVGPolygon object3DSVGPolygon = new Object3DSVGPolygon(
					new ColorARGB(0.4f, 0.5f, 0.5f, 0.8f),
					arrayList,
					0, 2,
					false,
					false
			);
			getCurrentScene().addChild(object3DSVGPolygon);

			Object3DSVGPolygon object3DSVGPolygon2 = new Object3DSVGPolygon(
					new ColorARGB(0.5f, 0.5f, 0.8f, 0.8f),
					arrayList2,
					0, 2,
					true,
					false
			);
			object3DSVGPolygon2.setPosition(3.01,0,0);
			getCurrentScene().addChild(object3DSVGPolygon2);
			//List<CompoundCurve3D> curve = svgPath.parseString("M 2226,1229.2  L2264.978,1201.752  L2266.958,1202.522  L2227.063,1230.63 Z");
		}
		private void drawGrid() {
			DebugVisualizer debugViz = new DebugVisualizer(this);

			GridFloor gridFloor = new GridFloor();
			gridFloor.setColor(Color.BLACK);
			debugViz.addChild(gridFloor);
			getCurrentScene().addChild(debugViz);
		}

		private void drawLine() {
			Stack<Vector3> points2 = new Stack<>();

			points2.add(new Vector3(0, 0, 0));
			points2.add(new Vector3(0, 0, 20));

			points2.add(new Vector3(0, 0, 0));
			points2.add(new Vector3(20, 0, 0));

			points2.add(new Vector3(0, 0, 0));
			points2.add(new Vector3(0, 20, 0));


			points2.add(new Vector3(0, 0, 0));
			points2.add(new Vector3(0, 0, -20));

			points2.add(new Vector3(0, 0, 0));
			points2.add(new Vector3(-20, 0, 0));

			Material material3 = new Material();
			Line3D line2 = new Line3D(points2, 1, Color.BLACK);
			line2.setMaterial(material3);
			getCurrentScene().addChild(line2);
		}
	}
}
