package entity.animation;

import processing.core.PImage;
import entity.Attacker;
import entity.Entity;

public class MeleeAttack extends Attack {
	protected Entity target;

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
	public void setTargetFrom(Entity from, Entity to) {
		target = to;
	}

	@Override
	public void updateAbility(Entity e) {
		/*if (isSetup())System.out.println("setup");
		if (isEvent())System.out.println("event");
		if (isNotOnCooldown())System.out.println("ncool");*/

		if (isSetup() && isEvent() && isNotOnCooldown()) {
			System.out.println(2);
			((Attacker) e).calculateDamage(this);
			target = null;
			startCooldown();
		}
	}

	public Entity getTarget() {
		return target;
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
