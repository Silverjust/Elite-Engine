package game;

import entity.Entity;
import shared.Player;
import shared.Updater.GameState;
import shared.ref;

public abstract class MapCode {
	protected Map map;

	public MapCode(Map map) {
		this.map = map;
	}

	public void setup() {
	}

	public void onGameStart() {// gamestart
	}

	public void onEntitySpawn(Entity e) {// gamestart
	}

	public void update() {
	}

	public void onEnd() {// gameend
	}

	public void dispose() {
	}

	/** @return boolean finished game, everyone won or lost */
	public boolean handleGameEnd(String[] c) {
		boolean finished = false;
		Player looserP;
		looserP = ref.updater.player.get(c[2]);
		if (c[1].equals("lost") && looserP.gameState != GameState.LOST) {
			looserP.gameState = GameState.LOST;
			if (looserP == ref.player) {
				ref.updater.gameState = GameState.LOST;
				ref.preGame.write("GAME", "you lost the game");
			} else {
				ref.preGame.write("GAME", looserP.getUser().name + " lost the game");

				int nPlayersInGame = 0;
				for (String key : ref.updater.player.keySet()) {
					Player player = ref.updater.player.get(key);
					if (player.gameState != GameState.LOST) {
						nPlayersInGame++;
					}
				}

				if (nPlayersInGame == 1) {
					Player lastPlayingPlayer = null;
					for (String key : ref.updater.player.keySet()) {
						Player player = ref.updater.player.get(key);
						if (player.gameState != GameState.LOST) {
							lastPlayingPlayer = player;
						}
					}
					if (lastPlayingPlayer != null)
						lastPlayingPlayer.gameState = GameState.WON;
					ref.updater.gameState = GameState.WON;
					if (ref.player != null)
						ref.player.gameState = GameState.WON;
					ref.preGame.write("GAME", "you win");
					finished=true;
				}
			}

		}

		return finished;
	}
}
