/*
 * Copyright 2012, 2013 Nicolas HERVE
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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.captiveimagination.jgn.JGN;
import com.captiveimagination.jgn.clientserver.JGNConnection;
import com.captiveimagination.jgn.clientserver.JGNServer;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class Server extends NetworkComponent {
	private JGNServer server;

	public Server() {
		super();
	}

	@Override
	public void init() throws NetworkException {
		try {
			server = new JGNServer(new InetSocketAddress(InetAddress.getLocalHost(), getTcpPort()), new InetSocketAddress(InetAddress.getLocalHost(), getUdpPort()));
			server.addClientConnectionListener(this);
		} catch (UnknownHostException e) {
			throw new NetworkException(e);
		} catch (IOException e) {
			throw new NetworkException(e);
		}

	}

	@Override
	public void connected(JGNConnection connection) {
		System.out.println(connection + " connected on server");
	}

	@Override
	public void disconnected(JGNConnection connection) {
		System.out.println(connection + " disconnected from server");
	}

	@Override
	public void start() throws NetworkException {
		JGN.createThread(server).start();
	}

	@Override
	public void stop() throws NetworkException {
		// TODO Auto-generated method stub

	}

}
