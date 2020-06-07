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
package name.herve.bastod;

import java.text.DecimalFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import name.herve.bastod.engine.Engine;
import name.herve.bastod.engine.Player;
import name.herve.bastod.gui.components.ImprovementButton;
import name.herve.bastod.gui.components.UnitInfoBox;
import name.herve.bastod.gui.screen.title.TitleScreen;
import name.herve.game.gui.AbstractGame;
import name.herve.game.gui.GUIResources;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class BASToD extends AbstractGame {
	public final static boolean ZOOM_AND_SCROLL_ACTIVATED = false;
	public final static boolean DRAW_PATH_ACTIVATED = false;

	public static void main(String[] args) {
		BASToD sltd = new BASToD(Engine._VIEWPORT_WIDTH, Engine._VIEWPORT_HEIGHT);

		Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
		cfg.setWindowedMode(sltd.getW(), sltd.getH());
		cfg.setDecorated(true);
		cfg.useVsync(true);
		cfg.setTitle("SLTD");
		cfg.setResizable(false);
		// cfg.resizable = false;
		// cfg.title = "SLTD";
		// cfg.useGL30 = true;
		// cfg.width = sltd.getW();
		// cfg.height = sltd.getH();

		new Lwjgl3Application(sltd, cfg);
	}

	public BASToD(int w, int h) {
		super(w, h);
	}

	@Override
	public void create() {
		initGUIResources(Engine._SQUARE_SIZE);
		setScreen(new TitleScreen(this));
	}

	private void initGUIResources(int sqs) {
		GUIResources r = GUIResources.getInstance();
		DecimalFormat blender = new DecimalFormat("0000");

		String set = "bricks";
		String view = "top";

		for (int a = 0; a < 360; a++) {
			r.addTexture("tank-red-" + a, new Texture(Gdx.files.internal(sqs + "/3d/" + set + "/" + view + "/tank/red/" + blender.format(a + 1) + ".png")));
			r.addTexture("tank-blue-" + a, new Texture(Gdx.files.internal(sqs + "/3d/" + set + "/" + view + "/tank/blue/" + blender.format(a + 1) + ".png")));

			r.addTexture("tower-red-" + a, new Texture(Gdx.files.internal(sqs + "/3d/" + set + "/" + view + "/tower/red/" + blender.format(a + 1) + ".png")));
			r.addTexture("tower-blue-" + a, new Texture(Gdx.files.internal(sqs + "/3d/" + set + "/" + view + "/tower/blue/" + blender.format(a + 1) + ".png")));
		}

		r.addTexture("tower-red", new Texture(Gdx.files.internal(sqs + "/3d/" + set + "/" + view + "/tower/red/" + blender.format(50) + ".png")));
		r.addTexture("tower-blue", new Texture(Gdx.files.internal(sqs + "/3d/" + set + "/" + view + "/tower/blue/" + blender.format(50) + ".png")));

		r.addTexture("factory-red", new Texture(Gdx.files.internal(sqs + "/3d/" + set + "/" + view + "/factory/red/" + blender.format(0) + ".png")));
		r.addTexture("factory-blue", new Texture(Gdx.files.internal(sqs + "/3d/" + set + "/" + view + "/factory/blue/" + blender.format(0) + ".png")));

		r.addTexture("target-red", new Texture(Gdx.files.internal(sqs + "/3d/" + set + "/" + view + "/target0/red/" + blender.format(0) + ".png")));
		r.addTexture("target-blue", new Texture(Gdx.files.internal(sqs + "/3d/" + set + "/" + view + "/target0/blue/" + blender.format(0) + ".png")));

		r.addTexture("wall", new Texture(Gdx.files.internal(sqs + "/3d/" + set + "/" + view + "/wall/" + blender.format(0) + ".png")));

		r.addTexture("shot-red", new Texture(Gdx.files.internal(sqs + "/shot-red.png")));
		r.addTexture("shot-blue", new Texture(Gdx.files.internal(sqs + "/shot-blue.png")));

		r.addTexture(ImprovementButton.IMPROVEMENT_BORDER, new Texture(Gdx.files.internal(sqs + "/improvement.png")));

		r.addTexture("noway", new Texture(Gdx.files.internal(sqs + "/noway.png")));
		r.addTexture("more_metal", new Texture(Gdx.files.internal(sqs + "/more_metal.png")));
		r.addTexture("increase_speed", new Texture(Gdx.files.internal(sqs + "/increase_speed.png")));
		r.addTexture("title", new Texture(Gdx.files.internal("title.png")));
		r.addTexture("background", new Texture(Gdx.files.internal("background.png")));

		r.addColor(Player.PLAYER_RED, Color.RED);
		r.addColor(Player.PLAYER_BLUE, Color.BLUE);

		r.addFont(Player.PLAYER_RED, GUIResources.createFont(GUIResources.DEFAULT_FONT, GUIResources.DEFAULT_FONT_SIZE, GUIResources.getInstance().getColor(Player.PLAYER_RED)));
		r.addFont(Player.PLAYER_BLUE, GUIResources.createFont(GUIResources.DEFAULT_FONT, GUIResources.DEFAULT_FONT_SIZE, GUIResources.getInstance().getColor(Player.PLAYER_BLUE)));
		r.addFont(UnitInfoBox.INFOBOX_FONT, GUIResources.createFont(GUIResources.DEFAULT_FONT, GUIResources.SMALL_FONT_SIZE, Color.YELLOW));
	}

}
