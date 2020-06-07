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

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JFrame;

import com.esotericsoftware.minlog.Log;

import name.herve.game.engine.gpi.LocalGamePlayerInterface;
import name.herve.game.engine.network.GameClient;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class FunnyGameClient extends GameClient<FunnyGameState, FunnyGameEngine> implements WindowListener {
	public static void main(String[] args) {
		try {
			new FunnyGameClient();
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
	}

	private FunnyGameDisplay display;

	public FunnyGameClient() {
		super();

		setEngine(new FunnyGameEngine(false));
		setNetworkEnabled(true);
		init();

		LocalGamePlayerInterface gpi = new LocalGamePlayerInterface(getEngine(), this);
		setGpi(gpi);

		try {
			connectToServer(2000);
		} catch (IOException e) {
			Log.error("client", e.getMessage());
			Log.info("client", "Playing alone ...");
			setNetworkEnabled(false);
		}

		display = new FunnyGameDisplay(gpi);
		display.startInterface();

		JFrame frame = new JFrame();
		frame.add(display);
		frame.addWindowListener(this);
		frame.setSize(getEngine().getState().getWidth(), getEngine().getState().getHeight());
		frame.setLocation(50, 300);
		frame.setTitle("Client");
		frame.setVisible(true);

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

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		display.stopInterface();
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void stepSimulatedEvent() {
		super.stepSimulatedEvent();
		display.repaint();
	}
}
