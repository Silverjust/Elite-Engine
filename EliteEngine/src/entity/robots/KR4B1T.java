package entity.robots;

import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.MeleeAttack;
import processing.core.PImage;
import shared.ref;

public class KR4B1T extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	MeleeAttack basicAttack;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "KR4B1T");
	}

	public KR4B1T(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new MeleeAttack(standingImg, 800);

		setAnimation(walk);

		// ************************************
		xSize = 20;
		ySize = 20;
		height = 0;

		kerit = 100;
		pax = 0;
		arcanum = 0;
		prunam = 1;
		trainTime = 1500;

		hp = hp_max = 100;
		armor = 3;
		speed = 1.2f;
		radius =4;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 50);
		basicAttack.range = (byte) (radius + 10);
		basicAttack.damage = 20;
		basicAttack.pirce = 2;
		basicAttack.cooldown = 800;
		basicAttack.setCastTime(500);
		basicAttack.targetable = groundPosition;

		descr = " ";
		stats = " ";
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
				sendAnimation("basicAttack " + importantEntity.number);
			} else if (importantEntity != null) {
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			}
		}
		basicAttack.updateAbility(this, isServer);
	}

	

	@Override
	public void calculateDamage(Attack a) {
		ref.updater.send("<hit " + basicAttack.getTarget().number + " "
				+ a.damage + " " + a.pirce);
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, currentFrame);
		drawTaged();
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

}
