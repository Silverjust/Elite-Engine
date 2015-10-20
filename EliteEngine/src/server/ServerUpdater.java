package server;

import entity.Entity;
import entity.Unit;
import game.Map;

import java.util.ArrayList;

import shared.ComHandler;
import shared.Player;
import shared.Updater;
import shared.ref;

public class ServerUpdater extends Updater {
	public ServerUpdater() {
		for (String key : ref.preGame.users.keySet()) {
			player.put(key, Player.createPlayer(ref.preGame.users.get(key)));
		}
		neutral = Player.createNeutralPlayer();

		map = new Map(ref.preGame.map);
	}

	@Override
	public void update() {
		if (gameState == GameState.PLAY) {
			for (int i = 0; i < toAdd.size(); i++) {
				Entity.entityCounter += 1;
				entities.add(toAdd.get(i));
				namedEntities.put(Entity.entityCounter, toAdd.get(i));
				toAdd.get(i).number = Entity.entityCounter;
				toAdd.get(i).onSpawn(true);
				toAdd.remove(i);
			}
			for (int i = 0; i < toRemove.size(); i++) {
				if (toRemove.get(i) != null) {
					int n = toRemove.get(i).number;
					namedEntities.remove(n);
					selected.remove(toRemove.get(i));
					entities.remove(toRemove.get(i));
					toRemove.remove(i);
					// System.out.println("removed " + n);
				}
			}
			if (ref.app.frameCount % 1000 == 0) {
				ref.updater.send("<say SERVER " + "sync");
				for (Entity e : entities) {
					if (e instanceof Unit) {
						ref.updater.send("<tp " + e.number + " " + e.x + " " + e.y + " false");
					}
				}
			}

			for (String key : player.keySet()) {
				player.get(key).visibleEntities.clear();
				for (Entity e : entities) {
					if (e.isVisibleTo(player.get(key))) {
						player.get(key).visibleEntities.add(e);
					}
				}
			}
			map.mapCodeUpdate();
			for (Entity e : entities) {
				e.updateAnimation();
				e.updateDecisions(true);
				e.updateMovement();
			}
		}
	}

	@Override
	public void send(String string) {
		ComHandler.executeCom(string);
		((ServerApp) ref.app).serverHandler.send(string);
	}

	public void reconnect() {
		gameState = GameState.PAUSE;
		ref.updater.send("<pause true");
		ArrayList<String> spawns = new ArrayList<String>();
		for (Entity entity : entities) {
			spawns.add("<spawn " + entity.getClass().getSimpleName() + " " + entity.player.getUser().ip + " " + entity.x
					+ " " + entity.y);
		}
		for (Entity entity : entities) {
			ref.updater.send("<remove " + entity.number);
		}
		for (String com : spawns) {
			ref.updater.send(com);
		}
		System.out.println("finished reconnect, restart game");
		ref.updater.send("<pause false");
		gameState = GameState.PLAY;
	}
}
