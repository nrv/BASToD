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
package name.herve.bastod.engine;

import name.herve.bastod.engine.Board;
import name.herve.bastod.tools.graph.Path;
import name.herve.bastod.tools.math.Dimension;
import name.herve.bastod.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class TestGraph {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Board board = new Board(new Dimension(10, 10), 10);
		board.clearPathFinderCache();

		Vector s = new Vector(1, 3);
		Vector e = new Vector(7, 5);
		
		Path p = board.shortestPathOnGrid(s, e);
		System.out.print(s + " - " + p.size() + " : ");
		System.out.println(p);
		
		board.clearPathFinderCache();
		
		p = board.shortestPathOnGrid(s, e);
		System.out.print(s + " - " + p.size() + " : ");
		System.out.println(p);
	}

}
