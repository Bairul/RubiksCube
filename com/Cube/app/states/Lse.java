package com.Cube.app.states;

import java.awt.Color;

import com.Cube.app.shapes.Cubie;
import com.Cube.app.shapes.FaceType;
import com.Cube.app.shapes.MoveType;
import com.Cube.app.shapes.Rubiks;

public class Lse {
	
	private static Cubie[] topEdges;
	private static Cubie bfEdge;
	private static Color top, bot;
	
	public static void setEdges(Cubie[] topE, Cubie botFront, Color top, Color bot) {
		topEdges = topE;
		bfEdge = botFront;
		Lse.top = top;
		Lse.bot = bot;
	}
	
	public static boolean solveLse4a(Color left, Color right, Rubiks rbx) {
		if (bfEdge == null || topEdges == null || topEdges.length == 0 || top == null || bot == null) 
			return false;
		
		// get orient center to good
		if (!rbx.getULayerCenter().getVisibleStickers()[0].equals(top)
				&& !rbx.getULayerCenter().getVisibleStickers()[0].equals(bot)) {
			rbx.turnLayer(MoveType.M, 1);
			System.out.println("M (orient)");
		}
		
		int numGoodEdgesTop = 0;
		boolean goodBfEdge = bfEdge.getStickerFacing(bot) == FaceType.Down
								|| bfEdge.getStickerFacing(top) == FaceType.Down;
		// counts number of good edges on top layer
		for (Cubie e: topEdges) {
			if (e.getStickerFacing(top) == FaceType.Up || e.getStickerFacing(bot) == FaceType.Up)
				numGoodEdgesTop++;
		}
		
		// determines 4a cases depending on number of good edges
		if (numGoodEdgesTop == 0) {
			if (goodBfEdge) {
				insert(rbx, true, true);
				System.out.println("M' U2 M");
			} else {
				insert(rbx, true, false);
			}
		} else if (numGoodEdgesTop == 1) {
			int aufTo = goodBfEdge ? 2 : 0; // 2 = front, 0 = back
			
			while (topEdges[aufTo].getStickerFacing(top) != FaceType.Up
					&& topEdges[aufTo].getStickerFacing(bot) != FaceType.Up) {
				rbx.turnLayer(MoveType.U, 1);
				System.out.println("U");
			}
			
			insert(rbx, !goodBfEdge, false);
		} else if (numGoodEdgesTop == 2) {
			while (topEdges[0].getStickerFacing(top) != FaceType.Up
					&& topEdges[0].getStickerFacing(bot) != FaceType.Up) {
				rbx.turnLayer(MoveType.U, 1);
				System.out.println("U");
			}
			if (topEdges[2].getStickerFacing(top) == FaceType.Up
					|| topEdges[2].getStickerFacing(bot) == FaceType.Up) {
				if (goodBfEdge) {
					rbx.turnLayer(MoveType.U, 1);
					System.out.println("U");
					insert(rbx, true, false);
				} else {
					insert(rbx, false, true);
				}
			} else {
				if (goodBfEdge) {
					if (topEdges[1].getStickerFacing(top) == FaceType.Up 
							|| topEdges[1].getStickerFacing(bot) == FaceType.Up) {
						insert(rbx, false, false);
						rbx.turnLayer(MoveType.U, 0);
						System.out.println("U2");
						insert(rbx, false, false);
					} else {
						rbx.turnLayer(MoveType.U, 1);
						System.out.println("U");
						insert(rbx, true, false);
						rbx.turnLayer(MoveType.U, 0);
						System.out.println("U2");
						insert(rbx, true, false);
					}
				} else {
					rbx.turnLayer(MoveType.M, 0);
					System.out.println("M2");
				}
			}
		} else if (numGoodEdgesTop == 3) {
			int aufTo = goodBfEdge ? 2 : 0;
			
			while (topEdges[aufTo].getStickerFacing(top) == FaceType.Up
					|| topEdges[aufTo].getStickerFacing(bot) == FaceType.Up) {
				rbx.turnLayer(MoveType.U, 1);
				System.out.println("U");
			}
			insert(rbx, !goodBfEdge, false);
			rbx.turnLayer(MoveType.U, 1);
			System.out.println("U");
			insert(rbx, !goodBfEdge, false);
		} else {
			// when 4a is solved, all edges are good edges
			if (goodBfEdge) {
				solveLrEdges(rbx, left, right);
				return true;
			} else
				insert(rbx, true, false);
		}
		
		return false;
	}
	
