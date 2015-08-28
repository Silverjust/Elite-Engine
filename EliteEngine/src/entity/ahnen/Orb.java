package entity.ahnen;

import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.MeleeAttack;
import processing.core.PImage;
import shared.ref;

public class Orb extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	MeleeAttack basicAttack;

	private int parent;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Orb");
	}

	public Orb(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new MeleeAttack(standingImg, 800);

		setAnimation(walk);

		if (c != null && c.length > 7 && c[7] != null) {
			parent = Integer.parseInt(c[7]);

		}

		// ************************************
		xSize = 7;
		ySize = 7;
		height = 30;

		kerit = 0;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		trainTime = 1500;

		hp = hp_max = 50;
		speed = 1.2f;
		radius = 5;
		sight = 70;
		groundPosition = Entity.GroundPosition.AIR;

		aggroRange = (byte) (radius + 50);
		basicAttack.range = 9;
		basicAttack.damage = 40;
		basicAttack.cooldown = 2000;
		basicAttack.setCastTime(500);

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void onStart(boolean isServer) {
		if (isServer) {
			Entity e;
			e = ref.updater.namedEntities.get(parent);
			e.sendAnimation("spawned " + number);
		}
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
