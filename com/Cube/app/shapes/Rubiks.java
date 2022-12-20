package com.Cube.app.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.Cube.app.App;

public class Rubiks {
	private Cubie[] cubies;
	private int[] renderOrder, cornerIndexes;
	private int dimension, numCubies, layerSize;
	
	public Rubiks(App app, int dimension, int cubieSize) {
		this.dimension = dimension;
		layerSize = dimension*dimension;
		numCubies = dimension*dimension*dimension;
		cubies = new Cubie[numCubies];
		renderOrder = new int[numCubies];
		cornerIndexes = new int[8];
		int index = 0;
		int offSet = cubieSize/2*(dimension-1);
		
		boolean xOutside = false, yOutside = false, zOutside = false;
		Color front, back, top, bot, right, left;
		
		for (int x=0, c=0; x>-dimension; x--) {
			front = Color.black;
			back = Color.black;
			xOutside = false;
			if (x == 0) {
				front = Color.red;
				xOutside = true;
			} else if (x == 1-dimension) {
				back = new Color(255,140,0);
				xOutside = true;
			}
			
			for (int z=0; z>-dimension; z--) {
				top = Color.black;
				bot = Color.black;
				zOutside = false;
				if (z == 0) {
					top = Color.yellow;
					zOutside = true;
				} else if (z == 1-dimension) {
					bot = Color.white;
					zOutside = true;
				}
				
				for (int y=0; y<dimension; y++) {
					right = Color.black;
					left = Color.black;
					yOutside = false;
					if (y == 0) {
						left = Color.blue;
						yOutside = true;
					} else if (y == dimension-1) {
						right = Color.green;
						yOutside = true;
					}
					boolean isCorner = false;
					if (xOutside && yOutside && zOutside) {
						isCorner = true;
						cornerIndexes[c] = index;
						c++;
					}
					
					cubies[index] = new Cubie(cubieSize, index, isCorner, (xOutside || yOutside || zOutside));
					cubies[index].setInitialStickColors(front, top, right, bot, left, back);
					cubies[index].translate(x*cubieSize+offSet, y*cubieSize-offSet, z*cubieSize+offSet);
					
					renderOrder[index] = index;
					index++;
				}
			}
		}
		sortCubies();
		
	}
	
	public void rotate(double degreesX, double degreesY, double degreesZ) {
		for (Cubie qb: cubies) {
			qb.rotate(degreesX, degreesY, degreesZ);
		}
		sortCubies();
	}
	
	public void sortCubies() {
		List<Cubie> newCubies = new ArrayList<Cubie>();
		
		for (Cubie qb: cubies) {
			newCubies.add(qb);
		}
		
		Collections.sort(newCubies, new Comparator<Cubie>() {
			@Override
			public int compare(Cubie q1, Cubie q2) {
				if (q2.averageDepth() - q1.averageDepth() == 0)
					return 1;
				return q2.averageDepth() - q1.averageDepth() < 0?1:-1;
			}
		});
		
		for (int i=0; i<cubies.length; i++) {
			renderOrder[i] = newCubies.get(i).getId();
		}
	}
	
	public int[] getCubieIndexes(MoveType move) {
		int[] arr = new int[layerSize];
		
		switch (move) {
		case F:
			for (int i=0,c=0; i<layerSize; i++) {
				arr[c] = i;
				c++;
			}
			return arr;
		case B:
			for (int i=numCubies-1-dimension*(dimension-1), c=0; i<numCubies; i+=dimension) {
				for (int k=i; k>i-dimension; k--) {
					arr[c] = k;
					c++;
				}
			}
			return arr;
		case U:
			for (int i=layerSize*(dimension-1), c=0; i>-1; i-=layerSize) {
				for (int k=i; k<i+dimension; k++) {
					arr[c] = k;
					c++;
				}
			}
			return arr;
		case D:
			for (int i=numCubies-1, c=layerSize-1; i>dimension-1; i-=layerSize) {
				for (int k=i; k>i-dimension; k--) {
					arr[c] = k;
					c--;
				}
			}
			return arr;
		case L:
			for (int i=layerSize*(dimension-1), c=0; i<numCubies-dimension+1;i+=dimension) {
				for (int k=i; k>i-layerSize*(dimension-1)-1; k-=layerSize) {
					arr[c] = k;
					c++;
				}
			}
			return arr;
		case R:
			for (int i=numCubies-1, c=layerSize-1; i>numCubies-(layerSize-dimension+2);i-=dimension) {
				for (int k=i; k>i-layerSize*(dimension-1)-1; k-=layerSize) {
					arr[c] = k;
					c--;
				}
			}
			return arr;
		case M:
			for (int i=layerSize*(dimension-1)+1, c=0; i<numCubies-dimension+2;i+=dimension) {
				for (int k=i; k>i-layerSize*(dimension-1)-1; k-=layerSize) {
					arr[c] = k;
					c++;
				}
			}
			return arr;
		default:
			return null;
		}
	}
	
