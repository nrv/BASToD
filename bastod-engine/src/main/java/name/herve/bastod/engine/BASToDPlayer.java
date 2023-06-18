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
package name.herve.bastod.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import name.herve.bastod.engine.units.AbstractUnit;
import name.herve.game.tools.Constants;
import name.herve.game.tools.IDGenerator;
import name.herve.game.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public abstract class BASToDPlayer implements Comparable<BASToDPlayer> {
	public static final String PLAYER_BLUE = "blue";
	public static final String PLAYER_RED = "red";

	private IDGenerator idGenerator;
	private List<BASToDPlayerAction> actions;
	private BASToDPlayerActionsProvider actionsProvider;
	private String color;
	private int maxMetal;
	private int maxScore;
	private int metal;
	private float metalAcc;
	private int metalAdded;
	private float metalMultiplier;
	private int metalRemoved;
	private int score;
	private boolean spawnEnabled;
	private float speedMultiplier;

	private Vector startPositionOnBoard;

	private Statistics stats;
	private Set<Unit> units;
	
	private String uuid;
	private int boardIndex;

	public BASToDPlayer() {
		super();
		units = new HashSet<>();
		metalAcc = 0;
		metalMultiplier = 1f;
		speedMultiplier = 1f;
		stats = new Statistics();
		setSpawnEnabled(false);

		actions = Collections.synchronizedList(new ArrayList<BASToDPlayerAction>());
		idGenerator = new IDGenerator();
	}

	public void addMetal(int amount) {
		// System.out.println("addMetal("+amount+")");
		metal += amount;

		metalAdded += amount;

		if (metal > maxMetal) {
			metal = maxMetal;
		}
	}

	public void addScore(int amount) {
		score += amount;
	}

	public boolean addUnit(Unit e) {
		e.setPlayer(this);
		((AbstractUnit) e).setId(idGenerator.getId());
		return units.add(e);
	}

	public void clearActions() {
		actions.clear();
	}

	@Override
	public int compareTo(BASToDPlayer p) {
		return uuid.compareTo(p.uuid);
	}

	public void endStep(long now) {
		stats.newStep(now, metalAdded, metalRemoved);
		metalAdded = 0;
		metalRemoved = 0;
	}

	public void gatherActions(long now) {
		List<BASToDPlayerAction> acts = actionsProvider.getActions(now);
		if (acts != null) {
			actions.addAll(acts);
		}
	}

	public List<BASToDPlayerAction> getActions() {
		return actions;
	}

	// public int getBoardXOffset() {
	// return boardXOffset;
	// }

	public BASToDPlayerActionsProvider getActionsProvider() {
		return actionsProvider;
	}

	public String getColor() {
		return color;
	}

	public int getMaxMetal() {
		return maxMetal;
	}

	public int getMaxScore() {
		return maxScore;
	}

	public int getMetal() {
		return metal;
	}

	public float getMetalAddedMean(long now) {
		return stats.getMetalAddedMean(now);
	}

	public float getMetalMultiplier() {
		return metalMultiplier;
	}

	public float getMetalRemovedMean(long now) {
		return stats.getMetalRemovedMean(now);
	}

	public int getScore() {
		return score;
	}

	public float getSpeedMultiplier() {
		return speedMultiplier;
	}

	public Vector getStartPositionOnBoard() {
		return startPositionOnBoard;
	}

	public Statistics getStats() {
		return stats;
	}

	public Set<Unit> getUnits() {
		return units;
	}

	public boolean isSpawnEnabled() {
		return spawnEnabled;
	}

	public void removeMetal(int amount) {
		metalRemoved += amount;
		metal -= amount;
	}

	// public void setBoardXOffset(int boardXOffset) {
	// this.boardXOffset = boardXOffset;
	// }

	public void removeScore(int amount) {
		score -= amount;
	}

	public boolean removeUnit(Unit o) {
		o.setOnBoard(false);
		return units.remove(o);
	}

	public void setActionsProvider(BASToDPlayerActionsProvider actionsProvider) {
		this.actionsProvider = actionsProvider;
	}

	public void setColor(String name) {
		color = name;
	}

	public void setMaxMetal(int maxMetal) {
		this.maxMetal = maxMetal;
	}

	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
		setScore(maxScore);
	}

	public void setMetal(int metal) {
		this.metal = metal;
	}

	public void setMetalMultiplier(float metalMultiplier) {
		this.metalMultiplier = metalMultiplier;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setSpawnEnabled(boolean spawnEnabled) {
		this.spawnEnabled = spawnEnabled;
	}

	public void setSpeedMultiplier(float speedMultiplier) {
		this.speedMultiplier = speedMultiplier;
	}

	public void setStartPositionOnBoard(Vector startPositionOnBoard) {
		this.startPositionOnBoard = startPositionOnBoard;
	}

	public void startNewStep(long now) {
		metalAdded = 0;
		metalRemoved = 0;
	}

	void stepManageResources(long delta, int incRatePerSec) {
		// System.out.println("metalAcc " + metalAcc);
		metalAcc += delta * incRatePerSec * metalMultiplier;
		int amount = (int) (metalAcc / Constants.NANO);
		// System.out.println("metalAcc " + metalAcc);
		metalAcc -= amount * Constants.NANO;
		// System.out.println("metalAcc " + metalAcc);
		// System.out.println("---");

		addMetal(amount);
	}

	@Override
	public String toString() {
		return "p" + getBoardIndex() + " (" + getScore() + "/" + getMetal() + ")" + getStats();
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getBoardIndex() {
		return boardIndex;
	}

	public void setBoardIndex(int boardIndex) {
		this.boardIndex = boardIndex;
	}
}
