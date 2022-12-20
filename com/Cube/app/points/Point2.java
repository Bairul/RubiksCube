package com.Cube.app.points;

public class Point2 {
	public double x,y;
	
	public Point2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void moveTowards(double dir, double speed) {
		x += Math.cos(dir) * speed;
		y += Math.sin(dir) * speed;
	}
	
	public double magnitude() {
		return Math.sqrt(x*x + y*y);
	}
	
	public String toString() {
		return "("+x+", "+y+")";
	}
	
	public int intX() {
		return Math.round((float)x);
	}
	
	public int intY() {
		return Math.round((float)y);
	}
}
