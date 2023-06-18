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
package name.herve.game.funny;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.herve.game.engine.GameState;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class FunnyGameState extends GameState {
	private Map<String, FunnyGamePlayer> players;
	private List<Ball> balls;
	private int width;
	private int height;

	public FunnyGameState() {
		super();
	}

	public FunnyGameState(int w, int h) {
		super();
		width = w;
		height = h;
		balls = new ArrayList<>();
		players = new HashMap<>();
	}

	public List<Ball> getBalls() {
		return balls;
	}

	public int getHeight() {
		return height;
	}

	public Map<String, FunnyGamePlayer> getPlayers() {
		return players;
	}

	public int getWidth() {
		return width;
	}

	@Override
	public String toString() {
		return "FunnyGameState [players=" + players.size() + ", balls=" + balls.size() + ", width=" + width + ", height=" + height + "]";
	}

}
