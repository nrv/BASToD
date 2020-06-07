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

import java.util.List;

import name.herve.bastod.engine.BASToDPlayer;
import name.herve.bastod.engine.Unit;
import name.herve.game.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public abstract class AbstractUnit implements Unit {
	private int id;
	private BASToDPlayer player;
	private Vector positionOnBoard;
	private boolean onBoard;

	public AbstractUnit() {
		super();

		setOnBoard(true);
	}

	@Override
	public float getAngle() {
		return 0;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public List<String> getInfos() {
		return null;
	}

	@Override
	public String getName() {
		return getClass().getName() + "-" + getPlayer().getColor() + "-" + getId();
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
	}

	@Override
	public boolean isOnBoard() {
		return onBoard;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void setOnBoard(boolean onBoard) {
		this.onBoard = onBoard;
	}

	@Override
	public void setPlayer(BASToDPlayer player) {
		this.player = player;
	}

	@Override
	public void setPositionOnBoard(Vector positionOnBoard) {
		this.positionOnBoard = positionOnBoard;
	}
}
