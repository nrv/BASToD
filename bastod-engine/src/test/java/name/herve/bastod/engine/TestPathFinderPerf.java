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

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import name.herve.bastod.engine.Game.Type;
import name.herve.bastod.engine.pathfinder.PathFinder;
import name.herve.bastod.engine.pathfinder.PathFinder.Algorithm;
import name.herve.bastod.engine.units.Blocking;
import name.herve.game.tools.GameException;
import name.herve.game.tools.conf.Configuration;
import name.herve.game.tools.graph.Path;
import name.herve.game.tools.math.Dimension;
import name.herve.game.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class TestPathFinderPerf {
	public static void main(String[] args) throws GameException {
		TestPathFinderPerf test = new TestPathFinderPerf();

		Configuration gconf = Configuration.load(Type.TWO_PLAYERS.getFile());
		long seed = 1864752003l;
		Game game = GameFactory.createGame(Type.TWO_PLAYERS, gconf, seed);

		// --------------------------------
		// test.comparePathFinders(game);

		// --------------------------------

		PathFinder pf = test.initPathFinder(game, Algorithm.THETASTAR, false, false);

		test.start();
		int iter = 1000;
		for (int i = 0; i < iter; i++) {
			test.testGamePath(pf, game, false);
		}
		test.stop();
		test.displayPerf(iter, "");

		// --------------------------------
		// PathFinder pfd = test.initPathFinder(game, Algorithm.DIJKSTRA, true);
		// PathFinder pfa = test.initPathFinder(game, Algorithm.ASTAR, true);
		// PathFinder pfa2 = test.initPathFinder(game, Algorithm.ASTAR2, true);
		// test.start();
		// test.testSomeGamePath(pfd, game, false);
		// test.stop();
		// test.displayPerf(1, "DIJKSTRA");
		//
		// test.start();
		// test.testSomeGamePath(pfa, game, false);
		// test.stop();
		// test.displayPerf(1, "ASTAR");
		//
		// test.start();
		// test.testSomeGamePath(pfa2, game, false);
		// test.stop();
		// test.displayPerf(1, "ASTAR2");
	}

	private long startTime;
	private long stopTime;
	private long startUserTime;
	private long startCPUTime;
	private long stopUserTime;
	private long stopCPUTime;

	private ThreadMXBean bean;

	public TestPathFinderPerf() {
		super();

		startUserTime = 0;
		startCPUTime = 0;
		stopUserTime = 0;
		stopCPUTime = 0;
		startTime = 0;
		stopTime = 0;

		bean = ManagementFactory.getThreadMXBean();
	}

	public void checkPerf(Game game) {
		int nbi = 1;
		PathFinder pf = initPathFinder(game, Algorithm.ASTAR, true, true);

		start();

		for (int i = 0; i < nbi; i++) {
			testGamePath(pf, game, false);
		}

		stop();
		displayPerf(nbi, "game");
	}

	public void comparePathFinders(Game game) {
		System.out.println("Comparing PathFinders");
		PathFinder pfDijkstra = initPathFinder(game, Algorithm.DIJKSTRA, true, true);
		System.out.println(" - DIJKSTRA initialized");
		PathFinder pfAstar = initPathFinder(game, Algorithm.ASTAR, true, true);
		System.out.println(" - ASTAR    initialized");
		PathFinder pfAstar2 = initPathFinder(game, Algorithm.ASTAR2, true, true);
		System.out.println(" - ASTAR2   initialized");

		Dimension dim = game.getBoard().getGridDimension();

		Vector s = new Vector(0, dim.getH() / 2);
		for (int t = 0; t < 5; t++) {
			Vector e = new Vector(dim.getW() - 1, (t * (dim.getH() - 1)) / 5);
			System.out.println("*** Path " + s + " -> " + e);
			Path pathDijkstra = pfDijkstra.shortestPath(s, e, false);
			System.out.println(" - DIJKSTRA : " + pathDijkstra.size());
			Path pathAstar = pfAstar.shortestPath(s, e, false);
			System.out.println(" - ASTAR    : " + pathAstar.size());
			Path pathAstar2 = pfAstar2.shortestPath(s, e, false);
			System.out.println(" - ASTAR2   : " + pathAstar2.size());
		}

		s = new Vector(dim.getW() - 1, dim.getH() / 2);
		for (int t = 0; t < 5; t++) {
			Vector e = new Vector(0, (t * (dim.getH() - 1)) / 5);
			System.out.println("*** Path " + s + " -> " + e);
			Path pathDijkstra = pfDijkstra.shortestPath(s, e, false);
			System.out.println(" - DIJKSTRA : " + pathDijkstra.size());
			Path pathAstar = pfAstar.shortestPath(s, e, false);
			System.out.println(" - ASTAR    : " + pathAstar.size());
			Path pathAstar2 = pfAstar2.shortestPath(s, e, false);
			System.out.println(" - ASTAR2   : " + pathAstar2.size());
		}
	}

	public void displayPerf(int nbIter, String msg) {
		long dt1 = stopTime - startTime;
		long dt2 = (stopUserTime - startUserTime) / 1000000;
		long dt3 = (stopCPUTime - startCPUTime) / 1000000;

		System.out.println("Time [" + msg + "] (" + nbIter + ") : " + dt1 + " / " + dt2 + " / " + dt3);
	}

	public PathFinder initPathFinder(Game game, Algorithm algo, boolean allowDiagonal, boolean useSmoothing) {
		// Don't use the internal PathFinder, create a new one instead
		Board board = game.getBoard();
		PathFinder pf = new PathFinder(algo, board.getGridDimension(), allowDiagonal);

		for (Unit u : board.getBoardUnits()) {
			if (u instanceof Blocking) {
				pf.close(board.fromBoardToGrid(u.getPositionOnBoard()));
			}
		}

		for (Player p : game.getPlayers()) {
			for (Unit u : p.getUnits()) {
				if (u instanceof Blocking) {
					pf.close(board.fromBoardToGrid(u.getPositionOnBoard()));
				}
			}
		}

		pf.clearCache();

		return pf;
	}

	public void start() {
		startUserTime = bean.getCurrentThreadUserTime();
		startCPUTime = bean.getCurrentThreadCpuTime();
		startTime = System.currentTimeMillis();
	}

	public void stop() {
		stopUserTime = bean.getCurrentThreadUserTime();
		stopCPUTime = bean.getCurrentThreadCpuTime();
		stopTime = System.currentTimeMillis();
	}

	public void testAllPath(PathFinder pf, Game game, boolean useCache) {
		Dimension dim = game.getBoard().getGridDimension();

		int ex = dim.getW() - 1;

		for (int sy = 0; sy < dim.getH(); sy++) {
			for (int ey = 0; ey < dim.getH(); ey++) {
				Vector s = new Vector(0, sy);
				Vector e = new Vector(ex, sy);
				pf.shortestPath(s, e, useCache);
				pf.shortestPath(e, s, useCache);
			}
		}
	}

	public void testGamePath(PathFinder pf, Game game, boolean useCache) {
		Dimension dim = game.getBoard().getGridDimension();

		int ex = dim.getW() - 1;
		int y2 = dim.getH() / 2;

		Vector s1 = new Vector(0, y2);
		Vector s2 = new Vector(ex, y2);

		for (int ey = 0; ey < dim.getH(); ey++) {
			Vector e1 = new Vector(0, ey);
			Vector e2 = new Vector(ex, ey);
			Path p1 = pf.shortestPath(s1, e2, useCache);
			Path p2 = pf.shortestPath(s2, e1, useCache);
			// System.out.println(s1 + " -> " + e2 + " : " + p1.size());
			// System.out.println(s2 + " -> " + e1 + " : " + p2.size());
		}
	}

	public void testSomeGamePath(PathFinder pf, Game game, boolean useCache) {
		Dimension dim = game.getBoard().getGridDimension();

		int step = 3;

		Vector s1 = new Vector(1, dim.getH() / 2);
		Vector s2 = new Vector(dim.getW() - 2, dim.getH() / 2);

		for (int t = 0; t < step; t++) {
			Vector e1 = new Vector(dim.getW() - 1, 1 + ((t * (dim.getH() - 1)) / step));
			Vector e2 = new Vector(1, 1 + ((t * (dim.getH() - 1)) / step));

			Path p1 = pf.shortestPath(s1, e1, useCache);
			Path p2 = pf.shortestPath(s2, e2, useCache);
			System.out.println(s1 + " -> " + e1 + " : " + p1.size());
			System.out.println(s2 + " -> " + e2 + " : " + p2.size());
		}
	}

}
