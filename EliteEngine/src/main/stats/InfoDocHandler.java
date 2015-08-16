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
	private static boolean newGame = false;

	static public void loadInfoDoc() {
		newGame = true;
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
			if (!oldInfo.hasKey("plays")) {
				oldInfo.setInt("plays", 0);
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

	public static void gameEndCalculations(float enemyRate) {
		if (newGame && (!GameSettings.singlePlayer)) {
			newGame = false;

			float rate = info.getFloat("rate");
			rate = rate
					+ (ref.updater.gameState == GameState.WON ? enemyRate
							/ rate : -rate / enemyRate);
			System.out.println("InfoDocHandler.gameEndCalculations()"
					+ (ref.updater.gameState == GameState.WON ? enemyRate
							/ rate : -rate / enemyRate) + " " + rate);
			info.setFloat("rate", rate);

			int plays = info.getInt("plays");
			plays++;
			info.setInt("plays", plays);

			if (ref.updater.gameState == GameState.WON) {
				int wins = info.getInt("wins");
				int nationWins = info.getInt(ref.player.nation.toString()
						+ "-wins");
				wins++;
				nationWins++;

				info.setInt("wins", wins);
				info.setInt(ref.player.nation.toString() + "-wins", nationWins);
			}
		}

	}

	public static void saveName(String name) {
		info.setString("name", name);
		ref.app.saveJSONObject(info, path + "info.json");
	}

	static public String getName() {
		return info.getString("name");
	}

	public static float getRate() {
		try {
			return info.getFloat("rate");
		} catch (Exception e) {
			return 10;
		}
	}

	public static void dispose() {
		ref.app.saveJSONObject(info, path + "info.json");
		// info = null;
	}
}
