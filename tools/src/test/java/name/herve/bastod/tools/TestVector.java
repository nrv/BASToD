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
package name.herve.bastod.tools;

import name.herve.bastod.tools.math.Vector;

public class TestVector {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Vector v1 = new Vector();
		Vector v2 = new Vector();
		
		for (int i = 0; i <= 360; i++) {
			Vector v2p = v2.copy().rotateRad(2 * Math.PI * i / 360);
			System.out.println(i + " -> " + v2p + " / " + v1.angleRad(v2p) / Math.PI);
		}

	}

}
