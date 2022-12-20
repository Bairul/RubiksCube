package com.Cube.app.shapes;

import java.awt.Color;
import java.awt.Graphics;

import com.Cube.app.App;
import com.Cube.app.points.Point3;

public class Cubie {
	
	private Polyhe cubie;
	private Polyhe sticks;
	private Color[] visibleStickers;
	private Color[] initialStickers;
	private int id;
	private boolean isOutside, isCorner;
	
	public Cubie(Color color, double length) {
		Point3 p1 = new Point3(length/2,-length/2,-length/2);
		Point3 p2 = new Point3(length/2,length/2,-length/2);
		Point3 p3 = new Point3(length/2,length/2,length/2);
		Point3 p4 = new Point3(length/2,-length/2,length/2);
		Point3 p5 = new Point3(-length/2,-length/2,-length/2);
		Point3 p6 = new Point3(-length/2,length/2,-length/2);
		Point3 p7 = new Point3(-length/2,length/2,length/2);
		Point3 p8 = new Point3(-length/2,-length/2,length/2);
		cubie = new Polyhe (
				new Poly(color, 0, p5,p6,p7,p8),
				new Poly(color, 1, p1,p2,p6,p5),
				new Poly(color, 2, p1,p5,p8,p4),
				new Poly(color, 3, p2,p6,p7,p3),
				new Poly(color, 4, p4,p3,p7,p8),
				new Poly(color, 5, p1,p2,p3,p4));
	}
	
	public Cubie(double length, int id, boolean isCorner, boolean isOutside) {
		this.id = id;
		this.isOutside = isOutside;
		this.isCorner = isCorner;
		Point3[] pts = new Point3[8];
		for (int i=-1, c=0; i<2; i+=2) {
			for (int k=-1; k<2; k+=2) {
				for (int j=-1; j<2; j+=2) {
					pts[c] = new Point3(length/2*i, length/2*k, length/2*j);
					c++;
				}
			}
		}
		
		cubie = new Polyhe (
				new Poly(Color.black, 0, pts[0],pts[2],pts[3],pts[1]),
				new Poly(Color.black, 1, pts[4],pts[6],pts[2],pts[0]),
				new Poly(Color.black, 2, pts[4],pts[0],pts[1],pts[5]),
				new Poly(Color.black, 3, pts[6],pts[2],pts[3],pts[7]),
				new Poly(Color.black, 4, pts[5],pts[7],pts[3],pts[1]),
				new Poly(Color.black, 5, pts[4],pts[6],pts[7],pts[5]));
		
		sticks = new Polyhe(6);
		for (int i=0; i<6; i++) {
			sticks.setPolys(i, new Poly(Color.black, i, cubie.getPolys()[i].getStickerPoints((int) (length/20))));
		}
		
		if (isCorner)
			visibleStickers = new Color[3];
		else
			visibleStickers = new Color[2];
	}
	
	public void rotateStickersZ(int type) { //only 1, -1, 0
		int[] stickerIndexes = {0,2,5,3};
		Color temp = sticks.getPolys()[stickerIndexes[0]].getColor();
		
		if (type == 0) {
			Color temp2 = sticks.getPolys()[stickerIndexes[1]].getColor();
			sticks.getPolys()[stickerIndexes[0]].setColor(sticks.getPolys()[stickerIndexes[2]].getColor());
			sticks.getPolys()[stickerIndexes[2]].setColor(temp);
			sticks.getPolys()[stickerIndexes[1]].setColor(sticks.getPolys()[stickerIndexes[3]].getColor());
			sticks.getPolys()[stickerIndexes[3]].setColor(temp2);
		} else {
			if (type == 1) {
				for (int i=0; i<stickerIndexes.length-1; i++) {
					sticks.getPolys()[stickerIndexes[i]].setColor(sticks.getPolys()[stickerIndexes[i+1]].getColor());
				}
			} else {
				for (int i=0, k=type; i>1-stickerIndexes.length; i+=type, k+=type) {
					sticks.getPolys()[stickerIndexes[i+ (i<0?4:0)]].setColor(sticks.getPolys()[stickerIndexes[k + 4]].getColor());
				}
			}
			sticks.getPolys()[type<0?stickerIndexes[1]:stickerIndexes[3]].setColor(temp);
		}
	}
	
