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
package name.herve.bastod.engine.ai;

import java.util.ArrayList;
import java.util.List;

import name.herve.bastod.engine.BASToDGame;
import name.herve.bastod.engine.BASToDPlayer;
import name.herve.bastod.engine.BASToDPlayerAction;
import name.herve.bastod.engine.BASToDPlayerAction.Action;
import name.herve.bastod.engine.BASToDPlayerActionsProvider;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class ArtificialIntelligence implements BASToDPlayerActionsProvider {
	private BASToDPlayer me;
	private BASToDGame game;

	public ArtificialIntelligence(BASToDPlayer me, BASToDGame game) {
		super();
		this.game = game;
		this.me = me;
	}

	@Override
	public List<BASToDPlayerAction> getActions(long now) {
		List<BASToDPlayerAction> ret = null;

		if (!me.isSpawnEnabled()) {
			ret = new ArrayList<>();
			ret.add(new BASToDPlayerAction(me, Action.START_SPAWN));
		}

		return ret;
	}

	@Override
	public BASToDPlayer getPlayer() {
		return me;
	}

}
