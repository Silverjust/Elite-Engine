package main.appdata;

import main.MainPreGame.GameSettings;
import processing.data.JSONObject;
import shared.ref;
import shared.Updater.GameState;

public class ProfileHandler implements appdataInfos {

	static JSONObject profile;
	private static boolean newGame = false;

	static public void loadProfile() {
		newGame = true;
		JSONObject oldProfile;
		try {
			oldProfile = ref.app.loadJSONObject(appdataInfos.path + "info.json");
		} catch (Exception e) {
			ref.app.saveJSONObject(new JSONObject(), appdataInfos.path + "info.json");
			oldProfile = ref.app.loadJSONObject(appdataInfos.path + "info.json");
		}
		try {
			if (!oldProfile.hasKey("name")) {
				oldProfile.setString("name", "unknown");
			}
			if (!oldProfile.hasKey("plays")) {
				oldProfile.setInt("plays", 0);
			}
			if (!oldProfile.hasKey("wins")) {
				oldProfile.setInt("wins", 0);
			}
			if (!oldProfile.hasKey("aliens-wins")) {
				oldProfile.setInt("aliens-wins", 0);
			}
			if (!oldProfile.hasKey("ahnen-wins")) {
				oldProfile.setInt("ahnen-wins", 0);
			}
			if (!oldProfile.hasKey("robots-wins")) {
				oldProfile.setInt("robots-wins", 0);
			}
			if (!oldProfile.hasKey("humans-wins")) {
				oldProfile.setInt("humans-wins", 0);
			}
			if (!oldProfile.hasKey("scientists-wins")) {
				oldProfile.setInt("scientists-wins", 0);
			}
			if (!oldProfile.hasKey("rate")) {
				oldProfile.setFloat("rate", 10);
			}
			profile = oldProfile;
			ref.app.saveJSONObject(profile, appdataInfos.path + "info.json");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void gameEndCalculations(float enemyRate) {
		if (newGame && (!GameSettings.singlePlayer)) {
			newGame = false;

			float rate = profile.getFloat("rate");
			rate = rate
					+ (ref.updater.gameState == GameState.WON ? enemyRate
							/ rate : -rate / enemyRate);
			System.out.println("InfoDocHandler.gameEndCalculations()"
					+ (ref.updater.gameState == GameState.WON ? enemyRate
							/ rate : -rate / enemyRate) + " " + rate);
			profile.setFloat("rate", rate);

			int plays = profile.getInt("plays");
			plays++;
			profile.setInt("plays", plays);

			if (ref.updater.gameState == GameState.WON) {
				int wins = profile.getInt("wins");
				int nationWins = profile.getInt(ref.player.nation.toString()
						+ "-wins");
				wins++;
				nationWins++;

				profile.setInt("wins", wins);
				profile.setInt(ref.player.nation.toString() + "-wins", nationWins);
			}
		}

	}

	public static void saveName(String name) {
		profile.setString("name", name);
		ref.app.saveJSONObject(profile, appdataInfos.path + "info.json");
	}

	static public String getName() {
		return profile.getString("name");
	}

	public static float getRate() {
		try {
			return profile.getFloat("rate");
		} catch (Exception e) {
			return 10;
		}
	}

	public static void dispose() {
		ref.app.saveJSONObject(profile, appdataInfos.path + "info.json");
		// info = null;
	}
}