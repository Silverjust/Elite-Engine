package entity.animation;

import entity.Attacker;
import entity.Entity;
import entity.Unit;
import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;

public class Attack extends Ability {
	public byte range;
	public byte damage;
	public byte pirce;
	protected Entity target;

	public Attack(PImage[][] IMG, int duration) {
		super(IMG, duration);
	}

	public Attack(PImage[] IMG, int duration) {
		super(IMG, duration);
	}

	public Attack(PImage IMG, int duration) {
		super(IMG, duration);
	}

	public void setTargetFrom(Entity attacker, Entity e) {
	}

	@Override
	public boolean isReady() {
		return isNotOnCooldown() && isSetup();
	}

	public Entity getTarget() {
		return target;
	}

	public static void updateExecAttack(String[] c, Entity attacker) {
		if (c[2].equals("basicAttack") && attacker instanceof Attacker) {
			Attack a = ((Attacker) attacker).getBasicAttack();
			if (a.isNotOnCooldown() && !a.isSetup()) {
				int n = Integer.parseInt(c[3]);
				Entity e = ref.updater.namedEntities.get(n);
				a.setTargetFrom(attacker, e);
				attacker.setAnimation(a);
				if (attacker instanceof Unit) {
					((Unit) attacker).isMoving = false;
				}
			}
		} else if (c[2].equals("setTarget") && attacker instanceof Attacker) {
			// Attack a = ((Attacker) attacker).getBasicAttack();
			int n = Integer.parseInt(c[3]);
			Entity e = ref.updater.namedEntities.get(n);
			attacker.sendAnimation("walk " + e.x + " " + e.y + " true");
			// a.setTargetFrom(attacker, e);

			// walk to target and attack
		}
	}

	public static void sendWalkToEnemy(Entity e, Entity target) {
		if (e instanceof Attacker) {
			if (((Attacker) e).getBasicAttack().range < PApplet.dist(target.x,
					target.y, e.x, e.y)) {
				e.sendAnimation("walk "
						+ (target.x + (e.x - target.x)
								/ PApplet.dist(target.x, target.y, e.x, e.y)
								* ((Attacker) e).getBasicAttack().range)
						+ " "
						+ (target.y + (e.y - target.y)
								/ PApplet.dist(target.x, target.y, e.x, e.y)
								* ((Attacker) e).getBasicAttack().range)
						+ " true");
			} else {
				e.sendAnimation("stand");
			}
		}
	}
}