	private static void solveLrEdges(Rubiks rbx, Color left, Color right) {
		for (int i=0; i<topEdges.length; i++) {
			if (topEdges[2].getStickerFacing(left) != FaceType.Front 
					&& topEdges[2].getStickerFacing(right) != FaceType.Front) {
				rbx.turnLayer(MoveType.U, 1);
				System.out.println("U");
			} else {
				rbx.turnLayer(MoveType.M, 0);
				System.out.println("M2");
				break;
			}
		}
		for (int i=0; i<topEdges.length; i++) {
			if (topEdges[2].getStickerFacing(left) != FaceType.Front 
					&& topEdges[2].getStickerFacing(right) != FaceType.Front) {
				rbx.turnLayer(MoveType.U, 1);
				System.out.println("U");
			} else {
				insert(rbx, true, true);
				break;
			}
		}
	}
	
	public static void solveLrLayer(Color left, Color right, Cubie[] corners, Rubiks rbx) {
		Color bfColor = bfEdge.getStickerFacing(left) == FaceType.Front ? left : right;
		
		for (int i=0; i<corners.length; i++) {
			if (corners[1].getStickerFacing(bfColor) == FaceType.Front) {
				rbx.turnLayer(MoveType.U, 0);
				rbx.turnLayer(MoveType.M, 0);
				System.out.println("U2 M2");
				if (bfColor.equals(left)) {
					rbx.turnLayer(MoveType.U, -1);
					System.out.println("U'");
				} else { 
					rbx.turnLayer(MoveType.U, 1);
					System.out.println("U");
				}
				break;
			}
			rbx.turnLayer(MoveType.U, 1);
			System.out.println("U");
		}
	}
	
	public static void solve4c(Rubiks rbx) {
		if (topEdges == null || top == null) {
			return;
		}
		// orients center correctly
		if (rbx.getULayerCenter().getStickerFacing(top) != FaceType.Up) {
			rbx.turnLayer(MoveType.M, 0);
			System.out.println("M2");
			return;
		}
		// check for non cycle cases (when m slice edges on a face have same color)
		if ((topEdges[0].getStickerFacing(top) == FaceType.Up
				&& topEdges[2].getStickerFacing(top) == FaceType.Up)
			|| (topEdges[0].getStickerFacing(bot) == FaceType.Up
					&& topEdges[2].getStickerFacing(bot) == FaceType.Up)) {
			// check for solid bar on top
			if (topEdges[0].getStickerFacing(rbx.getULayerCenter().getVisibleStickers()[0]) == FaceType.Up) {
				bar(rbx);
			} else {
				column(rbx);
			}
		} else {
			insert(rbx, true, true);
			rbx.turnLayer(MoveType.U, 0);
			System.out.println("U2");
		}
	}
	
	private static void column(Rubiks rbx) {
		rbx.turnLayer(MoveType.M, -1);
		rbx.turnLayer(MoveType.U, 0);
		rbx.turnLayer(MoveType.M, 0);
		rbx.turnLayer(MoveType.U, 0);
		rbx.turnLayer(MoveType.M, -1);
		System.out.println("M' U2 M2 U2 M'");
	}
	
	private static void bar(Rubiks rbx) {
		rbx.turnLayer(MoveType.U, 0);
		rbx.turnLayer(MoveType.M, 0);
		rbx.turnLayer(MoveType.U, 0);
		System.out.println("U2 M2 U2");
	}
	
	private static void insert(Rubiks rbx, boolean front, boolean twice) {
		int q = -1, q2 = 0;
		String m1 = "", m2 = "'", u = "";
		if (front) {
			q = 1;
			m1 = "'";
			m2 = "";
		}
		if (twice) {
			u = "2";
			q2++;
		}
		rbx.turnLayer(MoveType.M, -1 * q);
		rbx.turnLayer(MoveType.U, 1 - q2);
		rbx.turnLayer(MoveType.M, 1 * q);
		System.out.printf("M%s U%s M%s\n", m1, u, m2);
	}
}
