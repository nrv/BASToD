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
package name.herve.bastod.tools.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class Graph implements Iterable<Node> {
	private int id;
	private List<Node> nodes;

	public Graph() {
		super();
		id = 0;
		nodes = new ArrayList<>();
	}

	public void addEdge(Node node1, Node node2, float c) {
		node1.addNeighbour(node2, c);
		node2.addNeighbour(node1, c);
	}

	public void addNode(Node n) {
		n.setId(id++);
		nodes.add(n);
	}

	public Node get(int index) {
		return nodes.get(index);
	}

	public int indexOf(Node o) {
		return nodes.indexOf(o);
	}

	@Override
	public Iterator<Node> iterator() {
		return nodes.iterator();
	}

	public int size() {
		return nodes.size();
	}
}
