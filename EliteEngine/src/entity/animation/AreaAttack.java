package entity.animation;

import processing.core.PImage;
import entity.Attacker;
import entity.Entity;

@Deprecated
public class AreaAttack extends Attack {
	public float x, y;
	private boolean isSetup;

	@Deprecated
	public AreaAttack(PImage[][] IMG, int duration) {
		super(IMG, duration);
	}

	@Deprecated
	public AreaAttack(PImage[] IMG, int duration) {
		super(IMG, duration);
	}

	@Deprecated
	public AreaAttack(PImage IMG, int duration) {
		super(IMG, duration);
	}

	public void setPosition(float X, float Y) {
		x = X;
		y = Y;
		isSetup = true;
	}

	@Override
	public void updateAbility(Entity e) {
		if (isSetup() && isEvent() && isNotOnCooldown()) {
			((Attacker) e).calculateDamage(this);
			isSetup = false;
			startCooldown();
		}
	}

	@Override
	public boolean isSetup() {
		return isSetup;
	}

}
