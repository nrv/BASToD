/*
 * Copyright 2012, 2013 Nicolas HERVE
 * 
 * This file is part of BASToD.
 * 
 * BASToD is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * BASToD is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with BASToD. If not, see <http://www.gnu.org/licenses/>.
 */
package name.herve.bastod.engine.pathfinder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import name.herve.bastod.engine.Board;
import name.herve.bastod.tools.graph.AStar;
import name.herve.bastod.tools.graph.AStar2;
import name.herve.bastod.tools.graph.AStar3;
import name.herve.bastod.tools.graph.Dijkstra;
import name.herve.bastod.tools.graph.Graph;
import name.herve.bastod.tools.graph.NoGraph;
import name.herve.bastod.tools.graph.Node;
import name.herve.bastod.tools.graph.Path;
import name.herve.bastod.tools.graph.PathFinderAlgorithm;
import name.herve.bastod.tools.graph.ThetaStar;
import name.herve.bastod.tools.math.Dimension;
import name.herve.bastod.tools.math.Vector;

// see http://code.google.com/p/libgdx-users/wiki/ArtificialIntelligence

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class PathFinder {
	public enum Algorithm {
		ASTAR, ASTAR2, ASTAR3, DIJKSTRA, THETASTAR
	};

	private Algorithm algo;
	private PathFinderAlgorithm astar;
	private Map<Long, Path> astarCache;
	private Map<Long, Dijkstra> dijkstraCache;

	private Graph graph;
	private Node[][] grid;

	private Dimension gridDimension;
	private NoGraph nograph;
	private int squareSize;

	public PathFinder(Algorithm algo, Dimension gridDimension, boolean allowDiagonal) {
		super();

		this.algo = algo;

		this.gridDimension = gridDimension;

		switch (algo) {
		case DIJKSTRA:
			dijkstraCache = new HashMap<Long, Dijkstra>();
			initGraph(gridDimension, allowDiagonal);
			break;
		case ASTAR:
		case ASTAR2:
			astarCache = new HashMap<Long, Path>();
			initGraph(gridDimension, allowDiagonal);
			break;
		case ASTAR3:
		case THETASTAR:
			astarCache = new HashMap<Long, Path>();
			initGraph2(gridDimension, allowDiagonal);
			break;
		}

	}

	private boolean checkWalkablePositionOnBoard(Vector checkPositionBoardCenter) {
		Vector checkPositionBoard;
		Vector checkPositionGrid;
		Node nodeToCheck;
		int iNodeToCheck;

		switch (algo) {
		case DIJKSTRA:
		case ASTAR:
		case ASTAR2:
			checkPositionBoard = checkPositionBoardCenter.copy().add(squareSize, 0);
			checkPositionGrid = Board.b2g(checkPositionBoard, gridDimension, squareSize);
			nodeToCheck = getNode(checkPositionGrid);
			if ((nodeToCheck != null) && !nodeToCheck.isAvailable()) {
				return false;
			}

			checkPositionBoard = checkPositionBoardCenter.copy().add(0, squareSize);
			checkPositionGrid = Board.b2g(checkPositionBoard, gridDimension, squareSize);
			nodeToCheck = getNode(checkPositionGrid);
			if ((nodeToCheck != null) && !nodeToCheck.isAvailable()) {
				return false;
			}

			checkPositionBoard = checkPositionBoardCenter.copy().add(-squareSize, 0);
			checkPositionGrid = Board.b2g(checkPositionBoard, gridDimension, squareSize);
			nodeToCheck = getNode(checkPositionGrid);
			if ((nodeToCheck != null) && !nodeToCheck.isAvailable()) {
				return false;
			}

			checkPositionBoard = checkPositionBoardCenter.copy().add(0, -squareSize);
			checkPositionGrid = Board.b2g(checkPositionBoard, gridDimension, squareSize);
			nodeToCheck = getNode(checkPositionGrid);
			if ((nodeToCheck != null) && !nodeToCheck.isAvailable()) {
				return false;
			}
			break;
		case ASTAR3:
		case THETASTAR:
			checkPositionBoard = checkPositionBoardCenter.copy().add(squareSize, 0);
			checkPositionGrid = Board.b2g(checkPositionBoard, gridDimension, squareSize);
			iNodeToCheck = nograph.getNode(checkPositionGrid);
			if (!nograph.isAvailable(iNodeToCheck)) {
				return false;
			}

			checkPositionBoard = checkPositionBoardCenter.copy().add(0, squareSize);
			checkPositionGrid = Board.b2g(checkPositionBoard, gridDimension, squareSize);
			iNodeToCheck = nograph.getNode(checkPositionGrid);
			if (!nograph.isAvailable(iNodeToCheck)) {
				return false;
			}

			checkPositionBoard = checkPositionBoardCenter.copy().add(-squareSize, 0);
			checkPositionGrid = Board.b2g(checkPositionBoard, gridDimension, squareSize);
			iNodeToCheck = nograph.getNode(checkPositionGrid);
			if (!nograph.isAvailable(iNodeToCheck)) {
				return false;
			}

			checkPositionBoard = checkPositionBoardCenter.copy().add(0, -squareSize);
			checkPositionGrid = Board.b2g(checkPositionBoard, gridDimension, squareSize);
			iNodeToCheck = nograph.getNode(checkPositionGrid);
			if (!nograph.isAvailable(iNodeToCheck)) {
				return false;
			}
			break;
		}

		return true;
	}

	public void clearCache() {
		switch (algo) {
		case DIJKSTRA:
			dijkstraCache.clear();
			break;
		case ASTAR:
			astar = new AStar(graph);
			astarCache.clear();
			break;
		case ASTAR2:
			astar = new AStar2(graph);
			astarCache.clear();
			break;
		case ASTAR3:
			astar = new AStar3(nograph);
			astarCache.clear();
			break;
		case THETASTAR:
			astar = new ThetaStar(nograph);
			astarCache.clear();
			break;
		}
	}

	public void close(Vector p) {
		switch (algo) {
		case DIJKSTRA:
		case ASTAR:
		case ASTAR2:
			Node n = getNode(p);
			n.setAvailable(false);
			break;
		case ASTAR3:
			nograph.close(p.getXInt(), p.getYInt());
			break;
		case THETASTAR:
			nograph.close(p.getXInt(), p.getYInt());
			nograph.resetLoS();
			break;
		}
	}

	public Node getNode(Vector p) {
		if (p == null) {
			return null;
		}
		return grid[p.getXInt()][p.getYInt()];
	}

	private void initGraph(Dimension gridDimension, boolean allowDiagonal) {
		nograph = null;
		graph = new Graph();
		grid = new Node[gridDimension.getW()][gridDimension.getH()];

		for (int i = 0; i < gridDimension.getW(); i++) {
			for (int j = 0; j < gridDimension.getH(); j++) {
				Node n = new Node();
				n.setPosition(new Vector(i, j));
				graph.addNode(n);
				grid[i][j] = n;
			}
		}

		for (int i = 0; i < gridDimension.getW() - 1; i++) {
			for (int j = 0; j < gridDimension.getH(); j++) {
				graph.addEdge(grid[i][j], grid[i + 1][j], 1f);
			}
		}

		for (int i = 0; i < gridDimension.getW(); i++) {
			for (int j = 0; j < gridDimension.getH() - 1; j++) {
				graph.addEdge(grid[i][j], grid[i][j + 1], 1f);
			}
		}

		if (allowDiagonal) {
			float sqrt2 = (float) Math.sqrt(2);
			for (int i = 0; i < gridDimension.getW() - 1; i++) {
				for (int j = 0; j < gridDimension.getH() - 1; j++) {
					graph.addEdge(grid[i][j], grid[i + 1][j + 1], sqrt2);
				}
			}
			for (int i = 1; i < gridDimension.getW(); i++) {
				for (int j = 0; j < gridDimension.getH() - 1; j++) {
					graph.addEdge(grid[i][j], grid[i - 1][j + 1], sqrt2);
				}
			}
			for (int i = 1; i < gridDimension.getW(); i++) {
				for (int j = 1; j < gridDimension.getH(); j++) {
					graph.addEdge(grid[i][j], grid[i - 1][j - 1], sqrt2);
				}
			}
			for (int i = 0; i < gridDimension.getW() - 1; i++) {
				for (int j = 1; j < gridDimension.getH(); j++) {
					graph.addEdge(grid[i][j], grid[i + 1][j - 1], sqrt2);
				}
			}
		}
	}

	private void initGraph2(Dimension gridDimension, boolean allowDiagonal) {
		graph = null;
		grid = null;
		nograph = new NoGraph(gridDimension.getW(), gridDimension.getH(), allowDiagonal);
	}

	public boolean isOpened(Vector p) {
		switch (algo) {
		case DIJKSTRA:
		case ASTAR:
		case ASTAR2:
			Node n = getNode(p);
			return n.isAvailable();
		case ASTAR3:
		case THETASTAR:
			return nograph.isAvailable(nograph.getNode(p));
		}
		return false;
	}

	// Only AStar3 and ThetaStar
	public boolean isPathAvailable(Vector s, Vector e, Vector excluding) {
		// System.out.println("isPathAvailable(" + s + ", " + e + ", " +
		// excluding + ")");
		return astar.getPath(nograph.getNode(s), nograph.getNode(e), nograph.getNode(excluding)) != null;
	}

	public boolean lineOfSight(Vector s, Vector sp) {
		return nograph.lineOfSight(s, sp);
	}

	public void open(Vector p) {
		switch (algo) {
		case DIJKSTRA:
		case ASTAR:
		case ASTAR2:
			Node n = getNode(p);
			n.setAvailable(true);
			break;
		case ASTAR3:
		case THETASTAR:
			nograph.open(p.getXInt(), p.getYInt());
			break;
		}
	}

	public Path shortestPath(Vector s, Vector e) {
		return shortestPath(s, e, true);
	}

	public Path shortestPath(Vector s, Vector e, boolean useCache) {
		switch (algo) {
		case DIJKSTRA:
			return shortestPathDijkstra(s, e, useCache);
		case ASTAR:
		case ASTAR2:
			return shortestPathAStar(s, e, useCache);
		case ASTAR3:
		case THETASTAR:
			return shortestPathAStar3(s, e, useCache);
		default:
			return null;
		}
	}

	private Path shortestPathAStar(Vector s, Vector e, boolean useCache) {
		Path path = null;
		Node sn = getNode(s);
		Node en = getNode(e);

		if (useCache) {
			Long cacheKey = (long) sn.getId() * (long) graph.size() + (long) en.getId();
			if (astarCache.containsKey(cacheKey)) {
				path = astarCache.get(cacheKey);
			} else {
				path = astar.getPath(sn, en);
				// if (useSmoothing && (path != null)) {
				// path = smooth(s, path);
				// }
				astarCache.put(cacheKey, path);
				// System.out.println("Cache size : " + astarCache.size());
			}
		} else {
			path = astar.getPath(sn, en);
		}
		return path;
	}

	private Path shortestPathAStar3(Vector s, Vector e, boolean useCache) {
		Path path = null;

		int sn = nograph.getNode(s);
		int en = nograph.getNode(e);

		if (useCache) {
			Long cacheKey = (long) sn * (long) nograph.size() + (long) en;
			if (astarCache.containsKey(cacheKey)) {
				path = astarCache.get(cacheKey);
			} else {
				path = astar.getPath(sn, en);
				// if (useSmoothing && (path != null)) {
				// path = smooth(s, path);
				// }
				astarCache.put(cacheKey, path);
				// System.out.println("Cache size : " + astarCache.size());
			}
		} else {
			path = astar.getPath(sn, en);
		}
		return path;
	}

	private Path shortestPathDijkstra(Vector s, Vector e, boolean useCache) {
		Dijkstra dijkstra = null;
		Path path = null;
		Node sn = getNode(s);
		Node en = getNode(e);

		if (useCache) {
			Long cacheKey = (long) sn.getId();
			if (dijkstraCache.containsKey(cacheKey)) {
				dijkstra = dijkstraCache.get(cacheKey);
			} else {
				dijkstra = new Dijkstra(graph);
				dijkstra.compute(sn);
				dijkstraCache.put(cacheKey, dijkstra);
				// System.out.println("Cache size : " + dijkstraCache.size());
			}
			path = dijkstra.getPath(en, true);
		} else {
			dijkstra = new Dijkstra(graph);
			path = dijkstra.getPath(sn, en);
		}

		// if (useSmoothing) {
		// path = smooth(s, path);
		// }

		return path;
	}

	// private Path smooth(Vector sn, Path path) {
	// // System.out.println("Before : " + sn.getPosition().toString() +
	// // " ---> " + toString(pn));
	// if (path.size() < 2) {
	// return path;
	// }
	//
	// Path npn = new Path();
	//
	// Iterator<Vector> it = path.iterator();
	// Vector checkPoint = sn;
	// Vector currentPoint = it.next();
	//
	// while (it.hasNext()) {
	// Vector next = it.next();
	// if (!walkable(checkPoint, next)) {
	// checkPoint = currentPoint;
	// npn.add(checkPoint);
	// }
	// currentPoint = next;
	// }
	//
	// npn.add(currentPoint);
	//
	// // System.out.println("After  : " + sn.getPosition().toString() +
	// // " ---> " + toString(npn));
	//
	// return npn;
	// }

	public Path smoothOnBoard(Vector sn, Path path, float r, int nbstep) {
		for (int i = nbstep; i > 0; i--) {
			path = smoothOnBoard(sn, path, r);
			r /= 2;
		}

		return path;
	}

	private Path smoothOnBoard(Vector sn, Path path, float r) {
		if (path.size() < 2) {
			return path;
		}

		Iterator<Vector> it = path.iterator();
		Vector p1 = sn;
		Vector p2 = it.next();
		Vector p3 = null;

		Path np = new Path();

		while (it.hasNext()) {
			p3 = it.next();

			Vector v1 = p2.copy().remove(p1).normalize();
			Vector v2 = p3.copy().remove(p2).normalize();

			float a = (float) (v1.angleRad(v2) / Math.PI);
						
			if (a > 0.1) {
				//Vector tgt = v1.copy().add(v2.copy()).normalize().multiply(r * a * a);
				Vector tgt = v1.copy().add(v2.copy()).normalize().multiply(0.5f * r);

				Vector p2a = p2.copy().add(tgt);
				Vector p2c = p2.copy().add(tgt.rotateRad(Math.PI));

				np.add(p2c);
				np.add(p2a);
			} else {
				np.add(p2);
			}
			p1 = p2;
			p2 = p3;
		}
		np.add(p3);

		return np;
	}

	private boolean walkable(Vector n1, Vector n2) {
		Vector p1 = Board.g2b(n1, squareSize);
		Vector p2 = Board.g2b(n2, squareSize);

		Vector line = p2.copy().remove(p1);
		float lineLength = line.length();

		if (lineLength == 0) {
			return true;
		}

		float checkStep = ((float) squareSize) / 5f;
		float currentStep = 0f;

		// System.out.println(p1 + " -> " + p2 + " : lineLength " +
		// lineLength + " / checkStep " + checkStep);

		Vector direction = line.copy().multiply(1f / lineLength);

		Vector checkPositionBoardCenter;

		while (currentStep < lineLength) {
			checkPositionBoardCenter = p1.copy().add(direction.copy().multiply(currentStep));

			if (!checkWalkablePositionOnBoard(checkPositionBoardCenter)) {
				return false;
			}

			currentStep += checkStep;
		}

		// System.out.println(" walk[" + n1.getPosition().toString() + " -> " +
		// n2.getPosition().toString() + "] = " + w);

		return true;
	}
}
