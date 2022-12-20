package com.Cube.app.inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener {
	
	public int X = -1,Y = -1;
	private int Button = -1;
	private boolean leftLifted = false;
	
	public ClickType getButton() {
		switch(Button) {
		case 1:
			return ClickType.LeftClick;
		case 3:
			return ClickType.RightClick;
		default:
			return ClickType.Unknown;
		}
	}
	
	public boolean isLeftLifted() {
		if (leftLifted) {
			leftLifted = false;
			return true;
		}
		return false;
	}
	
	// implemented
	@Override
	public void mouseDragged(MouseEvent e) {
		X = e.getX();
		Y = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		X = e.getX();
		Y = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Button = e.getButton();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Button = -1;
		if (e.getButton() == MouseEvent.BUTTON3)
			leftLifted = true;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
