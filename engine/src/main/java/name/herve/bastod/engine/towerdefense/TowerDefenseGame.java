/*
 * Copyright 2012, 2013 Nicolas HERVE
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

import name.herve.bastod.engine.Game;
import name.herve.bastod.engine.Player;
import name.herve.bastod.engine.PlayerAction;
import name.herve.bastod.engine.PlayerActionsProvider;
import name.herve.bastod.engine.Unit;
import name.herve.bastod.engine.PlayerAction.Action;
import name.herve.bastod.engine.buildings.Factory;
import name.herve.bastod.tools.Constants;
import name.herve.bastod.tools.SLTDException;
import name.herve.bastod.tools.conf.Configuration;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class TowerDefenseGame implements PlayerActionsProvider {
	public final static String CF_TDG_LEVEL_WAIT_TIME_I = "tdg.level.wait_time";

	private Game game;
	private Player me;

	private long nextLevelAt;
	private long waitBeforeLaunch;
	private TowerDefenseLevel level;
	private Factory factory;
	private float difficulty;
	private int n;

	public TowerDefenseGame(Player me, Game game) throws SLTDException {
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
	public List<PlayerAction> getActions(long now) {
		List<PlayerAction> acts = null;

		if (factory == null) {
			for (Unit u : me.getUnits()) {
				if (u instanceof Factory) {
					factory = (Factory) u;
					break;
				}
			}
			if (factory == null) {
				if (acts == null) {
					acts = new ArrayList<PlayerAction>();
				}

				PlayerAction pa = new PlayerAction(me, Action.BUY_FACTORY);
				pa.setPositionOnGrid(game.getBoard().fromBoardToGrid(me.getStartPositionOnBoard()));
				acts.add(pa);
			}
			
			return acts;
		}
		
		if (level == null) {
			n++;
			level = new TowerDefenseLevel();
			level.n = n;
			level.metalAvailable = (int)(500 * (1 + difficulty));
			level.waitBetweenSpawnMilli = (int)(1000 * (1 - difficulty / 10));
			level.speed = 6 * (1 + difficulty / 10);
			level.tankArmor = (int)(20 * (1 + difficulty * 3));
			
			nextLevelAt = now + waitBeforeLaunch * Constants.NANO_MILLI;
		}
		
		if (now >= nextLevelAt && !level.started) {
			level.start(me, factory);
		}
		
		if (level.started && me.getMetal() < factory.getTankMetalCost()) {
			level = null;
			difficulty += 0.1;
		}

		return acts;
	}

	@Override
	public Player getPlayer() {
		return me;
	}

}
