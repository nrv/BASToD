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
package name.herve.bastod.engine.improvements;

import name.herve.bastod.engine.Engine;
import name.herve.bastod.engine.Improvement;
import name.herve.bastod.engine.Player;
import name.herve.bastod.engine.buildings.Tower;
import name.herve.bastod.engine.units.Blocking;
import name.herve.bastod.tools.GameException;
import name.herve.bastod.tools.conf.Configuration;
import name.herve.bastod.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class BuyTowerImprovement extends Improvement {
	private int towerMetalCost;
	private float towerRange;
	private int towerReload;
	private int towerShotDamage;
	private float towerShotSpeed;

	public BuyTowerImprovement(Configuration conf) throws GameException {
		super(conf);

		towerMetalCost = conf.getInt(Engine.CF_TOWER_METAL_COST_I);
		towerRange = conf.getFloat(Engine.CF_TOWER_SHOT_RANGE_F);
		towerShotDamage = conf.getInt(Engine.CF_TOWER_SHOT_DAMAGE_I);
		towerShotSpeed = conf.getFloat(Engine.CF_TOWER_SHOT_SPEED_F);
		towerReload = conf.getInt(Engine.CF_TOWER_RELOAD_TIME_MILLI_I);
	}

	public Tower createTower(Vector boardPosition) {
		Tower tw = new Tower(towerRange);
		tw.setPositionOnBoard(boardPosition);
		tw.setDamageDealt(towerShotDamage);
		tw.setShotSpeedOnGrid(towerShotSpeed);
		tw.setReloadTimeMilli(towerReload);
		tw.init(Engine._SQUARE_SIZE);

		return tw;
	}

	@Override
	public int getCost(Player p) {
		return towerMetalCost;
	}

	@Override
	public String getName() {
		return Engine.IMP_BUY_TOWER;
	}

	@Override
	public void improve(Engine engine, Player player, Vector position) {
		Tower tw = createTower(engine.fromGridToBoard(position));

		player.addUnit(tw);

		if (tw instanceof Blocking) {
			engine.closeOnBoard(tw.getPositionOnBoard(), true);
		}

	}

}
