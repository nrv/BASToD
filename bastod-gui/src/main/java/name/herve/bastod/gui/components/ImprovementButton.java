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

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import name.herve.bastod.engine.BASToDEngine;
import name.herve.bastod.engine.BASToDPlayer;
import name.herve.bastod.engine.Improvement;
import name.herve.bastod.gui.screen.game.PlayerManager;
import name.herve.game.gui.GUIResources;
import name.herve.game.gui.buttons.ImageButton;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class ImprovementButton extends ImageButton {
	public final static String IMPROVEMENT_BORDER = "ImprovementButton:border";

	public final static Map<String, String> IMP_TEXTURE;

	static {
		IMP_TEXTURE = new HashMap<>();
		IMP_TEXTURE.put(BASToDEngine.IMP_BUY_TOWER, "tower");
		IMP_TEXTURE.put(BASToDEngine.IMP_BUY_WALL, "wall");
		IMP_TEXTURE.put(BASToDEngine.IMP_MORE_METAL, "more_metal");
		IMP_TEXTURE.put(BASToDEngine.IMP_INCREASE_SPEED, "increase_speed");
	}

	private Improvement improvement;
	private BASToDPlayer player;
	private BitmapFont font;

	public ImprovementButton(Improvement imp, BASToDPlayer p, int x, int y) {
		super(imp.getName() + PlayerManager.NAME_SEPARATOR + p.getColor(), GUIResources.getInstance().getSprite(IMP_TEXTURE.get(imp.getName()), p.getColor()), -1, x, y, -1, -1,
				GUIResources.getInstance().getSprite(IMPROVEMENT_BORDER));

		player = p;
		improvement = imp;

		font = GUIResources.getInstance().getFont(GUIResources.FONT_SMALL_WHITE);

		setBounds(getBackground().getWidth(), (int) Math.ceil(getBackground().getHeight() + SMALL_SPACER + getBounds(font, "0").height));
		setyOffset(0);
	}

	@Override
	public void activated(int button, boolean hotkey) {
		// Done by listeners
	}

	@Override
	public void drawText() {
		draw(font, "$" + improvement.getCost(player), VerticalAlignment.TOP, HorizontalAlignment.CENTER);
	}

	public Improvement getImprovement() {
		return improvement;
	}

	public BASToDPlayer getPlayer() {
		return player;
	}

	@Override
	public String toString() {
		return "ImprovementButton [improvement=" + improvement + ", player=" + player + "]";
	}
}
