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
package name.herve.bastod.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import name.herve.bastod.engine.buildings.Wall;
import name.herve.bastod.tools.GameException;
import name.herve.bastod.tools.math.Dimension;
import name.herve.bastod.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class BoardFactory {
	private final static String MAP_DIR = "maps/" + Engine.GRID_WIDTH + "x" + Engine.GRID_HEIGHT + "/";
	private final static String MAP_E1 = "e1";
	private final static String MAP_E2 = "e2";
	private final static String MAP_EXT = ".csv";
	private final static String MAP_S1 = "s1";
	private final static String MAP_S2 = "s2";
	private final static String MAP_T1 = "t1";
	private final static String MAP_T2 = "t2";
	private final static String MAP_B1 = "b1";
	private final static String MAP_B2 = "b2";
	private final static String MAP_X = "x";

	private long seed;

	public BoardFactory() {
		super();
	}

	private void addWall(Board board, int x, int y) {
		addWall(board, new Vector(x, y));
	}

	private void addWall(Board board, Vector onGrid) {
		Wall wall = new Wall();
		wall.setPositionOnBoard(board.fromGridToBoard(onGrid));
		board.addUnit(wall);
	}

	private Board createBoard() throws GameException {
		return new Board(new Dimension(Engine.GRID_WIDTH, Engine.GRID_HEIGHT), Engine._SQUARE_SIZE);
	}

	public List<String> getAvailableMaps(boolean full) throws GameException {
		URL url = GameFactory.class.getClassLoader().getResource(MAP_DIR);

		if (url == null) {
			throw new GameException("Unable to find maps directory '" + MAP_DIR + "'");
		}

		List<String> ret = new ArrayList<String>();
		if (full) {
			ret.add("* Random");
			ret.add("* Custom");
			ret.add("* Empty");
		}

		try {
			File dir = new File(url.toURI());
			String[] files = dir.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(MAP_EXT);
				}
			});
			ret.addAll(Arrays.asList(files));
			return ret;
		} catch (URISyntaxException e) {
			throw new GameException(e);
		}
	}

	public Board getCustomBoard() throws GameException {
		Board board = getEmptyBoard();

		int bw = board.getGridDimension().getW();
		int ofbw = bw / 4;
		int ofbw3 = bw / 3;
		int ofbw4 = 2 * bw / 3;
		int ofbw5 = 5 * bw / 12;
		int mbw = bw / 2;
		int ofbw2 = bw - ofbw;
		int by = board.getGridDimension().getH();
		int ofby = by / 6;
		int ofby2 = by - ofby;
		int of2by = 2 * ofby;
		int of2by2 = by - 2 * ofby;

		for (int y = ofby; y < ofby2; y++) {
			addWall(board, ofbw, y);
			addWall(board, ofbw2, y);
		}

		for (int y = 0; y < of2by; y++) {
			addWall(board, mbw, y);
		}

		for (int y = of2by2; y < by; y++) {
			addWall(board, mbw, y);
		}

		for (int x = ofbw3; x < ofbw4; x++) {
			addWall(board, x, of2by2);
		}

		for (int x = ofbw; x < ofbw5; x++) {
			addWall(board, x, ofby2);
		}

		return board;
	}

	public Board getEmptyBoard() throws GameException {
		Board board = createBoard();

		setDefaultStartPositions(board);
		setDefaultEndPositions(board);
		setDefaultTowerPositions(board);
		setDefaultBuildPositions(board);

		return board;
	}

	public Board getRandomBoard() throws GameException {
		Board board = getEmptyBoard();

		Random rd = new Random(seed);

		int bw = board.getGridDimension().getW();
		int ofbw = bw / 4;
		bw -= 2 * ofbw;
		int by = board.getGridDimension().getH();

		int nbTrees = (int) (by * bw / 5);

		for (int i = 0; i < nbTrees; i++) {
			addWall(board, ofbw + rd.nextInt(bw), rd.nextInt(by));
		}

		return board;
	}

	public Board loadMap(String name) throws GameException {
		// System.out.println("loadMap : " + name);

		if (name.equals("* Empty")) {
			return getEmptyBoard();
		}

		if (name.equals("* Random")) {
			return getRandomBoard();
		}

		if (name.equals("* Custom")) {
			return getCustomBoard();
		}

		Board board = createBoard();

		System.out.println("    - board grid : " + board.getGridDimension());
		System.out.println("    - board dim  : " + board.getBoardDimension());

		String fileName = MAP_DIR + name;
		URL url = GameFactory.class.getClassLoader().getResource(fileName);
		if (url == null) {
			fileName += MAP_EXT;
			url = GameFactory.class.getClassLoader().getResource(fileName);
			if (url == null) {
				throw new GameException("Unable to find map file '" + fileName + "'");
			}
		}

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(new File(url.toURI())));
		} catch (URISyntaxException e) {
			throw new GameException(e);
		} catch (FileNotFoundException e) {
			throw new GameException(e);
		}

		int mw, mh;

		String line = null;

		try {
			line = reader.readLine();
		} catch (IOException e1) {
			throw new GameException(e1);
		}

		if (line == null) {
			throw new GameException("First line of '" + fileName + "' missing");
		}

		String[] l = line.split(",");

		try {
			mw = Integer.parseInt(l[0].trim());
		} catch (NumberFormatException e) {
			throw new GameException("First line of '" + fileName + "' must contains the width of the map");
		}

		try {
			line = reader.readLine();
		} catch (IOException e1) {
			throw new GameException(e1);
		}

		if (line == null) {
			throw new GameException("Second line of '" + fileName + "' missing");
		}

		l = line.split(",");
		try {
			mh = Integer.parseInt(l[0].trim());
		} catch (NumberFormatException e) {
			throw new GameException("Second line of '" + fileName + "' must contains the height of the map");
		}

		System.out.println("    - map dim  : [" + mw + ", " + mh + "]");

		int xOffset = (board.getGridDimension().getW() - mw) / 2;
		if (xOffset < 0) {
			throw new GameException("In '" + fileName + "' - Map width is too big : " + mw + " > " + board.getGridDimension().getW());
		}

		int yOffset = (board.getGridDimension().getH() - mh) / 2;
		if (yOffset < 0) {
			throw new GameException("In '" + fileName + "' - Map height is too big : " + mh + " > " + board.getGridDimension().getH());
		}

		System.out.println("    - xOffset = " + xOffset + ", yOffset = " + yOffset);

		for (int y = 0; y < mh; y++) {
			try {
				line = reader.readLine();
			} catch (IOException e) {
				throw new GameException(e);
			}

			if (line == null) {
				throw new GameException("In '" + fileName + "' - Map line " + (y + 1) + " is missing");
			}

			l = line.split(",");

			for (int x = 0; x < Math.min(l.length, mw); x++) {
				if (l[x].isEmpty()) {
					continue;
				}

				Vector currentPosition = new Vector(xOffset + x, yOffset + y);

				if (MAP_X.equalsIgnoreCase(l[x])) {
					addWall(board, currentPosition);
				} else if (MAP_S1.equalsIgnoreCase(l[x])) {
					if (board.getStartPosition(0) != null) {
						throw new GameException("In '" + fileName + "' - Start position for player 1 is defined several times !");
					}
					board.setStartPosition(0, currentPosition);
				} else if (MAP_S2.equalsIgnoreCase(l[x])) {
					if (board.getStartPosition(1) != null) {
						throw new GameException("In '" + fileName + "' - Start position for player 2 is defined several times !");
					}
					board.setStartPosition(1, currentPosition);
				} else if (MAP_E1.equalsIgnoreCase(l[x])) {
					board.addEndPosition(0, currentPosition);
				} else if (MAP_E2.equalsIgnoreCase(l[x])) {
					board.addEndPosition(1, currentPosition);
				} else if (MAP_T1.equalsIgnoreCase(l[x])) {
					board.addTowerPosition(0, currentPosition);
				} else if (MAP_T2.equalsIgnoreCase(l[x])) {
					board.addTowerPosition(1, currentPosition);
				} else if (MAP_B1.equalsIgnoreCase(l[x])) {
					board.addBuildPosition(0, currentPosition);
				} else if (MAP_B2.equalsIgnoreCase(l[x])) {
					board.addBuildPosition(1, currentPosition);
				}
			}
		}

		if (board.getNbStartPositions() < 2) {
			throw new GameException("In '" + fileName + "' - Not enough start positions : " + board.getNbStartPositions());
		}

		for (int i = 0; i < board.getNbStartPositions(); i++) {
			if (board.getEndPositions(i) == null) {
				throw new GameException("In '" + fileName + "' - No end position for player " + (i + 1));
			}
		}

		// Close nodes that are outside the map
		for (int y = 0; y < board.getGridDimension().getH(); y++) {
			for (int x = 0; x < xOffset; x++) {
				board.closeOnGrid(x, y);
			}
			for (int x = xOffset + mw; x < board.getGridDimension().getW(); x++) {
				board.closeOnGrid(x, y);
			}
		}

		for (int x = xOffset; x < xOffset + mw; x++) {
			for (int y = 0; y < yOffset; y++) {
				board.closeOnGrid(x, y);
			}
			for (int y = yOffset + mh; y < board.getGridDimension().getH(); y++) {
				board.closeOnGrid(x, y);
			}
		}
		// ---

		return board;

	}

	private void setDefaultEndPositions(Board board) {
		int gh = board.getGridDimension().getH();

		for (int i = 0; i < 2; i++) {
			Vector enemy = board.getStartPosition(1 - i);
			int ex = enemy.getXInt();
			int ey = enemy.getYInt();
			int dy = gh / 3;

			for (int y = (int) Math.max(0, ey - dy); y < (int) Math.min(gh, ey + dy); y++) {
				if (y != ey) {
					board.addEndPosition(i, new Vector(ex, y));
				}
			}
		}
	}

	private void setDefaultStartPositions(Board board) {
		int gw = board.getGridDimension().getW();
		int gh = board.getGridDimension().getH();

		for (int i = 0; i < 2; i++) {
			board.setStartPosition(i, new Vector(1 + i * (gw - 3), gh / 2));
		}
	}

	private void setDefaultTowerPositions(Board board) {
		for (int i = 0; i < 2; i++) {
			board.addTowerPosition(i, board.getStartPosition(i).copy().add(new Vector((1 - 2 * i) * 10, 0)));
		}
	}

	private void setDefaultBuildPositions(Board board) {
		for (int i = 0; i < 2; i++) {
			for (int dx = -10; dx <= 10; dx++) {
				for (int dy = -19; dy <= 19; dy++) {
					board.addBuildPosition(i, board.getStartPosition(i).copy().add(new Vector((1 - 2 * i) * (15 + dx), dy)));
				}
			}
		}
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

}
