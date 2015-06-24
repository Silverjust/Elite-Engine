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
	public Explosion explosion;
	private boolean isExploding;

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
		isSetup = true;
		isExploding = false;
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
		if (isSetup() && isEvent() && isNotOnCooldown()) {
			((Attacker) e).calculateDamage(this);
			isSetup = false;
			startCooldown();
		}
	}

	@Override
	public void drawAbility(Entity e, byte d) {
		if (e instanceof Shooter) {
			if (isSetup() && getProgressPercent() < 1
					// TODO ändern,dass explosion angezeigt werden kann
					&& start + beginTime <= Updater.Time.getMillis()
					&& isNotOnCooldown()) {
				((Shooter) e).drawShot(target, getProgressPercent());
			}
			if (explosion != null && getProgressPercent() >= 1 && !isSetup
					&& !isExploding) {
				isExploding = true;
				explosion.setup(null);
			}
		} else {
			System.err.println(e.getClass().getSimpleName()
					+ " should be shooter");
		}
		if (explosion != null && isExploding && !explosion.isFinished()
				&& target != null) {
			explosion.draw(target.x, target.y);
		}
	}

	@Override
	public float getProgressPercent() {
		float f = 1
				- (float) (start + beginTime + eventTime - Updater.Time
						.getMillis()) / eventTime;
		return f > 1 || f < 0 ? 1 : f;
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
