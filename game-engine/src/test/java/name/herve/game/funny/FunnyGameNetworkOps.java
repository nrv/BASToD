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
package name.herve.game.funny;

import java.util.ArrayList;
import java.util.HashMap;

import name.herve.game.engine.network.GameNetworkComponent;
import name.herve.game.engine.network.GameNetworkOps;

public class FunnyGameNetworkOps extends GameNetworkOps {
	public final static int tcpPort = 54741;
	public final static int udpPort = 54742;

	public static void configure(GameNetworkComponent component) {
		component.setTCPPort(tcpPort);
		component.setUDPPort(udpPort);
		component.addClassToRegister(int[].class);
		component.addClassToRegister(FunnyGameState.class);
		component.addClassToRegister(ArrayList.class);
		component.addClassToRegister(HashMap.class);
		component.addClassToRegister(FunnyGamePlayer.class);
	}
}
