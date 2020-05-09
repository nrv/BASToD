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
package name.herve.bastod.gui.components;

import name.herve.bastod.engine.Player;
import name.herve.bastod.guifwk.AbstractComponent;
import name.herve.bastod.guifwk.GUIResources;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class DualScoreBar extends AbstractComponent {
	private Player player1;
	private Player player2;
	private String str1;
	private String str2;

	public DualScoreBar(Player player1, Player player2, int x, int y, int w, int h) {
		super("dualscorebar", x, y, w, h);
		this.player1 = player1;
		this.player2 = player2;
	}

	@Override
	public void drawText() {
		drawLeft(GUIResources.getInstance().getFont(player1.getColor()), str1);
		drawRight(GUIResources.getInstance().getFont(player2.getColor()), str2);
	}

	@Override
	public void stop() {
		super.stop();
		
		disposeComponent();
	}
	
	@Override
	public Texture updateComponent() {
		Pixmap p = new Pixmap(getWidth(), getHeight(), Pixmap.Format.RGBA8888);
		p.setBlending(Blending.None);
		Color c = GUIResources.getInstance().getColor(GUIResources.WHITE);

		Color c1 = GUIResources.getInstance().getColor(player1.getColor()).cpy();
		c1.a = 0.5f;
		Color c2 = GUIResources.getInstance().getColor(player2.getColor()).cpy();
		c2.a = 0.5f;

		int w = player1.getScore() * (getWidth() - 2) / (player1.getScore() + player2.getScore());

		p.setColor(c1);
		p.fillRectangle(1, 1, w, getHeight() - 2);

		p.setColor(c2);
		p.fillRectangle(w + 1, 1, getWidth() - 2 - w, getHeight() - 2);

		p.setColor(c);
		p.drawRectangle(0, 0, getWidth(), getHeight());

		str1 = Integer.toString(player1.getScore());
		str2 = Integer.toString(player2.getScore());

		Texture t = new Texture(p);
		p.dispose();

		return t;
	}
}
