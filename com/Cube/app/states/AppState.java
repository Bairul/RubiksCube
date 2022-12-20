package com.Cube.app.states;

import java.awt.Color;
import java.awt.Graphics;

import com.Cube.app.App;
import com.Cube.app.inputs.ClickType;
import com.Cube.app.shapes.Cubie;
import com.Cube.app.shapes.FaceType;
import com.Cube.app.shapes.LinkedColors;
import com.Cube.app.shapes.MoveType;
import com.Cube.app.shapes.Rubiks;

public class AppState extends State {
	
	private Rubiks rbx;
	private int[] uLayerE, dLayerE, rLayerE, lLayerE, fLayerE, bLayerE, mLayer;
	private int[] uLayerC, dLayerC, rLayerC, lLayerC, fLayerC, bLayerC;
	private int moveCount;
	private int dimension, fullLength, layerSize;
	
	private int initialX,initialY;
	private double mouseSens = 3;

	public AppState(App app) {
		super(app);
		dimension = 3;
		fullLength = 240;
		layerSize = dimension*dimension;
		
		rbx = new Rubiks(app, dimension, fullLength/dimension);
		
		getLayerIndexes();
	}
	
	public void getLayerIndexes() {
		uLayerE = rbx.getEdgesIndexLayer(MoveType.U); uLayerC = rbx.getCornerIndexLayer(MoveType.U);
		dLayerE = rbx.getEdgesIndexLayer(MoveType.D); dLayerC = rbx.getCornerIndexLayer(MoveType.D);
		rLayerE = rbx.getEdgesIndexLayer(MoveType.R); rLayerC = rbx.getCornerIndexLayer(MoveType.R);
		lLayerE = rbx.getEdgesIndexLayer(MoveType.L); lLayerC = rbx.getCornerIndexLayer(MoveType.L);
		fLayerE = rbx.getEdgesIndexLayer(MoveType.F); fLayerC = rbx.getCornerIndexLayer(MoveType.F);
		bLayerE = rbx.getEdgesIndexLayer(MoveType.B); bLayerC = rbx.getCornerIndexLayer(MoveType.B);
		mLayer = rbx.getEdgesIndexLayer(MoveType.M);
	}

	@Override
	public void tick() {
		keyInput();
		mouseInput();
	}
	
	private void keyInput() {
		if (app.board().keyUp()) {
			if (app.key() == MoveType.Scram) {
				rbx.scramble();
				moveCount = 0;
				reset();
				app.board().offKeyUp();
				return;
			} else if (app.key() == MoveType.Solve) {
				solve();
				app.board().offKeyUp();
				return;
			} else if (app.key() == MoveType.Unknown)
				return;
			
			if (app.board().shift())
				rbx.turnLayer(app.key(), -1); // prime
			else if (app.board().ctrl())
				rbx.turnLayer(app.key(), 0); // double
			else
				rbx.turnLayer(app.key(), 1); // normal
			moveCount++;
			app.board().offKeyUp();
		}
	}
	
	private void mouseInput() {
		int x = app.mouse().X;
		int y = app.mouse().Y;
		if (app.mouse().getButton() == ClickType.LeftClick) {
			double dx = (x-initialX)/mouseSens;
			double dy = (y-initialY)/mouseSens;
			double dz = 0;
			
			rbx.rotate(dz, dy, dx);
		} 
		
		initialX = x;
		initialY = y;
	}
	
	private boolean solvedFBSquare, solvedFB, solvedSBSquare, solvedSB, solvedCmll, solved4a, solvedLr;
	
