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
package name.herve.bastod.gui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Texture;

import name.herve.bastod.engine.BASToDPlayer;
import name.herve.game.gui.AbstractComponent;
import name.herve.game.gui.GUIResources;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class SingleScoreBar extends AbstractComponent {
	private BASToDPlayer player;
	private String str;

	public SingleScoreBar(BASToDPlayer player, int x, int y, int w, int h) {
		super("singlescorebar-" + player.getBoardIndex(), x, y, w, h);
		this.player = player;
		str = null;
	}

	@Override
	public void drawText() {
		drawCentered(GUIResources.getInstance().getFont(player.getColor()), str);
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
		Color c1 = GUIResources.getInstance().getColor(player.getColor()).cpy();
		c1.a = 1f;
		p.setColor(c1);

		p.drawRectangle(0, 0, getWidth(), getHeight());

		c1.a = 0.5f;
		p.setColor(c1);
		int w = (player.getScore() * (getWidth() - 2)) / player.getMaxScore();
		p.fillRectangle(1, 1, w, getHeight() - 2);

		str = Integer.toString(player.getScore());

		Texture t = new Texture(p);
		p.dispose();

		return t;
	}
}
