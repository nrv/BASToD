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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.EndPoint;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class FunnyGame extends JPanel implements MouseListener {
	private static final long serialVersionUID = -8603475937834654185L;

	private float[] color;
	private List<Ball> balls;
	private Random rd;
	private boolean ableToCreateBalls;
	private EndPoint network;
	private String uuid;

	public FunnyGame(boolean ableToCreateBalls, EndPoint network) {
		super();

		this.ableToCreateBalls = ableToCreateBalls;
		this.network = network;
		balls = new ArrayList<>();
		rd = new Random(System.currentTimeMillis());
		color = new float[] { rd.nextFloat() / 2, rd.nextFloat() / 2, rd.nextFloat() / 2 };
	}

	public void addBall(Ball ball) {
		balls.add(ball);

		if (network instanceof Client) {
			Client c = (Client) network;
			FunnyGameNetworkOps.AddBallMessage msg = new FunnyGameNetworkOps.AddBallMessage();
			msg.ball = ball;
			c.sendTCP(msg);
		} else {
		}

		repaint();
	}

	public String getUuid() {
		return uuid;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (ableToCreateBalls) {
			double x = (double) e.getX() / (double) getWidth();
			double y = (double) e.getY() / (double) getHeight();
			double dx = (rd.nextDouble() * 2) - 1;
			double dy = (rd.nextDouble() * 2) - 1;

			Ball ball = new Ball();
			ball.setUuid(uuid);
			ball.setColor(color);
			ball.setPosition(new Point2D.Double(x, y));
			ball.setSpeed(new Point2D.Double(dx, dy));

			addBall(ball);
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

		Graphics2D g2 = (Graphics2D) g;
		int w = getWidth();
		int h = getHeight();
		int r = Math.max(w, h) / 25;
		int offset = r / 2;

		for (Ball ball : balls) {
			g2.setColor(ball.getColor());
			g2.fillOval((int) (ball.getPosition().getX() * w) - offset, (int) (ball.getPosition().getY() * h) - offset, r, r);
		}
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void startInterface() {
		addMouseListener(this);
	}
}
