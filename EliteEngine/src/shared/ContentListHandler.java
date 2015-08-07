package shared;

import main.MainPreGame.GameSettings;
import processing.data.JSONObject;

public class ContentListHandler {
	public static String path = "data/content.json";
	static JSONObject contentList;
	static JSONObject entityList;

	public static void load() {
		contentList = ref.app.loadJSONObject(path);
		entityList = contentList.getJSONObject("entities");
	}

	public static JSONObject getEntityContent() {
		return entityList;
	}

	public static JSONObject getModeMaps() {
		return contentList.getJSONObject("maps").getJSONObject(
				GameSettings.tutorial ? "tutorial" : "standard");
	}
}
