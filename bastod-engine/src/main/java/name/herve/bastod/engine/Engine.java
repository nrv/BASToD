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

import name.herve.bastod.engine.EngineEvent.Type;
import name.herve.bastod.engine.units.Blocking;
import name.herve.bastod.engine.units.Destructible;
import name.herve.bastod.engine.units.Firing;
import name.herve.bastod.engine.units.Mobile;
import name.herve.bastod.engine.units.Spawning;
import name.herve.game.tools.Constants;
import name.herve.game.tools.graph.Path;
import name.herve.game.tools.math.Dimension;
import name.herve.game.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class Engine {
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
	private Game game;
	private List<EngineListener> listeners;

	private List<Vector> openedBuildPositions;
	private Random rd;
	private float speed;
	private boolean paused;

	private boolean started;

	public Engine(long seed) {
		super();
		rd = new Random(seed);
		speed = 1;
		started = false;
		listeners = new ArrayList<>();
	}

	public boolean addBoardUnit(Unit e) {
		return game.getBoard().addUnit(e);
	}

	public boolean addListener(EngineListener l) {
		return listeners.add(l);
	}

	private void clearPathFinderCache() {
		game.getBoard().clearPathFinderCache();

		for (Player p : game.getPlayers()) {
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
		game.getBoard().closeOnBoard(p);
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

		Player aPlayer = game.getPlayers().iterator().next();
		Vector p1 = game.getBoard().fromBoardToGrid(aPlayer.getStartPositionOnBoard());
		Vector p2 = game.getBoard().fromBoardToGrid(aPlayer.getEnemyPositionOnBoard());

		for (Player player : game.getPlayers()) {
			List<Vector> bp = game.getBoard().getBuildPositions(player);
			if (bp != null) {
				for (Vector pos : bp) {
					if (game.getBoard().isOpened(pos) && game.getBoard().isPathAvailableOnGrid(p1, p2, pos)) {
						openedBuildPositions.add(pos);
					}
				}
			}
		}
	}

	public Vector fromBoardToGrid(Vector p) {
		return game.getBoard().fromBoardToGrid(p);
	}

	public Path fromGridToBoard(Path p) {
		return game.getBoard().fromGridToBoard(p);
	}

	public Vector fromGridToBoard(Vector p) {
		return game.getBoard().fromGridToBoard(p);
	}

	public Improvement getAvailableImprovement(String name) {
		return game.getAvailableImprovement(name);
	}

	public Collection<Improvement> getAvailableImprovements() {
		return game.getAvailableImprovements().values();
	}

	public Dimension getBoardDimension() {
		return game.getBoardDimension();
	}

	public Collection<Unit> getBoardUnits() {
		return game.getBoard().getBoardUnits();
	}

	public List<Vector> getBuildPositions(Player p) {
		return game.getBoard().getBuildPositions(p);
	}

	public float getElapsedTimeSec() {
		return (float) game.getNow() / (float) Constants.NANO;
	}

	public Dimension getGridDimension() {
		return game.getBoard().getGridDimension();
	}

	public int getGridSquareSize() {
		return game.getGridSquareSize();
	}

	public long getNow() {
		return game.getNow();
	}

	public long getNowMilli() {
		return game.getNow() / Constants.NANO_MILLI;
	}

	public Player getPlayer(int index) {
		return game.getPlayer(index);
	}

	public Player getPlayer(String name) {
		return game.getPlayer(name);
	}

	public Collection<Player> getPlayers() {
		return game.getPlayers();
	}

	public Collection<Shot> getShots() {
		return game.getShots();
	}

	public float getSpeed() {
		return speed;
	}

	public boolean isGameOver() {
		return game.isOver();
	}

	public boolean isImprovementAffordableForPlayer(String imp, Player p) {
		return getAvailableImprovement(imp).isAffordableForPlayer(p);
	}

	public boolean isOpenedBuildPosition(Player p, Vector v) {
		return isOpenedBuildPosition(v) && game.getBoard().getBuildPositions(p).contains(v);
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
				Player aPlayer = game.getPlayers().iterator().next();
				Vector p1 = game.getBoard().fromBoardToGrid(aPlayer.getStartPositionOnBoard());
				Vector p2 = game.getBoard().fromBoardToGrid(aPlayer.getEnemyPositionOnBoard());

				cachePathAvailableOnGrid[idx] = game.getBoard().isPathAvailableOnGrid(p1, p2, v);

				// System.out.println("cachePathAvailableOnGrid["+v+"] = " +
				// cachePathAvailableOnGrid[idx]);
			}

			return cachePathAvailableOnGrid[idx];
		}
	}

	public boolean isOpenedOnGrid(Vector p) {
		return game.getBoard().isOpened(p);
	}

	public boolean isPaused() {
		return paused;
	}

	public boolean isStarted() {
		return started;
	}

	public boolean isTowerDefenseGame() {
		return game.getType() == Game.Type.TOWER_DEFENSE;
	}

	public boolean lineOfSight(Vector s, Vector sp) {
		return game.lineOfSight(s, sp);
	}

	public boolean removeListener(EngineListener l) {
		return listeners.remove(l);
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	private void setSpawn(Player p, boolean v) {
		p.setSpawnEnabled(v);
		warnListeners(Type.SPAW_MODIFIED, p);
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void start() {
		game.setNow(0);
		paused = false;

		for (Unit u : game.getBoard().getBoardUnits()) {
			if (u instanceof Blocking) {
				game.getBoard().closeOnBoard(u.getPositionOnBoard());
			}
		}

		for (Player p : game.getPlayers()) {
			for (Unit u : p.getUnits()) {
				if (u instanceof Blocking) {
					game.getBoard().closeOnBoard(u.getPositionOnBoard());
				}
			}
		}

		clearPathFinderCache();

		if (PRECOMPUTE_OPEN_BUILD_POSITIONS) {
			computeOpenedBuildPositions();
		}

		started = true;
	}

	public void step(long deltaNano) {
		if (!isPaused()) {
			deltaNano *= speed;
			game.addNow(deltaNano);

			for (Player p : getPlayers()) {
				p.startNewStep(getNow());
				p.gatherActions(game.getNow());
			}

			stepDoPlayerActions();
			stepManageResources(deltaNano);
			stepSpawnUnits();
			stepMoveUnits(deltaNano);
			stepFireUnits();
			stepMoveShots(deltaNano);
			stepCheck();

			for (Player p : getPlayers()) {
				p.endStep(getNow());
			}
		}
	}

	private void stepCheck() {
		for (Player p : getPlayers()) {
			if (p.getScore() <= 0) {
				game.setOver(true);
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
		for (Player p : getPlayers()) {
			for (PlayerAction action : p.getActions()) {
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
		for (Player p : game.getPlayers()) {
			for (Unit u : p.getUnits()) {
				if (u instanceof Firing) {
					Firing f = (Firing) u;
					f.updateWeapons();
					if (f.isAbleToFire(game.getNow())) {
						f.acquireTarget(p.getEnemy());
						if (f.hasTarget()) {
							Shot shot = f.fire(game.getNow());
							shot.init(game.getGridSquareSize());
							shot.setPlayer(p);
							game.getShots().add(shot);
						}
					}
				}
			}
		}
	}

	private void stepManageResources(long delta) {
		for (Player p : game.getPlayers()) {
			p.stepManageResources(delta, game.getMetalIncreaseRatePerSec());
		}
	}

	private void stepMoveShots(long delta) {
		List<Shot> shotsWithTargetReached = new ArrayList<>();
		for (Shot s : game.getShots()) {
			if (s.isTargetReached()) {
				shotsWithTargetReached.add(s);
			} else {
				s.move(delta);
			}
		}
		for (Shot s : shotsWithTargetReached) {
			game.getShots().remove(s);
		}
	}

	private void stepMoveUnits(long delta) {
		for (Player p : game.getPlayers()) {
			List<Mobile> unitWithTargetReached = new ArrayList<>();
			for (Unit u : p.getUnits()) {
				if (u instanceof Mobile) {
					Mobile m = (Mobile) u;
					if (m.getTargetOnBoard() != null) {
						if (m.getPath() == null) {
							Path path = game.getBoard().shortestPathOnBoard(m.getPositionOnBoard(), m.getTargetOnBoard());
							if (path != null) {
								Path smoothed = game.getBoard().smoothPath(m.getPositionOnBoard(), path, getGridSquareSize(), 3);

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
		for (Player p : game.getPlayers()) {
			if (p.isSpawnEnabled()) {
				Set<Unit> newUnits = new HashSet<>();
				for (Unit u : p.getUnits()) {
					if (u instanceof Spawning) {
						Spawning spu = (Spawning) u;
						if (spu.isSpawnEnabled() && spu.isAbleToSpawn(game.getNow())) {
							Mobile s = spu.spawn(game.getNow());
							s.init(game.getGridSquareSize());
							if (s != null) {

								List<Vector> potentialTargets = game.getBoard().getEndPositions(p);
								Vector target = potentialTargets.get(rd.nextInt(potentialTargets.size()));
								s.setTargetOnBoard(game.getBoard().fromGridToBoard(target));

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

	private void warnListeners(EngineEvent.Type type) {
		warnListeners(type, null);
	}

	private void warnListeners(EngineEvent.Type type, Player p) {
		EngineEvent event = new EngineEvent(type);
		event.setPlayer(p);
		for (EngineListener l : listeners) {
			l.engineEvent(event);
		}
	}
}
