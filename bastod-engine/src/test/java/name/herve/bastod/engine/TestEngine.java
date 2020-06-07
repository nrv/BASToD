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

import name.herve.bastod.engine.Game.Type;
import name.herve.bastod.engine.simulator.Simulator;
import name.herve.game.tools.GameException;
import name.herve.game.tools.conf.Configuration;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class TestEngine {
	public static void main(String[] args) throws GameException {
		Configuration gconf = Configuration.load(Type.TWO_PLAYERS.getFile());
		long seed = 1864750354l;

		Game game = GameFactory.createGame(Type.TWO_PLAYERS, gconf, seed);

		Simulator simul = new Simulator(seed);
		simul.setFPS(60);
		simul.setSpeed(10);
		simul.start(game);
	}
}
