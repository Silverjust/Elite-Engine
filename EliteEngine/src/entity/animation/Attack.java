package entity.animation;

import entity.Attacker;
import entity.Entity;
import processing.core.PImage;
import shared.ref;

public class Attack extends Ability {
	public byte range;
	public byte damage;
	public byte pirce;

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

	public static void updateExecAttack(String[] c, Entity attacker) {
		if (c[2].equals("basicAttack") && attacker instanceof Attacker) {
			Attack a = ((Attacker) attacker).getBasicAttack();
			if (a.isNotOnCooldown() && !a.isSetup()) {
				int n = Integer.parseInt(c[3]);
				Entity e = ref.updater.namedEntities.get(n);
				a.setTargetFrom(attacker, e);
				attacker.setAnimation(a);
			}
		}
	}

}
