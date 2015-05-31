package entity.animation;

import entity.Entity;
import processing.core.PImage;

public class Build extends Animation {

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
			e.sendAnimation("stand");
		}

	}

}
