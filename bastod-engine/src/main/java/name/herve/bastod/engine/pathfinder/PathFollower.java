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
package name.herve.bastod.engine.pathfinder;

import java.util.Iterator;

import name.herve.bastod.engine.units.Mobile;
import name.herve.game.tools.Constants;
import name.herve.game.tools.graph.Path;
import name.herve.game.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class PathFollower {
	private float angle;
	private Iterator<Vector> it;
	private Vector nextPosition;
	private Mobile unit;

	public PathFollower(Mobile unit, Path path) {
		super();
		this.unit = unit;
		it = path.iterator();
		if (it.hasNext()) {
			nextPosition = it.next();
		} else {
			nextPosition = null;
		}
		angle = 0;
	}

	public float getAngle() {
		return angle;
	}

	public void move(long delta) {
		if (unit.getSpeedOnBoard() < unit.getMaxSpeedOnBoard()) {
			unit.setSpeedOnBoard(Math.min(unit.getSpeedOnBoard() + ((unit.getAccelerationOnBoard() * delta) / Constants.NANO), unit.getMaxSpeedOnBoard()));
		}

		float moveCapacity = (unit.getSpeedOnBoard() * delta) / Constants.NANO;

		if (moveCapacity > 0) {
			while ((moveCapacity > 0) && (nextPosition != null)) {
				Vector direction = nextPosition.copy().remove(unit.getPositionOnBoard());
				float distanceLeft = direction.length();
				angle = direction.angleDeg();

				if (distanceLeft > 0) {
					float move = Math.min(moveCapacity, distanceLeft);
					direction.multiply(move / distanceLeft);
					unit.getPositionOnBoard().add(direction);
					moveCapacity -= move;
				} else {
					if (it.hasNext()) {
						nextPosition = it.next();
					} else {
						nextPosition = null;
					}
				}
			}

			if (nextPosition == null) {
				unit.setTargetOnBoard(null);
				unit.setPath(null);
			}
		}
	}
}
