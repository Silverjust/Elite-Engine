package shared;

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

	public static JSONObject getMapContent() {
		return contentList.getJSONObject("maps");
	}
}
