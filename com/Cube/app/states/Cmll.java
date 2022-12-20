package com.Cube.app.states;

import java.awt.Color;

import com.Cube.app.shapes.Cubie;
import com.Cube.app.shapes.FaceType;
import com.Cube.app.shapes.MoveType;
import com.Cube.app.shapes.Rubiks;

public class Cmll {
	private static Cubie[] corners;
	
	public static void setCorners(Cubie[] c) {
		corners = c;
	}
	
	public static void executeCmll(Color top, Rubiks rbx) {
		if (corners == null)
			return;
		
		// orientate cmll
		int numTop = 0;
		for (Cubie qb: corners) {
			if (qb.getStickerFacing(top) == FaceType.Up)
				numTop++;
		}
		if (numTop == 0) {
			while (corners[1].getStickerFacing(top) != FaceType.Front || corners[2].getStickerFacing(top) != FaceType.Front) {
				rbx.turnLayer(MoveType.U, 1);
				System.out.println("U");
			}
			if (corners[0].getStickerFacing(top) == FaceType.Back) {
				System.out.println("H (cmll)");
				column(rbx);
			} else {
				System.out.println("Pi (cmll)");
				pi(rbx);
			}
		} else {
			while (corners[3].getStickerFacing(top) != FaceType.Up) {
				rbx.turnLayer(MoveType.U, 1);
				System.out.println("U");
			}
			if (numTop == 1) {
				if (corners[2].getStickerFacing(top) == FaceType.Front) {
					System.out.println("Sune (cmll)");
					sune(rbx);
				} else {
					System.out.println("Asune (cmll)");
					asune(rbx);
				}
			} else if (numTop == 2) {
				while (corners[1].getStickerFacing(top) != FaceType.Front) {
					rbx.turnLayer(MoveType.U, 1);
					System.out.println("U");
				}
				if (corners[3].getStickerFacing(top) == FaceType.Up) {
					if (corners[2].getStickerFacing(top) == FaceType.Up) {
						System.out.println("T (cmll)");
						omega(rbx);
					} else {
						System.out.println("U (cmll)");
						magnet(rbx);
					}
				} else {
					System.out.println("L (cmll)");
					commutator(rbx);
				}
			}
		}
		executePermutation(top, rbx);
		//return true;
	}
	private static void executePermutation(Color top, Rubiks rbx) {
		if (corners == null)
			return;
		
		int numHeadlights = 0, auf = 0;
		
		for (int i=0; i<corners.length; i++) {
			int j = (i+1) % corners.length;
			Color[] visiblesBack = corners[i].getVisibleStickers();
			Color[] visiblesFront = corners[j].getVisibleStickers();
			
			for (Color cb: visiblesBack) {
				if (cb.equals(top))
					continue;
				
				for (Color cf: visiblesFront) {
					if (cf.equals(top))
						continue;
					
					if (cb.equals(cf)
							&& corners[i].getStickerFacing(cb) == corners[j].getStickerFacing(cf)) {
						numHeadlights++;
					}
				}
			}
			if (numHeadlights != 1) {
				auf++;
			}
		}
		if (numHeadlights <= 1) {
			for (int i=0; i<auf; i++) {
				rbx.turnLayer(MoveType.U, 1);
				System.out.println("U");
			}
			if (numHeadlights == 0) {
				yperm(rbx);
			} else if (numHeadlights == 1) {
				jperm(rbx);
			} 
		} 
	}
	
