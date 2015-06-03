package main;

import processing.core.PConstants;
import processing.core.PGraphics;
import g4p_controls.GButton;
import g4p_controls.GControlMode;
import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import g4p_controls.GSlider;
import shared.Mode;
import shared.Nation;
import shared.Player;
import shared.PreGame;
import shared.ref;

public class MainPreGame extends PreGame {

	GButton starButton;
	GGameButton[] nationButtons = new GGameButton[5];
	GSlider playerSlider;

	PGraphics playerList;
	private String name;

	MainPreGame(String name) {
		this.name = name;

		starButton = new GButton(ref.app, ref.app.width - 320,
				ref.app.height - 200, 300, 175);
		starButton.setText("START");
		starButton.addEventHandler(this, "handleStartEvents");

		playerSlider = new GSlider(ref.app, ref.app.width - 10, 100, 500, 30,
				20);
		playerSlider.setRotation(PConstants.PI / 2, GControlMode.CORNER);
		playerSlider.setLimits(0, 0, 1);

		playerList = ref.app.createGraphics(281, 500);

		for (int i = 0; i < nationButtons.length; i++) {
			nationButtons[i] = new GGameButton(ref.app, 200 + 210 * i, 100,
					200, 500, getNationImages(i));// change buttons
			nationButtons[i].addEventHandler(this, "handleSelectNation");
		}
		setupPlayer();
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

		starButton.dispose();
		playerSlider.dispose();
		for (int i = 0; i < nationButtons.length; i++) {
			nationButtons[i].dispose();
		}

		((MainApp) ref.app).mode = Mode.LADESCREEN;
	}

	@Override
	public void addPlayer(String ip, String name) {
		if (!player.containsKey(ip)) {
			Player p = Player.createPlayer(ip, name);
			p.playerColor = ref.app.color(200, 0, 0);// TODO get color setting
			player.put(ip, p);
		}
	}

	public void addThisPlayer(String name) {
		Player p = Player.createPlayer(ClientHandler.identification, name);
		ref.player = p;
		p.playerColor = ref.app.color(0, 255, 100);// TODO get color setting
		player.put(ClientHandler.identification, p);

	}

	public void handleStartEvents(GButton button, GEvent event) {
		for (String key : player.keySet())
			if (player.get(key).nation == null)
				return;
		ClientHandler.send("<load");
	}

	public void handleSelectNation(GGameButton button, GEvent event) {
		if (event == GEvent.PRESSED) {
			for (int i = 0; i < nationButtons.length; i++) {
				// System.out.println(nationButtons[i] == button);
				if (nationButtons[i] == button) {
					nationButtons[i].setSwitch(true);
					ClientHandler.send("<setNation " + ref.player.ip + " " + i);
				} else {
					nationButtons[i].setSwitch(false);
				}
			}
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

}
