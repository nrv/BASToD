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
package name.herve.game.funny;

import java.awt.Color;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class Ball {
	public static Ball fromParams(String[] p) {
		Ball b = new Ball();
		int s = 8;
		b.uuid = p[--s];
		b.x = Integer.parseInt(p[--s]);
		b.y = Integer.parseInt(p[--s]);
		b.vx = Integer.parseInt(p[--s]);
		b.vy = Integer.parseInt(p[--s]);
		b.color = new int[3];
		b.color[0] = Integer.parseInt(p[--s]);
		b.color[1] = Integer.parseInt(p[--s]);
		b.color[2] = Integer.parseInt(p[--s]);
		return b;
	}

	private String uuid;
	private int x;
	private int y;
	private int vx;
	private int vy;
	private int[] color;

	public Ball() {
		super();
	}

	public String[] asParams() {
		int s = 8;
		String[] p = new String[s];
		p[--s] = uuid;
		p[--s] = Integer.toString(x);
		p[--s] = Integer.toString(y);
		p[--s] = Integer.toString(vx);
		p[--s] = Integer.toString(vy);
		p[--s] = Integer.toString(color[0]);
		p[--s] = Integer.toString(color[1]);
		p[--s] = Integer.toString(color[2]);
		return p;
	}

	public Color getColor() {
		return new Color(color[0], color[1], color[2]);
	}

	public String getUuid() {
		return uuid;
	}

	public int getVx() {
		return vx;
	}

	public int getVy() {
		return vy;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setColor(int[] color) {
		this.color = color;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setVx(int vx) {
		this.vx = vx;
	}

	public void setVy(int vy) {
		this.vy = vy;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Ball [uuid=" + uuid + ", x=" + x + ", y=" + y + ", vx=" + vx + ", vy=" + vy + "]";
	}

}
