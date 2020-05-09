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
package name.herve.bastod.guifwk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public abstract class AbstractScreen extends AbstractDisplay implements Screen, InputProcessor {
	private String cacheName;
	public OrthographicCamera camera;
	private boolean changeScreenOnNextRender;
	private AbstractGame game;
	private AbstractScreen nextScreen;
	private String nextScreenInCache;
	private int screenHeight;
	private int screenWidth;

	public AbstractScreen(AbstractGame game) {
		super();
		this.game = game;
		setCacheName(null);
	}

	@Override
	public void batchBegin() {
		super.batchBegin(cameraCombined());
	}

	protected Matrix4 cameraCombined() {
		return camera.combined;
	}

	public void cameraProject(Vector3 vec) {
		camera.project(vec);
	}

	public void cameraUnproject(Vector3 vec) {
		camera.unproject(vec);
	}

	public void cameraZoom(float z) {
		camera.zoom = z;
	}

	@Override
	public void dispose() {
	}

	public String getCacheName() {
		return cacheName;
	}

	public AbstractGame getGameApplication() {
		return game;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	@Override
	public void hide() {
	}

	public boolean isCached() {
		return getCacheName() != null;
	}

	public boolean isChangeScreenOnNextRender() {
		return changeScreenOnNextRender;
	}

	@Override
	public boolean keyDown(int arg0) {
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		return false;
	}

	@Override
	public boolean keyUp(int arg0) {
		return false;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		return false;
	}

	@Override
	public void pause() {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		renderFrame(delta);

		if (isChangeScreenOnNextRender()) {
			if (nextScreenInCache != null) {
				getGameApplication().setScreen(nextScreenInCache);
			} else {
				getGameApplication().setScreen(nextScreen);
			}
		}
	}

	public abstract void renderFrame(float delta);

	@Override
	public void resize(int w, int h) {
		screenWidth = w;
		screenHeight = h;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, screenWidth, screenHeight);
	}

	@Override
	public void resume() {
	}

	@Override
	public boolean scrolled(int arg0) {
		return false;
	}

	protected void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	public void setChangeScreenOnNextRender(AbstractScreen nextScreen) {
		changeScreenOnNextRender = true;
		nextScreenInCache = null;
		this.nextScreen = nextScreen;
	}

	public void setChangeScreenOnNextRender(String nextScreenInCache) {
		changeScreenOnNextRender = true;
		this.nextScreenInCache = nextScreenInCache;
		nextScreen = null;
	}

	@Override
	public void show() {
		unsetChangeScreenOnNextRender();
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		return false;
	}

	public void unsetChangeScreenOnNextRender() {
		changeScreenOnNextRender = false;
	}
}
