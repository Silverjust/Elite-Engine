package main;

import processing.core.PConstants;
import shared.Mode;
import shared.Nation;
import shared.ref;
import g4p_controls.GButton;
import g4p_controls.GControlMode;
import g4p_controls.GDropList;
import g4p_controls.GEvent;
import g4p_controls.GSlider;

public class PreGameSandboxDisplay extends PreGameNormalDisplay {
	GDropList playerDroplist;
	GDropList playerDroplistNation;
	GButton addPlayer;

	public PreGameSandboxDisplay() {
		super((byte) 1);
		startButton = new GButton(ref.app, ref.app.width - 320,
				ref.app.height - 200, 300, 175);
		startButton.setText("START");
		startButton.addEventHandler(this, "handleStartEvents");

		playerSlider = new GSlider(ref.app, ref.app.width - 10, 100, 300, 30,
				20);
		playerSlider.setRotation(PConstants.PI / 2, GControlMode.CORNER);
		playerSlider.setLimits(0, 0, 1);

		playerList = ref.app.createGraphics(281, 300);// 200 height?
		// **************************************
		playerDroplist = new GDropList(ref.app, 200, 200, 200, 200, 5);
		playerDroplist.addEventHandler(this, "handleDroplistSelectPlayer");
		updatePlayerDroplist();

		playerDroplistNation = new GDropList(ref.app, 420, 200, 100, 200, 5);
		String[] nations = new String[Nation.values().length];
		for (int i = 0; i < nations.length; i++)
			nations[i] = Nation.values()[i].toString();
		playerDroplistNation.setItems(nations, 0);
		playerDroplistNation
				.addEventHandler(this, "handleDroplistSelectNation");

		addPlayer = new GButton(ref.app, 540, 200, 100, 35, "add Player");
		addPlayer.addEventHandler(this, "handleAddPlayer");
		// ******************************
		setupMapSelection();
	}

	private void updatePlayerDroplist() {
		if (!preGame.player.isEmpty()) {
			String[] enemyArr = new String[preGame.player.size()];
			int i = 0;
			for (String key : preGame.player.keySet()) {
				enemyArr[i] = preGame.player.get(key).name;
				i++;
			}
			playerDroplist.setItems(enemyArr, 0);
		}
	}

	public void handleDroplistSelectPlayer(GDropList droplist, GEvent event) {
		if (event == GEvent.SELECTED
				&& ((MainApp) ref.app).mode == Mode.PREGAME) {
			String[] enemyArr = new String[preGame.player.size()];
			int i = 0;
			for (String key : preGame.player.keySet()) {
				enemyArr[i] = preGame.player.get(key).ip;
				i++;
			}
			Nation n = preGame.player
					.get(enemyArr[droplist.getSelectedIndex()]).nation;
			byte b = -1;
			if (n!=null) {
				for (byte j = 0; j < Nation.values().length; j++) {
					if (Nation.values()[j].toString().equals(n.toString()))
						b = j;
				}
			}
			System.out.println(b);
			playerDroplistNation.setSelected(b);
		}
	}

	public void handleDroplistSelectNation(GDropList droplist, GEvent event) {
		if (event == GEvent.SELECTED
				&& ((MainApp) ref.app).mode == Mode.PREGAME) {
			String[] enemyArr = new String[preGame.player.size()];
			int i = 0;
			for (String key : preGame.player.keySet()) {
				enemyArr[i] = preGame.player.get(key).ip;
				i++;
			}
			ClientHandler.send("<setNation "
					+ enemyArr[playerDroplist.getSelectedIndex()] + " "
					+ droplist.getSelectedText());

		}
	}

	public void handleAddPlayer(GButton button, GEvent event) {
		if (event == GEvent.CLICKED && ((MainApp) ref.app).mode == Mode.PREGAME) {
			preGame.addPlayer(preGame.player.size() + 1 + "", "n000bBot"
					+ preGame.player.size());
			updatePlayerDroplist();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		playerDroplist.dispose();
		playerDroplistNation.dispose();
		addPlayer.dispose();
	}
}
