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
package name.herve.bastod.tools;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import name.herve.bastod.tools.network.NetworkException;
import name.herve.bastod.tools.network.Server;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class StartServer extends JFrame implements WindowListener {
	private static final long serialVersionUID = -1250497986409928688L;
	private FunnyGame game;
	
	public StartServer() throws HeadlessException {
		super();
		game = new FunnyGame();
		game.setColor(Color.GREEN);
		game.startInterface();
		
		add(game);
		
		setSize(500, 500);
		setLocation(700, 300);
		setTitle("Server");
		addWindowListener(this);
		setVisible(true);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		Server s = new Server();
//
//		try {
//			s.init();
//			s.start();
//		} catch (NetworkException e) {
//			e.printStackTrace();
//		}

		StartServer server = new StartServer();
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
