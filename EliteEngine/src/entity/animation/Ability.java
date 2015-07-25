package entity.animation;

import processing.core.PImage;
import shared.Updater;
import entity.Entity;

public class Ability extends Animation {

	protected int eventTime;
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
		if (e.getAnimation() != this && isNotOnCooldown()) {
			System.out.println("Ability.setup()is new" + getCooldownPercent());
			consumeCosts();
			startCooldown();
			System.out.println("Ability.setup()is new2" + getCooldownPercent());

		}
	}

	public boolean isNotOnCooldown() {
		return cooldownTimer <= Updater.Time.getMillis();
	}

	public boolean isEvent() {
		return start + eventTime <= Updater.Time.getMillis();
	}

	public boolean isReady() {
		return isNotOnCooldown();
	}

	public void updateAbility(Entity e) {
		if (isSetup() && isEvent()) {
			/** do smthing */
			startCooldown();
		}
	}

	public void setCastTime(int castTime) {
		eventTime = castTime;
	}

	public void startCooldown() {
		cooldownTimer = Updater.Time.getMillis() + cooldown;
	}

	public float getCooldownPercent() {
		float f = 1 - (float) (cooldownTimer - Updater.Time.getMillis())
				/ cooldown;
		// System.out.println("Ability.getCooldownPercent()" + f);
		return f > 1 || f < 0 ? 1 : f;
	}

	public float getProgressPercent() {
		float f = 1 - (float) (start + eventTime - Updater.Time.getMillis())
				/ eventTime;
		return f > 1 || f < 0 ? 1 : f;
	}

	protected void consumeCosts() {

	}

	public void drawAbility(Entity e, byte d) {
	}

	public boolean isSetup() {
		return false;
	}

	public void removeCooldown(int time) {
		cooldownTimer -= time;
	}

	@Override
	public boolean doRepeat() {//FIXME is not overriding
		return false;
	}
}
