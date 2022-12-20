package com.Cube.app.states;

import java.awt.Graphics;

import com.Cube.app.App;

public abstract class State {
	protected App app;
	
	private static State currentState;
	
	public State(App app) {
		this.app = app;
	}
	
	public static State getState() {
		return currentState;
	}
	
	public static void setState(State newState) {
		currentState = newState;
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
}
