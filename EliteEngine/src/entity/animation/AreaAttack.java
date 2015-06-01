package entity.animation;

import processing.core.PImage;
import shared.ref;
import entity.Entity;
import entity.Entity.GroundPosition;

public class AreaAttack extends Ability {
	public byte range;
	public byte damage;
	public byte pirce;
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
		if (x > 0 && isEvent() && isNotOnCooldown()) {
			for (Entity e2 : ref.updater.entities) {
				// sometimes attacks ghosts
				if (e2 != null & e2.isEnemyTo(e)
						&& e2.isCollision(x, y, e2.radius + range)
						&& e2.groundPosition == GroundPosition.GROUND) {
					ref.updater.send("<hit " + e2.number + " " + damage + " "
							+ pirce);
				}
			}
			x = -100;
			startCooldown();
		}
	}

}
