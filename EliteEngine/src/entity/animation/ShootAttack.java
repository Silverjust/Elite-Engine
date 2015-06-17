package entity.animation;

import processing.core.PApplet;
import processing.core.PImage;
import shared.Updater;
import entity.Attacker;
import entity.Entity;
import entity.Shooter;

public class ShootAttack extends MeleeAttack {
	private int beginTime;
	public float speed;

	public ShootAttack(PImage[][] IMG, int duration) {
		super(IMG, duration);
	}

	public ShootAttack(PImage[] IMG, int duration) {
		super(IMG, duration);
	}

	public ShootAttack(PImage IMG, int duration) {
		super(IMG, duration);
	}

	@Override
	public void setTargetFrom(Entity from, Entity to) {
		target = to;
		eventTime = beginTime
				+ (int) (PApplet.dist(from.x, from.y, to.x, to.y) / speed);
	}

	@Override
	public void setCastTime(int castTime) {
		beginTime = castTime;
	}

	@Override
	public void updateAbility(Entity e) {
		/*
		 * if (target != null && getProgressPercent() == 1) {
		 * System.out.println("target"); }
		 */
		if (target != null && isEvent() && isNotOnCooldown()) {
			((Attacker) e).calculateDamage(this);
			target = null;
			startCooldown();
		}
	}

	@Override
	public void drawAbility(Entity e, byte d) {
		if (e instanceof Shooter) {
			if (target != null && start + beginTime <= Updater.Time.getMillis()
					&& isNotOnCooldown()) {
				((Shooter) e).drawShot(target, getProgressPercent());
			}
		} else {
			System.err.println(e.getClass().getSimpleName()
					+ " should be shooter");
		}
	}

	@Override
	public boolean isSetup() {
		return getTarget() != null;
	}
}
/*
 * float importance = 0; Entity importantEntity = null; for (Entity e :
 * player.visibleEntities) { if (e != this) { if (e.isEnemyTo(this)) { if
 * (e.isCollision(x, y, basicAttack.range + e.radius) && e.groundPosition ==
 * GroundPosition.GROUND) { float newImportance = calcImportanceOf(e); if
 * (newImportance > importance) { importance = newImportance; importantEntity =
 * e; } } } } } if (importantEntity != null &&
 * getBasicAttack().isNotOnCooldown()) { // System.out.println(thread);
 * sendAnimation("basicAttack " + importantEntity.number); }
 */