	public void solve() {
		if (rbx.isSolved()) {
			System.out.println("Solved");
			return;
		}
		if (solvedCmll) {
			Cubie[] top = new Cubie[4];
			Cubie bf = null;
			for (int i=0, t=0; i<rbx.getEdges().length && t<4; i++) {
				if (rbx.getEdges()[i].getId() == uLayerE[t]) {
					top[t] = rbx.getEdges()[i];
					t++;
					i=-1;
				} else if (rbx.getEdges()[i].getId() == dLayerE[0]) {
					bf = rbx.getEdges()[i];
				}
			}
			Lse.setEdges(top, bf, Color.yellow, Color.white);
			if (!solved4a) {
				solved4a = Lse.solveLse4a(Color.blue, Color.green, rbx);
			} else {
				if (solvedLr) {
					Lse.solve4c(rbx);
				} else {
					Cubie[] cmll = new Cubie[4];
					
					for (int i=0, c=0; i<rbx.getCorners().length && c<4; i++) {
						if (rbx.getCorners()[i].getId() == uLayerC[c]) {
							cmll[c] = rbx.getCorners()[i];
							c++;
							i=-1;
						}
					}
					Lse.solveLrLayer(Color.blue, Color.green, cmll, rbx);
					solvedLr = true;
				}
			}
		} else if (solvedSB) {
			Cubie[] cmll = new Cubie[4];
			
			for (int i=0, c=0; i<rbx.getCorners().length && c<4; i++) {
				if (rbx.getCorners()[i].getId() == uLayerC[c]) {
					cmll[c] = rbx.getCorners()[i];
					c++;
					i=-1;
				}
			}
			System.out.println("====");
			Cmll.setCorners(cmll);
			Cmll.executeCmll(Color.yellow, rbx);
			solvedCmll =  true;
		} else if (solvedFB) {
			// System.out.println("ran");
			if (solveSBPole(Color.green, Color.white)) {
				if (solvedSBSquare) {
					solvedSB = solveSBPair(new Color(255,140,0), Color.green);
				} else {
					solvedSBSquare = solveSBPair(Color.red, Color.green); 
				}
			}
		} else {
			if (solveFBPole(Color.blue, Color.white)) {
				if (solvedFBSquare) {
					solvedFB = solveFBPair(Color.red, Color.blue);
				} else {
					solvedFBSquare = solveFBPair(new Color(255,140,0), Color.blue); 
				}
				
			}
		}
		
	}
	
	private boolean solveFBPole(Color color1, Color color2) {
		for (Cubie e: rbx.getEdges()) {
			if ((e.getVisibleStickers()[0].getRGB() == color1.getRGB() && e.getVisibleStickers()[1].getRGB() == color2.getRGB()) ||
					(e.getVisibleStickers()[0].getRGB() == color2.getRGB() && e.getVisibleStickers()[1].getRGB() == color1.getRGB())) {
				if ((int) (e.averagePoint().magnitudeTo(rbx.getEdges()[6].averagePoint())) < 1) {
					if (e.getStickerFacing(color2) != FaceType.Down) {
						System.out.println("D F L");
						rbx.turnLayer(MoveType.D, 1);
						rbx.turnLayer(MoveType.F, 1);
						rbx.turnLayer(MoveType.L, 1);
					}
					return true;
				} else if (Math.abs((int) (e.averagePoint().magnitudeTo(rbx.getEdges()[6].averagePoint())) - 113) < 2) {
					for (int i=0; i<dLayerE.length; i++) {
						if (e.getId() == dLayerE[i]) {
							rbx.turnLayer(MoveType.D, 1);
							System.out.println("D");
							break;
						} else if (e.getId() == lLayerE[i]) {
							rbx.turnLayer(MoveType.L, 1);
							System.out.println("L");
							break;
						}
					}
				} else if (Math.abs((int) (e.averagePoint().magnitudeTo(rbx.getEdges()[6].averagePoint())) - 160) < 2) {
					for (int i=0; i<dLayerE.length; i++) {
						if (e.getId() == dLayerE[i]) {
							rbx.turnLayer(MoveType.D, 0);
							System.out.println("D2");
							break;
						} else if (e.getId() == lLayerE[i]) {
							rbx.turnLayer(MoveType.L, 0);
							System.out.println("L2");
							break;
						}
					}
				} else if (Math.abs((int) (e.averagePoint().magnitudeTo(rbx.getEdges()[6].averagePoint())) - 195) < 2) {
					for (int i=0; i<dLayerE.length; i++) {
						if (e.getId() == uLayerE[i]) {
							rbx.turnLayer(MoveType.U, 1);
							System.out.println("U");
							break;
						} else if (e.getId() == rLayerE[i]) {
							rbx.turnLayer(MoveType.R, 1);
							System.out.println("R");
							break;
						}
					}
				} else if (Math.abs((int) (e.averagePoint().magnitudeTo(rbx.getEdges()[6].averagePoint())) - 226) < 2) {
					rbx.turnLayer(MoveType.U, 0);
					rbx.turnLayer(MoveType.L, 0);
					System.out.println("U2 L2");
				}
			}
		}
		return false;
	}
	
