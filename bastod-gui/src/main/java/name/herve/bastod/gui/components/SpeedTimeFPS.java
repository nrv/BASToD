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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import name.herve.bastod.engine.BASToDEngine;
import name.herve.game.gui.AbstractComponent;
import name.herve.game.gui.GUIResources;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class SpeedTimeFPS extends AbstractComponent {
	private BASToDEngine engine;
	private DecimalFormat speedDf;
	private DecimalFormat timeDf;
	private BitmapFont font;

	public SpeedTimeFPS(BASToDEngine engine, int x, int y, int w, int h) {
		super("speedtimefps", x, y, w, h);

		this.engine = engine;

		speedDf = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		timeDf = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		font = GUIResources.getInstance().getFont(GUIResources.FONT_STANDARD_WHITE);
	}

	@Override
	public void drawText() {
		String str = "Speed : " + speedDf.format(engine.getSpeed()) + " - " + timeDf.format(engine.getElapsedTimeSec()) + " s - " + Gdx.graphics.getFramesPerSecond() + " FPS";
		draw(font, str, VerticalAlignment.MIDDLE, HorizontalAlignment.RIGHT);
	}

	@Override
	public Texture updateComponent() {
		return null;
	}

}
