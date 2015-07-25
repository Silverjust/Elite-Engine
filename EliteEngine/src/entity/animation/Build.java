package entity.animation;

import entity.Entity;
import processing.core.PImage;

public class Build extends Ability {

	public Build(PImage[][] IMG, int duration) {
		super(IMG, duration);
	}

	public Build(PImage[] IMG, int duration) {
		super(IMG, duration);
	}

	public Build(PImage IMG, int duration) {
		super(IMG, duration);
	}

	@Override
	public void update(Entity e) {
		if (isFinished()) {
			setup(e);
			if (isNotOnCooldown()) {
				e.sendDefaultAnimation(this);
			} else
				// if (nextAnimation() != null)
				e.setAnimation(this);
			// else
			// e.sendDefaultAnimation(this);
		}
	}

	public void setBuildTime(int buildTime) {
		cooldown = buildTime;
		startCooldown();

	}
}
