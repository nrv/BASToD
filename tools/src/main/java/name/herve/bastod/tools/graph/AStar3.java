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

import java.util.Arrays;

import name.herve.bastod.tools.sortedlist.MyMinBinaryHeap;

// See http://en.wikipedia.org/wiki/A*_search_algorithm
// See http://www.gamasutra.com/view/feature/131505/toward_more_realistic_pathfinding.php?page=2

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class AStar3 extends NoGraphPathFinderAlgorithm {
	private boolean[] closed;
	private int[] f;
	private int[] g;
	private int[] cf;

	private MyMinBinaryHeap open;

	public AStar3(NoGraph nograph) {
		super(nograph);

		int sz = nograph.size();
		closed = new boolean[sz];
		f = new int[sz];
		g = new int[sz];
		cf = new int[sz];

		open = new MyMinBinaryHeap(sz);
		open.setCost(f);
	}

	@Override
	public Path getPath(int start, int end) {
		return getPath(start, end, -1);
	}

	@Override
	public Path getPath(int ios, int ioe, int excluding) {
		Arrays.fill(closed, false);
		if (excluding >= 0) {
			closed[excluding] = true;
		}
		Arrays.fill(f, 0);
		Arrays.fill(g, 0);
		Arrays.fill(cf, -1);
		open.reset();

		open.add(ios);

		int dx;
		int dy;

		dx = nograph.g_x[ios] - nograph.g_x[ioe];
		dy = nograph.g_y[ios] - nograph.g_y[ioe];
		f[ios] = g[ios] + (int) (FLOAT_TO_INT * Math.sqrt((dx * dx) + (dy * dy)));

		while (!open.isEmpty()) {
			int current = open.get();

			if (current == ioe) {
				return reconstructPath(current);
			}

			closed[current] = true;

			for (int nbIdx = 0; nbIdx < 8; nbIdx++) {
				int inb = nograph.g_nb[(current * 8) + nbIdx];
				if (inb >= 0) {
					if (closed[inb] || !nograph.g_a[inb]) {
						continue;
					}

					int gAttempt = g[current] + (int) (FLOAT_TO_INT * nograph.g_c[(current * 8) + nbIdx]);

					int inbHeapIdx = open.getIndex(inb);
					if ((inbHeapIdx == -1) || (gAttempt < g[inb])) {
						cf[inb] = current;
						g[inb] = gAttempt;

						dx = nograph.g_x[inb] - nograph.g_x[ioe];
						dy = nograph.g_y[inb] - nograph.g_y[ioe];
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

	private Path reconstructPath(int current) {
		Path path = null;

		if (cf[current] >= 0) {
			path = reconstructPath(cf[current]);
			path.add(nograph.getPosition(current));
		} else {
			path = new Path();
		}

		return path;
	}
}
