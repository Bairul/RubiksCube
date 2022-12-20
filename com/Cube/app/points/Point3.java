package com.Cube.app.points;

public class Point3 {
	public double x,y,z;
	
	public Point3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void movePoint(double dx, double dy, double dz) {
		x+=dx;
		y+=dy;
		z+=dz;
	}
	
	public void moveTowardsPt(Point3 p, double dist) {
//		double dx = p.x - x;
//		double dy = p.y - y;
//		double dz = p.z - z;
//		
//		double dir = Math.atan2(dy, dx);
//		double zDir = Math.atan2(dz,dir*dist);
//		
//		x += Math.sin(dir);
//		y += Math.cos(dir);
//		z += Math.sin(zDir);
		if (p.x > x)
			x += dist;
		else if (p.x < x)
			x -= dist;
		
		if (p.y > y)
			y += dist;
		else if (p.y < y)
			y -= dist;
		
		if (p.z > z)
			z += dist;
		else if (p.z < z)
			z -= dist;
//		x += (x > p.x ?-1:1) * dist;
//		y += (y > p.y ?-1:1) * dist;
//		z += (z > p.z ?-1:1) * dist;
	}
	
	public double magnitude() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	public double magnitudeTo(Point3 p) {
		double dx = x - p.x;
		double dy = y - p.y;
		double dz = z - p.z;
		
		return Math.sqrt(dx*dx + dy*dy + dz*dz);
	}
	
	public String toString() {
		return "("+x+", "+y+", "+z+")";
	}
}
