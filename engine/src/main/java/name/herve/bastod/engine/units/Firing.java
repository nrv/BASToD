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
package name.herve.bastod.engine.units;

import name.herve.bastod.engine.Player;
import name.herve.bastod.engine.Shot;
import name.herve.bastod.engine.Unit;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public interface Firing extends Unit {
	boolean isAbleToFire(long now);
	boolean hasTarget();
	void acquireTarget(Player enemy);
	Shot fire(long now);
	void updateWeapons();
	void addDamageDone(int amount);
	void addEnemyKilled();
	int getTotalDamageDone();
	int getTotalEnemyKilled();
}
