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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Listener;

import name.herve.game.engine.GameEngine;
import name.herve.game.engine.GameState;
import name.herve.game.engine.PlayerAction;
import name.herve.game.engine.network.GameNetworkOps.GameNetworkMessage;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public abstract class GameNetworkApplication<S extends GameState, E extends GameEngine<S>> extends Listener {
	private boolean networkEnabled;
	private E engine;

	public GameNetworkApplication() {
		super();
	}

	public long getCurrentTick() {
		return engine.getCurrentTick();
	}

	public E getEngine() {
		return engine;
	}

	protected abstract EndPoint getNetworkEndPoint();

	protected abstract int getTCPPort();

	protected abstract int getUDPPort();

	public GameNetworkApplication<S, E> init() {
		startNetwork();
		registerNetworkClasses();
		return this;
	}

	public boolean isGameStarted() {
		return engine.isGameStarted();
	}

	public boolean isMaster() {
		return engine.isMaster();
	}

	public boolean isNetworkEnabled() {
		return networkEnabled;
	}

	protected void registerGenericNetworkClasses() {
		Kryo kryo = getNetworkEndPoint().getKryo();
		kryo.register(GameNetworkOps.GameNetworkMessage.class);
		kryo.register(GameNetworkOps.SetUUIDMessage.class);
		kryo.register(GameNetworkOps.GameEngineMessage.class);
		kryo.register(String[].class);
		kryo.register(PlayerAction.class);
		kryo.register(GameNetworkOps.PlayerActionMessage.class);
	}

	protected GameNetworkApplication<S, E> registerNetworkClasses() {
		if (isNetworkEnabled()) {
			registerGenericNetworkClasses();
			registerSpecificNetworkClasses();
		}

		return this;
	}

	protected abstract void registerSpecificNetworkClasses();

	public void setEngine(E engine) {
		this.engine = engine;
	}

	public GameNetworkApplication<S, E> setNetworkEnabled(boolean networkEnabled) {
		this.networkEnabled = networkEnabled;
		return this;
	}

	protected abstract GameNetworkApplication<S, E> startNetwork();
}
