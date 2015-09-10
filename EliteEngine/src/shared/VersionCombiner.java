package shared;

import java.util.Set;

import main.appdata.ProfileHandler;
import processing.data.JSONArray;
import processing.data.JSONObject;

public abstract class VersionCombiner {
	public final static String version = "1.5.3";

	public static boolean isNewerVersion(String s1, String s2) {
		String[] sa1 = s1.split("\\.");
		String[] sa2 = s2.split("\\.");
		System.out.println(s1 + " " + s2);
		for (int i = 0; i < sa1.length; i++) {
			System.out.println(Integer.parseInt(sa1[i]) + " "
					+ Integer.parseInt(sa2[i]));
			if (Integer.parseInt(sa1[i]) > Integer.parseInt(sa2[i]))
				return true;
		}
		return false;
	}

	public static void objToArray(JSONObject mapData, String mapName) {
		try {
			mapData.getJSONArray("entities");
		} catch (Exception e) {
			System.out.println("convert to json array");
			@SuppressWarnings("unchecked")
			Set<String> entitySet = mapData.getJSONObject("entities").keys();
			JSONArray entitys = new JSONArray();
			for (String string : entitySet) {
				JSONObject entity = mapData.getJSONObject("entities")
						.getJSONObject(string);
				entitys.append(entity);
			}
			mapData.remove("entities");
			mapData.setJSONArray("entities", entitys);
			if (ProfileHandler.isDeveloper())
				ref.app.saveJSONObject(mapData, System.getProperty("user.home")
						.replace("\\", "/") + "/Desktop/" + mapName + ".json");
		}
	}

	public static void rateChange(JSONObject oldProfile) {
		if (oldProfile.hasKey("rate") && !oldProfile.hasKey("version")) {
			float f = oldProfile.getFloat("rate");
			oldProfile.setFloat("rate", f + 1000 - 10);
		}
	}

	public static void versionSystemChange(JSONObject oldProfile) {
		if (oldProfile.hasKey("version")) {
			try {
				oldProfile.getString("version");
			} catch (Exception e) {
				oldProfile.remove("version");
				oldProfile.setString("version", version);
			}

		}
	}
}