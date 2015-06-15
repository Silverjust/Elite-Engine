package main;

import shared.ContentListHandler;
import shared.Mode;
import shared.Nation;
import shared.Player;
import shared.PreGame;
import shared.ref;

public class MainPreGame extends PreGame {

	public PreGameNormalDisplay display;

	private String name;

	public MainPreGame(String name) {
		this.name = name;
		ContentListHandler.load();
	}

	public void closeBecauseServer() {
		player.clear();
		name = "";
	}

	public void setup() {
		display = new PreGameNormalDisplay();
	}

	public void update() {
		if (display != null) {
			display.update();
		}
	}

	@Override
	public void addPlayer(String ip, String name) {
		if (!player.containsKey(ip)) {
			Player p = Player.createPlayer(ip, name);
			p.color = ref.app.color(200, 0, 0);// TODO get color setting
			player.put(ip, p);
		}
	}

	public void addThisPlayer(String name) {
		Player p = Player.createPlayer(ClientHandler.identification, name);
		ref.player = p;
		p.color = ref.app.color(0, 255, 100);// TODO get color setting
		player.put(ClientHandler.identification, p);

	}

	public void tryStart() {
		for (String key : player.keySet())
			if (player.get(key).nation == null)
				return;
		if (map == null)
			return;
		ClientHandler.send("<load");
	}

	public void setupPlayer() {
		if (!ClientHandler.singlePlayer) {
			addThisPlayer(name);
		} else {
			addThisPlayer(name);
			addPlayer("" + 2, "n000bBot");
			player.get("2").nation = Nation.HUMANS;
		}
	}

	@Override
	public void setMap(String string) {
		// TODO stop broken maps from lockin
		int size = ContentListHandler.getModeMaps().keys().size();
		@SuppressWarnings("unchecked")
		String[] mapArray = (String[]) ContentListHandler.getModeMaps().keys()
				.toArray(new String[size]);
		int index = -1;
		for (int i = 0; i < mapArray.length; i++) {
			if (mapArray[i].equals(string))
				index = i;
		}
		if (index < 0) {
			System.err.println(string + " not found");
			return;
		}
		map = ContentListHandler.getModeMaps().getString(mapArray[index]);
		if (display != null)
			display.maps.setSelected(index);
	}

	@Override
	public void startLoading() {
		// TODO remove, when all nations are stable
		for (String key : player.keySet()) {
			player.get(key).nation = Nation.ALIENS;
		}
		ref.loader = new MainLoader();

		display.dispose();

		((MainApp) ref.app).mode = Mode.LADESCREEN;
	}

}
