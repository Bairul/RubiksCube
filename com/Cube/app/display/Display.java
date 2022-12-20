package com.Cube.app.display;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Display {
	private int width, height, scale;
	private String title;
	
	private JFrame frame;
	private Canvas canvas;
	
	public Display(final int width, final int scale, final String title) {
		this.width = width * scale;
		this.scale = scale;
		height = this.width*3/4;
		this.title = title;
		
		init();
	}

	private void init() {
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
	}
	
	public void setTitle(final String title) {
		this.title = title;
		frame.setTitle(title);
	}
	
	public JFrame frame() {
		return frame;
	}
	
	public Canvas canvas() {
		return canvas;
	}
	
	public int width() {
		return width;
	}
	
	public int height() {
		return height;
	}
}
