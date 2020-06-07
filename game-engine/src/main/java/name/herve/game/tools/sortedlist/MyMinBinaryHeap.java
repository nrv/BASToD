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
package name.herve.game.tools.sortedlist;

import java.util.Arrays;

//See http://www.policyalmanac.org/games/binaryHeaps.htm

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class MyMinBinaryHeap implements MySortedList {
	private int[] f;
	private int[] heap;
	private int[] index;
	private int heapSize;

	public MyMinBinaryHeap(int maxSize) {
		super();

		heap = new int[maxSize + 1];
		index = new int[maxSize + 1];
		heapSize = 0;
	}

	@Override
	public void add(Integer n) {
		heapSize++;
		heap[heapSize] = n;
		index[n] = heapSize;
		bubbleHeap(heapSize);
	}

	private void bubbleHeap(int m) {
		int temp;
		int m2;
		while (m != 1) {
			m2 = m / 2;
			if (f[heap[m]] <= f[heap[m2]]) {
				temp = heap[m2];
				heap[m2] = heap[m];
				index[heap[m]] = m2;
				heap[m] = temp;
				index[temp] = m;
				m = m2;
			} else {
				break;
			}
		}
	}

	private boolean check() {
		return check(1);
	}

	private boolean check(int u) {
		int f1 = (2 * u) + 1;
		int f2 = 2 * u;
		if (f1 <= heapSize) {
			if (f[heap[u]] > f[heap[f1]]) {
				display();
				System.err.println("Error : f[heap[" + u + "]] > f[heap[" + f1 + "]] : " + f[heap[u]] + " > " + f[heap[f1]]);
				return false;
			}
			if (!check(f1)) {
				return false;
			}
		}
		if (f2 <= heapSize) {
			if (f[heap[u]] > f[heap[f2]]) {
				display();
				System.err.println("Error : f[heap[" + u + "]] > f[heap[" + f2 + "]] : " + f[heap[u]] + " > " + f[heap[f2]]);
				return false;
			}
			if (!check(f2)) {
				return false;
			}
		}
		return true;
	}

	public void display() {
		if (heapSize > 0) {
			System.out.print("HEAP(" + heapSize + ") : ");
			for (int n = 1; (n <= heapSize) && (n <= 20); n++) {
				System.out.print("[" + heap[n] + "/" + f[heap[n]] + "]");
			}
			System.out.println();
		}
	}

	@Override
	public Integer get() {
		int result = heap[1];

		heap[1] = heap[heapSize];
		index[heap[heapSize]] = 1;
		heapSize--;

		int v = 1;
		int u;
		int u2, u21;
		int temp;
		for (;;) {
			u = v;
			u2 = 2 * u;
			u21 = u2 + 1;

			if (u21 <= heapSize) {
				if (f[heap[u]] >= f[heap[u2]]) {
					v = u2;
				}
				if (f[heap[v]] >= f[heap[u21]]) {
					v = u21;
				}
			} else if (u2 <= heapSize) {
				if (f[heap[u]] >= f[heap[u2]]) {
					v = u2;
				}
			}

			if (u != v) {
				temp = heap[u];
				heap[u] = heap[v];
				index[heap[v]] = u;
				heap[v] = temp;
				index[temp] = v;
			} else {
				break;
			}
		}

		return result;
	}

	@Override
	public int getIndex(Integer n) {
		return index[n];
	}

	@Override
	public boolean isEmpty() {
		return heapSize == 0;
	}

	@Override
	public void reset() {
		Arrays.fill(heap, 0);
		Arrays.fill(index, -1);
		heapSize = 0;
	}

	@Override
	public void setCost(int[] f) {
		this.f = f;
	}

	@Override
	public void updatedCostAtIndex(int idx) {
		bubbleHeap(idx);
	}

}
