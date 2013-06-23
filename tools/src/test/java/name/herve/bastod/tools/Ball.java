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
package name.herve.bastod.tools;

import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class Ball {
	private int id;
	private Point2D position;
	private Point2D speed;
	private Color color;

	public Ball() {
		super();
	}

	public Color getColor() {
		return color;
	}

	public int getId() {
		return id;
	}

	public Point2D getPosition() {
		return position;
	}

	public Point2D getSpeed() {
		return speed;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPosition(Point2D position) {
		this.position = position;
	}

	public void setSpeed(Point2D speed) {
		this.speed = speed;
	}
}
