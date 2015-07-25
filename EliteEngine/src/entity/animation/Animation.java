package entity.animation;

import processing.core.PImage;
import shared.Updater;
import shared.ref;
import entity.Attacker;
import entity.Building;
import entity.Entity;
import entity.Trainer;
import entity.Unit;

public class Animation {
	public static Class<?> observe=Animation.class;//not assignable class == off
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
		start = Updater.Time.getMillis();
	}

	public void update(Entity e) {
		if (isFinished()) {
			if (doRepeat(e)) {
				if (Animation.observe.isAssignableFrom(e.getClass())) {
					System.out.println("Animation.update()" + getName(e));
				}
				e.setAnimation(this);
				setup(e);
			} else {
				e.sendDefaultAnimation(this);
				setup(e);// continue until com sets default animation
			}
		}
	}

	public void draw(Entity e, byte d, byte f) {
		if (Updater.Time.getMillis() - start >= speed() * e.currentFrame) {
			e.currentFrame = (byte) ((Updater.Time.getMillis() - start) / speed());
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
		return Updater.Time.getMillis() - start >= duration;

	}

	public boolean isInterruptable() {
		return true;
	}

	public boolean doRepeat(Entity e) {
		return true;
	}

	public String getName(Entity e) {
		if (this == e.stand)
			return "stand";
		if (e.death != null && this == e.death)
			return "death";
		if (e instanceof Building && this == ((Building) e).build)
			return "build";
		if (e instanceof Trainer && this == ((Trainer) e).getTraining())
			return "train";
		if (e instanceof Unit && this == ((Unit) e).walk)
			return "walk";
		if (e instanceof Attacker && this == ((Attacker) e).getBasicAttack())
			return "basicAttack";
		if (this instanceof Ability)
			return "ability";
		return super.toString();
	}
}