	public void scramble() {
		
		MoveType[] scramb = new MoveType[20];
		int prevOri = -1;
		int[] notation = new int[20];
		
		for (int i=0; i<20; i++) {
			int orientation = Math.round((float) (Math.random()*2));
			notation[i] = Math.round((float) (Math.random()*2))-1;
			int side = Math.round((float) (Math.random()));
			
			if (orientation == 0) {
				if (side == 0)
					scramb[i] = MoveType.F;
				else
					scramb[i] = MoveType.B;
			} else if (orientation == 1) {
				if (side == 0)
					scramb[i] = MoveType.R;
				else
					scramb[i] = MoveType.L;
			} else {
				if (side == 0)
					scramb[i] = MoveType.U;
				else
					scramb[i] = MoveType.D;
			}
			
			if (prevOri == orientation) {
				i--;
			} 
			
			prevOri = orientation;
		}
		
		for (int i=0; i<scramb.length; i++) {
			String s = "";
			if (notation[i] == -1)
				s+="\'";
			else if (notation[i] == 0)
				s+="2";
			
			System.out.print(scramb[i] + s +" ");
			turnLayer(scramb[i], notation[i]);
		}
		System.out.println();
	}
	
	private int[] getSequence(int orient, int subDimension) {
		int subLayerSize = subDimension*subDimension;
		if (subDimension < 1) {
			return null;
		}
		int[] arr = new int[subDimension*3 - 3];
		arr[1] = subLayerSize-1;
		if (orient == -1) {
			arr[0] = subDimension-1;
			arr[2] = subLayerSize - subDimension;
		} else {
			arr[2] = subDimension-1;
			arr[0] = subLayerSize - subDimension;
		}
		
		for (int i=2, c=3; i<subDimension; i++) {
			arr[c+1] = subLayerSize - i;
			if (orient == -1) {
				arr[c] = subDimension*i - 1;
				arr[c+2] = subLayerSize - subDimension*i;
			} else {
				arr[c+2] = subDimension*i - 1;
				arr[c] = subLayerSize - subDimension*i;
			}
			c+=3;
		}
		return arr;
	}
	
	private int[] getCenterIndexes(int[] cubieIndexes) {
		int inner = dimension-2;
		int[] arr = new int[inner*inner];
		for (int i=dimension+1, c=0; i<layerSize-dimension; i+=dimension) {
			for (int k=i; k<i+inner; k++) {
				arr[c] = cubieIndexes[k];
				c++;
			}
		}
		return arr;
	}
	
	public void turnLayer(MoveType move, int orient) {
		// orient == -1 is prime
		// orient == 0 is double
		// orient == 1 is normal
		if (Math.abs(orient) > 1)
			return;
		int adj = 1;
		
		if (move == MoveType.B || move == MoveType.L || move == MoveType.D || move == MoveType.M)
			adj = -1;
	
		turn(getCubieIndexes(move), getSequence(orient, dimension), move, orient*adj, dimension);
	}
	
