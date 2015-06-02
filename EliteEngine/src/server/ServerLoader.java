package server;

import game.ImageHandler;
import shared.Loader;
import shared.Mode;
import shared.Nation;
import shared.Player;
import shared.ref;

public class ServerLoader extends Loader {

	ServerApp app;

	public ServerLoader() {
		app = (ServerApp) ref.app;
	}

	@Override
	public void update() {
		switch (state) {

		case NEWGAME:
			// TODO remove, when all nations are stable
			for (String key : ref.preGame.player.keySet()) {
				ref.preGame.player.get(key).nation = Nation.ALIENS;
			}
			ref.updater = new ServerUpdater();
			state = State.STARTIMAGES;// map
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
			} else {
				state = State.MAP;
			}
			break;
		case MAP:
			ref.updater.map.setup();
			state = State.WAIT;
			break;
		case WAIT:
			break;
		case ENTITIES:// spawn entity-setup

			for (String key : ref.updater.player.keySet()) {
				Player p = ref.updater.player.get(key);
				for (int i = 0; i < 0; i++) {
					ref.updater.send("<spawn Ticul " + p.ip + " "
							+ ref.app.random(500) + " " + ref.app.random(500)
							+ " " + ref.app.random(500) + " "
							+ ref.app.random(500));
				}
				ref.updater.send("<spawn AlienMainBuilding " + p.ip + " "
						+ ref.app.random(500) + " " + ref.app.random(500));
				ref.updater.send("<spawn TestLab " + p.ip + " "
						+ ref.app.random(500) + " " + ref.app.random(500) + " "
						+ ref.app.random(500) + " " + ref.app.random(500));
				ref.updater.send("<spawn Prunam " + p.ip + " "
						+ ref.app.random(500) + " " + ref.app.random(500));
			}
			/*
			 * for (int i = 0; i < 10; i++) { ref.updater.send("<spawn Ticul " +
			 * 1 + " " + ref.app.random(1000) + " " + ref.app.random(1000) + " "
			 * + ref.app.random(1000) + " " + ref.app.random(1000)); } for (int
			 * i = 0; i < 10; i++) { ref.updater.send("<spawn Ticul " + 0 + " "
			 * + ref.app.random(1000) + " " + ref.app.random(1000) + " " +
			 * ref.app.random(1000) + " " + ref.app.random(1000)); } /*
			 * GameClient.send("<spawn BigTicul " + 0 + " " +
			 * ref.app.random(1000) + " " + ref.app.random(1000) + " " +
			 * ref.app.random(1000) + " " + ref.app.random(1000));
			 */
			/*
			 * ref.updater.send("<spawn TestBuilding 0 " + ref.app.random(1000)
			 * + " " + ref.app.random(1000));
			 * ref.updater.send("<spawn TestLab 0 " + ref.app.random(1000) + " "
			 * + ref.app.random(1000) + " " + ref.app.random(1000) + " " +
			 * ref.app.random(1000));
			 * 
			 * ref.updater.send("<spawn Prunam 0 " + ref.app.random(1000) + " "
			 * + ref.app.random(1000));
			 */
			state = State.END;
			break;
		case END:
			// ServerDisplay.main(new String[] {});
			state = State.NEWGAME;
			((ServerApp) ref.app).mode = Mode.GAME;
			((ServerApp) ref.app).serverHandler.send("<startGame");
			System.out.println("Game Start");
			break;
		case ERROR:
			System.out.println("error");
			ref.app.stop();
			break;
		default:
			break;
		}
	}

	@Override
	public void startGame() {

	}

	@Override
	public void tryStartGame() {
		if (state == State.WAIT && ref.updater.arePlayerReady()) {
			state = State.ENTITIES;
		}
	}
}
