package entity.animation;

import processing.core.PImage;
import shared.ref;
import entity.Entity;

public class Death extends Animation {

	public Death(PImage[][] IMG, int duration) {
		super(IMG, duration);

	}

	public Death(PImage[] IMG, int duration) {
		super(IMG, duration);
	}

	public Death(PImage IMG, int duration) {
		super(IMG, duration);
	}

	@Override
	public void update(Entity e) {
		if (isFinished()) {
			// setup(e);
			ref.updater.toRemove.add(e);
		}

	}

	@Override
	public boolean isInterruptable() {
		return false;
	}

}
