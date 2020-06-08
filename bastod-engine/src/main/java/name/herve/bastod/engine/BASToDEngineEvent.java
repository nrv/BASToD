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

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class BASToDEngineEvent {
	public enum Type {
		BOARD_MODIFIED, SPAW_MODIFIED
	}

	private Type type;
	private BASToDPlayer player;

	public BASToDEngineEvent(Type type) {
		super();
		this.type = type;
	}

	public BASToDPlayer getPlayer() {
		return player;
	}

	public Type getType() {
		return type;
	}

	public void setPlayer(BASToDPlayer player) {
		this.player = player;
	}
}