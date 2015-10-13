package main.preGame;

import main.ClientHandler;
import main.MainApp;
import main.MainLoader;
import entity.gameAI.GameAI;
import shared.ContentListHandler;
import shared.Mode;
import shared.Nation;
import shared.PreGame;
import shared.User;
import shared.ref;

public class MainPreGame extends PreGame {

	public PreGameDisplay display;

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
		if (GameSettings.singlePlayer && !GameSettings.campain)
			display = new PreGameSandboxDisplay();
		else if (GameSettings.campain)
			display = new PreGameCampainDisplay();
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
			User u;
			if (GameSettings.againstAI) {
				u = new GameAI(ip, name);
			} else
				u = new User(ip, name);
			users.put(ip, u);
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
		if (display != null)
			display.mapSelect.setMap(string);
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
		if (string.equals(""))// returns this user
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
		public static boolean campain;
		public static boolean againstAI;

		public static void setupGameSettings() {
			singlePlayer = false;
			sandbox = false;
			campain = false;
			againstAI = false;
		}
	}
}