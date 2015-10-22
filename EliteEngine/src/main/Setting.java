package main;

import java.awt.im.InputContext;

import processing.core.PConstants;
import processing.data.JSONArray;
import processing.data.JSONObject;
import shared.ref;

public class Setting {

	public int mouseCommand;
	public int mouseSelect;
	public int toggleChat;
	public int strg;
	public int shift;
	public int changeAbilityMode;
	public int togglePause;
	public char[][] unitsShortcuts = new char[3][7];
	public char[][] buildingsShortcuts = new char[3][7];
	public char[] hotKeys = new char[10];

	public void fromJSON(String s) {
		JSONObject o = null;
		try {
			o = ref.app.loadJSONObject(s);
		} catch (Exception e) {
			o = new JSONObject();
		}
		fillEmptySettings(o);
		mouseCommand = o.getInt("mouseCommand");
		mouseSelect = o.getInt("mouseSelect");
		toggleChat = o.getInt("toggleChat");
		strg = o.getInt("strg");
		shift = o.getInt("shift");
		changeAbilityMode = o.getInt("changeAbilityMode");
		togglePause = o.getInt("togglePause");
		{
			JSONArray jarr = o.getJSONArray("unitsShortcuts");
			for (int i = 0; i < jarr.size(); i++) {
				JSONArray jarrIn = jarr.getJSONArray(i);
				for (int j = 0; j < jarrIn.size(); j++) {
					unitsShortcuts[i][j] = jarrIn.getString(j).charAt(0);
				}
			}
		}
		{
			JSONArray jarr = o.getJSONArray("buildingsShortcuts");
			for (int i = 0; i < jarr.size(); i++) {
				JSONArray jarrIn = jarr.getJSONArray(i);
				for (int j = 0; j < jarrIn.size(); j++) {
					buildingsShortcuts[i][j] = jarrIn.getString(j).charAt(0);
				}
			}
		}
		{
			JSONArray jarr = o.getJSONArray("hotKeys");
			for (int i = 0; i < jarr.size(); i++) {
				hotKeys[i] = jarr.getString(i).charAt(0);
			}
		}

		ref.app.saveJSONObject(o, s);
	}

	private void fillEmptySettings(JSONObject o) {

		if (!o.hasKey("mouseCommand"))
			o.setInt("mouseCommand", mouseCommand = PConstants.RIGHT);
		if (!o.hasKey("mouseSelect"))
			o.setInt("mouseSelect", PConstants.LEFT);
		if (!o.hasKey("toggleChat"))
			o.setInt("toggleChat", PConstants.ENTER);
		if (!o.hasKey("strg"))
			o.setInt("strg", PConstants.CONTROL);
		if (!o.hasKey("shift"))
			o.setInt("shift", PConstants.SHIFT);
		if (!o.hasKey("changeAbilityMode"))
			o.setInt("changeAbilityMode", PConstants.TAB);

		if (!o.hasKey("togglePause"))
			o.setInt("togglePause", PConstants.ESC);

		String standardGrid = "[  [ \"q\", \"w\", \"e\", \"r\", \"t\", \"y\", \"u\" ],"
				+ "[ \"a\", \"s\", \"d\", \"f\", \"g\", \"h\", \"j\" ], "
				+ "[ \"z\", \"x\", \"c\", \"v\", \"b\", \"n\", \"m\" ] ]";
		InputContext context = InputContext.getInstance();
		if (context.getLocale().toString().equals("de_DE"))
			standardGrid = "[  [ \"q\", \"w\", \"e\", \"r\", \"t\", \"z\", \"u\" ],"
					+ "[ \"a\", \"s\", \"d\", \"f\", \"g\", \"h\", \"j\" ], "
					+ "[ \"y\", \"x\", \"c\", \"v\", \"b\", \"n\", \"m\" ] ]";

		if (!o.hasKey("unitsShortcuts"))
			o.setJSONArray("unitsShortcuts", JSONArray.parse(standardGrid));
		if (!o.hasKey("buildingsShortcuts"))
			o.setJSONArray("buildingsShortcuts", JSONArray.parse(standardGrid));
		if (!o.hasKey("hotKeys"))
			o.setJSONArray("hotKeys",
					JSONArray.parse("[ \"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\", \"0\" ]"));
	}

	public static String getKeyName(int i) {
		if (i == 39)
			return "right-click";
		if (i == 37)
			return "left-click";
		return i + "";
	}
}
