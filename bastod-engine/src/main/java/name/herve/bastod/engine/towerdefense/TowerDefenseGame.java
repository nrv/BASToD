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
package name.herve.bastod.engine.towerdefense;

import java.util.ArrayList;
import java.util.List;

import name.herve.bastod.engine.BASToDGame;
import name.herve.bastod.engine.BASToDPlayer;
import name.herve.bastod.engine.BASToDPlayerAction;
import name.herve.bastod.engine.BASToDPlayerAction.Action;
import name.herve.bastod.engine.BASToDPlayerActionsProvider;
import name.herve.bastod.engine.Unit;
import name.herve.bastod.engine.buildings.Factory;
import name.herve.game.tools.Constants;
import name.herve.game.tools.GameException;
import name.herve.game.tools.conf.Configuration;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class TowerDefenseGame implements BASToDPlayerActionsProvider {
	public final static String CF_TDG_LEVEL_WAIT_TIME_I = "tdg.level.wait_time";

	private BASToDGame game;
	private BASToDPlayer me;

	private long nextLevelAt;
	private long waitBeforeLaunch;
	private TowerDefenseLevel level;
	private Factory factory;
	private float difficulty;
	private int n;

	public TowerDefenseGame(BASToDPlayer me, BASToDGame game) throws GameException {
		super();
		this.game = game;
		this.me = me;

		Configuration conf = game.getConf();
		waitBeforeLaunch = conf.getInt(CF_TDG_LEVEL_WAIT_TIME_I);

		level = null;
		difficulty = 0;
		n = 0;

		me.setMetal(0);
		me.setMetalMultiplier(0);
		me.setMaxMetal(Integer.MAX_VALUE);
	}

	@Override
	public List<BASToDPlayerAction> getActions(long now) {
		List<BASToDPlayerAction> acts = null;

		if (factory == null) {
			for (Unit u : me.getUnits()) {
				if (u instanceof Factory) {
					factory = (Factory) u;
					break;
				}
			}
			if (factory == null) {
				if (acts == null) {
					acts = new ArrayList<>();
				}

				BASToDPlayerAction pa = new BASToDPlayerAction(me, Action.BUY_FACTORY);
				pa.setPositionOnGrid(game.getBoard().fromBoardToGrid(me.getStartPositionOnBoard()));
				acts.add(pa);
			}

			return acts;
		}

		if (level == null) {
			n++;
			level = new TowerDefenseLevel();
			level.n = n;
			level.metalAvailable = (int) (500 * (1 + difficulty));
			level.waitBetweenSpawnMilli = (int) (1000 * (1 - (difficulty / 10)));
			level.speed = 6 * (1 + (difficulty / 10));
			level.tankArmor = (int) (20 * (1 + (difficulty * 3)));

			nextLevelAt = now + (waitBeforeLaunch * Constants.NANO_MILLI);
		}

		if ((now >= nextLevelAt) && !level.started) {
			level.start(me, factory);
		}

		if (level.started && (me.getMetal() < factory.getTankMetalCost())) {
			level = null;
			difficulty += 0.1;
		}

		return acts;
	}

	@Override
	public BASToDPlayer getPlayer() {
		return me;
	}

}
