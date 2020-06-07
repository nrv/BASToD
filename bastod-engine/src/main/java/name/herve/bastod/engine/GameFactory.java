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
package name.herve.bastod.engine;

import java.util.List;

import name.herve.bastod.engine.Game.Type;
import name.herve.bastod.engine.ai.ArtificialIntelligence;
import name.herve.bastod.engine.buildings.Factory;
import name.herve.bastod.engine.buildings.Target;
import name.herve.bastod.engine.buildings.Tower;
import name.herve.bastod.engine.improvements.BuyFactoryImprovement;
import name.herve.bastod.engine.improvements.BuyTowerImprovement;
import name.herve.bastod.engine.improvements.BuyWallImprovement;
import name.herve.bastod.engine.improvements.IncreaseSpeedImprovement;
import name.herve.bastod.engine.improvements.MoreMetalImprovement;
import name.herve.bastod.engine.players.ComputerPlayer;
import name.herve.bastod.engine.towerdefense.TowerDefenseGame;
import name.herve.game.tools.GameException;
import name.herve.game.tools.conf.Configuration;
import name.herve.game.tools.math.Vector;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class GameFactory {
	public static Game createGame(Type type, Configuration conf, long seed) throws GameException {
		Player[] players = new Player[2];

		for (int i = 0; i < 2; i++) {
			players[i] = new ComputerPlayer(i);
		}

		BoardFactory boardFactory = new BoardFactory();
		boardFactory.setSeed(seed);

		// Board board = boardFactory.getRandomBoard(seed);
		Board board = boardFactory.getCustomBoard();
		// Board board = boardFactory.getEmptyBoard();
		// Board board = boardFactory.loadMap("map_04");

		return createGame(type, conf, players, board);
	}

	public static Game createGame(Type type, Configuration conf, Player[] players, Board board) throws GameException {
		Game game = new Game(type, conf);

		initImprovements(game);

		game.setBoard(board);

		for (int i = 0; i < players.length; i++) {
			players[i].setMetal(conf.getInt(Engine.CF_GAME_METAL_INITIAL_I));
			players[i].setMaxMetal(conf.getInt(Engine.CF_GAME_METAL_MAX_I));
			players[i].setMaxScore(conf.getInt(Engine.CF_GAME_INITIAL_SCORE_I));

			players[i].setStartPositionOnBoard(board.fromGridToBoard(board.getStartPosition(i)));

			if (conf.getBoolean(Engine.CF_GAME_INITIAL_FACTORY_B)) {
				Factory f = ((BuyFactoryImprovement) game.getAvailableImprovement(Engine.IMP_BUY_FACTORY)).createFactory(players[i].getStartPositionOnBoard().copy());
				players[i].addUnit(f);
			}

			if (conf.getBoolean(Engine.CF_GAME_INITIAL_TOWER_B)) {
				List<Vector> towers = game.getBoard().getTowerPositions(players[i]);
				if (towers != null) {
					for (Vector t : towers) {
						Tower tw = ((BuyTowerImprovement) game.getAvailableImprovement(Engine.IMP_BUY_TOWER)).createTower(board.fromGridToBoard(t));
						players[i].addUnit(tw);
					}
				}
			}

			List<Vector> potentialTargets = game.getBoard().getEndPositions(players[i]);
			for (Vector t : potentialTargets) {
				Target target = new Target();
				target.setPositionOnBoard(board.fromGridToBoard(t));
				players[i].addUnit(target);
			}

			game.addPlayer(players[i]);
		}

		players[0].setEnemy(players[1]);
		players[1].setEnemy(players[0]);

		players[0].setColor(Player.PLAYER_RED);
		players[1].setColor(Player.PLAYER_BLUE);

		// players[0].setMetalMultiplier(3f);
		// System.out.println("TEST - " + players[0] + " has metal bonus " +
		// players[0].getMetalMultiplier());
		// players[1].setMetalMultiplier(0f);
		// players[1].setMetal(0);
		// System.out.println("TEST - " + players[1] + " has no metal");

		for (Player p : players) {
			if (p instanceof ComputerPlayer) {
				PlayerActionsProvider pap = null;
				if (game.getType() == Type.TOWER_DEFENSE) {
					pap = new TowerDefenseGame(p, game);
				} else {
					pap = new ArtificialIntelligence(p, game);
				}

				((ComputerPlayer) p).initAI(pap);
			}
		}

		return game;
	}

	private static void initImprovements(Game game) throws GameException {
		game.addAvailableImprovement(new BuyTowerImprovement(game.getConf()));
		game.addAvailableImprovement(new BuyWallImprovement(game.getConf()));
		game.addAvailableImprovement(new BuyFactoryImprovement(game.getConf()));
		game.addAvailableImprovement(new MoreMetalImprovement(game.getConf()));
		game.addAvailableImprovement(new IncreaseSpeedImprovement(game.getConf()));
	}
}
