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
import java.util.Collections;
import java.util.List;

import name.herve.bastod.tools.sortedlist.MyMinBinaryHeap;
import name.herve.bastod.tools.sortedlist.MySortedList;

// See http://en.wikipedia.org/wiki/Dijkstra%27s_algorithm

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class Dijkstra extends GraphPathFinderAlgorithm {
	private int[] dist;
	private int[] previous;

	public Dijkstra(Graph g) {
		super(g);
	}

	public void compute(Node start) {
		compute(start, null);
	}

	public void compute(Node start, Node end) {
		// System.out.println("Dijkstra compute " + start + " - " + end);
		int sz = graph.size();

		dist = new int[sz];
		Arrays.fill(dist, Integer.MAX_VALUE);

		previous = new int[sz];
		Arrays.fill(previous, -1);

		MySortedList q2 = new MyMinBinaryHeap(sz);
		q2.setCost(dist);
		q2.reset();

		int ios = graph.indexOf(start);
		dist[ios] = 0;
		q2.add(ios);

		for (int n = 0; n < sz; n++) {
			if ((n != ios) && graph.get(n).isAvailable()) {
				q2.add(n);
			}
		}

		int e = graph.indexOf(end);

		while (!q2.isEmpty()) {
			int u = q2.get();

			if ((end != null) && (u == e)) {
				break;
			}

			if (dist[u] == Float.MAX_VALUE) {
				break;
			}

			Node nu = graph.get(u);

			for (Node nv : nu) {
				if (nv.isAvailable()) {
					int v = graph.indexOf(nv);
					int alt = dist[u] + (int) (FLOAT_TO_INT * nu.getCost(nv));
					if (alt < dist[v]) {
						dist[v] = alt;
						previous[v] = u;

						q2.updatedCostAtIndex(q2.getIndex(v));
					}
				}
			}
		}
	}

	@Override
	public Node getNode(int id) {
		return graph.get(id);
	}

	public Path getPath(Node end, boolean reverse) {
		return asPath(getPathNodes(end, reverse));
	}

	public List<Node> getPathNodes(Node end) {
		return getPathNodes(end, true);
	}

	public List<Node> getPathNodes(Node end, boolean reverse) {
		List<Node> path = new ArrayList<>();

		int u = graph.indexOf(end);

		while (previous[u] >= 0) {
			path.add(graph.get(u));
			u = previous[u];
		}

		if (reverse) {
			Collections.reverse(path);
		}

		return path;
	}

	@Override
	public List<Node> getPathNodes(Node start, Node end) {
		compute(start, end);
		return getPathNodes(end);
	}
}