	private boolean solveFBPair(Color color1, Color color2) {
		boolean solvedEdge = false, solvedPair = false;
		
		for (Cubie e: rbx.getEdges()) {
			if ((e.getVisibleStickers()[0].getRGB() == color1.getRGB() && e.getVisibleStickers()[1].getRGB() == color2.getRGB()) ||
					(e.getVisibleStickers()[0].getRGB() == color2.getRGB() && e.getVisibleStickers()[1].getRGB() == color1.getRGB())) {
				if ((int) (e.averagePoint().magnitudeTo(rbx.getEdges()[3].averagePoint())) < 1) {
					if (e.getStickerFacing(color2) != FaceType.Down) {
						System.out.println("M' U2 M2");
						rbx.turnLayer(MoveType.M, -1);
						rbx.turnLayer(MoveType.U, 0);
						rbx.turnLayer(MoveType.M, 0);
					}
					solvedEdge = true;
				} else if (Math.abs((int) (e.averagePoint().magnitudeTo(rbx.getEdges()[3].averagePoint())) - 113) < 2) {
					for (int i=0; i<dLayerE.length; i++) {
						if (e.getId() == fLayerE[i]) {
							rbx.turnLayer(MoveType.F, 1);
							System.out.println("F");
							break;
						} else if (e.getId() == dLayerE[i]) {
							rbx.turnLayer(MoveType.R, 1);
							System.out.println("R");
							break;
						}
					}
				} else if (Math.abs((int) (e.averagePoint().magnitudeTo(rbx.getEdges()[3].averagePoint())) - 160) < 2) {
					rbx.turnLayer(MoveType.M, 1);
					System.out.println("M");
				} else if (Math.abs((int) (e.averagePoint().magnitudeTo(rbx.getEdges()[3].averagePoint())) - 195) < 2) {
					for (int i=0; i<dLayerE.length; i++) {
						//System.out.println(e.getId() + " " + rLayerE[i]);
						if (e.getId() == uLayerE[i]) {
							rbx.turnLayer(MoveType.U, 1);
							System.out.println("U");
							break;
						} else if (e.getId() == lLayerE[i] && i != 0) {
							rbx.turnLayer(MoveType.B, -1);
							System.out.println("B'");
							break;
						} else if (e.getId() == bLayerE[i]) {
							rbx.turnLayer(MoveType.R, 0);
							System.out.println("R2");
							break;
						}
					}
				} else if (Math.abs((int) (e.averagePoint().magnitudeTo(rbx.getEdges()[3].averagePoint())) - 226) < 2) {
					rbx.turnLayer(MoveType.M, 0);
					System.out.println("M2");
				}
			}
		}
		if (solvedEdge) {
			for (Cubie c: rbx.getCorners()) {
				LinkedColors[] lc = new LinkedColors[3];
				lc[0] = new LinkedColors(c.getVisibleStickers()[0]);
				for (int i=1; i<c.getVisibleStickers().length; i++) {
					lc[i] = new LinkedColors(c.getVisibleStickers()[i]);
					lc[i-1].setNext(lc[i]);
				}
				LinkedColors current = lc[0];
				boolean match = true;
				while (current != null && match) {
					if (current.getColor().getRGB() != color1.getRGB() && current.getColor().getRGB() != color2.getRGB() && 
							current.getColor().getRGB() != Color.white.getRGB())
						match = false;
					current = current.getNext();
				}
				
				if (match) {
					// closest = 160, 226, 277
					if ((int) (c.averagePoint().magnitudeTo(rbx.getCorners()[1].averagePoint())) < 1) {
						while (c.getStickerFacing(Color.white) != FaceType.Right) {
							System.out.println("R U");
							rbx.turnLayer(MoveType.R, 1);
							rbx.turnLayer(MoveType.U, 1);
						}
						solvedPair = true;
					} else if (Math.abs((int) (c.averagePoint().magnitudeTo(rbx.getCorners()[1].averagePoint())) - 160) < 2) {
						for (int i=0; i<dLayerC.length; i++) {
							if (c.getId() == bLayerC[i]) {
								rbx.turnLayer(MoveType.R, -1);
								System.out.println("R'");
								break;
							} else if (c.getId() == rLayerC[i]) {
								rbx.turnLayer(MoveType.R, 1);
								System.out.println("R");
								break;
							}else if (c.getId() == fLayerC[i]) {
								rbx.turnLayer(MoveType.U, -1);
								System.out.println("U'");
								break;
							}
						}
					} else if (Math.abs((int) (c.averagePoint().magnitudeTo(rbx.getCorners()[1].averagePoint())) - 226) < 2) {
						for (int i=0; i<dLayerC.length; i++) {
							if (c.getId() == rLayerC[i]) {
								rbx.turnLayer(MoveType.R, 0);
								System.out.println("R2");
								break;
							} else if (c.getId() == uLayerC[i]) {
								rbx.turnLayer(MoveType.U, 0);
								System.out.println("U2");
								break;
							} else if (c.getId() == fLayerC[i]) {
								rbx.turnLayer(MoveType.L, -1);
								rbx.turnLayer(MoveType.U, -1);
								rbx.turnLayer(MoveType.L, 1);
								System.out.println("L' U' L");
								break;
							}
						}
					} else if (Math.abs((int) (c.averagePoint().magnitudeTo(rbx.getCorners()[1].averagePoint())) - 277) < 2) {
						rbx.turnLayer(MoveType.B, 0);
						System.out.println("B2");
					}
				}
			}
			
			if (solvedPair) {
				if (rbx.getCorners()[1].getStickerFacing(color2) == FaceType.Up) {
					rbx.turnLayer(MoveType.U, 0);
					rbx.turnLayer(MoveType.M, 0);
					rbx.turnLayer(MoveType.B, 1);
					System.out.println("U2 M2 B");
				} else {
					rbx.turnLayer(MoveType.R, -1);
					rbx.turnLayer(MoveType.F, 1);
					System.out.println("R' F");
				}
				return true;
			}
		}
		return false;
	}
	
