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
package name.herve.bastod.gui.screen.game;

import java.util.HashMap;
import java.util.Map;

import name.herve.bastod.BASToD;
import name.herve.bastod.engine.Engine;
import name.herve.bastod.engine.Player;
import name.herve.bastod.engine.Shot;
import name.herve.bastod.engine.Unit;
import name.herve.bastod.engine.buildings.Factory;
import name.herve.bastod.engine.buildings.Target;
import name.herve.bastod.engine.buildings.Tower;
import name.herve.bastod.engine.buildings.Wall;
import name.herve.bastod.engine.units.Destructible;
import name.herve.bastod.engine.units.Mobile;
import name.herve.bastod.engine.units.Tank;
import name.herve.bastod.guifwk.AbstractDisplayManager;
import name.herve.bastod.guifwk.GUIResources;
import name.herve.bastod.tools.math.Dimension;
import name.herve.bastod.tools.math.Vector;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class SpriteManager extends AbstractDisplayManager {
	private int armorLeftStep;
	private Map<String, Map<Integer, Texture>> armorLeftTextures;
	private Map<Mobile, Texture> pathTextures;

	private Engine engine;

	public SpriteManager() {
		super();

		armorLeftStep = 32;

		if (BASToD.DRAW_PATH_ACTIVATED) {
			pathTextures = new HashMap<Mobile, Texture>();
		}
	}

	private Texture getSprite(Unit u) {
		if (u instanceof Wall) {
			return GUIResources.getInstance().getSprite("wall");
		}

		if (u instanceof Tank) {
			return GUIResources.getInstance().getSprite("tank", u.getPlayer().getColor(), (int) u.getAngle());
		}

		if (u instanceof Target) {
			return GUIResources.getInstance().getSprite("target", u.getPlayer().getColor());
		}

		if (u instanceof Tower) {
			return GUIResources.getInstance().getSprite("tower", u.getPlayer().getColor(), (int) u.getAngle());
		}

		if (u instanceof Factory) {
			return GUIResources.getInstance().getSprite("factory", u.getPlayer().getColor());
		}

		return null;
	}

	private Texture initArmorLeft(int step, Color c) {
		Blending bck = Pixmap.getBlending();
		Pixmap.setBlending(Blending.None);

		int sqs = engine.getGridSquareSize();

		Pixmap p = new Pixmap(sqs, sqs + 4, Pixmap.Format.RGBA8888);
		p.setColor(c);

		//p.drawRectangle(0, 0, sqs, 3);

		int w = step * sqs / armorLeftStep;
		p.fillRectangle(0, 0, w, 3);

		Texture t = new Texture(p);
		p.dispose();
		Pixmap.setBlending(bck);

		return t;
	}

	private void render(Shot s) {
		float x = Engine._SP_SIDE + s.getPositionOnBoard().getX();
		float y = Engine._SP_BOTTOM + s.getPositionOnBoard().getY();

		drawWithOffset(GUIResources.getInstance().getSprite("shot", s.getPlayer().getColor()), x, y);
	}

	private void renderPath(Mobile m) {
		if (!pathTextures.containsKey(m)) {
			Dimension dimB = engine.getBoardDimension();

			Blending bck = Pixmap.getBlending();
			Pixmap.setBlending(Blending.None);

			Pixmap p = new Pixmap(dimB.getW() + 1, dimB.getH() + 1, Pixmap.Format.RGBA8888);

			Color c = Color.GREEN.cpy();
			p.setColor(c);
			Vector current = m.getPlayer().getStartPositionOnBoard();
			for (Vector next : m.getUnsmoothedPath()) {
				p.drawLine(current.getXInt(), dimB.getH() - current.getYInt(), next.getXInt(), dimB.getH() - next.getYInt());
				current = next;
			}

			c = Color.YELLOW.cpy();
			p.setColor(c);
			current = m.getPlayer().getStartPositionOnBoard();
			for (Vector next : m.getPath()) {
				p.drawLine(current.getXInt(), dimB.getH() - current.getYInt(), next.getXInt(), dimB.getH() - next.getYInt());
				current = next;
			}

			pathTextures.put(m, new Texture(p));
			p.dispose();
			Pixmap.setBlending(bck);
		}

		draw(pathTextures.get(m), Engine._SP_SIDE, Engine._SP_BOTTOM);

		// tPath.dispose();
	}

	private void drawWithOffset(Texture texture, float x, float y) {
		draw(texture, x - texture.getWidth() / 2, y - texture.getHeight() / 2);
	}
	
	private void render(Unit u) {
		float x = Engine._SP_SIDE + u.getPositionOnBoard().getX();
		float y = Engine._SP_BOTTOM + u.getPositionOnBoard().getY();

		drawWithOffset(getSprite(u), x, y);

		if (u instanceof Destructible) {
			Destructible d = (Destructible) u;
			int step = d.getArmor() * armorLeftStep / d.getMaxArmor();
			draw(armorLeftTextures.get(u.getPlayer().getColor()).get(step), x, y);
		}

		if (BASToD.DRAW_PATH_ACTIVATED && u instanceof Mobile) {
			Mobile m = (Mobile) u;
			renderPath(m);
		}
	}

	public void renderSprites(boolean showTargets) {
		batchBegin();

		for (Unit u : engine.getBoardUnits()) {
			render(u);
		}

		for (Player p : engine.getPlayers()) {
			for (Unit u : p.getUnits()) {
				if (showTargets || !(u instanceof Target)) {
					render(u);
				}
			}
		}

		for (Shot s : engine.getShots()) {
			render(s);
		}

		batchEnd();
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	@Override
	public void start() {
		super.start();

		armorLeftTextures = new HashMap<String, Map<Integer, Texture>>();

		for (Player p : engine.getPlayers()) {
			if (!armorLeftTextures.containsKey(p.getColor())) {
				Map<Integer, Texture> local = new HashMap<Integer, Texture>();
				for (int i = 0; i <= armorLeftStep; i++) {
					local.put(i, initArmorLeft(i, GUIResources.getInstance().getColor(p.getColor())));
				}
				armorLeftTextures.put(p.getColor(), local);
			}
		}
	}

	@Override
	public void stop() {
		if (armorLeftTextures != null) {
			for (Map<Integer, Texture> local : armorLeftTextures.values()) {
				for (Texture t : local.values()) {
					t.dispose();
				}
			}
			armorLeftTextures = null;
		}
		super.stop();
	}

}
