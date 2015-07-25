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

	public void setBuildTime(int buildTime) {
		cooldown = buildTime;
		startCooldown();
	}

	@Override
	public boolean doRepeat(Entity e) {
		return !isNotOnCooldown();
	}
	@Override
	public boolean isInterruptable() {
		return isNotOnCooldown();
	}
}
