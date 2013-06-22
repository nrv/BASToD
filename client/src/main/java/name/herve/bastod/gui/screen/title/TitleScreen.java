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
package name.herve.bastod.gui.screen.title;

import name.herve.bastod.gui.screen.menu.MenuScreen;
import name.herve.bastod.guifwk.AbstractGame;
import name.herve.bastod.guifwk.AbstractScreen;
import name.herve.bastod.guifwk.GUIResources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class TitleScreen extends AbstractScreen {
	public static final String CACHE_NAME = "title";
	
	// private float z;

	public TitleScreen(AbstractGame sltd) {
		super(sltd);
		// z = 0;
		setCacheName(CACHE_NAME);
	}

	@Override
	public void renderFrame(float delta) {
		// z += delta;

		// cameraZoom((4f + (float)Math.abs(Math.sin(z/4))) / 8f);

		batchBegin();
		Texture title = GUIResources.getInstance().getSprite("title");
		draw(title, (getGameApplication().getW() - title.getWidth()) / 2, (getGameApplication().getH() - title.getHeight()) / 2);
		batchEnd();
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if (getGameApplication().isScreenCached(MenuScreen.CACHE_NAME)) {
			setChangeScreenOnNextRender(MenuScreen.CACHE_NAME);
		} else {
			MenuScreen ms = new MenuScreen(getGameApplication());
			setChangeScreenOnNextRender(ms);
		}

		return true;
	}

	@Override
	public boolean keyDown(int k) {
		if (k == Input.Keys.ESCAPE) {
			Gdx.app.exit();
		}

		return false;
	}
}
