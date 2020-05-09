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

import name.herve.bastod.tools.sortedlist.MyMinBinaryHeap;

// See http://en.wikipedia.org/wiki/A*_search_algorithm
// See http://www.gamasutra.com/view/feature/131505/toward_more_realistic_pathfinding.php?page=2

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class AStar2 extends GraphPathFinderAlgorithm {
	private boolean[] closed;
	private int[] f;
	private int[] g;
	private int[] cf;

	private boolean g_a[];
	private int g_x[];
	private int g_y[];
	private int g_c[];
	private int g_nb[];

	private MyMinBinaryHeap open;

	public AStar2(Graph graph) {
		super(graph);

		this.graph = graph;

		int sz = graph.size();
		closed = new boolean[sz];
		f = new int[sz];
		g = new int[sz];
		cf = new int[sz];

		open = new MyMinBinaryHeap(sz);
		open.setCost(f);

		dumpGraphInInternalStructure();
	}

	private void dumpGraphInInternalStructure() {
		int sz = graph.size();
		g_x = new int[sz];
		g_y = new int[sz];
		g_a = new boolean[sz];
		g_nb = new int[sz * 8];
		g_c = new int[sz * 8];

		int idx;

		for (Node node : graph) {
			int n = graph.indexOf(node);
			g_a[n] = node.isAvailable();
			g_x[n] = node.getPosition().getXInt();
			g_y[n] = node.getPosition().getYInt();
			int nb = 0;
			for (Node nbn : node) {
				idx = (n * 8) + nb;
				g_nb[idx] = graph.indexOf(nbn);
				g_c[idx] = (int) (node.getCost(nbn) * FLOAT_TO_INT);
				nb++;
			}
			for (; nb < 8; nb++) {
				idx = (n * 8) + nb;
				g_nb[idx] = -1;
				g_c[idx] = Integer.MAX_VALUE;
			}
		}
	}

	@Override
	public Node getNode(int id) {
		return graph.get(id);
	}

	@Override
	public List<Node> getPathNodes(Node start, Node end) {
		// System.out.println("AStar2.path(" + start + ", " + end + ")");
		Arrays.fill(closed, false);
		Arrays.fill(f, 0);
		Arrays.fill(g, 0);
		Arrays.fill(cf, -1);
		open.reset();

		int ios = graph.indexOf(start);
		int ioe = graph.indexOf(end);

		open.add(ios);

		int dx;
		int dy;

		dx = g_x[ios] - g_x[ioe];
		dy = g_y[ios] - g_y[ioe];
		f[ios] = g[ios] + (int) (FLOAT_TO_INT * Math.sqrt((dx * dx) + (dy * dy)));

		while (!open.isEmpty()) {
			int current = open.get();

			if (current == ioe) {
				return reconstructPath(current);
			}

			closed[current] = true;

			for (int nbIdx = 0; nbIdx < 8; nbIdx++) {
				int inb = g_nb[(current * 8) + nbIdx];
				if (inb >= 0) {
					if (closed[inb] || !g_a[inb]) {
						continue;
					}

					int gAttempt = g[current] + g_c[(current * 8) + nbIdx];

					int inbHeapIdx = open.getIndex(inb);
					if ((inbHeapIdx == -1) || (gAttempt < g[inb])) {
						cf[inb] = current;
						g[inb] = gAttempt;

						dx = g_x[inb] - g_x[ioe];
						dy = g_y[inb] - g_y[ioe];
						int finb = gAttempt + (int) (FLOAT_TO_INT * Math.sqrt((dx * dx) + (dy * dy)));

						f[inb] = finb;

						if (inbHeapIdx == -1) {
							open.add(inb);
						} else {
							open.updatedCostAtIndex(inbHeapIdx);
						}
					}
				}
			}
		}

		return null;
	}

	private List<Node> reconstructPath(int current) {
		List<Node> path = null;

		if (cf[current] >= 0) {
			path = reconstructPath(cf[current]);
			path.add(graph.get(current));
		} else {
			path = new ArrayList<>();
		}

		return path;
	}
}
