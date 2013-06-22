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

import java.util.List;

import name.herve.bastod.engine.Unit;
import name.herve.bastod.guifwk.AbstractComponent;
import name.herve.bastod.guifwk.GUIResources;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class UnitInfoBox extends AbstractComponent {
	public static final String INFOBOX_FONT = "ibxfont";
	
	private Unit unit;
	private BitmapFont font;
	
	public UnitInfoBox(Unit u) {
		super(u.getName() + "-InfoBox", -1, -1, 200, 100);
		this.unit = u;
		
		setNeedUpdate(false);
		font = GUIResources.getInstance().getFont(INFOBOX_FONT);
		
		moveTo(u.getPositionOnBoard().getXInt(), u.getPositionOnBoard().getYInt());
		
		Blending bck = Pixmap.getBlending();
		Pixmap.setBlending(Blending.None);

		Pixmap p = new Pixmap(getWidth(), getHeight(), Pixmap.Format.RGBA8888);
		Color c1 = Color.YELLOW;
		c1.a = 1f;
		p.setColor(c1);

		p.drawRectangle(0, 0, getWidth(), getHeight());

		c1 = Color.BLACK;
		c1.a = 0.5f;
		p.setColor(c1);

		p.fillRectangle(1, 1, getWidth() - 2, getHeight() - 2);

		setBackground(new Texture(p));
		p.dispose();
		Pixmap.setBlending(bck);
	}

	@Override
	public void drawText() {
		List<String> infos = unit.getInfos();
		if ((infos != null) && (!infos.isEmpty())) {
			float spacer = getHeight() / (float)infos.size();
			float y = 0;
			for (String s : infos) {
				draw(font, s, getX(), getY() + y);
				y += spacer;
			}
		}
	}

	@Override
	public Texture updateComponent() {
		return null;
	}

}
