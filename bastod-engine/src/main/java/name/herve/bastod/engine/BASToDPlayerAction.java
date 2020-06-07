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

import name.herve.game.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class BASToDPlayerAction {
	public enum Action {
		BUY_TOWER, BUY_WALL, BUY_FACTORY, IMPROVE, START_SPAWN, STOP_SPAWN
	}

	private Action action;
	private String param;
	private BASToDPlayer player;
	private Vector positionOnGrid;
	private long time;

	public BASToDPlayerAction(BASToDPlayer player, Action action) {
		super();
		this.player = player;
		this.action = action;
	}

	public Action getAction() {
		return action;
	}

	public String getParam() {
		return param;
	}

	public BASToDPlayer getPlayer() {
		return player;
	}

	public Vector getPositionOnGrid() {
		return positionOnGrid;
	}

	public long getTime() {
		return time;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public void setPositionOnGrid(Vector positionOnGrid) {
		this.positionOnGrid = positionOnGrid;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
