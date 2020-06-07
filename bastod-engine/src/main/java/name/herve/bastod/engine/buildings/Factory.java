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
package name.herve.bastod.engine.buildings;

import name.herve.bastod.engine.units.AbstractUnit;
import name.herve.bastod.engine.units.Blocking;
import name.herve.bastod.engine.units.Mobile;
import name.herve.bastod.engine.units.Spawning;
import name.herve.bastod.engine.units.Tank;
import name.herve.game.tools.Constants;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class Factory extends AbstractUnit implements Spawning, Blocking {
	private long lastSpawnTime;
	private boolean spawnEnabled;
	private float tankAccelerationOnGrid;
	private long tankBuildTimeNano;
	private int tankMaxArmor;
	private int tankMetalCost;
	private int tankScoreValue;
	private float tankSpeedOnGrid;

	public Factory() {
		super();
	}

	public int getTankMetalCost() {
		return tankMetalCost;
	}

	@Override
	public boolean isAbleToSpawn(long now) {
		return ((now - lastSpawnTime) >= tankBuildTimeNano) && (getPlayer().getMetal() >= tankMetalCost);
	}

	@Override
	public boolean isSpawnEnabled() {
		return spawnEnabled;
	}

	@Override
	public void setSpawnEnabled(boolean se) {
		spawnEnabled = se;
	}

	public void setTankAccelerationOnGrid(float tankAccelerationOnGrid) {
		this.tankAccelerationOnGrid = tankAccelerationOnGrid;
	}

	public void setTankBuildTimeMilli(int tankBuildTimeMilli) {
		tankBuildTimeNano = tankBuildTimeMilli * Constants.NANO_MILLI;
	}

	public void setTankMaxArmor(int tankMaxArmor) {
		this.tankMaxArmor = tankMaxArmor;
	}

	public void setTankMetalCost(int tankMetalCost) {
		this.tankMetalCost = tankMetalCost;
	}

	public void setTankScoreValue(int tankScoreValue) {
		this.tankScoreValue = tankScoreValue;
	}

	public void setTankSpeedOnGrid(float tankSpeedOnGrid) {
		this.tankSpeedOnGrid = tankSpeedOnGrid;
	}

	@Override
	public Mobile spawn(long now) {
		lastSpawnTime = now;
		getPlayer().removeMetal(tankMetalCost);

		Tank tank = new Tank(tankMaxArmor, tankSpeedOnGrid * getPlayer().getSpeedMultiplier(), tankAccelerationOnGrid, tankScoreValue);
		tank.setPositionOnBoard(getPositionOnBoard().copy());

		return tank;
	}

}
