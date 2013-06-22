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
package name.herve.bastod.guifwk.buttons;

import name.herve.bastod.guifwk.GUIResources;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class TextButton extends ImageButton {
	protected BitmapFont font;
	private String text;
	
	public TextButton(String name, String text, int x, int y) {
		super(name, "white", x, y);
		setText(text);
		font = GUIResources.getInstance().getFont(GUIResources.FONT_STANDARD_WHITE);
	}

	@Override
	public void activated(int button, boolean hotkey) {
		// Done by listeners
	}
	
	@Override
	public void drawText() {
		drawCentered(font, getText());
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
