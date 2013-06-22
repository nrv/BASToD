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

import java.util.List;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public abstract class GraphPathFinderAlgorithm extends PathFinderAlgorithm {
	protected Graph graph;

	public GraphPathFinderAlgorithm(Graph graph) {
		super();
		this.graph = graph;
	}

	public Path asPath(List<Node> pn) {
		if (pn == null) {
			return null;
		}

		Path p = new Path();

		for (Node n : pn) {
			p.add(n.getPosition().copy());
		}

		return p;
	}

	public abstract Node getNode(int id);

	public Path getPath(int start, int end) {
		return asPath(getPathNodes(getNode(start), getNode(end)));
	}

	public Path getPath(Node start, Node end) {
		return asPath(getPathNodes(start, end));
	}

	public abstract List<Node> getPathNodes(Node start, Node end);

	@Override
	public Path getPath(int start, int end, int excluding) {
		throw new RuntimeException("Not implemented");
	}
}
