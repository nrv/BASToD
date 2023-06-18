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
package name.herve.game.engine.gpi;

import com.esotericsoftware.minlog.Log;

import name.herve.game.engine.GameEngine;
import name.herve.game.engine.GamePlayerAction;
import name.herve.game.engine.GameState;
import name.herve.game.engine.network.GameNetworkClient;
import name.herve.game.engine.network.GameNetworkOps;

public class LocalGamePlayerInterface extends DefaultGamePlayerInterface {
	private String playerUuid;
	private GameNetworkClient localClient;

	public LocalGamePlayerInterface(GameEngine localEngine, GameNetworkClient localClient) {
		super(localEngine);
		this.localClient = localClient;
		if (localClient != null) {
			localClient.setGpi(this);
		}
	}

	@Override
	public void executePlayerAction(GamePlayerAction pa) {
		super.executePlayerAction(pa);

		if (localClient.isNetworkEnabled()) {
			GameNetworkOps.PlayerActionMessage msg = new GameNetworkOps.PlayerActionMessage();
			msg.action = pa;
			localClient.sendTCP(msg);
		}
	}

	@Override
	public void gameEngineEvent(String event) {
		Log.debug("lgpi - engine event", event);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void gameStateEvent(GameState state) {
		Log.debug("lgpi - state event", state.toString());
		getEngine().setState(state);
	}

	@Override
	public String getPlayerUuid() {
		return playerUuid;
	}

	@Override
	public void setPlayerReady() {
		setPlayerReady(true);

		if (!localClient.isNetworkEnabled()) {
			getEngine().tryToStartGame();
		}
	}

	public void setPlayerUuid(String uuid) {
		playerUuid = uuid;
	}

	@Override
	public void stepSimulatedEvent() {
		localClient.stepSimulatedEvent();
	}
}
