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
package name.herve.bastod.tools.math;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class DynamicMean {
	private int index;
	private float[] values;
	private boolean filled;

	public DynamicMean(int size) {
		super();
		
		index = 0;
		values = new float[size];
		filled = false;
	}
	
	public void addValue(float v) {
		values[index++] = v;
		if (index == values.length) {
			index = 0;
			filled = true;
		}
	}
	
	public float getMean() {
		float m = 0f;
		if (filled) {
			for (int i = 0; i < values.length; i++) {
				m += values[i];
			}
			return m / values.length;
		} else if (index > 0) {
			for (int i = 0; i < index; i++) {
				m += values[i];
			}
			return m / index;
		} else {
			return 0f;
		}
	}
}
