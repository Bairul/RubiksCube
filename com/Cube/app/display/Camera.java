package com.Cube.app.display;

import com.Cube.app.shapes.Cubie;

public class Camera {
	private double x, y;
	
	public Camera(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void tick(Cubie qb) {
		x-=0.01;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
}
