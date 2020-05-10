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
import java.util.UUID;

import javax.swing.JFrame;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import name.herve.bastod.tools.network.FunnyGameNetworkOps.ClientConnection;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class StartServer extends JFrame implements WindowListener {
	private static final long serialVersionUID = -1250497986409928688L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			StartServer server = new StartServer();
		} catch (HeadlessException | IOException e) {
			e.printStackTrace();
		}
	}

	private class MyListener extends Listener {

		@Override
		public void connected(Connection connection) {
			ClientConnection c = (ClientConnection) connection;
			Log.info("connect", c.getID() + " - " + c.getUuid() + " - " + c.getRemoteAddressTCP());
			FunnyGameNetworkOps.SetUUIDMessage msg = new FunnyGameNetworkOps.SetUUIDMessage();
			msg.uuid = c.getUuid();
			c.sendTCP(msg);
		}

		@Override
		public void disconnected(Connection connection) {
			ClientConnection c = (ClientConnection) connection;
			Log.info("disconnect", c.getID() + " - " + c.getUuid());
		}

		@Override
		public void received(Connection connection, Object object) {
			ClientConnection c = (ClientConnection) connection;
			Log.info("received", c.getID() + " - " + c.getUuid() + " - " + object);

			if (object instanceof FunnyGameNetworkOps.AddBallMessage) {
				FunnyGameNetworkOps.AddBallMessage msg = (FunnyGameNetworkOps.AddBallMessage) object;
				Log.info("received", c.getID() + " - " + msg.ball);
				game.addBall(msg.ball);
			}
		}
	}

	private FunnyGame game;
	private Server server;

	public StartServer() throws HeadlessException, IOException {
		super();

		server = new Server() {
			protected Connection newConnection() {
				String uuid = UUID.randomUUID().toString();
				return new FunnyGameNetworkOps.ClientConnection(uuid);
			}
		};

		FunnyGameNetworkOps.register(server);

		server.addListener(new MyListener());

		server.bind(FunnyGameNetworkOps.tcpPort, FunnyGameNetworkOps.udpPort);
		server.start();

		game = new FunnyGame(false, server);
		game.startInterface();

		add(game);

		setSize(500, 500);
		setLocation(700, 300);
		setTitle("Server");
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
