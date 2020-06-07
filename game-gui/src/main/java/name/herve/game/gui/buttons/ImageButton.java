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
package name.herve.game.gui.buttons;

import com.badlogic.gdx.graphics.Texture;

import name.herve.game.gui.AbstractButton;
import name.herve.game.gui.GUIResources;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public abstract class ImageButton extends AbstractButton {
	public final static String TEXTURE_BORDER = "ImageButton:border";
	public final static String TEXTURE_LONG_BORDER_RED = "ImageButton:longborder-red";
	public final static String TEXTURE_LONG_BORDER_BLUE = "ImageButton:longborder-blue";
	public final static String TEXTURE_LONG_BORDER_WHITE = "ImageButton:longborder-white";

	private int hotkey;

	public ImageButton(String name, String color, int x, int y) {
		super(name, x, y, 160, 20);
		hotkey = -1;

		if (color.equals("red")) {
			setBackground(GUIResources.getInstance().getSprite(TEXTURE_LONG_BORDER_RED));
		} else if (color.equals("blue")) {
			setBackground(GUIResources.getInstance().getSprite(TEXTURE_LONG_BORDER_BLUE));
		} else if (color.equals("white")) {
			setBackground(GUIResources.getInstance().getSprite(TEXTURE_LONG_BORDER_WHITE));
		}

		setComponent(null);
	}

	public ImageButton(String name, Texture texture, int x, int y) {
		this(name, texture, -1, x, y);
	}

	public ImageButton(String name, Texture texture, int hotkey, int x, int y) {
		this(name, texture, hotkey, x, y, 20, 20, GUIResources.getInstance().getSprite(TEXTURE_BORDER));
	}

	public ImageButton(String name, Texture texture, int hotkey, int x, int y, int w, int h, Texture bgd) {
		super(name, x, y, w, h);

		this.hotkey = hotkey;

		setBackground(bgd);
		setComponent(texture);

		setBounds(w, h);
	}

	@Override
	public void drawText() {
	}

	@Override
	public int getHotKey() {
		return hotkey;
	}

	@Override
	public void setBounds(int w, int h) {
		super.setBounds(w, h);

		if (getComponent() != null) {
			setxOffset((getWidth() - getComponent().getWidth()) / 2);
			setyOffset((getHeight() - getComponent().getHeight()) / 2);
		}
	}

}
