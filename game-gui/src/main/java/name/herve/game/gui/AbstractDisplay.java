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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Align;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public abstract class AbstractDisplay {
	private SpriteBatch batch;
	private boolean started;

	public AbstractDisplay() {
		super();
		started = false;
	}

	public void batchBegin() {
		batch.begin();
	}

	protected void batchBegin(Matrix4 projectionMatrix) {
		batch.setProjectionMatrix(projectionMatrix);
		batch.begin();
	}

	public void batchEnd() {
		batch.end();
	}
	
	public void draw(BitmapFont font, CharSequence cs, float x, float y) {
		font.draw(batch, cs, x, y, 0, Align.left, false);
	}

	public void draw(Texture texture, float x, float y) {
		batch.draw(texture, x, y);
	}

	public boolean isStarted() {
		return started;
	}

	public void start() {
		batch = new SpriteBatch();
		started = true;
	}

	public void stop() {
		batch.dispose();
		batch = null;
		started = false;
	}
}
