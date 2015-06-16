package entity.animation;

import processing.core.PImage;
import shared.ref;
import entity.Attacker;
import entity.Entity;

public class AreaAttack extends Attack {
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
			((Attacker) e).calculateDamage(this);
			x = -100;
			startCooldown();
		}
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
