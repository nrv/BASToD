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
package name.herve.game.engine.gpi;

import name.herve.game.engine.GameEngine;
import name.herve.game.engine.GameEngineListener;
import name.herve.game.engine.GameState;
import name.herve.game.engine.GamePlayerAction;

public abstract class DefaultGamePlayerInterface implements GamePlayerInterface, GameEngineListener {
	private boolean playerReady;
	private GameEngine localEngine;

	public DefaultGamePlayerInterface(GameEngine localEngine) {
		super();
		this.localEngine = localEngine;
	}

	@Override
	public void executePlayerAction(GamePlayerAction pa) {
		pa.setPlayerUuid(getPlayerUuid());
		pa = localEngine.executePlayerAction(pa);
	}

	@Override
	public long getCurrentTick() {
		return localEngine.getCurrentTick();
	}

	public GameEngine getEngine() {
		return localEngine;
	}

	@Override
	public abstract String getPlayerUuid();

	@Override
	public GameState getState() {
		return localEngine.getState();
	}

	@Override
	public boolean isGameStarted() {
		return localEngine.isGameStarted();
	}

	@Override
	public boolean isPlayerReady() {
		return playerReady;
	}

	public abstract void setPlayerReady();

	protected void setPlayerReady(boolean ready) {
		playerReady = ready;
	}

}
