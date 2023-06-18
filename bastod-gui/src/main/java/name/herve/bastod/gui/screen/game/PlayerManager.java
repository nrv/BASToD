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

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector3;

import name.herve.bastod.engine.BASToDEngine;
import name.herve.bastod.engine.BASToDPlayer;
import name.herve.bastod.engine.BASToDPlayerAction;
import name.herve.bastod.engine.BASToDPlayerAction.Action;
import name.herve.bastod.engine.players.HumanPlayer;
import name.herve.game.gui.AbstractDisplayManager;
import name.herve.game.gui.GUIEvent;
import name.herve.game.gui.GUIResources;
import name.herve.game.gui.buttons.GUIButtonListener;
import name.herve.game.gui.layout.ComponentsLineLayout;
import name.herve.game.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class PlayerManager extends AbstractDisplayManager implements GUIButtonListener {
	public enum State {
		BUY_TOWER, BUY_WALL, NOTHING
	}

	public final static String NAME_SEPARATOR = "!";
	public final static String NAME_START_STOP_SPAWN = "spawn";

	private int actingPlayer;
	private int cx;
	private int cy;
	private BASToDEngine engine;
	private float halfSQS;
	private Map<BASToDPlayer, PlayerInterface> interfaces;
	private boolean snapToGrid;
	private State state;

	public PlayerManager() {
		super();
		setSnapToGrid(true);
	}

	public void addHumanPlayer(HumanPlayer p, ComponentsLineLayout ptl, ComponentsLineLayout pml, boolean doSingleScoreBar) {
		PlayerInterface pi = new PlayerInterface(this, p, engine, getScreenHeight(), ptl, pml, doSingleScoreBar);
		p.setActionsProvider(pi);
		interfaces.put(p, pi);
	}

	@Override
	public void buttonActivated(GUIEvent event) {
		String[] params = event.getSource().getName().split(NAME_SEPARATOR);

		if (params[0].equals(NAME_START_STOP_SPAWN)) {
			BASToDPlayer player = engine.getPlayer(Integer.parseInt(params[1]));
			BASToDPlayerAction pa = new BASToDPlayerAction(player, player.isSpawnEnabled() ? Action.STOP_SPAWN : Action.START_SPAWN);
			interfaces.get(pa.getPlayer()).addAction(pa);
		} else if (params[0].equals(BASToDEngine.IMP_BUY_TOWER)) {
			actingPlayer = Integer.parseInt(params[1]);
			state = State.BUY_TOWER;
		} else if (params[0].equals(BASToDEngine.IMP_BUY_WALL)) {
			actingPlayer = Integer.parseInt(params[1]);
			state = State.BUY_WALL;
		} else if (params[0].startsWith(BASToDEngine._IMPROVE)) {
			BASToDPlayer player = engine.getPlayer(Integer.parseInt(params[1]));
			BASToDPlayerAction pa = new BASToDPlayerAction(player, Action.IMPROVE);
			pa.setParam(params[0]);
			interfaces.get(pa.getPlayer()).addAction(pa);
		}
	}

	public State getState() {
		return state;
	}

	public boolean keyDown(int k) {
		return false;
	}

	public boolean mouseMoved(int x, int y) {
		cx = x;
		cy = y;

		if ((state == State.BUY_TOWER) || (state == State.BUY_WALL)) {
			return true;
		}

		return false;
	}

	private Vector positionOnGrid(int x, int y) {
		// System.out.println("positionOnGrid("+x+", "+y+")");
		Vector3 touchPos = new Vector3();
		touchPos.set(x, y, 0);
		cameraUnproject(touchPos);
		Vector vm = new Vector(touchPos.x - BASToDEngine._SP_SIDE, touchPos.y - BASToDEngine._SP_BOTTOM);

		// if (vm.getX() < 0 || vm.getY() < 0) {
		// return null;
		// }

		vm = engine.fromBoardToGrid(vm);

		// System.out.println(" -> " + vm);

		return vm;
	}

	public void render() {
		for (PlayerInterface pi : interfaces.values()) {
			pi.render();
		}

		if (state == State.BUY_TOWER) {
			renderBuyTower(cx, cy, positionOnGrid(cx, cy), actingPlayer);
		}

		if (state == State.BUY_WALL) {
			renderBuyWall(cx, cy, positionOnGrid(cx, cy), actingPlayer);
		}
	}

	private void renderBuy(int x, int y, Vector pos, String sprite, int player) {
		// System.out.println("renderBuy " + pos);
		batchBegin();

		if ((pos != null) && engine.isOpenedBuildPosition(engine.getPlayer(player), pos)) {
			if (snapToGrid) {
				Vector v = engine.fromGridToBoard(pos);
				draw(GUIResources.getInstance().getSprite(sprite, engine.getPlayer(player).getColor()), (BASToDEngine._SP_SIDE + v.getXInt()) - halfSQS, (BASToDEngine._SP_BOTTOM + v.getYInt()) - halfSQS);
			} else {
				draw(GUIResources.getInstance().getSprite(sprite, engine.getPlayer(player).getColor()), x - halfSQS, getScreenHeight() - y - halfSQS);
			}
		} else {
			draw(GUIResources.getInstance().getSprite("noway"), x - halfSQS, getScreenHeight() - y - halfSQS);
		}

		batchEnd();
	}

	public void renderBuyTower(int x, int y, Vector pos, int p) {
		renderBuy(x, y, pos, "tower", p);
	}

	public void renderBuyWall(int x, int y, Vector pos, int p) {
		renderBuy(x, y, pos, "wall", p);
	}

	public void setEngine(BASToDEngine engine) {
		this.engine = engine;
	}

	public void setSnapToGrid(boolean snapToGrid) {
		this.snapToGrid = snapToGrid;
	}

	@Override
	public void start() {
		super.start();

		state = State.NOTHING;
		halfSQS = engine.getGridSquareSize() / 2f;

		interfaces = new HashMap<>();
	}

	@Override
	public void stop() {
		super.stop();

		interfaces = null;
	}

	public boolean touchDown(int x, int y, int pointer, int button) {
		cx = x;
		cy = y;

		for (PlayerInterface pi : interfaces.values()) {
			if (pi.touchDown(x, getScreenHeight() - y, pointer, button)) {
				return true;
			}
		}

		if (state == State.BUY_TOWER) {
			state = State.NOTHING;
			if (button == 0) {
				Vector pog = positionOnGrid(cx, cy);
				if (pog != null) {
					BASToDPlayerAction pa = new BASToDPlayerAction(engine.getPlayer(actingPlayer), Action.BUY_TOWER);
					pa.setPositionOnGrid(pog);
					interfaces.get(pa.getPlayer()).addAction(pa);
				}
			}
		}

		if (state == State.BUY_WALL) {
			state = State.NOTHING;
			if (button == 0) {
				Vector pog = positionOnGrid(cx, cy);
				if (pog != null) {
					BASToDPlayerAction pa = new BASToDPlayerAction(engine.getPlayer(actingPlayer), Action.BUY_WALL);
					pa.setPositionOnGrid(pog);
					interfaces.get(pa.getPlayer()).addAction(pa);
				}
			}
		}

		return false;
	}

	public void updateDisplayComponents() {
		for (PlayerInterface pi : interfaces.values()) {
			pi.updateDisplayComponents();
		}
	}
}
