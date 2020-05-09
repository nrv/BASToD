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

import name.herve.bastod.tools.graph.NoGraph;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class TestLineOfSight {

	public static void main(String[] args) {
		NoGraph ng = new NoGraph(11, 11, true);

		System.out.println("Open");
		test(ng);

		for (int y1 = 0; y1 < 11; y1++) {
			System.out.println("Closing 5," + y1);
			ng.close(5, y1);
			test(ng);
		}

	}

	private static void test(NoGraph ng) {
		int x0 = 0;
		int y0 = 5;
		int x1 = 10;

		for (int y1 = 0; y1 < 11; y1++) {
			System.out.println(" - " + y1 + " : " + ng.lineOfSight(ng.getNode(x0, y0), ng.getNode(x1, y1)));
		}
	}

}
