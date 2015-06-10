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
			} else if (a instanceof ShootAttack) {
				int n = Integer.parseInt(c[3]);
				Entity e = ref.updater.namedEntities.get(n);
				((ShootAttack) a).setTargetFrom(attacker, e);
			}
			attacker.setAnimation(a);
		}
	}
}
