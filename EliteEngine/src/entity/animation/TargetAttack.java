package entity.animation;

import processing.core.PImage;
import shared.ref;
import entity.Entity;

public class TargetAttack extends Ability {

	public byte range;
	public byte damage;
	public Entity target;

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
			ref.updater.send("<hit " + target.number + " " + damage);
			target = null;
			startCooldown();
		}
	}
}
