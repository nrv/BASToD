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
package name.herve.bastod.tools.math;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class Dimension {
	private int h;
	private int w;

	public Dimension(int w, int h) {
		super();
		this.w = w;
		this.h = h;
	}

	public int getH() {
		return h;
	}

	public int getW() {
		return w;
	}
	
	public int size() {
		return w * h;
	}

	@Override
	public String toString() {
		return "Dim [" + w + ", " + h + "]";
	}
}
