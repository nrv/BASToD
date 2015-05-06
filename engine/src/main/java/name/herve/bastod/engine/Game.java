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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.herve.bastod.tools.GameException;
import name.herve.bastod.tools.conf.Configuration;
import name.herve.bastod.tools.conf.PropertiesConfiguration;
import name.herve.bastod.tools.math.Dimension;
import name.herve.bastod.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class Game {
	public enum Type {
		TOWER_DEFENSE("conf/tower_defense.conf"), TWO_PLAYERS("conf/two_players.conf");
		
		private final String file;
		
		private Type(String file) {
			this.file = file;
		}

		public String getFile() {
			return file;
		}
	};
		
	private Map<String, Improvement> availableImprovements;
	private Board board;
	private Configuration conf;
	private int metalIncreaseRatePerSec;
	private long now;
	private boolean over;
	private List<Player> players;
	private List<Shot> shots;
	private Type type;

	public Game(Type type, Configuration conf) throws GameException {
		super();

		this.conf = conf;

		players = new ArrayList<Player>();
		shots = new ArrayList<Shot>();
		over = false;

		availableImprovements = new HashMap<String, Improvement>();

		metalIncreaseRatePerSec = conf.getInt(Engine.CF_GAME_METAL_INCREASE_RATE_PER_SEC_I);
		this.type = type;
	}

	public void addAvailableImprovement(Improvement imp) {
		availableImprovements.put(imp.getName(), imp);
	}

	public void addNow(long delta) {
		now += delta;
	}

	public void addPlayer(Player p) {
		players.add(p.getIndex(), p);
	}

	public Improvement getAvailableImprovement(String name) {
		return availableImprovements.get(name);
	}

	public Map<String, Improvement> getAvailableImprovements() {
		return availableImprovements;
	}

	public Board getBoard() {
		return board;
	}

	public Dimension getBoardDimension() {
		return board.getBoardDimension();
	}

	public Configuration getConf() {
		return conf;
	}

	public int getGridSquareSize() {
		return board.getSquareSize();
	}

	public int getMetalIncreaseRatePerSec() {
		return metalIncreaseRatePerSec;
	}

	public long getNow() {
		return now;
	}

	public Player getPlayer(int index) {
		return players.get(index);
	}
	
	public Player getPlayer(String name) {
		for (Player p : players) {
			if (name.equals(p.getColor())) {
				return p;
			}
		}

		return null;
	}

	public Collection<Player> getPlayers() {
		return players;
	}

	public Collection<Shot> getShots() {
		return shots;
	}

	public boolean isOver() {
		return over;
	}

	public boolean lineOfSight(Vector s, Vector sp) {
		return board.lineOfSight(s, sp);
	}

	public void setBoard(Board board) {
		this.board = board;
	}
	
	public void setNow(long now) {
		this.now = now;
	}

	public void setOver(boolean over) {
		this.over = over;
	}

	public Type getType() {
		return type;
	}
}
