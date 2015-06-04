package entity.animation;

import entity.Entity;
import processing.core.PImage;
import shared.ref;

public class Extract extends Ability {

	public float efficenty;
	public String ressource;

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
	public void updateAbility(Entity e) {
		int amount = (int) (e.hp * 1.0 / e.hp_max * efficenty);
		amount = amount < 0 ? 0 : amount;
		if (isEvent() && isNotOnCooldown()) {
			ref.updater.send("<give " + e.player.ip + " " + ressource + " "
					+ amount);
			startCooldown();
		}
	}
}
