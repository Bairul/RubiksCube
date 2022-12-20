package com.Cube.app.points;

import com.Cube.app.App;
import com.Cube.app.display.Display;

public class PointConverter {
	private static double camDist = 100;
	private static double width, height;
	private static int scale = 1;
	
	public static Point2 toPoint2( Point3 point3) {
		double x3 = point3.y * scale;
		double y3 = point3.z * scale;
		double depth = point3.x * scale;
		double[] newVal = scale(x3,y3,depth);
		
		double x2 = width + newVal[0];
		double y2 = height - newVal[1];
		return new Point2(x2,y2);
	}
	
	public static double getWidth() {
		return width;
	}

	public static void setWidth(double width) {
		PointConverter.width = width;
	}

	public static double getHeight() {
		return height;
	}

	public static void setHeight(double height) {
		PointConverter.height = height;
	}

	private static double[] scale(double x3, double y3, double depth) {
		double dist = Math.sqrt(x3*x3 + y3*y3);
		double theta = Math.atan2(y3, x3);
		double depth2 = camDist - depth;
		double localScale = Math.abs(1400/(1400+depth2));
		dist *= localScale;
		
		double[] val = new double[2];
		val[0] = dist * Math.cos(theta);
		val[1] = dist * Math.sin(theta);
		return val;
	}
	
	public static void rotateX(Point3 p, double degrees) {
		double radius = Math.sqrt(p.z*p.z + p.y*p.y);
		double theta = Math.atan2(p.z,p.y);
		theta += 2*Math.PI/360*degrees*-1;
		p.y = radius * Math.cos(theta);
		p.z = radius * Math.sin(theta);
	}
	
	public static void rotateY(Point3 p, double degrees) {
		double radius = Math.sqrt(p.z*p.z + p.x*p.x);
		double theta = Math.atan2(p.z,p.x);
		theta += 2*Math.PI/360*degrees*-1;
		p.x = radius * Math.cos(theta);
		p.z = radius * Math.sin(theta);
	}
	
	public static void rotateZ(Point3 p, double degrees) {
		double radius = Math.sqrt(p.x*p.x + p.y*p.y);
		double theta = Math.atan2(p.x,p.y);
		theta += 2*Math.PI/360*degrees*-1;
		p.y = radius * Math.cos(theta);
		p.x = radius * Math.sin(theta);
	}

	public static double getCamDist() {
		return camDist;
	}

	public static void setCamDist(double camDist) {
		PointConverter.camDist = camDist;
	}
}
