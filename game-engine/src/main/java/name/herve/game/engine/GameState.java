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

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class GameState {
	public GameState() {
		super();
		gameStarted = false;
		currentTick = 0;
	}

	private boolean gameStarted;
	private long currentTick;

	public long getCurrentTick() {
		return currentTick;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public void setCurrentTick(long currentTick) {
		this.currentTick = currentTick;
	}

	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}

}
