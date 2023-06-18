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

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Listener;

import name.herve.game.engine.GameEngine;
import name.herve.game.engine.GamePlayerAction;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public abstract class GameNetworkComponent extends Listener {
	private int tcpPort;
	private int udpPort;
	private List<Class<?>> classesToRegister;
	private boolean networkEnabled;

	public GameNetworkComponent() {
		super();

		classesToRegister = new ArrayList<>();
		addClassToRegister(GameNetworkOps.GameNetworkMessage.class);
		addClassToRegister(GameNetworkOps.SetUUIDMessage.class);
		addClassToRegister(GameNetworkOps.GameEngineMessage.class);
		addClassToRegister(GameNetworkOps.GameStateMessage.class);
		addClassToRegister(String[].class);
		addClassToRegister(GamePlayerAction.class);
		addClassToRegister(GameNetworkOps.PlayerActionMessage.class);
	}

	public GameNetworkComponent addClassToRegister(Class<?> e) {
		classesToRegister.add(e);
		return this;
	}

	protected abstract EndPoint getNetworkEndPoint();
	protected abstract GameEngine getEngine();
		
	protected int getTCPPort() {
		return tcpPort;
	}

	protected int getUDPPort() {
		return udpPort;
	}

	public GameNetworkComponent init() {
		startNetwork();
		registerNetworkClasses();
		return this;
	}

	public boolean isNetworkEnabled() {
		return networkEnabled;
	}

	protected GameNetworkComponent registerNetworkClasses() {
		if (isNetworkEnabled()) {
			Kryo kryo = getNetworkEndPoint().getKryo();
			for (Class<?> clazz : classesToRegister) {
				kryo.register(clazz);
			}
		}

		return this;
	}

	public GameNetworkComponent setNetworkEnabled(boolean networkEnabled) {
		this.networkEnabled = networkEnabled;
		return this;
	}

	public GameNetworkComponent setTCPPort(int tcpPort) {
		this.tcpPort = tcpPort;
		return this;
	}

	public GameNetworkComponent setUDPPort(int udpPort) {
		this.udpPort = udpPort;
		return this;
	}

	protected abstract GameNetworkComponent startNetwork();
}