	private boolean solveSBPole(Color color1, Color color2) {
		for (Cubie e: rbx.getEdges()) {
			if ((e.getVisibleStickers()[0].getRGB() == color1.getRGB() && e.getVisibleStickers()[1].getRGB() == color2.getRGB()) ||
					(e.getVisibleStickers()[0].getRGB() == color2.getRGB() && e.getVisibleStickers()[1].getRGB() == color1.getRGB())) {
				if ((int) (e.averagePoint().magnitudeTo(rbx.getEdges()[7].averagePoint())) < 1) {
					if (e.getStickerFacing(color2) != FaceType.Down) {
						System.out.println("R2 U M' U R2");
						rbx.turnLayer(MoveType.R, 0);
						rbx.turnLayer(MoveType.U, 1);
						rbx.turnLayer(MoveType.M, -1);
						rbx.turnLayer(MoveType.U, 1);
						rbx.turnLayer(MoveType.R, 0);
					}
					return true;
				} else if (Math.abs((int) (e.averagePoint().magnitudeTo(rbx.getEdges()[7].averagePoint())) - 113) < 2) {
					for (int i=0; i<dLayerE.length; i++) {
						if (e.getId() == dLayerE[i]) {
							rbx.turnLayer(MoveType.M, 0);
							System.out.println("M2");
							break;
						} else if (e.getId() == rLayerE[i]) {
							rbx.turnLayer(MoveType.R, 1);
							System.out.println("R");
							break;
						}
					}
				} else if (Math.abs((int) (e.averagePoint().magnitudeTo(rbx.getEdges()[7].averagePoint())) - 160) < 2) {
					rbx.turnLayer(MoveType.R, 0);
					System.out.println("R2");
				} else if (Math.abs((int) (e.averagePoint().magnitudeTo(rbx.getEdges()[7].averagePoint())) - 195) < 2) {
					for (int i=0; i<dLayerE.length; i++) {
						if (e.getId() == uLayerE[i]) {
							rbx.turnLayer(MoveType.U, 1);
							System.out.println("U");
							break;
						}
					}
				} else if (Math.abs((int) (e.averagePoint().magnitudeTo(rbx.getEdges()[7].averagePoint())) - 226) < 2) {
					rbx.turnLayer(MoveType.U, 0);
					rbx.turnLayer(MoveType.R, 0);
					System.out.println("U2 R2");
				}
			}
		}
		return false;
	}
	
