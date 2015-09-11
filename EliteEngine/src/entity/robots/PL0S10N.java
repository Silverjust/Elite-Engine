package entity.robots;

import processing.core.PImage;
import shared.ref;
import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.MeleeAttack;

public class PL0S10N extends Unit implements Attacker {

	private static PImage standingImg;
	private static PImage attackImg;

	MeleeAttack basicAttack;

	private byte aggroRange;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "PL0S10N");
		attackImg = game.ImageHandler.load(path, "PL0S10N_plode");
	}

	public PL0S10N(String[] c) {
		super(c);

		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new MeleeAttack(attackImg, 1500);

		setAnimation(walk);

		// ************************************
		xSize = 25;
		ySize = 25;
		height = 10;

		kerit = 400;
		pax = 400;
		arcanum = 0;
		prunam = 0;
		trainTime = 1500;

		hp = hp_max = 300;
		armor = 3;
		speed = 0.9f;
		radius = 9;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = 60;
		basicAttack.damage = 80;
		basicAttack.pirce = 0;
		basicAttack.cooldown = 4000;
		basicAttack.range = 30;
		basicAttack.setCastTime(100);

		descr = " ";
		// ************************************
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
				sendAnimation("basicAttack " + number);
			} else if (importantEntity != null && basicAttack.isNotOnCooldown()) {
				// änderung wegen kiter
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			}
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public void calculateDamage(Attack a) {
		for (Entity e : ref.updater.entities) {
			if (e != null && e.isInRange(x, y, e.radius + a.range)
					&& a.canTargetable(e) && e.isEnemyTo(this)) {
				ref.updater.send("<hit " + e.number + " " + a.damage + " "
						+ a.pirce);
			}
		}
	}

	@Override
	public void renderGround() {
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