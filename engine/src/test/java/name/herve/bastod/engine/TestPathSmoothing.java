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

import name.herve.bastod.engine.pathfinder.PathFinder;
import name.herve.bastod.engine.pathfinder.PathFinder.Algorithm;
import name.herve.bastod.tools.graph.Path;
import name.herve.bastod.tools.math.Dimension;
import name.herve.bastod.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class TestPathSmoothing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PathFinder pf = new PathFinder(Algorithm.THETASTAR, new Dimension(10, 10), false);

		Vector s = new Vector(1, 1);
		Path p = new Path();
		p.add(new Vector(3, 5));
		p.add(new Vector(6, 3));
		p.add(new Vector(8, 1));

		System.out.println(s + " -> " + p);

		p = pf.smoothOnBoard(s, p, 1, 3);

		System.out.println(s + " -> " + p);
	}

}
