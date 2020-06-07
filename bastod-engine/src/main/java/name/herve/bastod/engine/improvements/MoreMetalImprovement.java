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

import name.herve.bastod.engine.BASToDEngine;
import name.herve.bastod.engine.Improvement;
import name.herve.bastod.engine.BASToDPlayer;
import name.herve.game.tools.GameException;
import name.herve.game.tools.conf.Configuration;
import name.herve.game.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class MoreMetalImprovement extends Improvement {
	private int cost;
	private float factor;

	public MoreMetalImprovement(Configuration conf) throws GameException {
		super(conf);

		cost = conf.getInt(BASToDEngine.CF_IMP_MORE_METAL_COST_I);
		factor = conf.getFloat(BASToDEngine.CF_IMP_MORE_METAL_FACTOR_F);
	}

	@Override
	public int getCost(BASToDPlayer p) {
		return cost;
	}

	@Override
	public String getName() {
		return BASToDEngine.IMP_MORE_METAL;
	}

	@Override
	public void improve(BASToDEngine engine, BASToDPlayer player, Vector position) {
		player.setMetalMultiplier(player.getMetalMultiplier() + factor);
	}

}
