package entity;

import main.appdata.ProfileHandler;
import shared.ref;

public abstract class MainBuilding extends Building {
	protected static final int RADIUS = 27;

	public MainBuilding(String[] c) {
		super(c);
		try {
			player.mainBuilding = this;
		} catch (Exception e) {
		}
	}

	@Override
	protected void onDeath() {
		super.onDeath();
		ref.updater.send("<lost " + player.ip + " " + ProfileHandler.getRate());
	}

}
