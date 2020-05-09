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

import name.herve.bastod.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class NoGraph {
	boolean g_a[];
	float g_c[];

	int g_nb[];
	int g_nbnb[];
	int g_x[];
	int g_y[];
	int w;
	int h;
	Boolean los[];
	int sz;

	public NoGraph(int w, int h, boolean allowDiagonal) {
		super();

		this.h = h;
		this.w = w;

		sz = w * h;
		g_x = new int[sz];
		g_y = new int[sz];
		g_a = new boolean[sz];
		g_nbnb = new int[sz];
		g_nb = new int[sz * 8];
		g_c = new float[sz * 8];
		los = new Boolean[(sz * (sz + 1)) / 2];

		Arrays.fill(g_a, true);
		Arrays.fill(g_nbnb, 0);
		Arrays.fill(g_nb, -1);
		Arrays.fill(g_c, Integer.MAX_VALUE);

		resetLoS();

		int id;
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				id = getNode(i, j);
				g_x[id] = i;
				g_y[id] = j;
			}
		}

		for (int i = 0; i < (w - 1); i++) {
			for (int j = 0; j < h; j++) {
				addBidirectionalEdge(getNode(i, j), getNode(i + 1, j), 1f);
			}
		}

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < (h - 1); j++) {
				addBidirectionalEdge(getNode(i, j), getNode(i, j + 1), 1f);
			}
		}

		if (allowDiagonal) {
			float sqrt2 = (float) Math.sqrt(2);
			for (int i = 0; i < (w - 1); i++) {
				for (int j = 0; j < (h - 1); j++) {
					addEdge(getNode(i, j), getNode(i + 1, j + 1), sqrt2);
				}
			}
			for (int i = 1; i < w; i++) {
				for (int j = 0; j < (h - 1); j++) {
					addEdge(getNode(i, j), getNode(i - 1, j + 1), sqrt2);
				}
			}
			for (int i = 1; i < w; i++) {
				for (int j = 1; j < h; j++) {
					addEdge(getNode(i, j), getNode(i - 1, j - 1), sqrt2);
				}
			}
			for (int i = 0; i < (w - 1); i++) {
				for (int j = 1; j < h; j++) {
					addEdge(getNode(i, j), getNode(i + 1, j - 1), sqrt2);
				}
			}
		}
	}

	private void addBidirectionalEdge(int node1, int node2, float c) {
		addEdge(node1, node2, c);
		addEdge(node2, node1, c);
	}

	private void addEdge(int node1, int node2, float c) {
		int idx = (node1 * 8) + g_nbnb[node1];
		g_nb[idx] = node2;
		g_c[idx] = c;
		g_nbnb[node1]++;
	}

	public void close(int x, int y) {
		g_a[getNode(x, y)] = false;
	}

	public int getNode(int x, int y) {
		if ((x >= 0) && (y >= 0) && (x < w) && (y < h)) {
			return (x * h) + y;
		}

		return -1;
	}

	public int getNode(Vector v) {
		return getNode(v.getXInt(), v.getYInt());
	}

	public Vector getPosition(int node) {
		return new Vector(g_x[node], g_y[node]);
	}

	public boolean isAvailable(int n) {
		if ((n >= 0) && (n < sz)) {
			return g_a[n];
		}
		return false;
	}

	public boolean isAvailable(int x, int y) {
		return isAvailable(getNode(x, y));
	}

	// see http://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
	public boolean lineOfSight(int s, int sp) {
		if (s > sp) {
			int t = sp;
			sp = s;
			s = t;
		}

		int idx = (sp + (s * sz)) - ((s * (1 + s)) / 2);

		if (los[idx] != null) {
			return los[idx];
		}

		if (!isAvailable(s) || !isAvailable(sp)) {
			los[idx] = false;
			return false;
		}

		int x0 = g_x[s];
		int y0 = g_y[s];
		int x1 = g_x[sp];
		int y1 = g_y[sp];

		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);

		int sy = 0;
		int sx = 0;

		if (x0 < x1) {
			sx = 1;
		} else {
			sx = -1;
		}

		if (y0 < y1) {
			sy = 1;
		} else {
			sy = -1;
		}
		int err = dx - dy;

		while (true) {
			if (!isAvailable(x0, y0)) {
				los[idx] = false;
				return false;
			}
			if ((x0 == x1) && (y0 == y1)) {
				break;
			}
			int e2 = 2 * err;
			if (e2 > -dy) {
				err = err - dy;
				x0 = x0 + sx;
			}
			if (e2 < dx) {
				err = err + dx;
				y0 = y0 + sy;
			}
		}

		los[idx] = true;
		return true;
	}

	public boolean lineOfSight(Vector s, Vector sp) {
		return lineOfSight(getNode(s), getNode(sp));
	}

	public void open(int x, int y) {
		g_a[getNode(x, y)] = true;
	}

	public void resetLoS() {
		Arrays.fill(los, null);
	}

	public int size() {
		return sz;
	}
}
