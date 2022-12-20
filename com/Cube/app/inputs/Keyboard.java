package com.Cube.app.inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.Cube.app.shapes.MoveType;

public class Keyboard implements KeyListener {
	
	private int key = -1;
	private boolean keyUp;
	private boolean shift = false, ctrl = false;
	
	public MoveType getKey() {
		switch(key) {
		case 82:
			return MoveType.R;
		case 85:
			return MoveType.U;
		case 76:
			return MoveType.L;
		case 68:
			return MoveType.D;
		case 66:
			return MoveType.B;
		case 70:
			return MoveType.F;
		case 77:
			return MoveType.M;
		case 10:
			return MoveType.Scram;
		case 8:
			return MoveType.Solve;
		default:
			return MoveType.Unknown;
		}
	}
	
	public boolean keyUp() {
		return keyUp;
	}
	
	public void offKeyUp() {
		keyUp = false;
	}
	
	public boolean shift() {
		return shift;
	}
	
	public boolean ctrl() {
		return ctrl;
	}
	
	// implemented
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		key = e.getKeyCode();
		//System.out.println(key);
		if (e.getKeyCode() == 16)
			shift = true;
		else if (e.getKeyCode() == 17)
			ctrl = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		key = -1;
		keyUp = true;
		if (e.getKeyCode() == 16)
			shift = false;
		else if (e.getKeyCode() == 17)
			ctrl = false;
	}

}