	public void rotateStickersY(int type) { //only 1, -1, 0
		int[] stickerIndexes = {0,4,5,1};
		Color temp = sticks.getPolys()[stickerIndexes[0]].getColor();
		
		if (type == 0) {
			Color temp2 = sticks.getPolys()[stickerIndexes[1]].getColor();
			sticks.getPolys()[stickerIndexes[0]].setColor(sticks.getPolys()[stickerIndexes[2]].getColor());
			sticks.getPolys()[stickerIndexes[2]].setColor(temp);
			sticks.getPolys()[stickerIndexes[1]].setColor(sticks.getPolys()[stickerIndexes[3]].getColor());
			sticks.getPolys()[stickerIndexes[3]].setColor(temp2);
		} else {
			if (type == 1) {
				for (int i=0; i<stickerIndexes.length-1; i++) {
					sticks.getPolys()[stickerIndexes[i]].setColor(sticks.getPolys()[stickerIndexes[i+1]].getColor());
				}
			} else {
				for (int i=0, k=type; i>1-stickerIndexes.length; i+=type, k+=type) {
					sticks.getPolys()[stickerIndexes[i+ (i<0?4:0)]].setColor(sticks.getPolys()[stickerIndexes[k + 4]].getColor());
				}
			}
			sticks.getPolys()[type<0?stickerIndexes[1]:stickerIndexes[3]].setColor(temp);
		}
	}
	
	public void rotateStickersX(int type) { //only 1, -1, 0
		int[] stickerIndexes = {1,3,4,2};
		Color temp = sticks.getPolys()[stickerIndexes[0]].getColor();
		
		if (type == 0) {
			Color temp2 = sticks.getPolys()[stickerIndexes[1]].getColor();
			sticks.getPolys()[stickerIndexes[0]].setColor(sticks.getPolys()[stickerIndexes[2]].getColor());
			sticks.getPolys()[stickerIndexes[2]].setColor(temp);
			sticks.getPolys()[stickerIndexes[1]].setColor(sticks.getPolys()[stickerIndexes[3]].getColor());
			sticks.getPolys()[stickerIndexes[3]].setColor(temp2);
		} else {
			if (type == 1) {
				for (int i=0; i<stickerIndexes.length-1; i++) {
					sticks.getPolys()[stickerIndexes[i]].setColor(sticks.getPolys()[stickerIndexes[i+1]].getColor());
				}
			} else {
				for (int i=0, k=type; i>1-stickerIndexes.length; i+=type, k+=type) {
					sticks.getPolys()[stickerIndexes[i+ (i<0?4:0)]].setColor(sticks.getPolys()[stickerIndexes[k + 4]].getColor());
				}
			}
			sticks.getPolys()[type<0?stickerIndexes[1]:stickerIndexes[3]].setColor(temp);
		}
	}
	
	public Color[] getStickColors() {
		Color[] arr = new Color[6];
		
		for (int i=0; i<6; i++) {
			arr[i] = sticks.getPolys()[i].getColor();
		}
		
		return arr;
	}
	
	public void setInitialStickColors(Color front, Color top, Color right, Color bot, Color left, Color back) {
		Color[] set = {back, bot, left, right, top, front};
		setStickColors(set);
		initialStickers = set;
	}
	
	public boolean isCurrentColorsInitial() {
		for (int i=0; i<initialStickers.length; i++) {
			if (!initialStickers[i].equals(sticks.getPolys()[i].getColor())) {
				return false;
			}
		}
		return true;
	}
	
	public void setStickColors(Color[] newColors) {
		for (int i=0, c=0; i<6; i++) {
			sticks.getPolys()[i].setColor(newColors[i]);
			
			if (newColors[i] != Color.black) {
				visibleStickers[c] = newColors[i];
				c++;
			}
		}
	}
	
	public void rotate(double degreesX, double degreesY, double degreesZ) {
		cubie.rotate(degreesX, degreesY, degreesZ);
		sticks.rotate(degreesX, degreesY, degreesZ);
	}
	
	public void translate(double dx, double dy, double dz) {
		cubie.translate(dx, dy, dz);
		sticks.translate(dx, dy, dz);
	}
	
	public void sortPolys() {
		cubie.sortPoly();
		//sticks.sortPoly();
	}
	
	public double averageDepth() {
		return cubie.averageDepth();
	}
	
	public Point3 averagePoint() {
		return cubie.getAveragePoint();
	}
	
	public FaceType getStickerFacing(Color c) {
		return sticks.getStickerFacing(c);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int newId) {
		id = newId;
	}
	
	public boolean isOutside() {
		return isOutside;
	}
	
	public boolean isCorner() {
		return isCorner;
	}
	
	public Color[] getVisibleStickers() {
		return visibleStickers;
	}
	
	public void render(Graphics g) {
		for (int i: cubie.getRenderOrder()) {
			cubie.getPolys()[i].render(g);
			if (sticks.getPolys()[i].getColor() != Color.black)
				sticks.getPolys()[i].render(g);
		}
	}
}
