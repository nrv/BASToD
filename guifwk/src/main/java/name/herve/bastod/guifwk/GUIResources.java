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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import name.herve.bastod.guifwk.buttons.CheckBox;
import name.herve.bastod.guifwk.buttons.ImageButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class GUIResources {
	private static GUIResources instance;
	
	public final static String DEFAULT_FONT = "font/arial.ttf";
	public final static int DEFAULT_FONT_SIZE = 20;
	public final static int SMALL_FONT_SIZE = 14;
	
	public final static String WHITE = "white";
	public final static String BLACK = "black";
	
	public final static String FONT_SMALL_WHITE = "small_white";
	public final static String FONT_STANDARD_WHITE = "standard_white";

	
	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-+.,*$():";
	public static final String SCORE_CHARACTERS = "0123456789-+.$";
	
	public static BitmapFont createFont(String file, int size, Color color) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(file));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		font.setColor(color);
		font.setUseIntegerPositions(true);
		font.setFixedWidthGlyphs(SCORE_CHARACTERS);
		return font;
	}
	
	public static GUIResources getInstance() {
		if (instance == null) {
			instance = new GUIResources();
			
			instance.addColor(WHITE, Color.WHITE);
			
			instance.addFont(FONT_STANDARD_WHITE, createFont(DEFAULT_FONT, DEFAULT_FONT_SIZE, Color.WHITE));
			instance.addFont(FONT_SMALL_WHITE, createFont(DEFAULT_FONT, SMALL_FONT_SIZE, Color.WHITE));
			
			instance.addColor(BLACK, Color.BLACK);
			
			instance.addTexture(CheckBox.TEXTURE_CHECKED, new Texture(Gdx.files.internal("checked.png")));
			instance.addTexture(CheckBox.TEXTURE_UNCHECKED, new Texture(Gdx.files.internal("unchecked.png")));
			instance.addTexture(ImageButton.TEXTURE_BORDER, new Texture(Gdx.files.internal("button.png")));
			instance.addTexture(ImageButton.TEXTURE_LONG_BORDER_BLUE, new Texture(Gdx.files.internal("blue-long-button.png")));
			instance.addTexture(ImageButton.TEXTURE_LONG_BORDER_RED, new Texture(Gdx.files.internal("red-long-button.png")));
			instance.addTexture(ImageButton.TEXTURE_LONG_BORDER_WHITE, new Texture(Gdx.files.internal("white-long-button.png")));
		}
		return instance;
	}

	private Map<String, Color> colors;
	private Map<String, BitmapFont> fonts;
	private Map<String, Texture> textures;

	public GUIResources() {
		super();
		textures = new HashMap<String, Texture>();
		colors = new HashMap<String, Color>();
		fonts = new HashMap<String, BitmapFont>();
	}
	
	public void addColor(String key, Color value) {
		colors.put(key, value);
	}
	
	public void addFont(String key, BitmapFont value) {
		fonts.put(key, value);
	}
	
	public void addTexture(String key, Texture value) {
		textures.put(key, value);
	}

	public Color getColor(String key) {
		return colors.get(key);
	}

	public Set<String> getColorNames() {
		return colors.keySet();
	}

	public BitmapFont getFont(String name) {
		return fonts.get(name);
	}
	
	public Texture getSprite(String name) {
		return getSprite(name, null, -1);
	}
	
	public Texture getSprite(String name, String color) {
		return getSprite(name, color, -1);
	}
	
	public Texture getSprite(String name, String color, int angle) {
		Texture res = null;
		
		if ((angle >= 0) && (color != null)) {
			res = textures.get(name + "-" + color + "-" + angle);
		}
		
		if ((res == null) && (color != null)) {
			res = textures.get(name + "-" + color);
		}
		
		if (res == null) {
			res = textures.get(name); 
		}
		
		return res;
	}
}
