package com.Cube.app.shapes;

import java.awt.Color;

public class LinkedColors {
	Color color;
	private LinkedColors next;
	public LinkedColors getNext() {
		return next;
	}
	public boolean hasNext() {
		return next != null;
	}
	public void setNext(LinkedColors next) {
		this.next = next;
	}
	public LinkedColors(Color c) {
		color = c;
		this.next = next;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
}
