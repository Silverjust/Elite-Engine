package entity.animation;

import processing.core.PImage;
import shared.ref;
import entity.Entity;

public class AreaHeal extends Ability {
	public byte range;
	public byte heal;
	float x, y;

	public AreaHeal(PImage[][] IMG, int duration) {
		super(IMG, duration);
	}

	public AreaHeal(PImage[] IMG, int duration) {
		super(IMG, duration);
	}

	public AreaHeal(PImage IMG, int duration) {
		super(IMG, duration);
	}

	public void setPosition(float X, float Y) {
		x = X;
		y = Y;
	}

	@Override
	public void updateAbility(Entity e) {
		if (isEvent() && isNotOnCooldown()) {
			for (Entity e2 : ref.updater.entities) {
				if (e2 != null & e2.isAllyTo(e)
						&& e2.isInArea(x, y, e2.radius + range)) {
					ref.updater.send("<heal " + e2.number + " " + heal);
				}
			}
			startCooldown();
		}
	}

}
