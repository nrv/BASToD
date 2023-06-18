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
import name.herve.game.engine.GameState;
import name.herve.game.engine.network.GameNetworkOps;
import name.herve.game.engine.network.GameNetworkOps.ClientConnection;

public class RemoteGamePlayerInterface extends DefaultGamePlayerInterface {
	private ClientConnection c;

	public RemoteGamePlayerInterface(GameEngine localEngine, ClientConnection c) {
		super(localEngine);
		this.c = c;
	}

	@Override
	public void gameEngineEvent(String event) {
		Log.debug("rgpi - engine event", event);
		GameNetworkOps.GameEngineMessage msg = new GameNetworkOps.GameEngineMessage();
		msg.event = event;
		c.sendTCP(msg);
	}

	@Override
	public void gameStateEvent(GameState state) {
		Log.debug("rgpi - state event", state.toString());
		GameNetworkOps.GameStateMessage msg = new GameNetworkOps.GameStateMessage();
		msg.state = state;
		c.sendTCP(msg);
	}

	@Override
	public String getPlayerUuid() {
		return c.getPlayerUuid();
	}

	@Override
	public void setPlayerReady() {
		setPlayerReady(true);
		getEngine().tryToStartGame();
	}

	@Override
	public void stepSimulatedEvent() {
	}
}
