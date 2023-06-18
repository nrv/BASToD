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
package name.herve.bastod.gui.screen.game;

import java.util.ArrayList;
import java.util.List;

import name.herve.bastod.engine.BASToDEngine;
import name.herve.bastod.engine.BASToDPlayer;
import name.herve.bastod.engine.BASToDPlayerAction;
import name.herve.bastod.engine.BASToDPlayerActionsProvider;
import name.herve.bastod.engine.Improvement;
import name.herve.bastod.gui.components.ImprovementButton;
import name.herve.bastod.gui.components.MetalBar;
import name.herve.bastod.gui.components.SingleScoreBar;
import name.herve.game.gui.AbstractComponent;
import name.herve.game.gui.buttons.CheckBox;
import name.herve.game.gui.layout.ComponentsLineLayout;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class PlayerInterface implements BASToDPlayerActionsProvider {
	private List<BASToDPlayerAction> actions;

	private CheckBox btSpawn;
	private List<AbstractComponent> components;
	private List<ImprovementButton> improvements;
	private BASToDPlayer p;

	public PlayerInterface(PlayerManager playerManager, BASToDPlayer p, BASToDEngine engine, int screenHeight, ComponentsLineLayout ptl, ComponentsLineLayout pml, boolean doSingleScoreBar) {
		super();
		this.p = p;

		improvements = new ArrayList<>();
		components = new ArrayList<>();

		MetalBar mb = new MetalBar(engine, p, -1, -1, 200, BASToDEngine._SP_TOP);
		mb.start();
		components.add(mb);
		ptl.addComponent(mb);

		if (doSingleScoreBar) {
			SingleScoreBar sb = new SingleScoreBar(p, -1, -1, 200, BASToDEngine._SP_TOP);
			sb.start();
			components.add(sb);
			ptl.addComponent(sb);
		}

		if (!engine.isTowerDefenseGame()) {
			btSpawn = new CheckBox(PlayerManager.NAME_START_STOP_SPAWN + PlayerManager.NAME_SEPARATOR + p.getBoardIndex(), "Spawning", -1, -1);
			btSpawn.addListener(playerManager);
			btSpawn.start();
			btSpawn.setChecked(p.isSpawnEnabled());
			components.add(btSpawn);
			pml.addComponent(btSpawn);
		}

		for (Improvement imp : engine.getAvailableImprovements()) {
			if (imp.isAvailableForPlayer(p)) {
				ImprovementButton btImp = new ImprovementButton(imp, p, -1, -1);
				btImp.addListener(playerManager);
				btImp.start();
				components.add(btImp);
				improvements.add(btImp);
				pml.addComponent(btImp);
			}
		}
	}

	public boolean addAction(BASToDPlayerAction e) {
		if (actions == null) {
			actions = new ArrayList<>();
		}
		return actions.add(e);
	}

	@Override
	public List<BASToDPlayerAction> getActions(long now) {
		List<BASToDPlayerAction> ret = actions;
		actions = null;
		return ret;
	}

	@Override
	public BASToDPlayer getPlayer() {
		return p;
	}

	public void render() {
		for (AbstractComponent c : components) {
			c.render();
		}
	}

	public boolean touchDown(int x, int ry, int pointer, int button) {
		for (AbstractComponent c : components) {
			if (c.touchDown(x, ry, pointer, button)) {
				return true;
			}
		}

		return false;
	}

	public void updateDisplayComponents() {
		for (ImprovementButton imp : improvements) {
			imp.setEnabled(imp.getImprovement().isAffordableForPlayer(imp.getPlayer()));
		}
	}

}
