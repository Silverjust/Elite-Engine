package game;

import processing.data.JSONObject;
import shared.ref;

public class ContentListHandler {
	static JSONObject contentList;
	static JSONObject entityList;

	public static void load() {
		contentList = ref.app.loadJSONObject("data/content.txt");
		entityList = contentList.getJSONObject("entities");
	}

	public static JSONObject getEntityContent() {
		return entityList;
	}
}
