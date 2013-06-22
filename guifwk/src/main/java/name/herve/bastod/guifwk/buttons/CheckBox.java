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

import name.herve.bastod.guifwk.AbstractButton;
import name.herve.bastod.guifwk.GUIResources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class CheckBox extends AbstractButton {
	public final static String TEXTURE_CHECKED = "CheckBox:checked";
	public final static String TEXTURE_UNCHECKED = "CheckBox:unchecked";

	private boolean checked;
	private BitmapFont font;
	private int hotkey;
	private Texture tChecked;
	private String text;
	private Texture tUnchecked;
	private float txo;
	private float tyo;

	public CheckBox(String name, String text, int x, int y) {
		this(name, text, -1, x, y);
	}

	public CheckBox(String name, String text, int hotkey, int x, int y) {
		super(name, x, y, -1, -1);

		this.text = text;
		this.hotkey = hotkey;

		tChecked = GUIResources.getInstance().getSprite(TEXTURE_CHECKED);
		tUnchecked = GUIResources.getInstance().getSprite(TEXTURE_UNCHECKED);
		font = GUIResources.getInstance().getFont(GUIResources.FONT_STANDARD_WHITE);

		setChecked(false);

		TextBounds b = font.getBounds(text);
		txo = tChecked.getWidth() + SMALL_SPACER;
		tyo = (tChecked.getHeight() - b.height) / 2;

		setBounds(tChecked.getWidth() + (int) Math.ceil(b.width), tChecked.getHeight());
	}

	@Override
	public void activated(int button, boolean hotkey) {
		switchState();
	}

	@Override
	public void drawText() {
		draw(font, text, getX() + txo, getY() + tyo);
	}

	@Override
	public int getHotKey() {
		return hotkey;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
		setComponent(checked ? tChecked : tUnchecked);
	}

	public void switchState() {
		setChecked(!isChecked());
	}
}