	private boolean solveSBPair(Color color1, Color color2) {
		boolean solvedEdge = false, solvedPair = false;
		
		for (Cubie e: rbx.getEdges()) {
			if ((e.getVisibleStickers()[0].getRGB() == color1.getRGB() && e.getVisibleStickers()[1].getRGB() == color2.getRGB()) ||
					(e.getVisibleStickers()[0].getRGB() == color2.getRGB() && e.getVisibleStickers()[1].getRGB() == color1.getRGB())) {
				if ((int) (e.averagePoint().magnitudeTo(rbx.getEdges()[3].averagePoint())) < 1) {
					if (e.getStickerFacing(color2) != FaceType.Down) {
						System.out.println("M' U2 M2");
						rbx.turnLayer(MoveType.M, -1);
						rbx.turnLayer(MoveType.U, 0);
						rbx.turnLayer(MoveType.M, 0);
					}
					solvedEdge = true;
				} else if (Math.abs((int) (e.averagePoint().magnitudeTo(rbx.getEdges()[3].averagePoint())) - 113) < 2) {
					takeOut(false);
				} else if (Math.abs((int) (e.averagePoint().magnitudeTo(rbx.getEdges()[3].averagePoint())) - 160) < 2) {
					rbx.turnLayer(MoveType.M, 1);
					System.out.println("M");
				} else if (Math.abs((int) (e.averagePoint().magnitudeTo(rbx.getEdges()[3].averagePoint())) - 195) < 2) {
					for (int i=0; i<dLayerE.length; i++) {
						if (e.getId() == uLayerE[i]) {
							rbx.turnLayer(MoveType.U, 1);
							System.out.println("U");
							break;
						} else if (e.getId() == bLayerE[i]) {
							takeOut(true);
							break;
						}
					}
				} else if (Math.abs((int) (e.averagePoint().magnitudeTo(rbx.getEdges()[3].averagePoint())) - 226) < 2) {
					rbx.turnLayer(MoveType.M, 0);
					System.out.println("M2");
				}
			}
		}
		if (solvedEdge) {
			for (Cubie c: rbx.getCorners()) {
				LinkedColors[] lc = new LinkedColors[3];
				lc[0] = new LinkedColors(c.getVisibleStickers()[0]);
				for (int i=1; i<c.getVisibleStickers().length; i++) {
					lc[i] = new LinkedColors(c.getVisibleStickers()[i]);
					lc[i-1].setNext(lc[i]);
				}
				LinkedColors current = lc[0];
				boolean match = true;
				while (current != null && match) {
					if (current.getColor().getRGB() != color1.getRGB() && current.getColor().getRGB() != color2.getRGB() && 
							current.getColor().getRGB() != Color.white.getRGB())
						match = false;
					current = current.getNext();
				}
				
				if (match) {
					// closest = 160, 226, 277
					if ((int) (c.averagePoint().magnitudeTo(rbx.getCorners()[0].averagePoint())) < 1) {
						while (c.getStickerFacing(Color.white) != FaceType.Left) {
							takeOut(true);
							rbx.turnLayer(MoveType.U, 0);
							System.out.println("U2");
						}
						solvedPair = true;
					} else if (Math.abs((int) (c.averagePoint().magnitudeTo(rbx.getCorners()[0].averagePoint())) - 160) < 2) {
						rbx.turnLayer(MoveType.U, 1);
						System.out.println("U");
					} else if (Math.abs((int) (c.averagePoint().magnitudeTo(rbx.getCorners()[0].averagePoint())) - 226) < 2) {
						for (int i=0; i<dLayerC.length; i++) {
							if (c.getId() == uLayerC[i]) {
								rbx.turnLayer(MoveType.U, 0);
								System.out.println("U2");
								break;
							} else if (c.getId() == rLayerC[i]) {
								takeOut(false);
								break;
							}
						}
					} else if (Math.abs((int) (c.averagePoint().magnitudeTo(rbx.getCorners()[0].averagePoint())) - 277) < 2) {
						rbx.turnLayer(MoveType.R, -1);
						rbx.turnLayer(MoveType.U, 0);
						rbx.turnLayer(MoveType.R, 1);
						System.out.println("R' U2 R");
					}
				}
			}
			
			if (solvedPair) {
				if (rbx.getCorners()[0].getStickerFacing(color2) == FaceType.Up) {
					rbx.turnLayer(MoveType.U, 0);
					rbx.turnLayer(MoveType.M, -1);
					System.out.println("U2 M'");
					takeOut(true);

				} else {
					rbx.turnLayer(MoveType.L, 1);
					rbx.turnLayer(MoveType.F, -1);
					rbx.turnLayer(MoveType.L, -1);
					System.out.println("L F' L'");
				}
				return true;
			}
		}
		return false;
	}
	
	private void takeOut(boolean reverse) {
		int orient = reverse?-1:1;
		rbx.turnLayer(MoveType.R, 1*orient);
		rbx.turnLayer(MoveType.U, 1*orient);
		rbx.turnLayer(MoveType.R, -1*orient);
		if (reverse)
			System.out.println("R' U' R");
		else
			System.out.println("R U R'");
	}
	
	private void reset() {
		solved4a = false;
		solvedCmll = false;
		solvedFB = false;
		solvedFBSquare = false;
		solvedLr = false;
		solvedSB = false;
		solvedSBSquare = false;
	}

	@Override
	public void render(Graphics g) {
		rbx.render(g);
		g.setColor(Color.white);
		g.drawString(""+moveCount, 10, 10);
	}

}
