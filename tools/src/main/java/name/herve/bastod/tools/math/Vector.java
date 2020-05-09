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
package name.herve.bastod.tools.math;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class Vector {
	private final static DecimalFormat df = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

	private float x;
	private float y;

	public Vector() {
		this(1, 0);
	}

	public Vector(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}

	public Vector(Vector v) {
		this(v.x, v.y);
	}

	public Vector add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public Vector add(Vector v) {
		x += v.x;
		y += v.y;
		return this;
	}

	public float angleDeg() {
		return (float) (radToDeg(Math.atan2(y, x)));
	}

	public float angleDeg(Vector o) {
		return (float) radToDeg(angleRad(o));
	}

	public float angleRad() {
		return (float) (Math.atan2(y, x));
	}

	public float angleRad(Vector o) {
		return (float) Math.acos(dot(o)) / (length() * o.length());
	}

	public Vector copy() {
		return new Vector(this);
	}

	public double degToRad(double a) {
		a = (Math.PI * a) / 180d;
		return a;
	}

	public float distance(Vector oth) {
		float dx = oth.x - x;
		float dy = oth.y - y;
		return (float) Math.sqrt((dx * dx) + (dy * dy));
	}

	public float dot(Vector o) {
		return (x * o.x) + (y * o.y);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vector) {
			Vector oth = (Vector) obj;
			return (oth.x == x) && (oth.y == y);
		}
		return false;
	}

	public float getX() {
		return x;
	}

	public int getXInt() {
		return (int) x;
	}

	public float getY() {
		return y;
	}

	public int getYInt() {
		return (int) y;
	}

	public boolean isAligned(Vector oth) {
		return (oth.x == x) || (oth.y == y);
	}

	public float length() {
		return (float) Math.sqrt((x * x) + (y * y));
	}

	public float manhattan(Vector oth) {
		float dx = Math.abs(oth.x - x);
		float dy = Math.abs(oth.y - y);
		return dx + dy;
	}

	public Vector multiply(float f) {
		x *= f;
		y *= f;
		return this;
	}

	public Vector normalize() {
		float l = length();
		if (l > 0) {
			multiply(1f / l);
		}
		return this;
	}

	public double radToDeg(double a) {
		a = (a * 180d) / Math.PI;
		if (a < 0) {
			a += 360;
		}
		return a;
	}

	public Vector remove(float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	public Vector remove(Vector v) {
		x -= v.x;
		y -= v.y;
		return this;
	}

	public Vector rotateDeg(double alpha) {
		return rotateRad(degToRad(alpha));
	}

	public Vector rotateRad(double alpha) {
		double s = Math.sin(alpha);
		double c = Math.cos(alpha);

		float xp = (float) ((x * c) - (y * s));
		y = (float) ((x * s) + (y * c));
		x = xp;

		return this;
	}

	public float squaredDistance(Vector oth) {
		float dx = oth.x - x;
		float dy = oth.y - y;
		return (dx * dx) + (dy * dy);
	}

	@Override
	public String toString() {
		return "(" + df.format(x) + ", " + df.format(y) + ")";
	}
}
