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

import name.herve.bastod.tools.Constants;
import name.herve.bastod.tools.math.TemporalDynamicMean;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class Statistics {
	private TemporalDynamicMean metalAdded;
	private TemporalDynamicMean metalRemoved;
	private int nbUnitsCrossed;
	private int nbUnitsLost;
	private int nbUnitsSpawn;

	public Statistics() {
		super();

		nbUnitsCrossed = 0;
		nbUnitsSpawn = 0;
		nbUnitsLost = 0;

		metalAdded = new TemporalDynamicMean(Constants.NANO, 100);
		metalRemoved = new TemporalDynamicMean(Constants.NANO, 100);
	}

	public float getMetalAddedMean(long now) {
		return metalAdded.getMean(now);
	}

	public float getMetalRemovedMean(long now) {
		return metalRemoved.getMean(now);
	}

	public int getNbUnitsCrossed() {
		return nbUnitsCrossed;
	}

	public int getNbUnitsLost() {
		return nbUnitsLost;
	}

	public int getNbUnitsSpawn() {
		return nbUnitsSpawn;
	}

	public int incNbUnitsCrossed() {
		return ++nbUnitsCrossed;
	}

	public int incNbUnitsLost() {
		return ++nbUnitsLost;
	}

	public int incNbUnitsSpawn() {
		return ++nbUnitsSpawn;
	}

	public void newStep(long now, float added, float removed) {
		metalAdded.addValue(now, added);
		metalRemoved.addValue(now, removed);
	}

	@Override
	public String toString() {
		return "(" + getNbUnitsSpawn() + ";" + getNbUnitsCrossed() + ";" + getNbUnitsLost() + ")";
	}
}
