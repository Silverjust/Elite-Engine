package entity;

import main.appdata.ProfileHandler;
import shared.ref;

public abstract class MainBuilding extends Building implements Commander {
	protected static final int RADIUS = 27;
	protected int commandingRange;

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
		ref.updater.send("<lost " + player.getUser().ip + " "
				+ ProfileHandler.getRate());
	}

	@Override
	public int commandRange() {
		return commandingRange;
	}

}
