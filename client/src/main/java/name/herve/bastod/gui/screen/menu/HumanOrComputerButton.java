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
package name.herve.bastod.gui.screen.menu;

import name.herve.bastod.guifwk.GUIResources;
import name.herve.bastod.guifwk.buttons.ImageButton;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class HumanOrComputerButton extends ImageButton {
	private BitmapFont font;
	private boolean human;

	public HumanOrComputerButton(String color, int x, int y) {
		super("HumanOrComputerButton-" + color, color, x, y);
		
		setHuman();
		font = GUIResources.getInstance().getFont(color);
	}

	@Override
	public void activated(int button, boolean hotkey) {
		human = !human;
	}

	@Override
	public void drawText() {
		drawCentered(font, isHuman() ? "Human" : "Computer");
	}
	
	public boolean isComputer() {
		return !isHuman();
	}

	public boolean isHuman() {
		return human;
	}

	public void setComputer() {
		this.human = false;
	}
	
	public void setHuman() {
		this.human = true;
	}
}
