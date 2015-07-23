package game;

import java.util.ArrayList;
import java.util.Set;

import entity.Entity;
import entity.MainBuilding;
import entity.neutral.KeritMine;
import entity.neutral.SandboxBuilding;
import processing.data.JSONObject;
import shared.ref;

public class MapHandler {

	public static void setupEntities(JSONObject map) {
		try {
			@SuppressWarnings("unchecked")
			Set<String> entitySet = map.getJSONObject("entities").keys();
			System.out.println("MapHandler.setupEntities() "+entitySet.size()+" Entities to spawn");
			for (String string : entitySet) {
				JSONObject entity = map.getJSONObject("entities")
						.getJSONObject(string);
				int playerNumber = entity.getInt("player");
				if (ref.updater.player.keySet().size() > playerNumber) {
					String player;
					if (playerNumber >= 0) {
						player = new ArrayList<String>(
								ref.updater.player.keySet()).get(playerNumber);
					} else {
						player = "0";
					}
					String type = entity.getString("type");
					if (type.equals("MainBuilding")) {
						type = ref.updater.player.get(player).nation
								.getNationInfo().getMainBuilding()
								.getSimpleName();
					} else if (type.equals("KeritMine")) {
						type = ref.updater.player.get(player).nation
								.getNationInfo().getKeritMine().getSimpleName();
					}
					/*
					 * if (type.equals("MainBuilding")) { type =
					 * ref.updater.player.get(player).nation
					 * .getMainBuilding().getSimpleName(); } if
					 * (type.equals("MainBuilding")) { type =
					 * ref.updater.player.get(player).nation
					 * .getMainBuilding().getSimpleName(); } if
					 * (type.equals("MainBuilding")) { type =
					 * ref.updater.player.get(player).nation
					 * .getMainBuilding().getSimpleName(); }
					 */
					float x = entity.getFloat("x");
					float y = entity.getFloat("y");
					ref.updater.send("<spawn " + type + " " + player + " " + x
							+ " " + y);
				}

			}
		} catch (Exception e) {
			System.err.println("there is something wrong with this map");
			e.printStackTrace();
		}
	}

	public static void saveMap(String intName, String name) {
		JSONObject oldMap = ref.app.loadJSONObject("data/" + ref.preGame.map
				+ ".json");
		JSONObject map = new JSONObject();
		map.setString("name", name);
		map.setString("descr", " ");
		map.setString("texture", oldMap.getString("texture"));
		map.setString("coll", oldMap.getString("coll"));
		map.setInt("w", ref.updater.map.width);
		map.setInt("h",  ref.updater.map.height);

		JSONObject entities = new JSONObject();
		int i = 0;
		for (Entity e : ref.updater.entities) {
			if (e.getClass() != SandboxBuilding.class) {
				i++;
				JSONObject atributes = new JSONObject();
				String type = e.getClass().getSimpleName().toString();
				if (e instanceof MainBuilding)
					type = "MainBuilding";
				else if (e instanceof KeritMine)
					type = "KeritMine";

				atributes.setString("type", type);
				int playerNumber = new ArrayList<String>(
						ref.updater.player.keySet()).indexOf(e.player.ip);
				atributes.setInt("player", playerNumber);
				atributes.setFloat("x", e.x);
				atributes.setFloat("y", e.y);
				entities.setJSONObject(i + "", atributes);
			}
		}
		map.setJSONObject("entities", entities);

		System.out.println("\"" + intName + "\" : \"maps/" + intName + "/"
				+ intName + "\"");

		ref.app.saveJSONObject(map,
				System.getProperty("user.home").replace("\\", "/")
						+ "/Desktop/" + intName + ".json");
	}
}
