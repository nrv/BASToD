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
package name.herve.bastod.engine;

import name.herve.game.tools.conf.Configuration;
import name.herve.game.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public abstract class Improvement {
	public Improvement(Configuration conf) {
		super();
	}

	public void buy(Engine engine, Player player) {
		buy(engine, player, null);
	}

	public void buy(Engine engine, Player player, Vector position) {
		player.removeMetal(getCost(player));
		improve(engine, player, position);
	}

	public abstract int getCost(Player p);

	public abstract String getName();

	public abstract void improve(Engine engine, Player player, Vector position);

	public boolean isAffordableForPlayer(Player p) {
		return getCost(p) <= p.getMetal();
	}

	public boolean isAvailableForPlayer(Player p) {
		return true;
	}

	@Override
	public String toString() {
		return "Improvement [" + getName() + "]";
	}
}
