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

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.esotericsoftware.minlog.Log;

import name.herve.game.engine.GameEngine;
import name.herve.game.engine.GamePlayerAction;
import name.herve.game.engine.gpi.DefaultGamePlayerInterface;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class FunnyGameEngine extends GameEngine<FunnyGameState> {
	private static String[] ANIMALS = { "cat", "dog", "horse", "eagle", "tiger", "lion", "ant", "wasp", "worm", "bear", "chicken", "spider" };
	private static String[] ADJECTIVES = { "Brave", "Excited", "Proud", "Attractive", "Happy", "Sweet", "Clever", "Warm", "Strong", "Mysterious" };

	public final static int BOARD_DIM = 500;

	public final static String ADD_BALL_ACTION = "addBall";
	public final static String READY_ACTION = "ready";
	private Set<String> existingNames;

	public FunnyGameEngine(boolean master) {
		super(master, new FunnyGameState(BOARD_DIM, BOARD_DIM));
	}

	@Override
	public GamePlayerAction executeSpecificPlayerAction(GamePlayerAction pa) {
		if (ADD_BALL_ACTION.equals(pa.getAction())) {
			Ball b = Ball.fromParams(pa.getParams());
			FunnyGamePlayer p = getState().getPlayers().get(b.getPlayerUUID());
			Log.debug("engine", "adding a ball [" + p.getName() + "] " + b);
			getState().getBalls().add(b);
		} else if (READY_ACTION.equals(pa.getAction())) {
			Log.debug("engine", "player ready");
			getPlayerInterface(pa.getPlayerUuid()).setPlayerReady();
		}

		return pa;
	}

	private String generateNewName() {
		if (existingNames == null) {
			existingNames = new HashSet<>();
		}
		Random r = new Random(System.currentTimeMillis());
		while (true) {
			String name = ADJECTIVES[r.nextInt(ADJECTIVES.length)] + " " + ANIMALS[r.nextInt(ANIMALS.length)];
			if (!existingNames.contains(name)) {
				existingNames.add(name);
				return name;
			}
		}
	}

	@Override
	public void registerPlayer(DefaultGamePlayerInterface gpi) {
		super.registerPlayer(gpi);

		if (isMaster()) {
			FunnyGamePlayer p = new FunnyGamePlayer();
			p.setName(generateNewName());
			getState().getPlayers().put(gpi.getPlayerUuid(), p);
			Log.debug("engine", "player <" + p.getName() + "> connected");
		}
	}

	@Override
	public void step(long deltaNano) {
		synchronized (getState()) {
			for (Ball b : getState().getBalls()) {
				b.setX(b.getX() + b.getVx());
				b.setY(b.getY() + b.getVy());
				if ((b.getX() < 12) || (b.getX() > (getState().getWidth() - 12))) {
					b.setVx(-b.getVx());
				}
				if ((b.getY() < 12) || (b.getY() > (getState().getHeight() - 12))) {
					b.setVy(-b.getVy());
				}
				Log.info(b.toString());
			}
		}
	}
}
