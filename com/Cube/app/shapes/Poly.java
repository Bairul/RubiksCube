package com.Cube.app.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import com.Cube.app.App;
import com.Cube.app.points.Point2;
import com.Cube.app.points.Point3;
import com.Cube.app.points.PointConverter;

public class Poly {
	private Point3[] points;
	private Polygon poly;
	private Color color;
	private int id;
	
	public Poly(Color c, int id, Point3... points) {
		color = c;
		this.id = id;
		this.points = new Point3[points.length];
		poly = new Polygon();
		
		for (int i=0; i<points.length; i++) {
			Point3 p = points[i];
			this.points[i] = new Point3(p.x,p.y,p.z);
			
			Point2 p2 = PointConverter.toPoint2(points[i]);
			poly.addPoint(p2.intX(), p2.intY());
		}
	}
	
	public void rotate(double degreesX, double degreesY, double degreesZ) {
		for (int i=0; i<points.length; i++) {
			Point3 p = points[i];
			PointConverter.rotateX(p, degreesX);
			PointConverter.rotateY(p, degreesY);
			PointConverter.rotateZ(p, degreesZ);
			
			Point2 p2 = PointConverter.toPoint2(p);
			poly.xpoints[i] = p2.intX();
			poly.ypoints[i] = p2.intY();
		}
	}
	
	public void translate(double dx, double dy, double dz) {
		poly.reset();
		for (int i=0; i<points.length; i++) {
			points[i].x += dx;
			points[i].y += dy;
			points[i].z += dz;
			
			Point2 p2 = PointConverter.toPoint2(points[i]);
			poly.addPoint(p2.intX(), p2.intY());
		}
	}
	
	public double averageDepth() {
		double sum = 0;
		for (Point3 p: points) {
			sum += p.x;
		}
		return sum/points.length;
	}
	
	public Point3 getAveragePoint() {
		Point3 p = new Point3(0,0,0);
		for (Point3 point: points) {
			p.x += point.x;
			p.y += point.y;
			p.z += point.z;
		}
		p.x /= points.length;
		p.y /= points.length;
		p.z /= points.length;
		
		return p;
	}
	
	public int getId() {
		return id;
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	public Color getColor() {
		return color;
	}
//	public Point3[] getPoints() {
//		Point3[] pts = new Point3[points.length];
//		for (int i=0; i<points.length; i++) {
//			pts[i] = new Point3(points[i].x, points[i].y, points[i].z);
//		}
//		return pts;
//	}
	
	public Point3[] getStickerPoints(int gap) {
		Point3[] pts = new Point3[points.length];
		for (int i=0; i<points.length; i++) {
			pts[i] = new Point3(points[i].x, points[i].y, points[i].z);
			pts[i].moveTowardsPt(getAveragePoint(), gap);
		}
		return pts;
	}
	
	public Point3[] getPoints() {
		return points;
	}
	
	public void render(Graphics g) {
		g.setColor(color);
		g.fillPolygon(poly);
	}
}
