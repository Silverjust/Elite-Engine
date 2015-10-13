package entity.robots;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;
import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.MeleeAttack;

public class SW4RM extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	MeleeAttack basicAttack;

	public static void loadImages() {
		// String path = path(new Object() {
		// });
		// standingImg = game.ImageHandler.load(path, "SW4RM");
	}

	public SW4RM(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new MeleeAttack(standingImg, 2000);

		setAnimation(walk);

		// ************************************
		xSize = 15;
		ySize = 15;

		kerit = 170;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		trainTime = 2500;

		hp = 100;
		hp_max = 400;
		armor = 1;
		speed = 0.9f;
		radius = hpToRadius();
		sight = 70;
		groundPosition = Entity.GroundPosition.AIR;

		aggroRange = (byte) (radius + 50);
		basicAttack.damage = 10;
		basicAttack.pirce = 0;
		basicAttack.cooldown = 1000;
		basicAttack.range = radius;
		basicAttack.setCastTime(500);// eventtime is defined by target distance

		descr = "B0T§can move while§attack";
		// ************************************
	}

	private byte hpToRadius() {
		return (byte) (hp * 0.1);
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isServer
				&& (getAnimation() == walk && isAggro || getAnimation() == stand)) {// ****************************************************
			boolean isEnemyInHitRange = false;
			float importance = 0;
			Entity importantEntity = null;
			for (Entity e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isInRange(x, y, aggroRange + e.radius)
								&& basicAttack.canTargetable(e)) {
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
						}
						if (e.isInRange(x, y, basicAttack.range + e.radius)
								&& basicAttack.canTargetable(e)) {
							isEnemyInHitRange = true;
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
						}
					}
				}
			}
			if (isEnemyInHitRange && basicAttack.isNotOnCooldown()) {
				sendAnimation("basicAttack " + importantEntity.number);
			} else if (importantEntity != null && basicAttack.isNotOnCooldown()) {
				// änderung wegen kiter
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			}
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		// move while attack
		if ("walk".equals(c[2])) {
			xTarget = Float.parseFloat(c[3]);
			yTarget = Float.parseFloat(c[4]);
			if (PApplet.dist(x, y, xTarget, yTarget) >= speed) {
				isMoving = true;
				// isAggro = Boolean.valueOf(c[5]);
				// setAnimation(walk);
			} else {
				isMoving = false;
				// setAnimation(stand);
			}
		}
		if (c[2].equals("basicAttack"))
			isMoving = true;
	}

	@Override
	public void calculateDamage(Attack a) {
		for (Entity e : ref.updater.entities) {
			if (e != null && e.isInRange(x, y, e.radius + a.range)
					&& a.canTargetable(e) && e.isEnemyTo(this)) {
				ref.updater.send("<hit " + e.number + " " + a.damage + " "
						+ a.pirce);
				if (e.isMortal())
					hp += a.damage
							* (1.0 - ((e.armor - a.pirce > 0) ? e.armor
									- a.pirce : 0) * 0.05);
			}
		}
	}

	@Override
	public void renderAir() {
		drawSelected();
		getAnimation().draw(this, direction, currentFrame);
		basicAttack.drawAbility(this, direction);
		drawTaged();
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

}