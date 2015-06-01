package main;

import shared.Loader;
import shared.Mode;
import shared.Player;
import shared.ref;
import game.GameUpdater;
import game.GameDrawer;
import game.ImageHandler;

public class MainLoader extends Loader {
	// TODO Alle daten laden

	public void update() {
		switch (state) {

		case NEWGAME:// create players
			LoadingScreen.setup();
			ref.updater = new GameUpdater();

			state = State.STARTIMAGES;
			break;
		case STARTIMAGES:
			boolean b = ImageHandler.requestAllImages();
			if (b) {
				state = State.IMAGES;
			} else {
				state = State.ERROR;
			}
			break;
		case IMAGES:
			float f = game.ImageHandler.stateOfLoading();
			if (f < 0) {
				state = State.ERROR;
			} else if (f < 1) {
				LoadingScreen.setPercent(f);
			} else {
				state = State.MAP;
			}
			break;
		case MAP:
			ref.updater.map.setup();
			state = State.ENTITIES;
			break;
		case ENTITIES:// spawn entity-setup

			if (ClientHandler.SinglePlayer) {

				for (String key : ref.updater.player.keySet()) {
					Player p = ref.updater.player.get(key);
					for (int i = 0; i < 0; i++) {
						ref.updater.send("<spawn Ticul " + p.ip + " "
								+ ref.app.random(500) + " "
								+ ref.app.random(500) + " "
								+ ref.app.random(500) + " "
								+ ref.app.random(500));
					}
					ref.updater.send("<spawn TestBuilding " + p.ip + " "
							+ ref.app.random(500) + " " + ref.app.random(500));
					ref.updater.send("<spawn TestLab " + p.ip + " "
							+ ref.app.random(500) + " " + ref.app.random(500)
							+ " " + ref.app.random(500) + " "
							+ ref.app.random(500));
					ref.updater.send("<spawn Prunam " + p.ip + " "
							+ ref.app.random(500) + " " + ref.app.random(500));
				}
			}
			GameDrawer.setup();
			if (ClientHandler.SinglePlayer) {
				GameDrawer.godeye = true;
				GameDrawer.godhand = true;
			}
			state = State.WAIT;
			break;
		case WAIT:
			ref.updater.send("<ready " + ClientHandler.identification);
			break;
		case END:
			state = State.NEWGAME;
			((MainApp) ref.app).mode = Mode.GAME;
			System.out.println("Game Start");
			break;
		case ERROR:
			System.out.println("error");
			ref.app.stop();
			break;
		default:
			break;
		}
		LoadingScreen.update();
	}

	@Override
	public void startGame() {
		if (state == State.WAIT) {
			state = State.END;
		} else {
			System.out.println("game started to early: " + state);
		}
	}

	@Override
	public void tryStartGame() {
		// do nothing when multiplayer
		if (ClientHandler.SinglePlayer)
			startGame();
	}
}
