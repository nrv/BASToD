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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.UUID;

import javax.swing.JPanel;

import com.esotericsoftware.minlog.Log;

import name.herve.game.engine.PlayerAction;
import name.herve.game.engine.gpi.GamePlayerInterface;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class FunnyGameDisplay extends JPanel implements MouseListener {
	private static final long serialVersionUID = -8603475937834654185L;
	private static DecimalFormat decf = new DecimalFormat("0.000");

	private int[] color;
	private Random rd;

	// for player
	private GamePlayerInterface gpi;

	// for server
	private FunnyGameEngine engine;

	// private Timer timer;

	public FunnyGameDisplay(GamePlayerInterface gpi) {
		super();
		this.gpi = gpi;

		rd = new Random(System.currentTimeMillis());
		color = new int[] { rd.nextInt(200), rd.nextInt(200), rd.nextInt(200) };
	}

	public FunnyGameDisplay(FunnyGameEngine engine) {
		super();
		this.engine = engine;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (gpi != null) {
			if (!gpi.isPlayerReady()) {
				PlayerAction pa = new PlayerAction();
				pa.setAction(FunnyGameEngine.READY_ACTION);
				gpi.executePlayerAction(pa);
			} else if (gpi.isGameStarted()) {
				PlayerAction pa = new PlayerAction();
				Ball ball = new Ball();
				ball.setUuid(UUID.randomUUID().toString());
				ball.setColor(color);
				ball.setX(e.getX());
				ball.setY(e.getY());
				ball.setVx((rd.nextInt(10) - 5));
				ball.setVy((rd.nextInt(10) - 5));

				pa.setAction(FunnyGameEngine.ADD_BALL_ACTION).setParams(ball.asParams());
				gpi.executePlayerAction(pa);
			}
			e.consume();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		long d = 0;
		if (Log.DEBUG) {
			d = System.nanoTime();
		}

		Graphics2D g2 = (Graphics2D) g;
		int w = getWidth();
		int h = getHeight();
		int r = Math.max(w, h) / 25;
		int offset = r / 2;

		FunnyGameState state = null;

		if (gpi != null) {
			// player display
			state = (FunnyGameState) gpi.getState();
			g2.setColor(Color.BLACK);
			g2.drawString("Ready : " + gpi.isPlayerReady() + " - Step : " + gpi.getCurrentTick(), 10, 20);
		} else {
			// server display
			state = (FunnyGameState) engine.getState();
			g2.setColor(Color.BLACK);
			g2.drawString("Started : " + engine.isGameStarted() + " - Step : " + engine.getCurrentTick(), 10, 20);
		}

		synchronized (state) {
			for (Ball ball : state.getBalls()) {
				g2.setColor(ball.getColor());
				g2.fillOval((int) (ball.getX()) - offset, (int) (ball.getY()) - offset, r, r);
			}
		}

		Toolkit.getDefaultToolkit().sync();

		if (Log.DEBUG) {
			d = System.nanoTime() - d;
			Log.debug("paint", decf.format(d / 1000000.) + " ms");
		}
	}

	public void startInterface() {
		addMouseListener(this);
		// timer = new Timer(15, new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// repaint();
		// }
		// });
		// timer.start();
	}

	public void stopInterface() {
		// if (timer != null) {
		// timer.stop();
		// }
	}
}
