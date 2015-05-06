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
package name.herve.bastod.engine.improvements;

import name.herve.bastod.engine.Engine;
import name.herve.bastod.engine.Improvement;
import name.herve.bastod.engine.Player;
import name.herve.bastod.engine.buildings.Factory;
import name.herve.bastod.engine.players.ComputerPlayer;
import name.herve.bastod.engine.units.Blocking;
import name.herve.bastod.tools.GameException;
import name.herve.bastod.tools.conf.Configuration;
import name.herve.bastod.tools.conf.PropertiesConfiguration;
import name.herve.bastod.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class BuyFactoryImprovement extends Improvement {
	private int tankBuildTimeMilli;
	private int tankMaxArmor;
	private int tankMetalCost;
	private int tankScoreValue;
	private float tankSpeedOnGrid;
	private float tankAccelerationOnGrid;

	public BuyFactoryImprovement(Configuration conf) throws GameException {
		super(conf);
		
		tankBuildTimeMilli = conf.getInt(Engine.CF_TANK_BUILD_TIME_MILLI_I);
		tankMetalCost = conf.getInt(Engine.CF_TANK_METAL_COST_I);
		tankMaxArmor = conf.getInt(Engine.CF_TANK_MAX_ARMOR_I);
		tankSpeedOnGrid = conf.getFloat(Engine.CF_TANK_SPEED_F);
		tankAccelerationOnGrid = conf.getFloat(Engine.CF_TANK_ACCELERATION_F);
		tankScoreValue = conf.getInt(Engine.CF_TANK_SCORE_VALUE_I);
	}

	@Override
	public void improve(Engine engine, Player player, Vector position) {
		Factory f = createFactory(engine.fromGridToBoard(position));

		player.addUnit(f);

		if (f instanceof Blocking) {
			engine.closeOnBoard(f.getPositionOnBoard(), true);
		}
		
	}

	public Factory createFactory(Vector boardPosition) {
		Factory f = new Factory();
		f.setTankBuildTimeMilli(tankBuildTimeMilli);
		f.setTankMetalCost(tankMetalCost);
		f.setTankMaxArmor(tankMaxArmor);
		f.setTankSpeedOnGrid(tankSpeedOnGrid);
		f.setTankAccelerationOnGrid(tankAccelerationOnGrid);
		f.setTankScoreValue(tankScoreValue);
		f.setSpawnEnabled(true);
		f.init(Engine._SQUARE_SIZE);

		f.setPositionOnBoard(boardPosition);
		
		return f;
	}
	
	@Override
	public int getCost(Player p) {
		return 0;
	}

	@Override
	public String getName() {
		return Engine.IMP_BUY_FACTORY;
	}

	@Override
	public boolean isAvailableForPlayer(Player p) {
		return p instanceof ComputerPlayer;
	}

}
