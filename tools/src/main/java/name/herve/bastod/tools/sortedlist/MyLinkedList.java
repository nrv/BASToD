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
package name.herve.bastod.tools.sortedlist;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class MyLinkedList implements MySortedList {
	private int[] cost;
	private LinkedList<Integer> list;

	public MyLinkedList() {
		super();
		list = new LinkedList<Integer>();
	}

	@Override
	public void add(Integer v) {
		int pos = 0;
		float cv = cost[v];
		Iterator<Integer> it = list.listIterator();
		while (it.hasNext() && (cost[it.next()] < cv)) {
			pos++;
		}
		list.add(pos, v);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Integer get() {
		return list.poll();
	}
	
	@Override
	public void setCost(int[] cost) {
		this.cost = cost;
	}

	@Override
	public void updatedCostAtIndex(int idx) {
		Integer v = list.remove(idx);
		add(v);
	}

	@Override
	public int getIndex(Integer e) {
		return list.indexOf(e);
	}

	@Override
	public void reset() {
		list.clear();
	}
}
