package entity.animation;

import entity.Entity;
import processing.core.PImage;
import shared.ref;

public class Extract extends Ability {

	public float efficenty;
	public String ressource;
	private boolean isSetup;

	public Extract(PImage[][] IMG, int duration) {
		super(IMG, duration);
	}

	public Extract(PImage[] IMG, int duration) {
		super(IMG, duration);
	}

	public Extract(PImage IMG, int duration) {
		super(IMG, duration);
	}

	@Override
	public void updateAbility(Entity e, boolean isServer) {
		if (isSetup() && isEvent()) {
			int amount = (int) (e.hp * 1.0 / e.hp_max * efficenty);
			amount = amount < 0 ? 0 : amount;
			if (isServer) {
				ref.updater.send("<give " + e.player.ip + " " + ressource + " "
						+ amount);
			}
			isSetup = false;
		}
		if (isNotOnCooldown()) {
			isSetup = true;
			startCooldown();
		}
	}

	@Override
	public boolean isSetup() {
		return isSetup;
	}

	@Override
	public boolean doRepeat(Entity e) {
		return true;
	}
}
