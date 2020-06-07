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
package name.herve.bastod.engine.units;

import name.herve.bastod.engine.pathfinder.PathFollower;
import name.herve.game.tools.graph.Path;
import name.herve.game.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class Tank extends AbstractUnit implements Mobile, Destructible {
	private float accelerationOnBoard;
	private float accelerationOnGrid;
	private int armor;
	private int maxArmor;
	private float maxSpeedOnBoard;
	private float maxSpeedOnGrid;
	private Path path;
	private PathFollower pf;
	private int scoreValue;
	private float speedOnBoard;
	private boolean targetable;
	private Vector targetOnBoard;
	private boolean targetReached;
	private boolean targetSet;
	private Path unsmoothedPath;

	public Tank(int maxArmor, float maxSpeedOnGrid, float accelerationOnGrid, int scoreValue) {
		super();

		this.maxArmor = maxArmor;
		armor = maxArmor;
		this.accelerationOnGrid = accelerationOnGrid;
		this.maxSpeedOnGrid = maxSpeedOnGrid;
		this.scoreValue = scoreValue;

		path = null;
		targetOnBoard = null;
		targetReached = false;
		targetSet = false;
		targetable = true;
		pf = null;
	}

	@Override
	public float getAccelerationOnBoard() {
		return accelerationOnBoard;
	}

	@Override
	public float getAngle() {
		if (pf != null) {
			return pf.getAngle();
		}
		return 0;
	}

	@Override
	public int getArmor() {
		return armor;
	}

	@Override
	public int getMaxArmor() {
		return maxArmor;
	}

	@Override
	public float getMaxSpeedOnBoard() {
		return maxSpeedOnBoard;
	}

	@Override
	public Path getPath() {
		return path;
	}

	@Override
	public int getScoreValue() {
		return scoreValue;
	}

	@Override
	public float getSpeedOnBoard() {
		return speedOnBoard;
	}

	@Override
	public Vector getTargetOnBoard() {
		return targetOnBoard;
	}

	@Override
	public Path getUnsmoothedPath() {
		return unsmoothedPath;
	}

	@Override
	public void init(int boardSquareSize) {
		speedOnBoard = 0;
		maxSpeedOnBoard = maxSpeedOnGrid * boardSquareSize;
		accelerationOnBoard = accelerationOnGrid * boardSquareSize;
	}

	@Override
	public boolean isAlive() {
		return getArmor() > 0;
	}

	@Override
	public boolean isTargetable() {
		return targetable;
	}

	@Override
	public boolean isTargetReached() {
		return targetSet && targetReached;
	}

	@Override
	public void move(long delta) {
		if (pf != null) {
			pf.move(delta);
		}
	}

	@Override
	public void removeArmor(int amount) {
		armor -= amount;
	}

	@Override
	public void setPath(Path path) {
		this.path = path;
		if ((path != null)) {
			pf = new PathFollower(this, path);
		} else {
			pf = null;
		}
	}

	@Override
	public void setSpeedOnBoard(float s) {
		speedOnBoard = s;
	}

	@Override
	public void setTargetable(boolean t) {
		targetable = t;
	}

	@Override
	public void setTargetOnBoard(Vector targetOnBoard) {
		if (targetOnBoard != null) {
			targetSet = true;
		} else {
			targetReached = true;
		}
		this.targetOnBoard = targetOnBoard;
	}

	@Override
	public void setUnsmoothedPath(Path unsmoothedPath) {
		this.unsmoothedPath = unsmoothedPath;
	}

	@Override
	public String toString() {
		return "TK-" + getPositionOnBoard() + " / " + getArmor();
	}

}
