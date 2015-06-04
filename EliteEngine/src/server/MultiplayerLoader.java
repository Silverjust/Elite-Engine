package server;

import main.ClientHandler;
import game.ImageHandler;
import shared.Loader;
import shared.Mode;
import shared.Nation;
import shared.Player;
import shared.ref;

public class MultiplayerLoader extends Loader {

	ServerApp app;

	public MultiplayerLoader() {
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

			if (ClientHandler.singlePlayer) {
				int i = 0;
				for (String key : ref.updater.player.keySet()) {
					i++;
					Player p = ref.updater.player.get(key);
					if (i == 1) {
						ref.updater.send("<spawn AlienMainBuilding " + p.ip
								+ " " + 100 + " " + 800);
						ref.updater.send("<spawn KeritMine " + p.ip + " " + 35
								+ " " + 850);
					} else if (i == 2) {
						ref.updater.send("<spawn AlienMainBuilding " + p.ip
								+ " " + 700 + " " + 100);
						ref.updater.send("<spawn KeritMine " + p.ip + " " + 750
								+ " " + 35);
					}
					ref.updater.send("<spawn KeritMine " + p.ip + " "
							+ ref.app.random(200, 600) + " "
							+ ref.app.random(200, 600));
					ref.updater.send("<spawn Kerit " + p.ip + " "
							+ ref.app.random(200, 600) + " "
							+ ref.app.random(200, 600));
					ref.updater.send("<spawn Pax " + p.ip + " "
							+ ref.app.random(200, 600) + " "
							+ ref.app.random(200, 600));
					ref.updater.send("<spawn Arcanum " + p.ip + " "
							+ ref.app.random(200, 600) + " "
							+ ref.app.random(200, 600));
					ref.updater.send("<spawn Prunam " + p.ip + " "
							+ ref.app.random(200, 600) + " "
							+ ref.app.random(200, 600));
				}
			}
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
