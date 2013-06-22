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

import java.util.Arrays;

import name.herve.bastod.tools.sortedlist.MyMinBinaryHeap;

// See http://aigamedev.com/open/tutorials/theta-star-any-angle-paths/

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class ThetaStar extends NoGraphPathFinderAlgorithm {
	private boolean[] closed;
	private int[] f;
	private int[] g;
	private int ioe;
	private MyMinBinaryHeap open;
	private int[] parent;

	public ThetaStar(NoGraph nograph) {
		super(nograph);

		int sz = nograph.size();
		closed = new boolean[sz];
		f = new int[sz];
		g = new int[sz];
		parent = new int[sz];
		ioe = -1;

		open = new MyMinBinaryHeap(sz);
		open.setCost(f);
	}

	private void computeCost(int s, int sp, int nbIdx) {
		if (nograph.lineOfSight(parent[s], sp)) {
			int gAttempt = g[parent[s]] + h(parent[s], sp);
			if (gAttempt < g[sp]) {
				parent[sp] = parent[s];
				g[sp] = gAttempt;
			}
		} else {
			int gAttempt = g[s] + h(s, sp);
			if (gAttempt < g[sp]) {
				parent[sp] = s;
				g[sp] = gAttempt;
			}
		}
	}

	@Override
	public Path getPath(int start, int end) {
		return getPath(start, end, -1);
	}

	@Override
	public Path getPath(int sstart, int goal, int excluding) {
		// System.out.println("ThetaStar.getPath(" + sstart + ", " + goal + ", " + excluding + ")");
		this.ioe = goal;

		Arrays.fill(closed, false);
		if (excluding >= 0 && excluding != goal) {
			closed[excluding] = true;
		}
		Arrays.fill(f, 0);
		Arrays.fill(g, 0);
		Arrays.fill(parent, -1);
		open.reset();

		parent[sstart] = sstart;

		f[sstart] = g[sstart] + h(sstart);
		open.add(sstart);

		while (!open.isEmpty()) {
			int s = open.get();
			int spInOpen = -2;

			if (s == goal) {
				//System.out.println(" ---> ok");
				return reconstructPath(s);
			}

			closed[s] = true;

			for (int nbIdx = 0; nbIdx < 8; nbIdx++) {
				int sp = nograph.g_nb[s * 8 + nbIdx];
				if ((sp >= 0) && (nograph.g_a[sp] || (excluding >= 0 && sp == goal))) {
					if (!closed[sp]) {
						spInOpen = open.getIndex(sp);
						if (spInOpen == -1) {
							parent[sp] = -1;
							g[sp] = Integer.MAX_VALUE;
						}
					}

					int gOld = g[sp];

					computeCost(s, sp, nbIdx);

					if (g[sp] < gOld) {
						f[sp] = g[sp] + h(sp);
						if (spInOpen == -2) {
							spInOpen = open.getIndex(sp);
						}
						if (spInOpen == -1) {
							open.add(sp);
						} else {
							open.updatedCostAtIndex(spInOpen);
						}
					}
				}
			}
		}

		//System.out.println(" ---> nok");
		return null;
	}

	private int h(int s) {
		int dx = nograph.g_x[s] - nograph.g_x[ioe];
		int dy = nograph.g_y[s] - nograph.g_y[ioe];

		return (int) (FLOAT_TO_INT * Math.sqrt(dx * dx + dy * dy));
	}

	private int h(int s1, int s2) {
		int dx = nograph.g_x[s1] - nograph.g_x[s2];
		int dy = nograph.g_y[s1] - nograph.g_y[s2];

		return (int) (FLOAT_TO_INT * Math.sqrt(dx * dx + dy * dy));
	}

	private Path reconstructPath(int current) {
		Path path = null;

		if (parent[current] >= 0 && parent[current] != current) {
			path = reconstructPath(parent[current]);
			path.add(nograph.getPosition(current));
		} else {
			path = new Path();
		}

		return path;
	}
}
