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

	MeleeAttack basicAttack;

	private byte aggroRange;

	private byte healAmount;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "PL0S10N");
	}

	public PL0S10N(String[] c) {
		super(c);

		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new MeleeAttack(standingImg, 800);

		setAnimation(walk);

		// ************************************
		xSize = 15;
		ySize = 15;

		kerit = 400;
		pax = 400;
		arcanum = 0;
		prunam = 0;
		trainTime = 1500;

		hp = hp_max = 300;
		armor = 3;
		speed = 0.9f;
		radius = 7;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = 60;
		basicAttack.damage = 80;
		basicAttack.pirce = 0;
		basicAttack.cooldown = 4000;
		basicAttack.range = 30;
		basicAttack.setCastTime(100);

		descr = " ";
		stats = "heal/s: " + healAmount + "/" + (basicAttack.cooldown / 1000.0);
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isServer
				&& (getAnimation() == walk && isAggro || getAnimation() == stand)) {// ****************************************************
			float importance = 0;
			Entity importantEntity = null;
			for (Entity e : player.visibleEntities) {
				if (e.isAllyTo(this)) {
					if (e.isInRange(x, y, aggroRange + e.radius)
							&& basicAttack.canTargetable(e)) {
						float newImportance = calcImportanceOf(e);
						if (newImportance > importance && e.hp < e.hp_max) {
							importance = newImportance;
							importantEntity = e;
						}
					}

				}

			}
			if (importantEntity != null) {
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			}
		}
		if (basicAttack.isNotOnCooldown()) {
			basicAttack.startCooldown();
			basicAttack.setTargetFrom(this, this);
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public void calculateDamage(Attack a) {
		for (Entity e : ref.updater.entities) {
			if (e != null && e.isInRange(x, y, e.radius + a.range)
					&& e.isEnemyTo(this)) {
				ref.updater.send("<hit " + e.number + " " + basicAttack.damage
						+ " 0");
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