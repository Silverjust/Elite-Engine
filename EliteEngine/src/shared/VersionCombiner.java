package shared;

import java.util.Set;

import main.appdata.ProfileHandler;
import processing.data.JSONArray;
import processing.data.JSONObject;

public abstract class VersionCombiner {
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
}