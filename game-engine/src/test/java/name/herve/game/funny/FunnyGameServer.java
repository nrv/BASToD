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

import java.awt.HeadlessException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JFrame;

import com.esotericsoftware.minlog.Log;

import name.herve.game.engine.network.GameServer;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class FunnyGameServer extends GameServer<FunnyGameState, FunnyGameEngine> implements WindowListener {
	public static void main(String[] args) {
		try {
			new FunnyGameServer();
		} catch (HeadlessException | IOException e) {
			Log.error(e.getMessage(), e);
		}
	}

	private FunnyGameDisplay display;

	public FunnyGameServer() throws HeadlessException, IOException {
		super();

		setEngine(new FunnyGameEngine(true));
		init();
		startServer();

		display = new FunnyGameDisplay(getEngine());
		display.startInterface();

		JFrame frame = new JFrame();
		frame.add(display);
		frame.addWindowListener(this);
		frame.setSize(500, 500);
		frame.setLocation(50, 300);
		frame.setTitle("Server");
		frame.setVisible(true);
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

	@Override
	protected int getTCPPort() {
		return FunnyGameNetworkOps.tcpPort;
	}

	@Override
	protected int getUDPPort() {
		return FunnyGameNetworkOps.udpPort;
	}

	@Override
	protected void registerSpecificNetworkClasses() {
		FunnyGameNetworkOps.register(getNetworkEndPoint());
	}
}
