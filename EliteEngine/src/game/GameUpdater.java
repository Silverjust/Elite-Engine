package game;

import java.util.Collections;

import main.preGame.MainPreGame.GameSettings;
import main.ClientHandler;
import shared.Nation;
import shared.Player;
import shared.Updater;
import shared.User;
import shared.ref;
import entity.Entity;
import entity.EntityHeightComparator;
import entity.gameAI.GameAI;
import entity.gameAI.testAliensAI;
import entity.gameAI.testRobotsRush;

public class GameUpdater extends Updater {
	// FIXME einheiten vibrieren

	public Input input;

	public GameUpdater() {
		neutral = Player.createNeutralPlayer();
		input = new Input();
		map = new Map(ref.preGame.map);

		for (String key : ref.preGame.users.keySet()) {
			User user = ref.preGame.users.get(key);
			if (user instanceof GameAI) {
				if (user.nation == Nation.ALIENS)
					((GameAI) user).ai = testAliensAI.class;
				if (user.nation == Nation.ROBOTS)
					((GameAI) user).ai = testRobotsRush.class;
				user = ((GameAI) user).getAI();
			}
			Player p = Player.createPlayer(user);
			if (p.getUser().ip == ClientHandler.identification) {
				p.color = ref.app.color(0, 255, 100);
				ref.player = p;
			} else
				p.color = ref.app.color(200, 0, 0);
			player.put(key, p);
		}
	}

	public void update() {
		input.update();
		updateAIs();

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
					if (entity.isSelected) {
						selectionChanged = true;
						keepGrid = true;
					}
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
			Collections.sort(ref.player.visibleEntities, new EntityHeightComparator());
			map.mapCodeUpdate();
			for (Entity e : entities) {
				e.updateAnimation();
				e.updateDecisions(GameSettings.singlePlayer);
				e.updateMovement();
			}
			if (selectionChanged) {
				if (!keepGrid)
					HUD.activesGrid.resetGrid();
				HUD.activesGrid.selectionChange();
				selectionChanged = false;
				keepGrid = false;
			}
		}

	}

	private void updateAIs() {
		if (GameSettings.againstAI) {
			for (String key : player.keySet()) {
				Player p = player.get(key);
				if (p.getUser() instanceof GameAI) {
					try {
						((GameAI) p.getUser()).update();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
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
