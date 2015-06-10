package entity.animation;

import processing.core.PImage;
import shared.ref;
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
		return cooldownTimer <= ref.app.millis();
	}

	public boolean isEvent() {
		return start + eventTime <= ref.app.millis();
	}

	public void updateAbility(Entity e) {
		if (/** security */
		isEvent() && isNotOnCooldown()) {
			/** do smthing */
			startCooldown();
		}
	}

	protected void startCooldown() {
		cooldownTimer = ref.app.millis() + cooldown;
	}

	public float getCooldownPercent() {
		float f = 1 - (float) (cooldownTimer - ref.app.millis()) / cooldown;
		return f > 1 || f < 0 ? 1 : f;
	}

	public float getProgress() {
		float f = 1 - (float) (start + eventTime - ref.app.millis())
				/ eventTime;
		return f > 1 || f < 0 ? 1 : f;
	}

	private void consumeCosts() {

	}
}
