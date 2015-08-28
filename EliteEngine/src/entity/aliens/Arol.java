package entity.aliens;

import entity.Attacker;
import entity.Building;
import entity.Entity;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.MeleeAttack;
import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;

public class Arol extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	MeleeAttack basicAttack;
	byte attackDistance;

	private float xDirection, yDirection;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Arol");
	}

	public Arol(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new MeleeAttack(standingImg, 800);

		setAnimation(walk);

		// ************************************
		xSize = 50;
		ySize = 35;

		kerit = 650;
		pax = 0;
		arcanum = 100;
		prunam = 0;
		trainTime = 10000;

		hp = hp_max = 1900;
		armor = 3;
		speed = 0.4f;
		radius = 10;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 50);
		basicAttack.range = 10;
		basicAttack.damage = 40;
		basicAttack.cooldown = 2000;
		basicAttack.setCastTime(100);
		basicAttack.targetable = groundPosition;
		attackDistance = 10;

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
								&& e.groundPosition == GroundPosition.GROUND) {
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
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("basicAttack")) {
			xDirection = basicAttack.getTarget().x;
			yDirection = basicAttack.getTarget().y;
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		float x, y;
		x = (this.x + (xDirection - this.x)
				/ PApplet.dist(this.x, this.y, xDirection, yDirection)
				* (attackDistance));
		y = (this.y + (yDirection - this.y)
				/ PApplet.dist(this.x, this.y, xDirection, yDirection)
				* (attackDistance));
		for (Entity e : ref.updater.entities) {
			if (e != null & e.isEnemyTo(this)
					&& e.isInRange(x, y, e.radius + a.range)
					&& basicAttack.canTargetable(e)) {
				ref.updater.send("<hit " + e.number + " "
						+ (e instanceof Building ? a.damage * 2 : a.damage)
						+ " " + a.pirce);
			}
		}
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, currentFrame);
		drawTaged();
	}

	@Override
	public void renderRange() {
		if (this instanceof Unit) {
			ref.app.line(x, y / 2, ((Unit) this).xTarget,
					((Unit) this).yTarget / 2);
		}
		float x, y;
		x = (this.x + (xDirection - this.x)
				/ PApplet.dist(this.x, this.y, xDirection, yDirection)
				* (attackDistance));
		y = (this.y + (yDirection - this.y)
				/ PApplet.dist(this.x, this.y, xDirection, yDirection)
				* (attackDistance));
		drawCircle(x, y, basicAttack.range);
		drawCircle(x, y,
				(byte) (basicAttack.range * basicAttack.getCooldownPercent()));
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

}
