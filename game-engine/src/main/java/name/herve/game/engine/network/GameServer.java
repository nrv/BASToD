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
package name.herve.game.engine.network;

import java.io.IOException;
import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import name.herve.game.engine.GameEngine;
import name.herve.game.engine.GameState;
import name.herve.game.engine.gpi.RemoteGamePlayerInterface;
import name.herve.game.engine.network.GameNetworkOps.ClientConnection;
import name.herve.game.engine.network.GameNetworkOps.GameNetworkMessage;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public abstract class GameServer<S extends GameState, E extends GameEngine<S>> extends GameNetworkApplication<S, E> {
	private Server networkServer;

	public GameServer() {
		super();
	}

	@Override
	public void connected(Connection connection) {
		super.connected(connection);
		ClientConnection c = (ClientConnection) connection;
		Log.info("connect", c.getID() + " - " + c.getPlayerUuid() + " - " + c.getRemoteAddressTCP());

		RemoteGamePlayerInterface gpi = new RemoteGamePlayerInterface(getEngine(), c);
		getEngine().registerPlayer(gpi);

		GameNetworkOps.SetUUIDMessage msg = new GameNetworkOps.SetUUIDMessage();
		msg.uuid = c.getPlayerUuid();
		c.sendTCP(msg);
	}

	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
		ClientConnection c = (ClientConnection) connection;
		Log.info("disconnect", c.getID() + " - " + c.getPlayerUuid());
	}

	@Override
	protected EndPoint getNetworkEndPoint() {
		return networkServer;
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);

		if (object instanceof GameNetworkMessage) {
			GameNetworkOps.ClientConnection c = (GameNetworkOps.ClientConnection) connection;
			Log.debug("received", c.getID() + " - " + c.getPlayerUuid() + " - " + object);

			if (object instanceof GameNetworkOps.PlayerActionMessage) {
				GameNetworkOps.PlayerActionMessage pa = (GameNetworkOps.PlayerActionMessage) object;
				getEngine().executePlayerAction(pa.action);
			}
		}
	}

	public void startServer() throws IOException {
		networkServer.addListener(this);
		networkServer.bind(getTCPPort(), getUDPPort());
		networkServer.start();
	}

	@Override
	protected GameServer<S, E> startNetwork() {
		setNetworkEnabled(true);

		networkServer = new Server() {
			@Override
			protected Connection newConnection() {
				String uuid = UUID.randomUUID().toString();
				return new GameNetworkOps.ClientConnection(uuid);
			}
		};
		return this;
	}
}
