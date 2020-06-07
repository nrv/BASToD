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

import com.esotericsoftware.minlog.Log;

import name.herve.game.engine.GameEngine;
import name.herve.game.engine.PlayerAction;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class FunnyGameEngine extends GameEngine<FunnyGameState> {
	public final static int BOARD_DIM = 500;
	public final static String ADD_BALL_ACTION = "addBall";
	public final static String READY_ACTION = "ready";

	public FunnyGameEngine(boolean master) {
		super(master, new FunnyGameState(BOARD_DIM, BOARD_DIM));
	}

	@Override
	public PlayerAction executeSpecificPlayerAction(PlayerAction pa) {
		if (ADD_BALL_ACTION.equals(pa.getAction())) {
			Ball b = Ball.fromParams(pa.getParams());
			Log.debug("engine", "adding a ball " + b);
			getState().getBalls().add(b);
		} else if (READY_ACTION.equals(pa.getAction())) {
			Log.debug("engine", "player ready");
			getPlayer(pa.getPlayerUuid()).setPlayerReady();
		}

		return pa;
	}

	@Override
	public void step(long deltaNano) {
		synchronized (getState()) {
			for (Ball b : getState().getBalls()) {
				b.setX(b.getX() + b.getVx());
				b.setY(b.getY() + b.getVy());
				if (b.getX() < 10 || b.getX() > getState().getWidth() - 10) {
					b.setVx(-b.getVx());
				}
				if (b.getY() < 10 || b.getY() > getState().getHeight() - 10) {
					b.setVy(-b.getVy());
				}
			}
		}
	}
}
