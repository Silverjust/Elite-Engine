package server;

import entity.Entity;
import game.Map;
import shared.ComHandler;
import shared.Player;
import shared.Updater;
import shared.ref;

public class ServerUpdater extends Updater {
	public ServerUpdater() {
		player = ref.preGame.player;
		neutral = Player.createNeutralPlayer();

		map = new Map();
	}

	@Override
	public void update() {
		for (int i = 0; i < toAdd.size(); i++) {
			Entity.entityCounter += 1;
			entities.add(toAdd.get(i));
			namedEntities.put(Entity.entityCounter, toAdd.get(i));
			toAdd.get(i).number = Entity.entityCounter;
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
		for (String key : player.keySet()) {
			player.get(key).visibleEntities.clear();
			for (Entity e : entities) {
				if (e.isVisibleTo(player.get(key))) {
					player.get(key).visibleEntities.add(e);
				}
			}
		}
		for (Entity e : entities) {
			e.updateAnimation();
			e.updateDecisions();
			e.updateMovement();
		}
	}

	@Override
	public void write(String ip, String[] text) {
		String name = ref.updater.player.get(ip).name;
		if (name != null)
			name = ip;
		String completeText = "";
		for (int i = 2; i < text.length; i++) {// c[0] und c[1] auslassen
			completeText = completeText.concat(" ").concat(text[i]);
		}
		((ServerApp) ref.app).gui.addChatText(name + ">>" + completeText);
		System.out.println(" " + name + ">>" + completeText);
	}

	@Override
	public void send(String string) {
		ComHandler.executeCom(string);
		((ServerApp) ref.app).serverHandler.send(string);
	}
}
