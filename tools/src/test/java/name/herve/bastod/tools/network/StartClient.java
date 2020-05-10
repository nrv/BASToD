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

import java.awt.HeadlessException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.InetAddress;

import javax.swing.JFrame;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.esotericsoftware.minlog.Log;

import name.herve.bastod.tools.network.FunnyGameNetworkOps.ClientConnection;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class StartClient extends JFrame implements WindowListener {
	private static final long serialVersionUID = -593393793558481885L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			StartClient client = new StartClient();
		} catch (HeadlessException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private class MyListener extends Listener {

		@Override
		public void connected(Connection connection) {
		}

		@Override
		public void disconnected(Connection connection) {
		}

		@Override
		public void received(Connection connection, Object object) {
			Log.info("received", object.toString());

			if (object instanceof FunnyGameNetworkOps.SetUUIDMessage) {
				FunnyGameNetworkOps.SetUUIDMessage msg = (FunnyGameNetworkOps.SetUUIDMessage) object;
				game.setUuid(msg.uuid);
			}
		}
	}

	private FunnyGame game;
	private Client client;

	public StartClient() throws HeadlessException, IOException {
		super();
		
		client = new Client();
		client.start();

		FunnyGameNetworkOps.register(client);
		
		client.addListener(new ThreadedListener(new MyListener()));
		InetAddress address = client.discoverHost(FunnyGameNetworkOps.udpPort, 5000);
		client.connect(5000, address, FunnyGameNetworkOps.tcpPort, FunnyGameNetworkOps.udpPort);
		
		game = new FunnyGame(true, client);
		game.startInterface();

		add(game);

		setSize(500, 500);
		setLocation(50, 300);
		setTitle("Client");
		addWindowListener(this);
		setVisible(true);
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}
}
