package main;

import shared.Mode;
import shared.Nation;
import shared.ref;
import g4p_controls.GDropList;
import g4p_controls.GEvent;

public class PreGameSandboxDisplay extends PreGameNormalDisplay {
	GDropList playerDroplist;
	GDropList playerDroplistNation;

	public PreGameSandboxDisplay() {
		super();
		playerDroplist = new GDropList(ref.app, 200, 620, 200, 200, 5);
		updatePlayerDroplist();

		playerDroplistNation = new GDropList(ref.app, 420, 620, 100, 200, 5);
		String[] nations = new String[Nation.values().length];
		for (int i = 0; i < nations.length; i++) {
			nations[i] = Nation.values()[i].toString();
		}
		playerDroplistNation.setItems(nations, 0);
		playerDroplistNation
				.addEventHandler(this, "handleDroplistSelectNation");

	}

	private void updatePlayerDroplist() {
		if (!preGame.player.isEmpty()) {
			String[] enemyArr = new String[preGame.player.size()];
			int i = 0;
			for (String key : preGame.player.keySet()) {
				enemyArr[i] = preGame.player.get(key).name;
				i++;
			}
			playerDroplist.setItems(enemyArr, 1);
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

	@Override
	public void dispose() {
		super.dispose();
		playerDroplist.dispose();
		playerDroplistNation.dispose();
	}
}
