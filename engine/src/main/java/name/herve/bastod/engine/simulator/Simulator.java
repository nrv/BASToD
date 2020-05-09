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
package name.herve.bastod.engine.simulator;

import java.text.DecimalFormat;

import name.herve.bastod.engine.Engine;
import name.herve.bastod.engine.Game;
import name.herve.bastod.engine.Player;
import name.herve.bastod.tools.Constants;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class Simulator {
	private Engine engine;
	private int FPS;

	public Simulator(long seed) {
		super();
		engine = new Engine(seed);
		setFPS(50);
	}

	public Engine getEngine() {
		return engine;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	public void setFPS(int FPS) {
		this.FPS = FPS;
	}

	public void setSpeed(float speed) {
		engine.setSpeed(speed);
	}

	public void start(Game game) {
		engine.setGame(game);

		Player[] players = game.getPlayers().toArray(new Player[2]);

		DecimalFormat df = new DecimalFormat("0.00");

		engine.start();

		long startTime = System.nanoTime();
		long currentTime = 0;
		long previousTime = 0;
		long sleepTime = 0;
		long frameTime = Constants.NANO / FPS;
		long lastFrameTime = 0;
		long accSleep = 0;
		int f = 0;
		currentTime = System.nanoTime();
		while (!game.isOver()) {
			previousTime = System.nanoTime();
			engine.step(previousTime - currentTime);
			currentTime = System.nanoTime();
			f++;
			lastFrameTime = currentTime - previousTime;
			accSleep += (frameTime - lastFrameTime);

			sleepTime = accSleep / Constants.NANO_MILLI;
			if (sleepTime >= 50) {
				try {
					accSleep -= (sleepTime * Constants.NANO_MILLI);
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if ((f % 100) == 0) {
				float t = (float) (currentTime - startTime) / (float) Constants.NANO;
				System.out.println("Time : " + df.format(engine.getElapsedTimeSec()) + " s - FPS : " + df.format(f / t) + " / " + FPS + " - " + players[0] + " - " + players[1]);
			}
		}
	}
}
