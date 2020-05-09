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
package name.herve.bastod.tools.network;

import com.captiveimagination.jgn.clientserver.JGNConnectionListener;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public abstract class NetworkComponent implements JGNConnectionListener {
	public final static int DEFAULT_TCP_PORT = 2000;

	public final static int DEFAULT_UDP_PORT = 2100;
	private int tcpPort;

	private int udpPort;

	public NetworkComponent() {
		super();

		setTcpPort(DEFAULT_TCP_PORT);
		setUdpPort(DEFAULT_UDP_PORT);
	}

	public int getTcpPort() {
		return tcpPort;
	}

	public int getUdpPort() {
		return udpPort;
	}

	public abstract void init() throws NetworkException;

	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}

	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

	public abstract void start() throws NetworkException;

	public abstract void stop() throws NetworkException;

}
