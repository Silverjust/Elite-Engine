package entity.animation;

import processing.core.PImage;
import shared.Updater;
import shared.ref;
import entity.Entity;

public class Explosion extends Animation {

	private float xSize = 50;
	private float ySize = 50;

	public Explosion(PImage[] IMG, int duration) {
		super(IMG, duration);
	}

	public Explosion(PImage IMG, int duration) {
		super(IMG, duration);
	}

	public void setup(Entity e) {
		start = Updater.Time.getMillis();
	}

	@Override
	@Deprecated
	public void update(Entity e) {
		if (isFinished()) {
			setup(e);
		}
	}

	@Override
	@Deprecated
	public void draw(Entity e, byte d, byte f) {
	}

	public void draw(float x, float y) {
		int currentFrame = 0;
		if (Updater.Time.getMillis() - start >= speed() * currentFrame) {
			currentFrame = (byte) ((Updater.Time.getMillis() - start) / speed());
			if (currentFrame > img.length - 1) {
				currentFrame = (byte) (img.length - 1);
			}
			if (currentFrame < 0) {
				currentFrame = 0;
			}
		}

		if (img != null && currentFrame < img.length) {
			ref.app.image(img[currentFrame], Entity.xToGrid(x),
					Entity.yToGrid(y), xSize, ySize);
		}
	}

	@Override
	public boolean doRepeat(Entity e) {
		return false;
	}
}
