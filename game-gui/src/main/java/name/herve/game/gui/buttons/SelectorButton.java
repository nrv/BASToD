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

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class SelectorButton<T> extends TextButton {
	private int idx;
	private String label;
	private T[] objs;

	public SelectorButton(String name, String label, T[] objs, int x, int y) {
		super(name, objs[0].toString(), x, y);

		this.idx = 0;
		this.objs = objs;
		this.label = label;
	}

	@Override
	public void activated(int button, boolean hotkey) {
		idx++;

		if (idx == objs.length) {
			idx = 0;
		}
	}

	@Override
	public void drawText() {
		super.drawText();

		if (label != null) {
			drawLeft(font, label);
		}
	}

	public T getSelected() {
		return objs[idx];
	}

	@Override
	public String getText() {
		return objs[idx].toString();
	}

}
