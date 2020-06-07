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

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;

import name.herve.game.engine.PlayerAction;
import name.herve.game.engine.network.GameNetworkOps.GameNetworkMessage;

public class GameNetworkOps {
	public static class GameNetworkMessage {
		@Override
		public String toString() {
			return "GameNetworkMessage []";
		}
	}

	public static class SetUUIDMessage extends GameNetworkMessage {
		public String uuid;

		@Override
		public String toString() {
			return "SetUUIDMessage [uuid=" + uuid + "]";
		}
	}

	public static class PlayerActionMessage extends GameNetworkMessage {
		public PlayerAction action;

		@Override
		public String toString() {
			return "PlayerActionMessage [action=" + action + "]";
		}
	}

	public static class GameEngineMessage extends GameNetworkMessage {
		public String event;

		@Override
		public String toString() {
			return "GameEngineMessage [event=" + event + "]";
		}

	}

	public static class ClientConnection extends Connection {
		private String playerUuid;

		public ClientConnection(String uuid) {
			super();
			this.playerUuid = uuid;
		}

		public String getPlayerUuid() {
			return playerUuid;
		}

		@Override
		public int sendTCP(Object object) {
			if (object instanceof GameNetworkMessage) {
				Log.debug("sendTCP", object.toString());
			}
			return super.sendTCP(object);
		}

		@Override
		public int sendUDP(Object object) {
			if (object instanceof GameNetworkMessage) {
				Log.debug("sendUDP", object.toString());
			}
			return super.sendUDP(object);
		}
	}
}
