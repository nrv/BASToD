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

import name.herve.bastod.engine.Game;
import name.herve.bastod.engine.Player;
import name.herve.bastod.engine.PlayerAction;
import name.herve.bastod.engine.PlayerAction.Action;
import name.herve.bastod.engine.PlayerActionsProvider;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class ArtificialIntelligence implements PlayerActionsProvider {
	private Player me;
	private Game game;

	public ArtificialIntelligence(Player me, Game game) {
		super();
		this.game = game;
		this.me = me;
	}

	@Override
	public List<PlayerAction> getActions(long now) {
		List<PlayerAction> ret = null;

		if (!me.isSpawnEnabled()) {
			ret = new ArrayList<>();
			ret.add(new PlayerAction(me, Action.START_SPAWN));
		}

		return ret;
	}

	@Override
	public Player getPlayer() {
		return me;
	}

}
