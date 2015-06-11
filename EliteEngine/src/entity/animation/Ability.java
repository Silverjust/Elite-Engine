package entity.animation;

import processing.core.PImage;
import shared.Updater;
import entity.Entity;

public class Ability extends Animation {

	public int eventTime;
	public int cooldown;
	int cooldownTimer;

	public Ability(PImage[][] IMG, int duration) {
		super(IMG, duration);
	}

	public Ability(PImage[] IMG, int duration) {
		super(IMG, duration);
	}

	public Ability(PImage IMG, int duration) {
		super(IMG, duration);
	}

	@Override
	public void setup(Entity e) {
		super.setup(e);
		consumeCosts();
	}

	@Override
	public void update(Entity e) {
		if (isFinished()) {
			setup(e);
			e.sendDefaultAnimation(this);
		}
	}

	public void start() {
	}

	public boolean isNotOnCooldown() {
		return cooldownTimer <= Updater.Time.getMillis();
	}

	public boolean isEvent() {
		return start + eventTime <= Updater.Time.getMillis();
	}

	public void updateAbility(Entity e) {
		if (/** security */
		isEvent() && isNotOnCooldown()) {
			/** do smthing */
			startCooldown();
		}
	}

	protected void startCooldown() {
		cooldownTimer = Updater.Time.getMillis() + cooldown;
	}

	public float getCooldownPercent() {
		float f = 1 - (float) (cooldownTimer - Updater.Time.getMillis())
				/ cooldown;
		return f > 1 || f < 0 ? 1 : f;
	}

	public float getProgressPercent() {
		float f = 1 - (float) (start + eventTime - Updater.Time.getMillis())
				/ eventTime;
		return f > 1 || f < 0 ? 1 : f;
	}

	private void consumeCosts() {

	}
}
