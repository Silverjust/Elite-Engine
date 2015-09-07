package game;

import java.util.Collections;

import main.ClientHandler;
import main.MainPreGame.GameSettings;
import shared.Helper;
import shared.Player;
import shared.Updater;
import shared.ref;
import entity.Entity;
import entity.EntityHeightComparator;
import entity.Unit;

public class GameUpdater extends Updater {
	// FIXME einheiten vibrieren

	public Input input;

	public GameUpdater() {
		player = ref.preGame.player;
		neutral = Player.createNeutralPlayer();
		input = new Input();
		map = new Map(ref.preGame.map);
	}

	public void update() {
		input.update();

		if (gameState == GameState.PLAY) {
			for (int i = 0; i < toAdd.size(); i++) {
				Entity.entityCounter += 1;
				entities.add(toAdd.get(i));
				namedEntities.put(Entity.entityCounter, toAdd.get(i));
				toAdd.get(i).number = Entity.entityCounter;
				toAdd.get(i).onSpawn(GameSettings.singlePlayer);
				map.mapCode.onEntitySpawn(toAdd.get(i));
				toAdd.remove(i);
			}
			for (int i = 0; i < toRemove.size(); i++) {
				Entity entity = toRemove.get(i);
				if (entity != null) {
					int n = entity.number;
					for (Group g : GroupHandler.groups)
						g.remove(entity);
					namedEntities.remove(n);
					if (entity.isSelected)
						selectionChanged = true;
					selected.remove(entity);
					entities.remove(entity);
					toRemove.remove(i);
					// System.out.println("removed " + n);
				}
			}
			for (String key : player.keySet()) {
				player.get(key).visibleEntities.clear();
				for (Entity e : entities) {
					/*
					 * if (player.get(key) == ref.player) { selected.remove(e);
					 * if (e.isSelected) { selected.add(e); } }
					 */
					if (GameDrawer.godeye || e.isVisibleTo(player.get(key))) {
						player.get(key).visibleEntities.add(e);
					}
				}
			}
			// sortierfunktion
			Collections.sort(ref.player.visibleEntities,
					new EntityHeightComparator());
			map.mapCodeUpdate();
			for (Entity e : entities) {
				e.updateAnimation();
				e.updateDecisions(GameSettings.singlePlayer);
				e.updateMovement();
			}
			if (selectionChanged) {
				boolean containsUnits = false;
				if (GroupHandler.recentGroup != null) {
					containsUnits = GroupHandler.recentGroup.unitActives;
				} else {
					for (Entity entity : selected) {
						if (entity instanceof Unit)
							containsUnits = true;
					}
				}
				HUD.activesGrid.selectionChange(containsUnits);
				selectionChanged = false;
			}
		}

	}

	@Override
	public void write(String ip, String[] text) {
		String name = Helper.ipToName(ip);
		String completeText = "";
		for (int i = 2; i < text.length; i++) {// c[0] und c[1] auslassen
			completeText = completeText.concat(" ").concat(text[i]);
		}
		HUD.chat.println(name, completeText);
	}

	@Override
	public void send(String string) {
		ClientHandler.send(string);
	}

	@Override
	public void startPause() {
		HUD.menue = new IngameMenu();
	}

	@Override
	public void endPause() {
		HUD.menue.dispose();
		HUD.menue = null;
	}

}
