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
package name.herve.bastod.tools;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class StartClient extends JFrame implements WindowListener {
	private static final long serialVersionUID = -593393793558481885L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Client c = new Client();
		//
		// try {
		// c.init();
		// c.start();
		// try {
		// Thread.sleep(5000);
		// } catch (InterruptedException e) {
		// }
		// c.stop();
		// } catch (NetworkException e) {
		// e.printStackTrace();
		// }

		StartClient client = new StartClient();

	}

	private FunnyGame game;

	public StartClient() throws HeadlessException {
		super();
		game = new FunnyGame();
		game.setColor(Color.RED);
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
