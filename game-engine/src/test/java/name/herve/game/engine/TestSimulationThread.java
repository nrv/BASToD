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

import java.util.Random;

import name.herve.game.engine.simu.SimulationThread;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class TestSimulationThread {
	private class DummySimulated extends GameEngine<GameState> {
		public DummySimulated(boolean master, GameState state) {
			super(master, state);
		}

		private Random rd = new Random(System.nanoTime());
		private int todo = 10000;

		@Override
		public void step(long deltaNano) {
			int done = Math.min(todo, rd.nextInt(100));
			for (int i = 0; i < done; i++) {
				for (int j = 0; j < 10000; j++) {
					double r = Math.sqrt(rd.nextDouble());
					r = Math.exp(r) * Math.PI;
					r = Math.sqrt(r);
				}
			}
			todo -= done;
			if (todo <= 0) {
				event(GameEngine.STOP_GAME_EVENT);
			}
		}

		@Override
		public PlayerAction executeSpecificPlayerAction(PlayerAction pa) {
			return pa;
		}
	}

	private void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// ignore
		}
	}

	public static void main(String[] args) {
		new TestSimulationThread().start();
	}

	private void start() {
		DummySimulated dummy = new DummySimulated(true, new GameState());
		SimulationThread t = new SimulationThread(10, dummy);
		t.start();
		sleep(2000);
		dummy.event(GameEngine.START_GAME_EVENT);
	}

}
