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

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import name.herve.game.tools.conf.Configuration;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public abstract class AbstractGame extends Game {
	private Configuration gameConf;
	private int h;
	private Map<String, AbstractScreen> screensCache;
	private int w;

	public AbstractGame(int w, int h) {
		super();
		this.h = h;
		this.w = w;
		screensCache = new HashMap<>();
	}

	public Configuration getGameConf() {
		return gameConf;
	}

	public int getH() {
		return h;
	}

	public int getW() {
		return w;
	}

	public boolean isScreenCached(String screenName) {
		return screensCache.containsKey(screenName);
	}

	public void setGameConf(Configuration gameConf) {
		this.gameConf = gameConf;
	}

	public void setScreen(AbstractScreen screen) {
		if (screen.isCached()) {
			screensCache.put(screen.getCacheName(), screen);
		}

		if (!screen.isStarted()) {
			screen.start();
		}

		Gdx.input.setInputProcessor(screen);

		Screen current = getScreen();

		super.setScreen(screen);

		if (current != null) {
			AbstractScreen currentScreen = (AbstractScreen) current;
			if (!currentScreen.isCached()) {
				currentScreen.stop();
			}
		}
	}

	public void setScreen(String screenName) {
		setScreen(screensCache.get(screenName));
	}
}
