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
package name.herve.game.gui;

import java.util.ArrayList;
import java.util.List;

import name.herve.game.gui.buttons.GUIButtonListener;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public abstract class AbstractClickableComponent extends AbstractComponent {
	private List<GUIButtonListener> listeners;

	public AbstractClickableComponent(String name, int x, int y, int w, int h) {
		super(name, x, y, w, h);
		listeners = new ArrayList<>();
	}

	public abstract void activated(int button, boolean hotkey);

	public boolean addListener(GUIButtonListener l) {
		return listeners.add(l);
	}

	public abstract int getHotKey();

	private boolean isClicked(int x, int y) {
		return (getX() <= x) && (getY() <= y) && (x < (getX() + getWidth())) && (y < (getY() + getHeight()));
	}

	@Override
	public boolean keyDown(int k) {
		if (isEnabled() && (k == getHotKey())) {
			activated(-1, true);
			warnListeners();
			return true;
		}

		return false;
	}

	public boolean removeListener(GUIButtonListener l) {
		return listeners.remove(l);
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if (isEnabled() && isClicked(x, y)) {
			activated(button, false);
			warnListeners();
			return true;
		}

		return false;
	}

	private void warnListeners() {
		GUIEvent event = new GUIEvent(this);
		for (GUIButtonListener l : listeners) {
			l.buttonActivated(event);
		}
	}
}
