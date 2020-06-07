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
package name.herve.bastod.engine.buildings;

import name.herve.bastod.engine.BASToDPlayer;
import name.herve.bastod.engine.Shot;
import name.herve.bastod.engine.units.Destructible;
import name.herve.bastod.engine.units.Firing;
import name.herve.game.tools.Constants;
import name.herve.game.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class TowerShot implements Shot {
	private int damageDealt;
	private BASToDPlayer player;
	private Vector positionOnBoard;
	private float speedOnBoard;
	private float speedOnGrid;
	private Destructible target;
	private Firing parent;

	public TowerShot(Firing parent, Vector positionOnBoard, Destructible target, float speedOnGrid, int damageDealt) {
		super();

		this.positionOnBoard = positionOnBoard;
		this.speedOnGrid = speedOnGrid;
		this.damageDealt = damageDealt;
		this.target = target;
		this.parent = parent;
	}

	@Override
	public float getAngle() {
		return 0;
	}

	@Override
	public BASToDPlayer getPlayer() {
		return player;
	}

	@Override
	public Vector getPositionOnBoard() {
		return positionOnBoard;
	}

	@Override
	public void init(int boardSquareSize) {
		speedOnBoard = speedOnGrid * boardSquareSize;
	}

	@Override
	public boolean isTargetReached() {
		return target == null;
	}

	@Override
	public void move(long delta) {
		if (!target.isAlive()) {
			target = null;
			return;
		}

		Vector direction = target.getPositionOnBoard().copy().remove(positionOnBoard);
		float distanceLeft = direction.length();
		float moveCapacity = (speedOnBoard * delta) / Constants.NANO;
		if (distanceLeft <= moveCapacity) {
			target.removeArmor(damageDealt);
			parent.addDamageDone(damageDealt);
			if (!target.isAlive()) {
				parent.addEnemyKilled();
			}
			target = null;
			return;
		}

		direction.multiply(moveCapacity / distanceLeft);
		positionOnBoard.add(direction);
	}

	@Override
	public void setPlayer(BASToDPlayer player) {
		this.player = player;
	}

	@Override
	public void setPositionOnBoard(Vector position) {
		positionOnBoard = position;
	}

	@Override
	public String toString() {
		return "TS-" + positionOnBoard;
	}

}