	private static void sune(Rubiks rbx) {
		rbx.turnLayer(MoveType.U, 0);
		rbx.turnLayer(MoveType.R, 1);
		rbx.turnLayer(MoveType.U, 1);
		rbx.turnLayer(MoveType.R, -1);
		rbx.turnLayer(MoveType.U, 1);
		rbx.turnLayer(MoveType.R, 1);
		rbx.turnLayer(MoveType.U, 0);
		rbx.turnLayer(MoveType.R, -1);
		System.out.println("U2 R U R' U R U2 R'");
	}
	private static void asune(Rubiks rbx) {
		rbx.turnLayer(MoveType.R, 1);
		rbx.turnLayer(MoveType.U, 0);
		rbx.turnLayer(MoveType.R, -1);
		rbx.turnLayer(MoveType.U, -1);
		rbx.turnLayer(MoveType.R, 1);
		rbx.turnLayer(MoveType.U, -1);
		rbx.turnLayer(MoveType.R, -1);
		System.out.println("R U2 R' U' R U' R'");
	}
	private static void commutator(Rubiks rbx) {
		rbx.turnLayer(MoveType.F, 1);
		rbx.turnLayer(MoveType.R, -1);
		rbx.turnLayer(MoveType.F, -1);
		rbx.turnLayer(MoveType.R, 1);
		rbx.turnLayer(MoveType.U, 1);
		rbx.turnLayer(MoveType.R, 1);
		rbx.turnLayer(MoveType.U, -1);
		rbx.turnLayer(MoveType.R, -1);
		System.out.println("F R' F' R U R U' R'");
	}
	private static void magnet(Rubiks rbx) {
		rbx.turnLayer(MoveType.U, 1);
		rbx.turnLayer(MoveType.F, 1);
		rbx.turnLayer(MoveType.R, 1);
		rbx.turnLayer(MoveType.U, 1);
		rbx.turnLayer(MoveType.R, -1);
		rbx.turnLayer(MoveType.U, -1);
		rbx.turnLayer(MoveType.F, -1);
		System.out.println("U F R U R' U' F'");
	}
	private static void omega(Rubiks rbx) {
		rbx.turnLayer(MoveType.R, 1);
		rbx.turnLayer(MoveType.U, 1);
		rbx.turnLayer(MoveType.R, -1);
		rbx.turnLayer(MoveType.U, -1);
		rbx.turnLayer(MoveType.R, -1);
		rbx.turnLayer(MoveType.F, 1);
		rbx.turnLayer(MoveType.R, 1);
		rbx.turnLayer(MoveType.F, -1);
		System.out.println("R U R' U' R' F R F'");
	}
	private static void pi(Rubiks rbx) {
		magnet(rbx);
		rbx.turnLayer(MoveType.U, -1);
		System.out.println("U'");
		magnet(rbx);
		//System.out.println("U F R U R' U' F' U' U F R U R' U' F'");
	}
	private static void column(Rubiks rbx) {
		rbx.turnLayer(MoveType.R, 1);
		rbx.turnLayer(MoveType.U, 0);
		rbx.turnLayer(MoveType.R, -1);
		rbx.turnLayer(MoveType.U, -1);
		rbx.turnLayer(MoveType.R, 1);
		rbx.turnLayer(MoveType.U, 1);
		rbx.turnLayer(MoveType.R, -1);
		rbx.turnLayer(MoveType.U, -1);
		rbx.turnLayer(MoveType.R, 1);
		rbx.turnLayer(MoveType.U, -1);
		rbx.turnLayer(MoveType.R, -1);
		System.out.println("R U2 R' U' R U R' U' R U' R'");
	}
	private static void jperm(Rubiks rbx) {
		rbx.turnLayer(MoveType.R, 1);
		rbx.turnLayer(MoveType.U, 1);
		rbx.turnLayer(MoveType.R, -1);
		rbx.turnLayer(MoveType.F, -1);
		rbx.turnLayer(MoveType.R, 1);
		rbx.turnLayer(MoveType.U, 1);
		rbx.turnLayer(MoveType.R, -1);
		rbx.turnLayer(MoveType.U, -1);
		rbx.turnLayer(MoveType.R, -1);
		rbx.turnLayer(MoveType.F, 1);
		rbx.turnLayer(MoveType.R, 0);
		rbx.turnLayer(MoveType.U, -1);
		rbx.turnLayer(MoveType.R, -1);
		System.out.println("R U R' F' R U R' U' R' F R2 U' R' (jperm)");
	}
	private static void yperm(Rubiks rbx) {
		rbx.turnLayer(MoveType.L, 0);
		rbx.turnLayer(MoveType.U, 1);
		rbx.turnLayer(MoveType.L, -1);
		rbx.turnLayer(MoveType.F, 1);
		rbx.turnLayer(MoveType.L, 1);
		rbx.turnLayer(MoveType.U, -1);
		rbx.turnLayer(MoveType.R, 0);
		rbx.turnLayer(MoveType.D, -1);
		rbx.turnLayer(MoveType.B, -1);
		rbx.turnLayer(MoveType.D, -1);
		rbx.turnLayer(MoveType.B,  1);
		rbx.turnLayer(MoveType.L, 0);
		rbx.turnLayer(MoveType.R, 0);
		System.out.println("L2 U L' F L U' R2 D' B' D' B L2 R2 (yperm)");
	}
}
