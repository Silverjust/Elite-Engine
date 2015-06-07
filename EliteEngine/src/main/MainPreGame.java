package main;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.data.JSONObject;
import g4p_controls.GButton;
import g4p_controls.GControlMode;
import g4p_controls.GDropList;
import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import g4p_controls.GSlider;
import shared.ContentListHandler;
import shared.Mode;
import shared.Nation;
import shared.Player;
import shared.PreGame;
import shared.ref;

public class MainPreGame extends PreGame {

	GButton startButton;
	GGameButton[] nationButtons = new GGameButton[5];
	GSlider playerSlider;
	GDropList maps;
	String[] intNames;

	PGraphics playerList;
	private String name;

	public MainPreGame(String name) {
		this.name = name;
	}

	public void closeBecauseServer() {
		player.clear();
		name = "";
	}

	public void setup() {

		startButton = new GButton(ref.app, ref.app.width - 320,
				ref.app.height - 200, 300, 175);
		startButton.setText("START");
		startButton.addEventHandler(this, "handleStartEvents");

		playerSlider = new GSlider(ref.app, ref.app.width - 10, 100, 300, 30,
				20);
		playerSlider.setRotation(PConstants.PI / 2, GControlMode.CORNER);
		playerSlider.setLimits(0, 0, 1);

		playerList = ref.app.createGraphics(281, 300);// 200 height?

		for (int i = 0; i < nationButtons.length; i++) {
			nationButtons[i] = new GGameButton(ref.app, 200 + 210 * i, 100,
					200, 500, getNationImages(i));// change buttons
			nationButtons[i].addEventHandler(this, "handleSelectNation");
		}
		{
			ContentListHandler.load();
			int i = ContentListHandler.getMapContent().size();
			maps = new GDropList(ref.app, ref.app.width - 320,
					ref.app.height - 450, 300, 200, 5);
			@SuppressWarnings("unchecked")
			String[] intNames = (String[]) ContentListHandler.getMapContent()
					.keys().toArray(new String[i]);
			this.intNames = intNames;
			String[] names = new String[i];
			for (int j = 0; j < names.length; j++) {
				try {
					JSONObject mapData = ref.app.loadJSONObject("data/"
							+ ContentListHandler.getMapContent().getString(
									intNames[j]) + ".json");
					names[j] = mapData.getString("name");
				} catch (Exception e) {
					names[j] = "(X) " + intNames[j];
					e.printStackTrace();
				}
			}
			maps.setItems(names, 0);
			map = intNames[0];
			maps.addEventHandler(this, "handleSelectMap");
		}
	}

	public void update() {
		ref.app.background(50);
		playerList.beginDraw();
		playerList.background(255);
		if (!ref.preGame.player.isEmpty()) {
			playerSlider.setLimits(0, ref.preGame.player.size() * 20 - 19);
			int i = 0;
			for (String key : player.keySet()) {
				player.get(key).display(playerList, 0,
						20 * i - playerSlider.getValueI());
				i++;
			}
		}
		playerList.endDraw();
		ref.app.image(playerList, ref.app.width - playerList.width - 40, 100);
	}

	@Override
	public void startLoading() {
		// TODO remove, when all nations are stable
		for (String key : player.keySet()) {
			player.get(key).nation = Nation.ALIENS;
		}
		ref.loader = new MainLoader();

		startButton.dispose();
		playerSlider.dispose();
		maps.dispose();
		for (int i = 0; i < nationButtons.length; i++) {
			nationButtons[i].dispose();
		}

		((MainApp) ref.app).mode = Mode.LADESCREEN;
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

	public void handleStartEvents(GButton button, GEvent event) {
		for (String key : player.keySet())
			if (player.get(key).nation == null)
				return;
		if (map == null)
			return;
		ClientHandler.send("<load");
	}

	public void handleSelectNation(GGameButton button, GEvent event) {
		if (event == GEvent.PRESSED) {
			for (int i = 0; i < nationButtons.length; i++) {
				// System.out.println(nationButtons[i] == button);
				if (nationButtons[i] == button) {
					nationButtons[i].setSwitch(true);
					ClientHandler.send("<setNation " + ref.player.ip + " "
							+ Nation.fromNumber(i).toString());
				} else {
					nationButtons[i].setSwitch(false);
				}
			}
		}
	}

	public void handleSelectMap(GDropList list, GEvent event) {
		if (event == GEvent.SELECTED) {
			ClientHandler.send("<setMap " + ref.player.ip + " "
					+ intNames[list.getSelectedIndex()]);
		}
	}

	private String[] getNationImages(int i) {
		String[] s = new String[3];
		Nation nation = Nation.fromNumber(i);
		for (int j = 0; j < s.length; j++) {
			s[j] = "preGame/" + nation.toString() + "_" + (j + 1) + ".jpg";
			System.out.println(s[j]);
		}
		return s;
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
		//TODO stop broken maps from lockin
		int size = ContentListHandler.getMapContent().keys().size();
		@SuppressWarnings("unchecked")
		String[] mapArray = (String[]) ContentListHandler.getMapContent()
				.keys().toArray(new String[size]);
		int index = -1;
		for (int i = 0; i < mapArray.length; i++) {
			if (mapArray[i].equals(string))
				index = i;
		}
		if (index < 0) {
			System.err.println(string + " not found");
			return;
		}
		map = string;
		maps.setSelected(index);
	}
}
