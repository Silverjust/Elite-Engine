package entity.animation;

import processing.core.PApplet;
import processing.core.PImage;
import entity.Attacker;
import entity.Entity;
import entity.Shooter;

public class ShootAttack extends Attack {
	private Entity target;
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

	public void setTargetFrom(Entity from, Entity to) {
		target = to;
		eventTime = (int) (PApplet.dist(from.x, from.y, to.x, to.y) / speed);
	}

	@Override
	public void updateAbility(Entity e) {
		if (target != null && isEvent() && isNotOnCooldown()) {
			((Attacker) e).calculateDamage(this);
			target = null;
			startCooldown();
		}
	}

	public Entity getTarget() {
		return target;
	}

	@Override
	public void draw(Entity e, byte d, byte f) {
		super.draw(e, d, f);
		if (e instanceof Shooter && target != null) {
			((Shooter) e).drawShot(target, getProgress());
		}
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
