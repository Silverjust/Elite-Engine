package entity.animation;

import processing.core.PImage;
import shared.ref;
import entity.Entity;

public class TargetAttack extends Ability {

	public byte range;
	public byte damage;
	public byte pirce;
	private Entity target;

	public TargetAttack(PImage[][] IMG, int duration) {
		super(IMG, duration);
	}

	public TargetAttack(PImage[] IMG, int duration) {
		super(IMG, duration);
	}

	public TargetAttack(PImage IMG, int duration) {
		super(IMG, duration);
	}

	public void setTarget(Entity e) {
		target = e;
	}

	@Override
	public void updateAbility(Entity e) {
		if (target != null && isEvent() && isNotOnCooldown()) {
			ref.updater.send("<hit " + target.number + " " + damage + " "
					+ pirce);
			target = null;
			startCooldown();
		}
	}

	public Entity getTarget() {
		return target;
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
