package entity.animation;

import processing.core.PImage;

public class Attack extends Ability {
	public byte range;
	public byte damage;
	public byte pirce;

	public Attack(PImage[][] IMG, int duration) {
		super(IMG, duration);
	}

	public Attack(PImage[] IMG, int duration) {
		super(IMG, duration);
	}

	public Attack(PImage IMG, int duration) {
		super(IMG, duration);
	}
}
