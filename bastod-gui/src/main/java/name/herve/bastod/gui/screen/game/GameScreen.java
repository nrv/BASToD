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

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import name.herve.bastod.BASToDGUI;
import name.herve.bastod.engine.BASToDEngine;
import name.herve.bastod.engine.BASToDPlayer;
import name.herve.bastod.engine.Unit;
import name.herve.bastod.engine.buildings.Tower;
import name.herve.bastod.engine.players.HumanPlayer;
import name.herve.bastod.gui.components.DualScoreBar;
import name.herve.bastod.gui.components.SpeedTimeFPS;
import name.herve.bastod.gui.screen.menu.MenuScreen;
import name.herve.game.gui.AbstractComponent;
import name.herve.game.gui.AbstractGame;
import name.herve.game.gui.AbstractScreen;
import name.herve.game.gui.buttons.CheckBox;
import name.herve.game.gui.layout.ComponentsLineLayout;
import name.herve.game.gui.layout.ComponentsLineLayout.Spacing;
import name.herve.game.tools.Constants;
import name.herve.game.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class GameScreen extends AbstractScreen {

	private List<AbstractComponent> components;
	private int cx;
	private int cy;
	private int cz;
	private BASToDEngine engine;
	private OverlayManager overlayManager;
	private CheckBox showBuildPositions;
	private CheckBox showGrid;
	private CheckBox showRanges;
	private CheckBox showTargetPositions;
	private SpriteManager spriteManager;
	private PlayerManager playerManager;
	private float zf;
	private ComponentsLineLayout topLine;
	private ComponentsLineLayout middleLine;
	private ComponentsLineLayout bottomLine;

	private Unit selected;

	public GameScreen(AbstractGame sltd, BASToDEngine engine) {
		super(sltd);
		this.engine = engine;
	}

	private void initComponents() {
		components = new ArrayList<>();

		topLine = new ComponentsLineLayout();
		topLine.moveTo(0, getScreenHeight() - BASToDEngine._SP_TOP);
		topLine.setBounds(getScreenWidth(), BASToDEngine._SP_TOP);

		bottomLine = new ComponentsLineLayout();
		bottomLine.moveTo(0, 0);
		bottomLine.setBounds(getScreenWidth(), BASToDEngine._SP_BOTTOM / 2);

		middleLine = new ComponentsLineLayout();
		middleLine.moveTo(0, bottomLine.getHeight());
		middleLine.setBounds(getScreenWidth(), BASToDEngine._SP_BOTTOM - bottomLine.getHeight());

		int nbHuman = 0;
		for (BASToDPlayer p : engine.getPlayers()) {
			if (p instanceof HumanPlayer) {
				nbHuman++;
			}
		}

		if (engine.getPlayer(0) instanceof HumanPlayer) {
			ComponentsLineLayout ptl;
			ComponentsLineLayout pml;

			if (nbHuman == 1) {
				ptl = topLine;
				pml = middleLine;
			} else {
				ptl = new ComponentsLineLayout();
				ptl.setSpacing(Spacing.LEFT);
				ptl.setBounds((getScreenWidth() / 2) - 100, BASToDEngine._SP_TOP);
				topLine.addComponent(ptl);
				pml = new ComponentsLineLayout();
				pml.setSpacing(Spacing.LEFT);
				pml.setBounds(getScreenWidth() / 2, BASToDEngine._SP_BOTTOM - (BASToDEngine._SP_BOTTOM / 2));
				middleLine.addComponent(pml);
			}

			playerManager.addHumanPlayer((HumanPlayer) engine.getPlayer(0), ptl, pml, engine.isTowerDefenseGame());
		}

		if (!engine.isTowerDefenseGame()) {
			DualScoreBar db = null;
			db = new DualScoreBar(engine.getPlayer(0), engine.getPlayer(1), -1, -1, 200, BASToDEngine._SP_TOP);
			db.start();
			components.add(db);
			topLine.addComponent(db);
		}

		if (nbHuman == 1) {
			SpeedTimeFPS stf = new SpeedTimeFPS(engine, -1, -1, 250, 20);
			stf.start();
			components.add(stf);
			topLine.addComponent(stf);
		}

		if (engine.getPlayer(1) instanceof HumanPlayer) {
			ComponentsLineLayout ptl;
			ComponentsLineLayout pml;

			if (nbHuman == 1) {
				ptl = topLine;
				pml = middleLine;
			} else {
				ptl = new ComponentsLineLayout();
				ptl.setBounds((getScreenWidth() / 2) - 100, BASToDEngine._SP_TOP);
				ptl.setSpacing(Spacing.RIGHT);
				topLine.addComponent(ptl);
				pml = new ComponentsLineLayout();
				pml.setBounds(getScreenWidth() / 2, BASToDEngine._SP_BOTTOM - (BASToDEngine._SP_BOTTOM / 2));
				pml.setSpacing(Spacing.RIGHT);
				middleLine.addComponent(pml);
			}

			playerManager.addHumanPlayer((HumanPlayer) engine.getPlayer(1), ptl, pml, engine.isTowerDefenseGame());
		}

		showTargetPositions = new CheckBox("checkbox-show-target", "(F1) Show target", Input.Keys.F1, -1, -1);
		showTargetPositions.start();
		components.add(showTargetPositions);
		bottomLine.addComponent(showTargetPositions);

		showBuildPositions = new CheckBox("checkbox-show-buildpos", "(F2) Show build", Input.Keys.F2, -1, -1);
		showBuildPositions.start();
		components.add(showBuildPositions);
		bottomLine.addComponent(showBuildPositions);

		showRanges = new CheckBox("checkbox-show-ranges", "(F3) Show ranges", Input.Keys.F3, -1, -1);
		showRanges.start();
		components.add(showRanges);
		bottomLine.addComponent(showRanges);

		showGrid = new CheckBox("checkbox-show-grid", "(F4) Show grid", Input.Keys.F4, -1, -1);
		showGrid.start();
		components.add(showGrid);
		bottomLine.addComponent(showGrid);

		topLine.validate();
		middleLine.validate();
		bottomLine.validate();
	}

	@Override
	public boolean keyDown(int k) {
		if (!engine.isPaused()) {
			for (AbstractComponent c : components) {
				if (c.keyDown(k)) {
					return true;
				}
			}

			if (playerManager.keyDown(k)) {
				return true;
			}
		}

		if (k == Input.Keys.PLUS) {
			int s = (int) (engine.getSpeed() * 100);
			if (s >= 100) {
				s += 100;
			} else if (s >= 10) {
				s += 10;
			} else {
				s += 1;
			}

			s = Math.min(s, 1000);

			engine.setPaused(false);

			engine.setSpeed(s / 100f);
			return true;
		} else if (k == Input.Keys.MINUS) {
			int s = (int) (engine.getSpeed() * 100);
			if (s > 100) {
				s -= 100;
			} else if (s > 10) {
				s -= 10;
			} else {
				s -= 1;
			}

			s = Math.max(s, 0);

			engine.setPaused(s == 0);

			engine.setSpeed(s / 100f);
			return true;
		} else if (k == Input.Keys.F12) {
			engine.setPaused(!engine.isPaused());
			return true;
		} else if (k == Input.Keys.ESCAPE) {
			setChangeScreenOnNextRender(MenuScreen.CACHE_NAME);
			return true;
		}
		// else if (k == Input.Keys.L) {
		// if (state == State.DEBUG_LOS) {
		// state = State.NOTHING;
		// } else {
		// state = State.DEBUG_LOS;
		// }
		// return true;
		// }

		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		cx = x;
		cy = y;

		return playerManager.mouseMoved(x, y);
	}

	@Override
	public void renderFrame(float delta) {
		if (!engine.isStarted()) {
			for (BASToDPlayer p : engine.getPlayers()) {
				p.setSpawnEnabled(false);
			}
			engine.start();
			initComponents();
		}

		overlayManager.renderBackground();

		if (showGrid.isChecked()) {
			overlayManager.renderGrid();
		}

		if (showBuildPositions.isChecked()) {
			overlayManager.renderBuildPositions();
		}

		if (showRanges.isChecked()) {
			for (BASToDPlayer p : engine.getPlayers()) {
				for (Unit u : p.getUnits()) {
					if (u instanceof Tower) {
						overlayManager.renderUnitOverlay(u);
					}
				}
			}
		}

		spriteManager.renderSprites(showTargetPositions.isChecked());

		playerManager.updateDisplayComponents();

		for (AbstractComponent c : components) {
			c.render();
		}

		playerManager.render();

		if (selected != null) {
			if (!selected.isOnBoard()) {
				selected = null;
			}
			overlayManager.renderUnitInfoBox(selected);
		}

		engine.step((long) (delta * Constants.NANO));

		if (engine.isGameOver()) {
			for (BASToDPlayer p : engine.getPlayers()) {
				System.out.println(p + " : " + p.getStats());
			}
			setChangeScreenOnNextRender(MenuScreen.CACHE_NAME);
		}
	}

	@Override
	public void resize(int w, int h) {
		super.resize(w, h);

		cx = 0;
		cy = 0;
		cz = 0;
		zf = 1f;

		cameraZoom(zf);
	}

	@Override
	public boolean scrolled(int s) {
		if (BASToDGUI.ZOOM_AND_SCROLL_ACTIVATED) {
			cz -= s;
			zf = 1f + (cz / 100f);
			cameraZoom(zf);
			return true;
		}

		return false;
	}

	@Override
	public void start() {
		super.start();

		spriteManager = new SpriteManager();
		spriteManager.attach(this);
		spriteManager.setEngine(engine);
		spriteManager.start();

		overlayManager = new OverlayManager();
		overlayManager.attach(this);
		overlayManager.setEngine(engine);
		overlayManager.start();

		playerManager = new PlayerManager();
		playerManager.attach(this);
		playerManager.setEngine(engine);
		playerManager.start();
	}

	@Override
	public void stop() {
		super.stop();

		spriteManager.stop();
		spriteManager = null;

		overlayManager.stop();
		overlayManager = null;

		for (AbstractComponent c : components) {
			c.stop();
		}
		components = null;

		playerManager.stop();
		playerManager = null;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if (engine.isPaused()) {
			return false;
		}

		cx = x;
		cy = y;

		selected = null;

		for (AbstractComponent c : components) {
			if (c.touchDown(x, getScreenHeight() - y, pointer, button)) {
				return true;
			}
		}

		Vector3 touchPos = new Vector3();
		touchPos.set(x, y, 0);
		cameraUnproject(touchPos);

		Vector vm = new Vector(touchPos.x - BASToDEngine._SP_SIDE, touchPos.y - BASToDEngine._SP_BOTTOM);

		vm = engine.fromBoardToGrid(vm);

		if (vm != null) {
			for (BASToDPlayer p : engine.getPlayers()) {
				for (Unit u : p.getUnits()) {
					Vector vu = engine.fromBoardToGrid(u.getPositionOnBoard());
					if (vm.equals(vu)) {
						if (button == 1) {
							selected = u;
							return true;
						}
					}
				}
			}
		}

		return playerManager.touchDown(x, y, pointer, button);
	}

	@Override
	public boolean touchDragged(int x, int y, int arg2) {
		if (BASToDGUI.ZOOM_AND_SCROLL_ACTIVATED) {
			float dx = (cx - x) * zf;
			float dy = (y - cy) * zf;

			Vector2 tv = new Vector2(dx, dy);

			camera.translate(tv);

			cx = x;
			cy = y;

			return true;
		}

		return false;
	}

}
