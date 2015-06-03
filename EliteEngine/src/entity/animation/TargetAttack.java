package entity.animation;

import processing.core.PImage;
import shared.ref;
import entity.Attacker;
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

	public static void updateExecAttack(String[] c, Entity attacker) {
		if (c[2].equals("basicAttack") && attacker instanceof Attacker) {
			Ability a = ((Attacker) attacker).getBasicAttack();
			if (a instanceof TargetAttack) {
				int n = Integer.parseInt(c[3]);
				Entity e = ref.updater.namedEntities.get(n);
				((TargetAttack) a).setTarget(e);
			} else if (a instanceof AreaAttack) {
				float x = Float.parseFloat(c[3]);
				float y = Float.parseFloat(c[4]);
				((AreaAttack) a).setPosition(x, y);
			}
			attacker.setAnimation(a);
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
