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
package name.herve.bastod.gui.screen.menu;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;

import name.herve.bastod.engine.BASToDEngine;
import name.herve.bastod.engine.BASToDGame;
import name.herve.bastod.engine.BASToDGame.Type;
import name.herve.bastod.engine.BASToDGameFactory;
import name.herve.bastod.engine.BASToDPlayer;
import name.herve.bastod.engine.Board;
import name.herve.bastod.engine.BoardFactory;
import name.herve.bastod.engine.players.ComputerPlayer;
import name.herve.bastod.engine.players.HumanPlayer;
import name.herve.bastod.gui.screen.game.GameScreen;
import name.herve.bastod.gui.screen.title.TitleScreen;
import name.herve.game.gui.AbstractComponent;
import name.herve.game.gui.AbstractGame;
import name.herve.game.gui.AbstractScreen;
import name.herve.game.gui.GUIEvent;
import name.herve.game.gui.buttons.GUIButtonListener;
import name.herve.game.gui.buttons.SelectorButton;
import name.herve.game.gui.buttons.TextButton;
import name.herve.game.tools.GameException;
import name.herve.game.tools.conf.Configuration;

// TODO regarder http://steigert.blogspot.fr/

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class MenuScreen extends AbstractScreen implements GUIButtonListener {
	public static final String CACHE_NAME = "menu";
	private static final String START_NAME = "start";
	private static final String GAME_TYPE_NAME = "gametype";

	private List<AbstractComponent> components;
	private SelectorButton<Type> gt;
	private SelectorButton<String> msb;
	private HumanOrComputerButton p1;
	private HumanOrComputerButton p2;
	private BoardFactory boardFactory;

	public MenuScreen(AbstractGame game) {
		super(game);
		setCacheName(CACHE_NAME);
		boardFactory = new BoardFactory();
	}

	@Override
	public void buttonActivated(GUIEvent event) {
		if (event.getSource().getName().equals(START_NAME)) {
			try {
				getGameApplication().setGameConf(Configuration.load(gt.getSelected().getFile()));
				long seed = System.currentTimeMillis();
				boardFactory.setSeed(seed);

				BASToDPlayer[] players = new BASToDPlayer[2];

				if (p1.isHuman()) {
					players[0] = new HumanPlayer(0);
				} else {
					players[0] = new ComputerPlayer(0);
				}

				if (p2.isHuman()) {
					players[1] = new HumanPlayer(1);
				} else {
					players[1] = new ComputerPlayer(1);
				}

				Board board = boardFactory.loadMap(msb.getText());

				BASToDGame game = BASToDGameFactory.createGame(gt.getSelected(), getGameApplication().getGameConf(), players, board);
				BASToDEngine engine = new BASToDEngine(seed);
				engine.setGame(game);
				GameScreen gs = new GameScreen(getGameApplication(), engine);
				setChangeScreenOnNextRender(gs);
			} catch (GameException e) {
				Gdx.app.error("conf", e.getMessage());
				Gdx.app.exit();
			}
		} else if (event.getSource().getName().equals(GAME_TYPE_NAME)) {
			switch (gt.getSelected()) {
			case TOWER_DEFENSE:
				p1.setEnabled(false);
				p1.setHuman();
				p2.setEnabled(false);
				p2.setComputer();
				break;
			case TWO_PLAYERS:
				p1.setEnabled(true);
				p2.setEnabled(true);
				break;
			}
		}
	}

	private void initComponents() {
		int w = getScreenWidth();
		int h = getScreenHeight();

		components = new ArrayList<>();

		gt = new SelectorButton<>(GAME_TYPE_NAME, "Choose a game type : ", BASToDGame.Type.values(), (w - 160) / 2, (h / 2) + 100);
		gt.start();
		gt.addListener(this);
		components.add(gt);

		p1 = new HumanOrComputerButton(BASToDPlayer.PLAYER_RED, BASToDEngine._SP_SIDE + 50, h / 2);
		p1.setHuman();
		p1.start();
		components.add(p1);

		p2 = new HumanOrComputerButton(BASToDPlayer.PLAYER_BLUE, w - BASToDEngine._SP_SIDE - 210, h / 2);
		p2.setComputer();
		p2.start();
		components.add(p2);

		if (gt.getSelected() == Type.TOWER_DEFENSE) {
			p1.setEnabled(false);
			p2.setEnabled(false);
		}

		TextButton start = new TextButton(START_NAME, "Launch game", (w - 160) / 2, (h / 2) - 100);
		start.addListener(this);
		start.start();
		components.add(start);

		try {
			String[] maps = boardFactory.getAvailableMaps(true).toArray(new String[] {});
			msb = new SelectorButton<>("MapSelectorButton", "Choose a map : ", maps, (w - 160) / 2, (h / 2) - 50);
			msb.start();
			components.add(msb);
		} catch (GameException e) {
			Gdx.app.error("maps", e.getMessage());
			Gdx.app.exit();
		}
	}

	@Override
	public boolean keyDown(int k) {
		if (k == Input.Keys.ESCAPE) {
			setChangeScreenOnNextRender(TitleScreen.CACHE_NAME);
		}

		return false;
	}

	@Override
	public void renderFrame(float delta) {
		if (components == null) {
			initComponents();
		}

		for (AbstractComponent c : components) {
			c.render();
		}
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		Vector3 touchPos = new Vector3();
		touchPos.set(x, y, 0);
		cameraUnproject(touchPos);

		for (AbstractComponent c : components) {
			if (c.touchDown(x, getScreenHeight() - y, pointer, button)) {
				return true;
			}
		}

		return false;
	}
}
