package com.Cube.app.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.Cube.app.points.Point3;

public class Polyhe {
	private Poly[] polys;
	private int[] renderOrder;
	
	public Polyhe(Poly... polys) {
		this.polys = polys;
		
		renderOrder = new int[this.polys.length];
		for (int i=0; i<this.polys.length; i++) {
			renderOrder[i] = i;
		}
	}
	
	public Polyhe(int size) {
		polys = new Poly[size];
		
		renderOrder = new int[size];
		for (int i=0; i<size; i++) {
			renderOrder[i] = i;
		}
	}
	
	public void setPolys(int index, Poly newPoly) {
		polys[index] = newPoly;
	}
	
	public void rotate(double degreesX, double degreesY, double degreesZ) {
		for (Poly p: polys) {
			p.rotate(degreesX, degreesY, degreesZ);
		}
		sortPoly();
	}
	
	public void translate(double dx, double dy, double dz) {
		for (Poly p: polys) {
			p.translate(dx, dy, dz);
		}
		sortPoly();
	}
	
	public double averageDepth() {
		double sum = 0;
		for (Poly p: polys) {
			sum += p.averageDepth();
		}
		return sum/polys.length;
	}
	
	public Point3 getAveragePoint() {
		Point3 p = new Point3(0,0,0);
		for (Poly po: polys) {
			p.x += po.getAveragePoint().x;
			p.y += po.getAveragePoint().y;
			p.z += po.getAveragePoint().z;
		}
		p.x /= polys.length;
		p.y /= polys.length;
		p.z /= polys.length;
		
		return p;
	}
	
	public FaceType getStickerFacing(Color c) {
		Point3 facePt = null;
		Point3 ave = getAveragePoint();
		
		for (Poly p: polys) {
			if (p.getColor().getRGB() == c.getRGB()) {
				facePt = p.getAveragePoint();
				break;
			}
		}
		
		if (facePt == null) 
			return FaceType.Unknown;
		
		if (Math.round(ave.y - facePt.y) > 30)
			return FaceType.Left;
		else if (Math.round(ave.y - facePt.y) < -30)
			return FaceType.Right;
		else if (Math.round(ave.z - facePt.z) > 30)
			return FaceType.Down;
		else if (Math.round(ave.z - facePt.z)  < -30)
			return FaceType.Up;
		else if (Math.round(ave.x - facePt.x) > 30)
			return FaceType.Back;
		
		return FaceType.Front;
	}
	
	public void sortPoly() {
		List<Poly> newPolys = new ArrayList<Poly>();
		
		for (Poly p: polys) {
			newPolys.add(p);
		}
		
		Collections.sort(newPolys, new Comparator<Poly>() {
			@Override
			public int compare(Poly p1, Poly p2) {
				return p2.averageDepth() - p1.averageDepth() < 0?1:-1;
			}
		});
		
		for (int i=0; i<polys.length; i++) {
			renderOrder[i] = newPolys.get(i).getId();
			//polys[i] = newPolys.get(i);
		}
	}
	
	public Poly[] getPolys() {
		return polys;
	}
	
	public int[] getRenderOrder() {
		return renderOrder;
	}
	
	public void setColor(Color front, Color top, Color right, Color bot, Color left, Color back) {
		polys[0].setColor(back);
		polys[1].setColor(bot);
		polys[2].setColor(left);
		polys[3].setColor(right);
		polys[4].setColor(top);
		polys[5].setColor(front);
	}
	
	public void render(Graphics g) {
		
//		for (Poly p: polys) {
//			System.out.println(p.getId());
//			p.render(g);
//		}
	}
}
