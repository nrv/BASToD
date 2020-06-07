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
package name.herve.game.tools.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import name.herve.game.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class Path implements Iterable<Vector> {
	private List<Vector> positions;
	private int size;

	public Path() {
		super();
		positions = new ArrayList<>();
		size = 0;
	}

	public boolean add(Vector e) {
		size++;
		return positions.add(e);
	}

	@Override
	public Iterator<Vector> iterator() {
		return positions.iterator();
	}

	// public void optimize(Vector start) {
	// if (size > 0) {
	// List<Vector> np = new ArrayList<Vector>();
	// Vector previous = start;
	// Vector last = start;
	// for (Vector current : positions) {
	// if (!current.isAligned(last)) {
	// np.add(previous);
	// last = previous;
	// }
	// previous = current;
	// }
	//
	// if (np.isEmpty() || (np.get(np.size() - 1) != positions.get(positions.size() - 1))) {
	// np.add(positions.get(positions.size() - 1));
	// }
	//
	// positions = np;
	// }
	// }

	public int size() {
		return size;
	}

	@Override
	public String toString() {
		String p = "";

		for (Vector pos : this) {
			if (p.length() > 0) {
				p += " -> ";
			}
			p += pos.toString();
		}

		return p;
	}
}
