package entity.humans;

import processing.core.PImage;
import shared.ref;
import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.MeleeAttack;

public class HeavyAssault extends Unit implements Attacker {
	//FIXME schßt nicht immer

	private static PImage standingImg;

	byte aggroRange;

	MeleeAttack basicAttack;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "HeavyAssault");
	}

	public HeavyAssault(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new MeleeAttack(standingImg, 1000);

		animation = nextAnimation = walk;
		// ************************************
		xSize = 10;
		ySize = 10;

		kerit = 180;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		trainTime = 1500;

		hp = hp_max = 120;
		armor = 2;
		speed = 0.9f;
		radius = 5;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 50);
		basicAttack.damage = 10;
		basicAttack.pirce = 0;
		basicAttack.cooldown = 1000;
		basicAttack.range = 30;
		basicAttack.setCastTime(1000);// eventtime is defined by target distance

		descr = "scout zum scouten";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions() {
		if (animation == walk || animation == stand) {// ****************************************************
			boolean isEnemyInHitRange = false;
			float importance = 0;
			Entity importantEntity = null;
			for (Entity e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isInRange(x, y, aggroRange + e.radius)) {
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
						}
						if (e.isInRange(x, y, basicAttack.range + e.radius)) {
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
			if (isEnemyInHitRange && basicAttack.isNotOnCooldown()
					&& !basicAttack.isSetup()) {
				sendAnimation("basicAttack " + importantEntity.number);
			} else if (importantEntity != null && !isEnemyInHitRange) {
				sendAnimation("walk " + importantEntity.x + " "
						+ importantEntity.y);
			}
		}
		basicAttack.updateAbility(this);
	}

	/*
	 * @Override public void sendDefaultAnimation(Animation oldAnimation) { /if
	 * (oldAnimation == basicAttack) { Entity target = ((MeleeAttack)
	 * oldAnimation).getTarget(); if (target != null && target .isInRange(x, y,
	 * target.radius + basicAttack.range)) { sendAnimation("basicAttack " +
	 * target.number); } else { sendAnimation("walk " + xTarget + " " +
	 * yTarget); } } else { sendAnimation("walk " + xTarget + " " + yTarget); }
	 * }
	 */

	@Override
	public void calculateDamage(Attack a) {
		System.out.println(3);

		ref.updater.send("<hit " + basicAttack.getTarget().number + " "
				+ a.damage + " " + a.pirce);
		// SoundHandler.startIngameSound(HUD.hm, x, y);
	}

	@Override
	public void renderGround() {
		drawSelected();
		animation.draw(this, direction, currentFrame);
		drawShot();
		drawTaged();
	}

	public void drawShot() {
		if (basicAttack.getTarget() != null) {
			Entity e = basicAttack.getTarget();
			ref.app.stroke(255, 100, 0);
			ref.app.line(xToGrid(x), yToGrid(y), xToGrid(e.x), yToGrid(e.y));
			ref.app.stroke(0);
		}
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

}