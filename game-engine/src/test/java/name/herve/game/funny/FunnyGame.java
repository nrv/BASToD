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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;

import com.esotericsoftware.minlog.Log;

import name.herve.game.engine.gpi.LocalGamePlayerInterface;
import name.herve.game.engine.network.GameNetworkClient;
import name.herve.game.engine.network.GameNetworkServer;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class FunnyGame implements WindowListener {
	public static void main(String[] args) {
		try {
			Log.set(Log.LEVEL_DEBUG);
			new FunnyGame().start(args);
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
	}

	private FunnyGameDisplay display;
	private FunnyGameEngine engine;

	public FunnyGame() {
		super();
	}

	private void start(String[] args) {
		boolean needHelp = false;
		if (args.length == 0) {
			needHelp = true;
		} else {
			if (args[0].equalsIgnoreCase("client")) {
				startClient();
			} else if (args[0].equalsIgnoreCase("server")) {
				try {
					startServer();
				} catch (IOException e) {
					Log.error("start server", e);
				}
			} else {
				needHelp = true;
			}
		}
		if (needHelp) {
			Log.error("Usage : FunnyGame [client | server]");
		}
	}

	private void startClient() {
		engine = new FunnyGameEngine(false);

		GameNetworkClient networkClient = new GameNetworkClient();
		FunnyGameNetworkOps.configure(networkClient);
		networkClient.setNetworkEnabled(true);
		networkClient.init();

		LocalGamePlayerInterface gpi = new LocalGamePlayerInterface(engine, networkClient);

		try {
			networkClient.connectToServer(2000);
		} catch (IOException e) {
			Log.error("client", e.getMessage());
			Log.info("client", "Playing alone ...");
			networkClient.setNetworkEnabled(false);
			gpi.setPlayerUuid("local");
			engine.registerPlayer(gpi);
		}

		startUI(gpi);
	}

	private void startServer() throws IOException {
		engine = new FunnyGameEngine(true);

		GameNetworkServer networkServer = new GameNetworkServer(engine);
		FunnyGameNetworkOps.configure(networkServer);
		networkServer.setNetworkEnabled(true);
		networkServer.init();
		networkServer.startServer();

		startUI(null);
	}

	private void startUI(LocalGamePlayerInterface gpi) {
		display = (gpi != null) ? new FunnyGameDisplay(gpi) : new FunnyGameDisplay(engine);
		display.startInterface();

		Random rd = new Random();

		int w = engine.getState().getWidth() + 50;
		int h = engine.getState().getHeight() + 50;

		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new GridBagLayout());
		frame.setSize(w, h);
		frame.setLocation(50 + rd.nextInt(1000), 50 + rd.nextInt(500));
		frame.setTitle((gpi != null) ? "Client" : "Server");

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1;
		c.weighty = 1;
		frame.getContentPane().add(display, c);
		
		frame.addWindowListener(this);
		frame.setVisible(true);
		frame.setMinimumSize(frame.getMinimumSize());
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		display.stopInterface();
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
