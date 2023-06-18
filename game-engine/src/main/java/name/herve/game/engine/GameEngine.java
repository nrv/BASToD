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
package name.herve.game.engine;

import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.minlog.Log;

import name.herve.game.engine.gpi.DefaultGamePlayerInterface;
import name.herve.game.engine.simu.Simulated;
import name.herve.game.engine.simu.SimulationThread;

//https://developer.valvesoftware.com/wiki/Source_Multiplayer_Networking

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public abstract class GameEngine<S extends GameState> implements Simulated {
	public final static String START_GAME_EVENT = "startGame";
	public final static String STOP_GAME_EVENT = "stopGame";

	private S state;
	private boolean master;
	private Map<String, DefaultGamePlayerInterface> playerInterfaces;
	private SimulationThread t;

	public GameEngine(boolean master, S state) {
		super();
		this.master = master;
		this.state = state;
		playerInterfaces = new HashMap<>();
	}

	public GamePlayerAction executePlayerAction(GamePlayerAction pa) {
		pa.setTick(getCurrentTick());
		pa = executeSpecificPlayerAction(pa);
		return pa;
	}

	public abstract GamePlayerAction executeSpecificPlayerAction(GamePlayerAction pa);

	public void genericEvent(String event) {
		Log.debug("engine event", event);
		for (GameEngineListener l : playerInterfaces.values()) {
			l.gameEngineEvent(event);
		}
		if (START_GAME_EVENT.equals(event)) {
			startGame();
		} else if (STOP_GAME_EVENT.equals(event)) {
			stopGame();
		}
	}

	public long getCurrentTick() {
		return state.getCurrentTick();
	}

	public DefaultGamePlayerInterface getPlayerInterface(String uuid) {
		return playerInterfaces.get(uuid);
	}

	public S getState() {
		return state;
	}

	@Override
	public boolean isGameStarted() {
		return state.isGameStarted();
	}

	public boolean isMaster() {
		return master;
	}

	public void registerPlayer(DefaultGamePlayerInterface gpi) {
		playerInterfaces.put(gpi.getPlayerUuid(), gpi);
	}

	public void setState(S state) {
		this.state = state;
	}

	private void startGame() {
		Log.debug("engine", "starting game with state : " + state);
		state.setGameStarted(true);
		t = new SimulationThread(10, this);
		t.start();
	}

	public void stateEvent(S state) {
		Log.debug("engine state", state.toString());
		for (GameEngineListener l : playerInterfaces.values()) {
			l.gameStateEvent(state);
		}
	}

	public abstract void step(long deltaNano);

	@Override
	public void step(long tick, long deltaNano) {
		Log.trace("engine", "step " + tick);
		state.setCurrentTick(tick);
		step(deltaNano);
		for (GameEngineListener l : playerInterfaces.values()) {
			l.stepSimulatedEvent();
		}
	}

	private void stopGame() {
		Log.debug("engine", "stopping game");
		state.setGameStarted(false);
		t = null;
	}

	public void tryToStartGame() {
		if (isGameStarted()) {
			return;
		}

		boolean allPlayersReady = true;
		for (DefaultGamePlayerInterface gpi : playerInterfaces.values()) {
			if (!gpi.isPlayerReady()) {
				allPlayersReady = false;
				break;
			}
		}

		if (allPlayersReady) {
			stateEvent(state);
			genericEvent(START_GAME_EVENT);
		}
	}
}
