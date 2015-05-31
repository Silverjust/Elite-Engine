package entity.animation;

import processing.core.PImage;
import shared.ref;
import entity.Entity;

public class Animation {// TODO frames zu zeit umstellen
	byte directions;
	private byte frames;
	PImage[][] imgWD;
	PImage[] img;
	public int duration;
	int start;

	public Animation(PImage[][] IMG, int duration) {
		imgWD = IMG;
		if (IMG != null) {
			directions = (byte) imgWD.length;
			frames = (byte) imgWD[0].length;
		}
		this.duration = duration;
	}

	public Animation(PImage[] IMG, int duration) {
		img = IMG;
		directions = 1;
		if (IMG != null)
			frames = (byte) img.length;
		this.duration = duration;

	}

	public Animation(PImage IMG, int duration) {
		img = new PImage[1];
		img[0] = IMG;
		directions = 1;
		frames = 1;
		this.duration = duration;

	}

	public int speed() {
		return duration / frames;
	}

	public void setup(Entity e) {
		e.currentFrame = 0;
		start = ref.app.millis();
	}

	public void update(Entity e) {
		if (isFinished()) {
			setup(e);
		}
	}

	public void draw(Entity e, byte d, byte f) {
		if (ref.app.millis() - start >= speed() * e.currentFrame) {
			e.currentFrame = (byte) ((ref.app.millis() - start) / speed());
			if (e.currentFrame > frames - 1) {
				e.currentFrame = (byte) (frames - 1);
			}
			if (e.currentFrame < 0) {
				e.currentFrame = 0;
			}
		}

		if (imgWD != null && d < directions && f < frames) {
			ref.app.image(imgWD[d][f], Entity.xToGrid(e.x),
					Entity.yToGrid(e.y), e.xSize, e.ySize);
		} else if (img != null && f < frames) {
			ref.app.image(img[f], Entity.xToGrid(e.x),
					Entity.yToGrid(e.y - e.height), e.xSize, e.ySize);
		}
	}

	public boolean isNotOnCooldown() {
		return true;
	}

	public boolean isFinished() {
		return ref.app.millis() - start >= duration;

	}
}
