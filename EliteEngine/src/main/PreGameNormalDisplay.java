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
import shared.ref;

public class PreGameNormalDisplay {

	GButton startButton;
	GGameButton[] nationButtons = new GGameButton[5];
	GSlider playerSlider;
	GDropList mapSelector;
	String[] intNames;

	PGraphics playerList;

	protected MainPreGame preGame;
	public static int startMap = 0;
	private int previousMap = startMap;

	public PreGameNormalDisplay() {
		preGame = (MainPreGame) ref.preGame;
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
		setupMapSelection();
	}

	protected PreGameNormalDisplay(byte b) {
		preGame = (MainPreGame) ref.preGame;
	}

	protected void setupMapSelection() {
		{
			int size = ContentListHandler.getModeMaps().size();
			mapSelector = new GDropList(ref.app, ref.app.width - 320,
					ref.app.height - 450, 300, 200, 5);
			@SuppressWarnings("unchecked")
			String[] intNames = (String[]) ContentListHandler.getModeMaps()
					.keys().toArray(new String[size]);
			this.intNames = intNames;
			String[] names = new String[size];
			for (int j = 0; j < names.length; j++) {
				try {
					JSONObject mapData = ref.app.loadJSONObject("data/"
							+ ContentListHandler.getModeMaps().getString(
									intNames[j]) + ".json");
					names[j] = mapData.getString("name");
				} catch (Exception e) {
					names[j] = "(X) " + intNames[j];
					e.printStackTrace();
				}
			}
			mapSelector.setItems(names, startMap);
			preGame.map = ContentListHandler.getModeMaps().getString(
					intNames[startMap]);
			mapSelector.addEventHandler(this, "handleSelectMap");
		}
	}

	public void update() {
		ref.app.background(50);
		playerList.beginDraw();
		playerList.background(255);
		if (!ref.preGame.player.isEmpty()) {
			playerSlider.setLimits(0, ref.preGame.player.size() * 20 - 19);
			int i = 0;
			for (String key : preGame.player.keySet()) {
				preGame.player.get(key).display(playerList, 0,
						20 * i - playerSlider.getValueI());
				i++;
			}
		}
		playerList.endDraw();
		ref.app.image(playerList, ref.app.width - playerList.width - 40, 100);
	}

	public void handleSelectNation(GGameButton button, GEvent event) {
		if (event == GEvent.PRESSED && ((MainApp) ref.app).mode == Mode.PREGAME) {
			// System.out.println(((MainApp) ref.app).mode);
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
		if (event == GEvent.SELECTED
				&& ((MainApp) ref.app).mode == Mode.PREGAME) {
			String file = "";
			try {
				file = "data/"
						+ ContentListHandler.getModeMaps().getString(
								intNames[list.getSelectedIndex()]) + ".json";
				@SuppressWarnings("unused")
				JSONObject mapData = ref.app.loadJSONObject(file);
				previousMap = list.getSelectedIndex();
				ClientHandler.send("<setMap " + ref.player.ip + " "
						+ intNames[list.getSelectedIndex()]);
			} catch (Exception e) {
				System.err.println(file
						+ " does not exist or could not be read");
				mapSelector.setSelected(previousMap);
			}
		}
	}

	public void handleStartEvents(GButton button, GEvent event) {
		// System.out.println(event);
		if (event == GEvent.CLICKED && ((MainApp) ref.app).mode == Mode.PREGAME) {
			preGame.tryStart();
		}
	}

	private String[] getNationImages(int i) {
		String[] s = new String[3];
		Nation nation = Nation.fromNumber(i);
		for (int j = 0; j < s.length; j++) {
			s[j] = "preGame/" + nation.toString() + "_" + (j + 1) + ".jpg";
			// System.out.println(s[j]);
		}
		return s;
	}

	public void dispose() {
		startButton.dispose();
		playerSlider.dispose();
		mapSelector.dispose();
		if (nationButtons[0] != null) {
			for (int i = 0; i < nationButtons.length; i++) {
				nationButtons[i].dispose();
			}
		}
	}

	public void setActive(boolean b) {
		startButton.setEnabled(b);
		playerSlider.setEnabled(b);
		mapSelector.setEnabled(b);
		if (nationButtons[0] != null) {
			for (int i = 0; i < nationButtons.length; i++) {
				nationButtons[i].setVisible(b);
			}
		}
	}
}