	private void turn(int[] cubieIndexes, int[] sequence, MoveType move, int orient, int subDimension) {
		if (subDimension < 1) {
			return;
		}
		
		for (int i=0,c=0; i<subDimension-1; i++) {
			Color[] temp = cubies[cubieIndexes[i]].getStickColors();
			
			if (orient == 0) {
				Color[] temp2 = cubies[cubieIndexes[sequence[c]]].getStickColors();
				cubies[cubieIndexes[i]].setStickColors(cubies[cubieIndexes[sequence[c+1]]].getStickColors());
				cubies[cubieIndexes[sequence[c+1]]].setStickColors(temp);
				cubies[cubieIndexes[sequence[c]]].setStickColors(cubies[cubieIndexes[sequence[c+2]]].getStickColors());
				cubies[cubieIndexes[sequence[c+2]]].setStickColors(temp2);
				
			} else {
				cubies[cubieIndexes[i]].setStickColors(cubies[cubieIndexes[sequence[c]]].getStickColors());
				cubies[cubieIndexes[sequence[c]]].setStickColors(cubies[cubieIndexes[sequence[c+1]]].getStickColors());
				cubies[cubieIndexes[sequence[c+1]]].setStickColors(cubies[cubieIndexes[sequence[c+2]]].getStickColors());
				cubies[cubieIndexes[sequence[c+2]]].setStickColors(temp);
			}
			c += 3;
		}
		if (subDimension == dimension) {
			for (int i=0; i<cubieIndexes.length; i++) {
				if (move == MoveType.U || move == MoveType.D) {
					cubies[cubieIndexes[i]].rotateStickersZ(orient);
				} else if (move == MoveType.R || move == MoveType.L || move == MoveType.M) {
					cubies[cubieIndexes[i]].rotateStickersY(orient);
				} else {
					cubies[cubieIndexes[i]].rotateStickersX(orient);
				}
			}
		}
		if (subDimension > 3) {
			turn(getCenterIndexes(cubieIndexes), getSequence(orient, subDimension-2), move, orient, subDimension-2);
		}
	}
	
	public Cubie[] getCorners() {
		Cubie[] corners = new Cubie[8];
		for (int i=0; i<8; i++) {
			corners[i] = cubies[cornerIndexes[i]];
		}
		
		return corners;
	}
	
	public Cubie[] getEdges() {
		if (dimension < 3) 
			return null;
		
		int center = dimension-2;
		int outer = center*center*6;
		int inner = center*center*center;
		Cubie[] edges = new Cubie[numCubies-8-outer-inner];
		
		for (int i=0, e=0; i<cubies.length; i++) {
			if (cubies[i].isOutside() && !cubies[i].isCorner() && cubies[i].getVisibleStickers()[1] != null) {
				edges[e] = cubies[i];
				e++;
			}
		}
		
		return edges;
	}
	
	public int[] getEdgesIndexLayer(MoveType layer) {
		if (dimension < 3)
			return null;
		
		int center = dimension-2;
		int[] edges = new int[layerSize-4-center*center];
		int[] indexes = getCubieIndexes(layer);
		int[] sequence = getSequence(1,dimension);
		
		edges[0] = indexes[1];
		for (int i=3, c=1; i<sequence.length; i++) {
			edges[c] = indexes[sequence[i]];
			
			c++;
		}
		
		return edges;
	}
	
	public int[] getCornerIndexLayer(MoveType layer) {
		int[] corners = new int[4];
		int[] indexes = getCubieIndexes(layer);
		int[] sequence = getSequence(1,dimension);
		
		corners[0] = indexes[0];
		for (int i=0, c=1; i<3; i++) {
			corners[c] = indexes[sequence[i]];
			c++;
		}
		
		return corners;
	}
	
	public Cubie getULayerCenter() {
		return cubies[10];
	}
	
	public void render(Graphics g) {
//		for (Cubie qb: cubies) {
//			if (qb.getId() == 6 || qb.getId() == 8||qb.getId() == 24||qb.getId() == 26) {
//				qb.render(g);
//			}
//		}
		for (int i: renderOrder) {
			if (cubies[i].isOutside())
				cubies[i].render(g);
		}
	}
	
	public boolean isSolved() {
		for (Cubie qb: cubies) {
			if (!qb.isCurrentColorsInitial())
				return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(32);
		for (int z=0, i=0; z<dimension; z++) {
			sb.append("z-Layer ");
			sb.append(z+1);
			sb.append("\n");
			for (int y=0; y<dimension; y++) {
				for (int x=0; x<dimension; x++) {
					sb.append(cubies[i].getId());
					sb.append(cubies[i].isCurrentColorsInitial()?"":"*");
					sb.append(" ");
					i++;
				}
				sb.append("\n");
			}
			sb.append("\n\n");
		}
		return sb.toString();
	}
}
