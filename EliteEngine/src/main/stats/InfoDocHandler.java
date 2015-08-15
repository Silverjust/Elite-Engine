package main.stats;

import main.MainPreGame.GameSettings;
import processing.data.JSONObject;
import shared.ref;
import shared.Updater.GameState;

public class InfoDocHandler {

	private static String nameOfFolder = "BattleOfOrion";
	static String path = System.getProperty("user.home").replace("\\", "/")
			+ "/AppData/Roaming/" + nameOfFolder + "/";
	static JSONObject info;

	static public void loadInfoDoc() {

		JSONObject oldInfo;
		try {
			oldInfo = ref.app.loadJSONObject(path + "info.json");
		} catch (Exception e) {
			ref.app.saveJSONObject(new JSONObject(), path + "info.json");
			oldInfo = ref.app.loadJSONObject(path + "info.json");
		}
		try {
			if (!oldInfo.hasKey("name")) {
				oldInfo.setString("name", "unknown");
			}
			if (!oldInfo.hasKey("wins")) {
				oldInfo.setInt("wins", 0);
			}
			if (!oldInfo.hasKey("aliens-wins")) {
				oldInfo.setInt("aliens-wins", 0);
			}
			if (!oldInfo.hasKey("ahnen-wins")) {
				oldInfo.setInt("ahnen-wins", 0);
			}
			if (!oldInfo.hasKey("robots-wins")) {
				oldInfo.setInt("robots-wins", 0);
			}
			if (!oldInfo.hasKey("humans-wins")) {
				oldInfo.setInt("humans-wins", 0);
			}
			if (!oldInfo.hasKey("scientists-wins")) {
				oldInfo.setInt("scientists-wins", 0);
			}
			if (!oldInfo.hasKey("rate")) {
				oldInfo.setFloat("rate", 10);
			}
			info = oldInfo;
			ref.app.saveJSONObject(info, path + "info.json");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addWin() {
		if (ref.updater.gameState == GameState.WON
				&& !GameSettings.singlePlayer) {
			int wins = info.getInt("wins");
			int nationWins = info
					.getInt(ref.player.nation.toString() + "-wins");
			wins++;
			nationWins++;
			info.setInt("wins", nationWins);
			info.setInt(ref.player.nation.toString() + "-wins", wins);
		}
	}

	static public String getName() {
		return info.getString("name");
	}

	public static void saveName(String name) {
		info.setString("name", name);
		ref.app.saveJSONObject(info, path + "info.json");
	}

	public static void dispose() {
		ref.app.saveJSONObject(info, path + "info.json");
		info = null;
	}
}
