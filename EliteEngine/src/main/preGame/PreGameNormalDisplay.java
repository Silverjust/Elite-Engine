package main.preGame;

import g4p_controls.GEvent;
import g4p_controls.GGameButton;

public class PreGameNormalDisplay extends PreGameDisplay {

	public PreGameNormalDisplay() {
		super();
		startMap = getStartMap();
		nationSelect = new NationSelect(this);
		playerPanel = new PlayerPanel();
	}

	@Override
	public void update() {
		super.update();
		playerPanel.draw(this);
	}

	@Override
	public void handleSelectNation(GGameButton button, GEvent event) {
		nationSelect.handleSelectNation(button, event);
	}

	public void dispose() {
		super.dispose();
		playerPanel.dispose();
		nationSelect.dispose();
	}

	public void setActive(boolean b) {
		super.setActive(b);
		playerPanel.setEnabled(b);
		nationSelect.setEnabled(b);
	}

	public static int getStartMap() {
		return 0;
	}
}
