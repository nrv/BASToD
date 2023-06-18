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
import java.net.InetAddress;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.minlog.Log;

import name.herve.game.engine.GameEngine;
import name.herve.game.engine.gpi.LocalGamePlayerInterface;
import name.herve.game.engine.network.GameNetworkOps.GameNetworkMessage;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class GameNetworkClient extends GameNetworkComponent {
	private Client serverConnection;
	private LocalGamePlayerInterface gpi;

	public GameNetworkClient() {
		super();
	}

	public void connectToServer(int timeout) throws IOException {
		if (isNetworkEnabled()) {
			InetAddress host = serverConnection.discoverHost(getUDPPort(), timeout);
			if (host == null) {
				throw new IOException("Unable to find a server on the local network");
			}
			connectToServer(timeout, host);
		}
	}

	public void connectToServer(int timeout, InetAddress host) throws IOException {
		if (isNetworkEnabled()) {
			serverConnection.addListener(new ThreadedListener(this));
			serverConnection.connect(timeout, host, getTCPPort(), getUDPPort());
		}
	}

	public void connectToServer(int timeout, String host) throws IOException {
		if (isNetworkEnabled()) {
			connectToServer(timeout, InetAddress.getByName(host));
		}
	}

	@Override
	protected GameEngine getEngine() {
		return getGpi().getEngine();
	}

	protected LocalGamePlayerInterface getGpi() {
		return gpi;
	}

	@Override
	protected EndPoint getNetworkEndPoint() {
		return serverConnection;
	}

	public String getPlayerUuid() {
		return getGpi().getPlayerUuid();
	}

	public boolean isPlayerReady() {
		return (getGpi() != null) && getGpi().isPlayerReady();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);

		if (object instanceof GameNetworkMessage) {
			Log.debug("received", object.toString());

			if (object instanceof GameNetworkOps.SetUUIDMessage) {
				GameNetworkOps.SetUUIDMessage msg = (GameNetworkOps.SetUUIDMessage) object;
				getGpi().setPlayerUuid(msg.uuid);
				getEngine().registerPlayer(gpi);
			} else if (object instanceof GameNetworkOps.GameEngineMessage) {
				GameNetworkOps.GameEngineMessage msg = (GameNetworkOps.GameEngineMessage) object;
				getEngine().genericEvent(msg.event);
			} else if (object instanceof GameNetworkOps.GameStateMessage) {
				GameNetworkOps.GameStateMessage msg = (GameNetworkOps.GameStateMessage) object;
				getEngine().stateEvent(msg.state);
			}
		}
	}

	public int sendTCP(Object object) {
		Log.debug("sendTCP", object.toString());
		return serverConnection.sendTCP(object);
	}

	public int sendUDP(Object object) {
		Log.debug("sendUDP", object.toString());
		return serverConnection.sendUDP(object);
	}

	public void setGpi(LocalGamePlayerInterface gpi) {
		this.gpi = gpi;
	}

	@Override
	protected GameNetworkClient startNetwork() {
		serverConnection = new Client();
		serverConnection.start();
		return this;
	}

	public void stepSimulatedEvent() {
		Log.trace("simulation", "step");
	}
}
