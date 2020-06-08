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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import name.herve.bastod.engine.BASToDEngineEvent.Type;
import name.herve.bastod.engine.units.Blocking;
import name.herve.bastod.engine.units.Destructible;
import name.herve.bastod.engine.units.Firing;
import name.herve.bastod.engine.units.Mobile;
import name.herve.bastod.engine.units.Spawning;
import name.herve.game.engine.GameEngine;
import name.herve.game.engine.GamePlayerAction;
import name.herve.game.tools.Constants;
import name.herve.game.tools.graph.Path;
import name.herve.game.tools.math.Dimension;
import name.herve.game.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class BASToDEngine extends GameEngine<BASToDGame> {
	public final static String _IMPROVE = "improve.";

	public final static int _VIEWPORT_WIDTH = 800;
	public final static int _VIEWPORT_HEIGHT = 480;
	public final static int _SQUARE_SIZE = 32;
	public final static int _SP_BOTTOM = 98;
	public final static int _SP_SIDE = 0;
	public final static int _SP_TOP = 30;
	public final static int BOARD_WIDTH = _VIEWPORT_WIDTH - (2 * _SP_SIDE);
	public final static int BOARD_HEIGHT = _VIEWPORT_HEIGHT - _SP_BOTTOM - _SP_TOP;
	public final static int GRID_WIDTH = BOARD_WIDTH / _SQUARE_SIZE;
	public final static int GRID_HEIGHT = BOARD_HEIGHT / _SQUARE_SIZE;

	public final static String CF_GAME_INITIAL_FACTORY_B = "game.initial.factory";
	public final static String CF_GAME_INITIAL_SCORE_I = "game.initial.score";
	public final static String CF_GAME_INITIAL_TOWER_B = "game.initial.tower";
	public final static String CF_GAME_METAL_INCREASE_RATE_PER_SEC_I = "game.metal.increase_rate";
	public final static String CF_GAME_METAL_INITIAL_I = "game.metal.initial";
	public final static String CF_GAME_METAL_MAX_I = "game.metal.max";
	public final static String CF_IMP_INCREASE_SPEED_COST_I = "imp.increase_speed.metal_cost";
	public final static String CF_IMP_INCREASE_SPEED_FACTOR_F = "imp.increase_speed.factor";
	public final static String CF_IMP_MORE_METAL_COST_I = "imp.more_metal.metal_cost";
	public final static String CF_IMP_MORE_METAL_FACTOR_F = "imp.more_metal.factor";
	public final static String CF_TANK_BUILD_TIME_MILLI_I = "tank.build_time";
	public final static String CF_TANK_MAX_ARMOR_I = "tank.max_armor";
	public final static String CF_TANK_METAL_COST_I = "tank.metal_cost";
	public final static String CF_TANK_SCORE_VALUE_I = "tank.score_value";
	public final static String CF_TANK_SPEED_F = "tank.speed";
	public final static String CF_TANK_ACCELERATION_F = "tank.acceleration";
	public final static String CF_TOWER_METAL_COST_I = "tower.metal_cost";
	public final static String CF_TOWER_RELOAD_TIME_MILLI_I = "tower.reload_time";
	public final static String CF_TOWER_SHOT_DAMAGE_I = "tower.shot.damage";
	public final static String CF_TOWER_SHOT_RANGE_F = "tower.shot.range";
	public final static String CF_TOWER_SHOT_SPEED_F = "tower.shot.speed";
	public final static String CF_WALL_METAL_COST_I = "wall.metal_cost";

	public final static String IMP_BUY_TOWER = _IMPROVE + "tower";
	public final static String IMP_BUY_WALL = _IMPROVE + "wall";
	public final static String IMP_BUY_FACTORY = _IMPROVE + "factory";
	public final static String IMP_INCREASE_SPEED = _IMPROVE + "increase_speed";
	public final static String IMP_MORE_METAL = _IMPROVE + "more_metal";

	public final static boolean PRECOMPUTE_OPEN_BUILD_POSITIONS = false;

	private int cacheH;
	private Boolean[] cachePathAvailableOnGrid;
	private List<BASToDEngineListener> listeners;
	private List<Vector> openedBuildPositions;
	private Random rd;
	private float speed;
	private boolean paused;
	private boolean started;

	public BASToDEngine(long seed, BASToDGame game, boolean master) {
		super(master, game);
		rd = new Random(seed);
		speed = 1;
		started = false;
		listeners = new ArrayList<>();
	}

	public boolean addBoardUnit(Unit e) {
		return getState().getBoard().addUnit(e);
	}

	public boolean addListener(BASToDEngineListener l) {
		return listeners.add(l);
	}

	private void clearPathFinderCache() {
		getState().getBoard().clearPathFinderCache();

		for (BASToDPlayer p : getState().getPlayers()) {
			for (Unit u : p.getUnits()) {
				if (u instanceof Mobile) {
					Mobile m = (Mobile) u;
					m.setPath(null);
				}
			}
		}

		if (!PRECOMPUTE_OPEN_BUILD_POSITIONS) {
			if (cachePathAvailableOnGrid == null) {
				cachePathAvailableOnGrid = new Boolean[getGridDimension().size()];
				cacheH = getGridDimension().getH();
			}

			Arrays.fill(cachePathAvailableOnGrid, null);
		}

	}

	public void closeOnBoard(Vector p, boolean clearCacheAndWarn) {
		getState().getBoard().closeOnBoard(p);
		if (clearCacheAndWarn) {
			clearPathFinderCache();
			if (PRECOMPUTE_OPEN_BUILD_POSITIONS) {
				computeOpenedBuildPositions();
			}
			warnListeners(Type.BOARD_MODIFIED);
		}
	}

	private void computeOpenedBuildPositions() {
		openedBuildPositions = new ArrayList<>();

		BASToDPlayer aPlayer = getState().getPlayers().iterator().next();
		Vector p1 = getState().getBoard().fromBoardToGrid(aPlayer.getStartPositionOnBoard());
		Vector p2 = getState().getBoard().fromBoardToGrid(aPlayer.getEnemyPositionOnBoard());

		for (BASToDPlayer player : getState().getPlayers()) {
			List<Vector> bp = getState().getBoard().getBuildPositions(player);
			if (bp != null) {
				for (Vector pos : bp) {
					if (getState().getBoard().isOpened(pos) && getState().getBoard().isPathAvailableOnGrid(p1, p2, pos)) {
						openedBuildPositions.add(pos);
					}
				}
			}
		}
	}

	@Override
	public GamePlayerAction executeSpecificPlayerAction(GamePlayerAction pa) {
		// TODO Auto-generated method stub
		return null;
	}

	public Vector fromBoardToGrid(Vector p) {
		return getState().getBoard().fromBoardToGrid(p);
	}

	public Path fromGridToBoard(Path p) {
		return getState().getBoard().fromGridToBoard(p);
	}

	public Vector fromGridToBoard(Vector p) {
		return getState().getBoard().fromGridToBoard(p);
	}

	public Improvement getAvailableImprovement(String name) {
		return getState().getAvailableImprovement(name);
	}

	public Collection<Improvement> getAvailableImprovements() {
		return getState().getAvailableImprovements().values();
	}

	public Dimension getBoardDimension() {
		return getState().getBoardDimension();
	}

	public Collection<Unit> getBoardUnits() {
		return getState().getBoard().getBoardUnits();
	}

	public List<Vector> getBuildPositions(BASToDPlayer p) {
		return getState().getBoard().getBuildPositions(p);
	}

	public float getElapsedTimeSec() {
		return (float) getState().getNow() / (float) Constants.NANO;
	}

	public Dimension getGridDimension() {
		return getState().getBoard().getGridDimension();
	}

	public int getGridSquareSize() {
		return getState().getGridSquareSize();
	}

	public long getNow() {
		return getState().getNow();
	}

	// public BASToDPlayer getPlayer(int index) {
	// return getState().getPlayer(index);
	// }
	//
	// public BASToDPlayer getPlayer(String name) {
	// return getState().getPlayer(name);
	// }

	public long getNowMilli() {
		return getState().getNow() / Constants.NANO_MILLI;
	}

	public Collection<BASToDPlayer> getPlayers() {
		return getState().getPlayers();
	}

	public Collection<Shot> getShots() {
		return getState().getShots();
	}

	public float getSpeed() {
		return speed;
	}

	public boolean isGameOver() {
		return getState().isOver();
	}

	public boolean isImprovementAffordableForPlayer(String imp, BASToDPlayer p) {
		return getAvailableImprovement(imp).isAffordableForPlayer(p);
	}

	public boolean isOpenedBuildPosition(BASToDPlayer p, Vector v) {
		return isOpenedBuildPosition(v) && getState().getBoard().getBuildPositions(p).contains(v);
	}

	public boolean isOpenedBuildPosition(Vector v) {
		if (PRECOMPUTE_OPEN_BUILD_POSITIONS) {
			for (Vector o : openedBuildPositions) {
				if (o.equals(v)) {
					return true;
				}
			}
			return false;
		} else {
			if (!isOpenedOnGrid(v)) {
				return false;
			}

			int idx = (v.getXInt() * cacheH) + v.getYInt();
			if (cachePathAvailableOnGrid[idx] == null) {
				BASToDPlayer aPlayer = getState().getPlayers().iterator().next();
				Vector p1 = getState().getBoard().fromBoardToGrid(aPlayer.getStartPositionOnBoard());
				Vector p2 = getState().getBoard().fromBoardToGrid(aPlayer.getEnemyPositionOnBoard());

				cachePathAvailableOnGrid[idx] = getState().getBoard().isPathAvailableOnGrid(p1, p2, v);

				// System.out.println("cachePathAvailableOnGrid["+v+"] = " +
				// cachePathAvailableOnGrid[idx]);
			}

			return cachePathAvailableOnGrid[idx];
		}
	}

	public boolean isOpenedOnGrid(Vector p) {
		return getState().getBoard().isOpened(p);
	}

	public boolean isPaused() {
		return paused;
	}

	public boolean isStarted() {
		return started;
	}

	public boolean isTowerDefenseGame() {
		return getState().getType() == BASToDGame.Type.TOWER_DEFENSE;
	}

	public boolean lineOfSight(Vector s, Vector sp) {
		return getState().lineOfSight(s, sp);
	}

	public boolean removeListener(BASToDEngineListener l) {
		return listeners.remove(l);
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	private void setSpawn(BASToDPlayer p, boolean v) {
		p.setSpawnEnabled(v);
		warnListeners(Type.SPAW_MODIFIED, p);
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void start() {
		getState().setNow(0);
		paused = false;

		for (Unit u : getState().getBoard().getBoardUnits()) {
			if (u instanceof Blocking) {
				getState().getBoard().closeOnBoard(u.getPositionOnBoard());
			}
		}

		for (BASToDPlayer p : getState().getPlayers()) {
			for (Unit u : p.getUnits()) {
				if (u instanceof Blocking) {
					getState().getBoard().closeOnBoard(u.getPositionOnBoard());
				}
			}
		}

		clearPathFinderCache();

		if (PRECOMPUTE_OPEN_BUILD_POSITIONS) {
			computeOpenedBuildPositions();
		}

		started = true;
	}

	@Override
	public void step(long deltaNano) {
		if (!isPaused()) {
			deltaNano *= speed;
			getState().addNow(deltaNano);

			for (BASToDPlayer p : getPlayers()) {
				p.startNewStep(getNow());
				p.gatherActions(getState().getNow());
			}

			stepDoPlayerActions();
			stepManageResources(deltaNano);
			stepSpawnUnits();
			stepMoveUnits(deltaNano);
			stepFireUnits();
			stepMoveShots(deltaNano);
			stepCheck();

			for (BASToDPlayer p : getPlayers()) {
				p.endStep(getNow());
			}
		}
	}

	private void stepCheck() {
		for (BASToDPlayer p : getPlayers()) {
			if (p.getScore() <= 0) {
				getState().setOver(true);
			}
			List<Destructible> destroyedUnits = new ArrayList<>();
			for (Unit u : p.getUnits()) {
				if (u instanceof Destructible) {
					Destructible d = (Destructible) u;
					if (!d.isAlive()) {
						destroyedUnits.add(d);
					}
				}
			}
			for (Destructible d : destroyedUnits) {
				p.removeUnit(d);
				p.getStats().incNbUnitsLost();
			}
		}
	}

	private void stepDoPlayerActions() {
		for (BASToDPlayer p : getPlayers()) {
			for (BASToDPlayerAction action : p.getActions()) {
				switch (action.getAction()) {
				case START_SPAWN:
					setSpawn(action.getPlayer(), true);
					break;
				case STOP_SPAWN:
					setSpawn(action.getPlayer(), false);
					break;
				case BUY_TOWER:
					if (isImprovementAffordableForPlayer(IMP_BUY_TOWER, action.getPlayer()) && isOpenedBuildPosition(action.getPlayer(), action.getPositionOnGrid())) {
						getAvailableImprovement(IMP_BUY_TOWER).buy(this, action.getPlayer(), action.getPositionOnGrid());
					}
					break;
				case BUY_WALL:
					if (isImprovementAffordableForPlayer(IMP_BUY_WALL, action.getPlayer()) && isOpenedBuildPosition(action.getPlayer(), action.getPositionOnGrid())) {
						getAvailableImprovement(IMP_BUY_WALL).buy(this, action.getPlayer(), action.getPositionOnGrid());
					}
					break;
				case BUY_FACTORY:
					if (isImprovementAffordableForPlayer(IMP_BUY_FACTORY, action.getPlayer()) && isOpenedBuildPosition(action.getPlayer(), action.getPositionOnGrid())) {
						getAvailableImprovement(IMP_BUY_FACTORY).buy(this, action.getPlayer(), action.getPositionOnGrid());
					}
					break;
				case IMPROVE:
					if (isImprovementAffordableForPlayer(action.getParam(), action.getPlayer())) {
						getAvailableImprovement(action.getParam()).buy(this, action.getPlayer());
					}
					break;
				}
			}

			p.clearActions();
		}
	}

	private void stepFireUnits() {
		for (BASToDPlayer p : getState().getPlayers()) {
			for (Unit u : p.getUnits()) {
				if (u instanceof Firing) {
					Firing f = (Firing) u;
					f.updateWeapons();
					if (f.isAbleToFire(getState().getNow())) {
						f.acquireTarget(p.getEnemy());
						if (f.hasTarget()) {
							Shot shot = f.fire(getState().getNow());
							shot.init(getState().getGridSquareSize());
							shot.setPlayer(p);
							getState().getShots().add(shot);
						}
					}
				}
			}
		}
	}

	private void stepManageResources(long delta) {
		for (BASToDPlayer p : getState().getPlayers()) {
			p.stepManageResources(delta, getState().getMetalIncreaseRatePerSec());
		}
	}

	private void stepMoveShots(long delta) {
		List<Shot> shotsWithTargetReached = new ArrayList<>();
		for (Shot s : getState().getShots()) {
			if (s.isTargetReached()) {
				shotsWithTargetReached.add(s);
			} else {
				s.move(delta);
			}
		}
		for (Shot s : shotsWithTargetReached) {
			getState().getShots().remove(s);
		}
	}

	private void stepMoveUnits(long delta) {
		for (BASToDPlayer p : getState().getPlayers()) {
			List<Mobile> unitWithTargetReached = new ArrayList<>();
			for (Unit u : p.getUnits()) {
				if (u instanceof Mobile) {
					Mobile m = (Mobile) u;
					if (m.getTargetOnBoard() != null) {
						if (m.getPath() == null) {
							Path path = getState().getBoard().shortestPathOnBoard(m.getPositionOnBoard(), m.getTargetOnBoard());
							if (path != null) {
								Path smoothed = getState().getBoard().smoothPath(m.getPositionOnBoard(), path, getGridSquareSize(), 3);

								m.setPath(smoothed);
								m.setUnsmoothedPath(path);
							} else {
								System.err.println("WARNING : Unable to find a path for " + m + " ! ");
							}
						}
						m.move(delta);
						if (m.isTargetReached()) {
							unitWithTargetReached.add(m);
						}
					}
				}
			}

			for (Mobile m : unitWithTargetReached) {
				if (m instanceof Destructible) {
					((Destructible) m).setTargetable(false);
				}
				p.removeUnit(m);
				p.addScore(m.getScoreValue());
				p.getEnemy().removeScore(m.getScoreValue());
				p.getStats().incNbUnitsCrossed();
			}
		}
	}

	private void stepSpawnUnits() {
		for (BASToDPlayer p : getState().getPlayers()) {
			if (p.isSpawnEnabled()) {
				Set<Unit> newUnits = new HashSet<>();
				for (Unit u : p.getUnits()) {
					if (u instanceof Spawning) {
						Spawning spu = (Spawning) u;
						if (spu.isSpawnEnabled() && spu.isAbleToSpawn(getState().getNow())) {
							Mobile s = spu.spawn(getState().getNow());
							s.init(getState().getGridSquareSize());
							if (s != null) {

								List<Vector> potentialTargets = getState().getBoard().getEndPositions(p);
								Vector target = potentialTargets.get(rd.nextInt(potentialTargets.size()));
								s.setTargetOnBoard(getState().getBoard().fromGridToBoard(target));

								newUnits.add(s);
								p.getStats().incNbUnitsSpawn();
							}
						}
					}
				}
				for (Unit u : newUnits) {
					p.addUnit(u);
				}
			}
		}
	}

	private void warnListeners(BASToDEngineEvent.Type type) {
		warnListeners(type, null);
	}

	private void warnListeners(BASToDEngineEvent.Type type, BASToDPlayer p) {
		BASToDEngineEvent event = new BASToDEngineEvent(type);
		event.setPlayer(p);
		for (BASToDEngineListener l : listeners) {
			l.engineEvent(event);
		}
	}
}