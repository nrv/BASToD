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
import java.util.Arrays;
import java.util.List;

import name.herve.bastod.tools.sortedlist.MyLinkedList;

// See http://en.wikipedia.org/wiki/A*_search_algorithm

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class AStar extends GraphPathFinderAlgorithm {
	private class EuclidianHeuristic implements Heuristic {
		@Override
		public int estimateCost(Node n1, Node n2) {
			return (int) (FLOAT_TO_INT * n1.getPosition().distance(n2.getPosition()));
		}
	}

	private interface Heuristic {
		int estimateCost(Node n1, Node n2);
	}

	// private class ManhattanHeuristic implements Heuristic {
	// @Override
	// public float estimateCost(Node n1, Node n2) {
	// return n1.getPosition().manhattan(n2.getPosition());
	// }
	// }

	private Heuristic h;

	public AStar(Graph g) {
		super(g);

		h = new EuclidianHeuristic();
	}

	@Override
	public Node getNode(int id) {
		return graph.get(id);
	}

	@Override
	public List<Node> getPathNodes(Node start, Node end) {
		// System.out.println("AStar path " + start + " - " + end);

		int sz = graph.size();

		boolean[] closed = new boolean[sz];
		Arrays.fill(closed, false);

		int[] f = new int[sz];
		Arrays.fill(f, 0);

		int[] g = new int[sz];
		Arrays.fill(g, 0);

		int[] cf = new int[sz];
		Arrays.fill(cf, -1);

		MyLinkedList open2 = new MyLinkedList();
		open2.setCost(f);
		open2.reset();

		int ios = graph.indexOf(start);
		int ioe = graph.indexOf(end);
		open2.add(ios);
		f[ios] = g[ios] + h.estimateCost(start, end);

		while (!open2.isEmpty()) {
			int current = open2.get();

			if (current == ioe) {
				return reconstructPath(cf, current);
			}

			closed[current] = true;

			Node cn = graph.get(current);
			for (Node nb : cn) {
				int inb = graph.indexOf(nb);
				if (closed[inb] || !nb.isAvailable()) {
					continue;
				}

				int gAttempt = g[current] + (int) (FLOAT_TO_INT * cn.getCost(nb));
				int nbIdx = open2.getIndex(inb);

				if ((nbIdx == -1) || (gAttempt < g[inb])) {
					cf[inb] = current;
					g[inb] = gAttempt;
					int finb = gAttempt + h.estimateCost(nb, end);
					f[inb] = finb;

					if (nbIdx != -1) {
						open2.updatedCostAtIndex(nbIdx);
					} else {
						open2.add(inb);
					}
				}
			}
		}

		return null;
	}

	private List<Node> reconstructPath(int[] c, int current) {
		List<Node> path = null;

		if (c[current] >= 0) {
			path = reconstructPath(c, c[current]);
			path.add(graph.get(current));
		} else {
			path = new ArrayList<>();
		}

		return path;
	}
}
