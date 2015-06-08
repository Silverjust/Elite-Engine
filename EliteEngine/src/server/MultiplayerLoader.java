package server;

import game.ImageHandler;
import game.MapHandler;
import shared.Loader;
import shared.Mode;
import shared.Nation;
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
			MapHandler.setupEntities(ref.updater.map.mapData);
			state = State.END;
			break;
		case END:
			// ServerDisplay.main(new String[] {});
			state = State.NEWGAME;
			app.mode = Mode.GAME;
			app.serverHandler.send("<startGame");
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
