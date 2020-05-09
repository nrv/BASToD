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
package name.herve.bastod.tools.math;

import java.util.Arrays;

import name.herve.bastod.tools.Constants;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class TemporalDynamicMean {
	private int index;
	private float[] values;
	private long[] timestamps;
	private long windowSize;

	public TemporalDynamicMean(long windowSize, int maxTimestampPerSec) {
		super();

		index = 0;
		this.windowSize = windowSize;
		int size = (int) Math.ceil((windowSize * maxTimestampPerSec) / Constants.NANO);
		values = new float[size];
		Arrays.fill(values, 0);
		timestamps = new long[size];
		Arrays.fill(timestamps, 0);
	}

	public void addValue(long time, float v) {
		if (v > 0) {
			// System.out.println("TemporalDynamicMean[" + time + "] = " + v);
			values[index] = v;
			timestamps[index] = time;

			index++;
			if (index == values.length) {
				index = 0;
			}
		}
	}

	public float getMean(long now) {
		float m = 0;
		long window = now - windowSize;
		// System.out.println("now : " + now);
		// System.out.println("window : " + window);
		for (int i = 0; i < values.length; i++) {
			if (timestamps[i] > window) {
				m += values[i];
			}
		}

		return m;
	}
}
