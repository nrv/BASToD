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
package name.herve.bastod.tools.graph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import name.herve.bastod.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class Node implements Iterable<Node> {
	private boolean available;
	private int id;
	private Map<Node, Float> neighbours;
	private Vector position;

	public Node() {
		super();
		neighbours = new HashMap<Node, Float>();
		setAvailable(true);
	}

	public void addNeighbour(Node n, Float cost) {
		neighbours.put(n, cost);
	}

	public Float getCost(Node key) {
		return neighbours.get(key);
	}

	public int getId() {
		return id;
	}

	public Vector getPosition() {
		return position;
	}

	public boolean isAvailable() {
		return available;
	}

	@Override
	public Iterator<Node> iterator() {
		return neighbours.keySet().iterator();
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	void setId(int id) {
		this.id = id;
	}

	public void setPosition(Vector position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "Node(" + id + "/" + neighbours.size() + "/" + position + ")";
	}
}
