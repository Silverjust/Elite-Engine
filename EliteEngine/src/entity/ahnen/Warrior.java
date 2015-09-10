package entity.ahnen;

import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.ahnen.Leuchte.Upgrade;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.MeleeAttack;
import processing.core.PImage;
import shared.ref;

public class Warrior extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	MeleeAttack basicAttack;

	private byte bonusSpeed;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Warrior");
	}

	public Warrior(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new MeleeAttack(standingImg, 500);

		setAnimation(walk);

		// ************************************
		xSize = 15;
		ySize = 15;
		height = 10;

		kerit = 250;
		pax = 0;
		arcanum = 15;
		prunam = 0;
		trainTime = 3000;

		hp = hp_max = 400;
		armor = 3;
		speed = 1.5f;
		bonusSpeed = 1;
		radius = 7;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 50);
		basicAttack.range = 11;
		basicAttack.damage = 30;
		basicAttack.pirce = 2;
		basicAttack.cooldown = 800;
		basicAttack.setCastTime(300);
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
	public void updateMovement() {
		boolean speedboost = false;
		for (Entity e : ref.updater.entities) {
			if (e instanceof Leuchte
					&& ((Leuchte) e).upgrade != Upgrade.STANDARD
					&& isInRange(e.x, e.y, ((Leuchte) e).getBasicAttack().range))
				speedboost = true;
		}
		if (speedboost) {
			speed += bonusSpeed;
		}
		super.updateMovement();
		if (speedboost) {
			speed -= bonusSpeed;
		}
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
