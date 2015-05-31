package entity.animation;

import processing.core.PImage;
import shared.ref;
import entity.Entity;

public class AreaAttack extends Ability {
	public byte range;
	public byte damage;
	float x, y;

	public AreaAttack(PImage[][] IMG, int duration) {
		super(IMG, duration);
	}

	public AreaAttack(PImage[] IMG, int duration) {
		super(IMG, duration);
	}

	public AreaAttack(PImage IMG, int duration) {
		super(IMG, duration);
	}

	public void setPosition(float X, float Y) {
		x = X;
		y = Y;
	}

	@Override
	public void updateAbility(Entity e) {
		if (isEvent() && isNotOnCooldown()) {
			if (damage > 0) {
				for (Entity e2 : ref.updater.entities) {
					// sometimes attacks ghosts
					if (e2 != null & e2.isEnemyTo(e)
							&& e2.isCollision(x, y, e2.radius + range)) {
						ref.updater.send("<hit " + e2.number + " " + damage);
					}
				}
			} else {
				for (Entity e2 : ref.updater.entities) {
					if (e2 != null & e2.isAllyTo(e)
							&& e2.isCollision(x, y, e2.radius + range)) {
						ref.updater.send("<hit " + e2.number + " " + damage);
					}
				}
			}
			startCooldown();
		}
	}

}
