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
package name.herve.bastod.engine.buildings;

import java.util.ArrayList;
import java.util.List;

import name.herve.bastod.engine.Player;
import name.herve.bastod.engine.Shot;
import name.herve.bastod.engine.Unit;
import name.herve.bastod.engine.units.AbstractUnit;
import name.herve.bastod.engine.units.Blocking;
import name.herve.bastod.engine.units.Destructible;
import name.herve.bastod.engine.units.Firing;
import name.herve.bastod.tools.Constants;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class Tower extends AbstractUnit implements Firing, Blocking {
	private float angle;
	private Destructible currentTarget;
	private int damageDealt;
	private long lastFiringTime;
	private float rangeOnBoard;
	private float rangeOnGrid;
	private long reloadTimeNano;
	private float shotSpeedOnGrid;
	private float squaredRangeOnBoard;
	private int statsDamageDone;
	private int statsEnemyKilled;

	public Tower(float rangeOnGrid) {
		super();

		this.rangeOnGrid = rangeOnGrid;
		statsDamageDone = 0;
		statsEnemyKilled = 0;
	}

	@Override
	public void acquireTarget(Player enemy) {
		if ((currentTarget == null) || (!currentTarget.isAlive()) || (!currentTarget.isTargetable()) || (!isInRange(currentTarget))) {
			currentTarget = null;
			float sqDist;
			float currentSqDist = Float.MAX_VALUE;
			for (Unit u : enemy.getUnits()) {
				if (u instanceof Destructible) {
					sqDist = getPositionOnBoard().squaredDistance(u.getPositionOnBoard());
					if ((sqDist <= currentSqDist) && (sqDist <= squaredRangeOnBoard)) {
						currentSqDist = sqDist;
						currentTarget = (Destructible) u;
					}
				}
			}
		}
	}

	@Override
	public void addDamageDone(int amount) {
		statsDamageDone += amount;
	}

	@Override
	public void addEnemyKilled() {
		statsEnemyKilled++;
	}

	@Override
	public Shot fire(long now) {
		lastFiringTime = now;
		return new TowerShot(this, getPositionOnBoard().copy(), currentTarget, shotSpeedOnGrid, damageDealt);
	}

	@Override
	public float getAngle() {
		return angle;
	}

	@Override
	public List<String> getInfos() {
		ArrayList<String> infos = new ArrayList<String>();
		infos.add("Range          : " + rangeOnGrid);
		infos.add("Damage         : " + damageDealt);
		infos.add("Reload         : " + reloadTimeNano);
		infos.add("Damage done    : " + statsDamageDone);
		infos.add("Enemies killed : " + statsEnemyKilled);
		return infos;
	}

	public float getRangeOnBoard() {
		return rangeOnBoard;
	}

	@Override
	public int getTotalDamageDone() {
		return statsDamageDone;
	}

	@Override
	public int getTotalEnemyKilled() {
		return statsEnemyKilled;
	}

	@Override
	public boolean hasTarget() {
		return currentTarget != null;
	}

	@Override
	public void init(int boardSquareSize) {
		rangeOnBoard = rangeOnGrid * boardSquareSize;
		squaredRangeOnBoard = rangeOnBoard * rangeOnBoard;
	}

	@Override
	public boolean isAbleToFire(long now) {
		return (now - lastFiringTime) >= reloadTimeNano;
	}

	public boolean isInRange(Destructible u) {
		return getPositionOnBoard().squaredDistance(u.getPositionOnBoard()) <= squaredRangeOnBoard;
	}

	public void multiplyRange(float factor) {
		rangeOnBoard *= factor;
		squaredRangeOnBoard = rangeOnBoard * rangeOnBoard;
	}

	public void setDamageDealt(int damageDealt) {
		this.damageDealt = damageDealt;
	}

	public void setReloadTimeMilli(long reloadTimeMilli) {
		this.reloadTimeNano = reloadTimeMilli * Constants.NANO_MILLI;
	}

	public void setShotSpeedOnGrid(float shotSpeedOnGrid) {
		this.shotSpeedOnGrid = shotSpeedOnGrid;
	}

	@Override
	public void updateWeapons() {
		if (currentTarget != null) {
			angle = currentTarget.getPositionOnBoard().copy().remove(getPositionOnBoard()).angleDeg();
		}
	}

}
