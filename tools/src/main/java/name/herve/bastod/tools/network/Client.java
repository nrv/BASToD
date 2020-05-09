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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.captiveimagination.jgn.JGN;
import com.captiveimagination.jgn.clientserver.JGNClient;
import com.captiveimagination.jgn.clientserver.JGNConnection;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class Client extends NetworkComponent {
	private JGNClient client;

	public Client() {
		super();
	}

	@Override
	public void connected(JGNConnection connection) {
		System.out.println("logged in as " + connection.getPlayerId());
	}

	@Override
	public void disconnected(JGNConnection connection) {
		System.out.println("logged off");
	}

	@Override
	public void init() throws NetworkException {
		try {
			client = new JGNClient(new InetSocketAddress(InetAddress.getLocalHost(), 0), new InetSocketAddress(InetAddress.getLocalHost(), 0));
			client.addServerConnectionListener(this);
		} catch (UnknownHostException e) {
			throw new NetworkException(e);
		} catch (IOException e) {
			throw new NetworkException(e);
		}
	}

	@Override
	public void start() throws NetworkException {
		try {
			JGN.createThread(client).start();
			client.connectAndWait(new InetSocketAddress(InetAddress.getLocalHost(), getTcpPort()), new InetSocketAddress(InetAddress.getLocalHost(), getUdpPort()), 5000);
		} catch (UnknownHostException e) {
			throw new NetworkException(e);
		} catch (IOException e) {
			throw new NetworkException(e);
		} catch (InterruptedException e) {
			throw new NetworkException(e);
		}

	}

	@Override
	public void stop() throws NetworkException {
		try {
			client.close();
		} catch (IOException e) {
			throw new NetworkException(e);
		}
	}

}
