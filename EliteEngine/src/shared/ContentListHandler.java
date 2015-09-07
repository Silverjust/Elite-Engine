package shared;

import main.MainPreGame.GameSettings;
import processing.data.JSONObject;

public class ContentListHandler {
	public static String path = "data/content.json";
	static JSONObject contentList;
	static JSONObject entityList;
	static JSONObject campainEntityList;

	public static void load() {
		contentList = ref.app.loadJSONObject(path);
		entityList = contentList.getJSONObject("entities");
		campainEntityList = contentList.getJSONObject("campainEntities");
		for (Object o : campainEntityList.keys()) {
			entityList.setString((String) o, campainEntityList.getString((String) o));
		}
	}

	public static JSONObject getEntityContent() {
		return entityList;
	}

	public static JSONObject getModeMaps() {
		return contentList.getJSONObject("maps").getJSONObject(
				GameSettings.tutorial ? "tutorial" : "standard");
	}
}
