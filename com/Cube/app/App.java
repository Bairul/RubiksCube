package com.Cube.app;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import com.Cube.app.display.Display;
import com.Cube.app.inputs.Keyboard;
import com.Cube.app.inputs.Mouse;
import com.Cube.app.points.PointConverter;
import com.Cube.app.shapes.MoveType;
import com.Cube.app.states.AppState;
import com.Cube.app.states.State;

public class App implements Runnable {
	
	private boolean running;
	private Thread thread;
	
	private static final String TITLE = "Rubik's Cube";
	
	private Display display;
	private BufferStrategy bs;
	private Graphics g;
	//private Graphics2D g2d;
	
	private State appState;
	
	// inputs
	private Mouse mouse;
	private Keyboard board;

	@Override
	public void run() {
		init();
		
		int fps = 30;
		double fpns = 1000000000/fps;
		long now;
		long past = System.nanoTime();
		double delta = 0;
		
		int frames = 0;
//		long timer = 0;
		
		while (running) {
			now = System.nanoTime();
//			timer += now-past;
			delta += (now-past)/fpns;
			past = now;
			
			while (delta >= 1) {
				tick();
				render();
				delta--;
				frames++;
			}
			
//			if (timer >= 1000000000) {
//				display.setTitle(TITLE+" | Fps: "+frames);
//				frames = 0;
//				timer = 0;
//			}
			
			try {
				thread.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		stop();
	}
	
	private void init() {
		display = new Display(128, 5, TITLE);
		
		PointConverter.setWidth(width()/2);
		PointConverter.setHeight(height()/2);
		
		appState = new AppState(this);
		State.setState(appState);
		
		mouse = new Mouse();
		display.canvas().addMouseListener(mouse);
		display.canvas().addMouseMotionListener(mouse);
		display.frame().addMouseListener(mouse);
		display.frame().addMouseMotionListener(mouse);
		
		board = new Keyboard();
		display.canvas().addKeyListener(board);
		display.frame().addKeyListener(board);
	}
	
	private void tick() {
		if (State.getState() != null)
			State.getState().tick();
	}

	private void render() {
		bs = display.canvas().getBufferStrategy();
		
		if (bs == null) {
			display.canvas().createBufferStrategy(3);
			return;
		}
		
		g = bs.getDrawGraphics();
		//g2d = (Graphics2D) g;
		
		//g.clearRect(0, 0, display.width(), display.height());
		g.fillRect(0, 0, display.width(), display.height());
		
		if (State.getState() != null)
			State.getState().render(g);
		
		g.dispose();
		bs.show();
	}

	public synchronized void start() {
		if (running) return;
		
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		if (!running) return;
		
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public int width() {
		return display.width();
	}
	
	public int height() {
		return display.height();
	}
	
	public Mouse mouse() {
		return mouse;
	}
	
	public Keyboard board() {
		return board;
	}
	
	public MoveType key() {
		return board.getKey();
	}

}
