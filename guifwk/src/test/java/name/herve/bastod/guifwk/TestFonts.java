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
package name.herve.bastod.guifwk;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.stbtt.TrueTypeFontFactory;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class TestFonts implements ApplicationListener {

	public void test(float f1, float f2, float f3, float f4, float f5) {
		BitmapFont font = TrueTypeFontFactory.createBitmapFont(Gdx.files.internal("font/arial.ttf"), "ABCDEFGHIJKLMNOPQRSTUVWXYZ", f1, f2, f3, f4, f5);

		TextBounds b = font.getBounds("NICOLAS");
		
		System.out.println(f1 + " " + f2+ " " + f3+ " " + f4+ " " + f5+ " --> " + b.width + " " + b.height);
		
		font.dispose();
	}

	
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.resizable = false;
		cfg.useGL20 = true;
		cfg.width = 1024;
		cfg.height = 768;

		new LwjglApplication(new TestFonts(), cfg);
	}



	@Override
	public void create() {
		for (int s = 1; s < 60; s++) {
			test(1024, 768, s, 1024, 768);
		}
		Gdx.app.exit();
		
	}



	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
