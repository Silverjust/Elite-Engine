package entity;

import shared.ref;

public abstract class MainBuilding extends Building {

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
		ref.updater.send("<lost " + player.ip);
	}

}
