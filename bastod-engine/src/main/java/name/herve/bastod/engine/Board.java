/*
 * Copyright 2012, 2020 Nicolas HERVE
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
package name.herve.bastod.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.herve.bastod.engine.pathfinder.PathFinder;
import name.herve.bastod.engine.pathfinder.PathFinder.Algorithm;
import name.herve.game.tools.graph.Path;
import name.herve.game.tools.math.Dimension;
import name.herve.game.tools.math.Vector;

// see http://code.google.com/p/libgdx-users/wiki/TiledMaps

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class Board {
	public static Vector b2g(Vector p, Dimension gridDimension, int squareSize) {
		float x = p.getX() / squareSize;
		float y = p.getY() / squareSize;

		if ((x >= 0) && (y >= 0) && (x < gridDimension.getW()) && (y < gridDimension.getH())) {
			return new Vector((int) x, (int) y);
		}

		return null;
	}

	public static Path g2b(Path p, int squareSize) {
		Path p2 = new Path();
		for (Vector pos : p) {
			p2.add(g2b(pos, squareSize));
		}
		return p2;
	}

	public static Vector g2b(Vector p, int squareSize) {
		return new Vector((p.getX() + 0.5f) * squareSize, (p.getY() + 0.5f) * squareSize);
	}

	private Dimension boardDimension;
	private List<Unit> boardUnits;
	private Map<Integer, List<Vector>> buildPositions;
	private Map<Integer, List<Vector>> endPositions;
	private Dimension gridDimension;
	private PathFinder pathFinder;
	private int squareSize;
	private Map<Integer, Vector> startPositions;
	private Map<Integer, List<Vector>> towerPositions;

	public Board(Dimension gridDimension, int squareSize) {
		super();
		this.gridDimension = gridDimension;
		this.squareSize = squareSize;
		boardDimension = new Dimension(gridDimension.getW() * squareSize, gridDimension.getH() * squareSize);
		pathFinder = new PathFinder(Algorithm.THETASTAR, gridDimension, false);
		boardUnits = new ArrayList<>();
		startPositions = new HashMap<>();
		endPositions = new HashMap<>();
		towerPositions = new HashMap<>();
		buildPositions = new HashMap<>();
	}

	public void addBuildPosition(int index, Vector pos) {
		addPosition(buildPositions, index, pos);
	}

	public void addEndPosition(int index, Vector pos) {
		addPosition(endPositions, index, pos);
	}

	private void addPosition(Map<Integer, List<Vector>> map, int index, Vector pos) {
		if (!map.containsKey(index)) {
			map.put(index, new ArrayList<Vector>());
		}
		map.get(index).add(pos);
	}

	public void addTowerPosition(int index, Vector pos) {
		addPosition(towerPositions, index, pos);
	}

	public boolean addUnit(Unit e) {
		e.setPlayer(null);
		e.init(squareSize);
		return boardUnits.add(e);
	}

	public void clearPathFinderCache() {
		pathFinder.clearCache();
	}

	public void closeOnBoard(Vector p) {
		pathFinder.close(fromBoardToGrid(p));
	}

	public void closeOnGrid(int x, int y) {
		pathFinder.close(new Vector(x, y));
	}

	public Vector fromBoardToGrid(Vector p) {
		return b2g(p, gridDimension, squareSize);
	}

	public Path fromGridToBoard(Path p) {
		return g2b(p, squareSize);
	}

	public Vector fromGridToBoard(Vector p) {
		return g2b(p, squareSize);
	}

	public Dimension getBoardDimension() {
		return boardDimension;
	}

	public Collection<Unit> getBoardUnits() {
		return boardUnits;
	}

	public List<Vector> getBuildPositions(int index) {
		return buildPositions.get(index);
	}

	public List<Vector> getBuildPositions(Player p) {
		return buildPositions.get(p.getIndex());
	}

	public List<Vector> getEndPositions(int index) {
		return endPositions.get(index);
	}

	public List<Vector> getEndPositions(Player p) {
		return endPositions.get(p.getIndex());
	}

	public Dimension getGridDimension() {
		return gridDimension;
	}

	public int getNbStartPositions() {
		return startPositions.size();
	}

	public int getSquareSize() {
		return squareSize;
	}

	public Vector getStartPosition(int index) {
		return startPositions.get(index);
	}

	public List<Vector> getTowerPositions(int index) {
		return towerPositions.get(index);
	}

	public List<Vector> getTowerPositions(Player p) {
		return towerPositions.get(p.getIndex());
	}

	public boolean isOpened(Vector p) {
		return pathFinder.isOpened(p);
	}

	public boolean isPathAvailableOnGrid(Vector s, Vector e, Vector excluding) {
		return pathFinder.isPathAvailable(s, e, excluding);
	}

	public boolean lineOfSight(Vector s, Vector sp) {
		return pathFinder.lineOfSight(s, sp);
	}

	public void openOnBoard(Vector p) {
		pathFinder.open(fromBoardToGrid(p));
	}

	public void setStartPosition(int index, Vector element) {
		startPositions.put(index, element);
		addBuildPosition(index, element);
	}

	public Path shortestPathOnBoard(Vector s, Vector e) {
		Vector sg = fromBoardToGrid(s);
		Vector se = fromBoardToGrid(e);

		if ((sg != null) && (se != null)) {
			Path p = shortestPathOnGrid(sg, se);
			p = fromGridToBoard(p);
			return p;
		}

		return null;
	}

	public Path shortestPathOnGrid(Vector s, Vector e) {
		return pathFinder.shortestPath(s, e, true);
	}

	public Path smoothPath(Vector sn, Path path, float r, int nbstep) {
		return pathFinder.smoothOnBoard(sn, path, r, nbstep);
	}
}
