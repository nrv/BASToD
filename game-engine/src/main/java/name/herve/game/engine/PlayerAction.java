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
package name.herve.game.engine;

import java.util.Arrays;

public class PlayerAction {
	private long tick;
	private String playerUuid;
	private String action;
	private String[] params;

	public PlayerAction() {
		super();
	}

	public String getAction() {
		return action;
	}

	public String[] getParams() {
		return params;
	}

	public String getPlayerUuid() {
		return playerUuid;
	}

	public long getTick() {
		return tick;
	}

	public PlayerAction setAction(String action) {
		this.action = action;
		return this;
	}

	public PlayerAction setParams(String[] params) {
		this.params = params;
		return this;
	}

	public PlayerAction setPlayerUuid(String uuid) {
		playerUuid = uuid;
		return this;
	}

	public PlayerAction setTick(long tick) {
		this.tick = tick;
		return this;
	}

	@Override
	public String toString() {
		return "PlayerAction [action=" + action + ", tick=" + tick + ", playerUuid=" + playerUuid + ", params=" + Arrays.toString(params) + "]";
	}
}
