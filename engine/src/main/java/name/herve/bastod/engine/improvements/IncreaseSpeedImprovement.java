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
import name.herve.bastod.tools.SLTDException;
import name.herve.bastod.tools.conf.Configuration;
import name.herve.bastod.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class IncreaseSpeedImprovement extends Improvement {
	private int cost;
	private float factor;

	public IncreaseSpeedImprovement(Configuration conf) throws SLTDException {
		super(conf);
		
		cost = conf.getInt(Engine.CF_IMP_INCREASE_SPEED_COST_I);
		factor = conf.getFloat(Engine.CF_IMP_INCREASE_SPEED_FACTOR_F);
	}

	@Override
	public void improve(Engine engine, Player player, Vector position) {
		player.setSpeedMultiplier(player.getSpeedMultiplier() + factor);
	}

	@Override
	public int getCost(Player p) {
		return cost;
	}

	@Override
	public String getName() {
		return Engine.IMP_INCREASE_SPEED;
	}

}
