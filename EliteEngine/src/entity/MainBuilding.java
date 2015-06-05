package entity;

import shared.ref;

public class MainBuilding extends Building {

	public MainBuilding(String[] c) {
		super(c);
		player.mainBuilding = this;
	}

	@Override
	protected void onDeath() {
		super.onDeath();
		ref.updater.send("<lost " + player.ip);
	}

}
