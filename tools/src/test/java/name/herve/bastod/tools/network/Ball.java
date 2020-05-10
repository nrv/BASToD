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
package name.herve.bastod.tools.network;

import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class Ball {
	private String uuid;
	private Point2D position;
	private Point2D speed;
	private float[] color;

	public Ball() {
		super();
	}

	public Color getColor() {
		return new Color(color[0], color[1], color[2]);
	}

	public Point2D getPosition() {
		return position;
	}

	public Point2D getSpeed() {
		return speed;
	}

	public String getUuid() {
		return uuid;
	}

	public void setColor(float[] color) {
		this.color = color;
	}

	public void setPosition(Point2D position) {
		this.position = position;
	}

	public void setSpeed(Point2D speed) {
		this.speed = speed;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "Ball [id=" + uuid + ", position=" + position + ", speed=" + speed + "]";
	}
}
