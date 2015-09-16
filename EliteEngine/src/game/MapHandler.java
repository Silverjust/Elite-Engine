package game;

import java.util.ArrayList;

import entity.Entity;
import entity.MainBuilding;
import entity.neutral.KeritMine;
import entity.neutral.SandboxBuilding;
import processing.data.JSONArray;
import processing.data.JSONObject;
import shared.ref;

public class MapHandler {

	public static void setupEntities(JSONObject map) {
		try {
			JSONArray entitys = map.getJSONArray("entities");
			System.out.println("MapHandler.setupEntities() " + entitys.size()
					+ " Entities to spawn");
			for (int i = 0; i < entitys.size(); i++) {
				JSONObject entity = entitys.getJSONObject(i);
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
						type = ref.updater.player.get(player).getNation()
								.getNationInfo().getMainBuilding()
								.getSimpleName();
					} else if (type.equals("KeritMine")) {
						type = ref.updater.player.get(player).getNation()
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
		map.setInt("h", ref.updater.map.height);

		JSONArray entities = new JSONArray();
		for (Entity e : ref.updater.entities) {
			if (e.getClass() != SandboxBuilding.class) {
				JSONObject atributes = new JSONObject();
				String type = e.getClass().getSimpleName().toString();
				if (e instanceof MainBuilding)
					type = "MainBuilding";
				else if (e instanceof KeritMine)
					type = "KeritMine";

				atributes.setString("type", type);
				int playerNumber = new ArrayList<String>(
						ref.updater.player.keySet()).indexOf(e.player.getUser().ip);
				atributes.setInt("player", playerNumber);
				atributes.setFloat("x", e.x);
				atributes.setFloat("y", e.y);
				entities.append(atributes);
			}
		}
		map.setJSONArray("entities", entities);

		System.out.println("\"" + intName + "\" : \"maps/" + intName + "/"
				+ intName + "\"");

		ref.app.saveJSONObject(map,
				System.getProperty("user.home").replace("\\", "/")
						+ "/Desktop/" + intName + ".json");
	}
}
