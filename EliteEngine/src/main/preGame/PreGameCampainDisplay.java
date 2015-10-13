package main.preGame;

import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import shared.ref;

public class PreGameCampainDisplay extends PreGameDisplay {
	public PreGameCampainDisplay() {
		super();
		nationSelect = new NationSelect(this);
	}

	@Override
	public void handleSelectNation(GGameButton button, GEvent event) {
		nationSelect.handleSelectNation(button, event);
		ref.preGame.getUser("").nation = nationSelect.nation;
		mapSelect.setupMapSelection();
	}

	public void dispose() {
		super.dispose();
		nationSelect.dispose();
	}

	public void setActive(boolean b) {
		super.setActive(b);
		nationSelect.setEnabled(b);
	}
}
