package main;

import processing.data.JSONObject;
import shared.ContentListHandler;
import shared.Mode;
import shared.Nation;
import shared.PreGame;
import shared.User;
import shared.ref;

public class MainPreGame extends PreGame {

	public PreGameNormalDisplay display;

	private String name;

	public MainPreGame(String name) {
		this.name = name;
		ContentListHandler.load();
	}

	public void closeBecauseServer() {
		users.clear();
		name = null;
	}

	public void setup() {
		if (GameSettings.singlePlayer && !GameSettings.tutorial)
			display = new PreGameSandboxDisplay();
		else
			display = new PreGameNormalDisplay();
	}

	public void update() {
		if (display != null) {
			display.update();
		}
	}

	@Override
	public void addPlayer(String ip, String name) {
		if (!users.containsKey(ip)) {
			// Player p = Player.createPlayer(ip, name);
			// p.color = ref.app.color(200, 0, 0);// TODO get color setting
			users.put(ip, new User(ip, name));
		}
	}

	public void addThisPlayer(String name) {
		User u = new User(ClientHandler.identification, name);
		// ref.player = Player.createPlayer(u);
		// p.color = ref.app.color(0, 255, 100);// TODO get color setting
		users.put(ClientHandler.identification, u);

	}

	public void tryStart() {
		for (String key : users.keySet())
			if (users.get(key).nation == null)
				return;
		if (map == null)
			return;
		ClientHandler.send("<load");
	}

	public void setupPlayer() {
		if (!GameSettings.singlePlayer) {
			System.out.println(GameSettings.singlePlayer);
			addThisPlayer(name);
		} else {
			addThisPlayer(name);
			addPlayer("" + 2, "n000bBot");
			users.get("2").nation = Nation.ALIENS;
		}
	}

	@Override
	public void setMap(String string) {
		int size = ContentListHandler.getModeMaps().keys().size();
		@SuppressWarnings("unchecked")
		String[] mapArray = (String[]) ContentListHandler.getModeMaps().keys()
				.toArray(new String[size]);
		int index = -1;
		for (int i = 0; i < mapArray.length; i++) {
			try {
				if (mapArray[i].equals(string)) {
					@SuppressWarnings("unused")
					JSONObject mapData = ref.app.loadJSONObject("data/"
							+ ContentListHandler.getModeMaps().getString(
									mapArray[i]) + ".json");
					index = i;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if (index < 0) {
			System.err.println(string + " not found");
			return;
		}
		map = ContentListHandler.getModeMaps().getString(mapArray[index]);
		if (display != null)
			display.mapSelector.setSelected(index);
	}

	@Override
	public void startLoading() {
		Nation.setNationsToPlayableNations();
		ref.loader = new MainLoader();

		display.dispose();

		((MainApp) ref.app).mode = Mode.LADESCREEN;
	}

	@Override
	public User getUser(String string) {
		if (string.equals(""))//returns this user
			return users.get(ClientHandler.identification);
		return super.getUser(string);
	}

	@Override
	public void dispose() {
		if (display != null) {
			display.dispose();
		}
	}

	@Override
	public void setActive(boolean b) {
		if (display != null) {
			display.setActive(b);
		}
	}

	public static class GameSettings {
		public static boolean singlePlayer;
		public static boolean sandbox;
		public static boolean tutorial;

		public static void setupGameSettings() {
			singlePlayer = false;
			sandbox = false;
			tutorial = false;
		}
	}
}
