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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class FunnyGame extends JPanel implements MouseListener {
	private static final long serialVersionUID = -8603475937834654185L;

	private Color color;
	private List<Ball> balls;
	private Random rd;

	public FunnyGame() {
		super();

		balls = new ArrayList<Ball>();
		rd = new Random(System.currentTimeMillis());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		double x = (double) e.getX() / (double) getWidth();
		double y = (double) e.getY() / (double) getHeight();
		double dx = (rd.nextDouble() * 2) - 1;
		double dy = (rd.nextDouble() * 2) - 1;

		Ball ball = new Ball();
		ball.setColor(color);
		ball.setPosition(new Point2D.Double(x, y));
		ball.setSpeed(new Point2D.Double(dx, dy));

		balls.add(ball);
		
		repaint();
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
		
		Graphics2D g2 = (Graphics2D)g;
		int w = getWidth();
		int h = getHeight();
		int r = Math.max(w, h) / 25;
		int offset = r / 2;
		
		for (Ball ball : balls) {
			g2.setColor(ball.getColor());
			g2.fillOval((int)(ball.getPosition().getX() * w) - offset, (int)(ball.getPosition().getY() * h) - offset, r, r);
		}
	}

	public void startInterface() {
		addMouseListener(this);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
