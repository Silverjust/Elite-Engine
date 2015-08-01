package entity.animation;

import processing.core.PImage;
import entity.Attacker;
import entity.Entity;

public class MeleeAttack extends Attack {
	protected boolean isSetup;

	public MeleeAttack(PImage[][] IMG, int duration) {
		super(IMG, duration);
	}

	public MeleeAttack(PImage[] IMG, int duration) {
		super(IMG, duration);
	}

	public MeleeAttack(PImage IMG, int duration) {
		super(IMG, duration);
	}

	@Override
	public void setup(Entity e) {
		super.setup(e);
		if (isNotOnCooldown() && doRepeat(e))
			if (getTarget() != null && getTarget().isAlive()) {
				System.out.println("MeleeAttack.setup()");
				isSetup = true;
				startCooldown();
			} else {
				e.sendDefaultAnimation(this);
			}
	}

	@Override
	public void setTargetFrom(Entity from, Entity to) {
		target = to;
		isSetup = true;
	}

	@Override
	public void updateAbility(Entity e, boolean isServer) {
		/*
		 * if (isSetup())System.out.println("setup"); if
		 * (isEvent())System.out.println("event"); if
		 * (isNotOnCooldown())System.out.println("ncool");
		 */
		if (isSetup() && isEvent()) {
			if (isServer)
				((Attacker) e).calculateDamage(this);
			isSetup = false;
		}
	}

	@Override
	public boolean isSetup() {
		return getTarget() != null && isSetup;
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
