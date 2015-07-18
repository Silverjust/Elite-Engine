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

		animation = nextAnimation = walk;
		// ************************************
		xSize = 50;
		ySize = 35;

		kerit = 700;
		pax = 0;
		arcanum = 130;
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
		basicAttack.cooldown = 5000;
		basicAttack.setCastTime(100);
		attackDistance = 10;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions() {
		if (animation == walk && isAggro || animation == stand) {// ****************************************************
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
				Attack.sendWalkToEnemy(this,importantEntity);
			}
		}
		basicAttack.updateAbility(this);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("basicAttack") && basicAttack.isNotOnCooldown()
				&& !basicAttack.isSetup()) {
			xTarget = basicAttack.getTarget().x;
			yTarget = basicAttack.getTarget().y;
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		float x, y;
		x = (this.x + (xTarget - this.x)
				/ PApplet.dist(this.x, this.y, xTarget, yTarget)
				* (attackDistance));
		y = (this.y + (yTarget - this.y)
				/ PApplet.dist(this.x, this.y, xTarget, yTarget)
				* (attackDistance));
		for (Entity e : ref.updater.entities) {
			if (e != null & e.isEnemyTo(this)
					&& e.isInRange(x, y, e.radius + a.range)
					&& e.groundPosition == GroundPosition.GROUND) {
				ref.updater.send("<hit " + e.number + " "
						+ (e instanceof Building ? a.damage * 2 : a.damage)
						+ " " + a.pirce);
			}
		}
	}

	@Override
	public void renderGround() {
		drawSelected();
		float x, y;
		x = (this.x + (xTarget - this.x)
				/ PApplet.dist(this.x, this.y, xTarget, yTarget)
				* (attackDistance));
		y = (this.y + (yTarget - this.y)
				/ PApplet.dist(this.x, this.y, xTarget, yTarget)
				* (attackDistance));
		drawCircle(x, y, basicAttack.range);
		animation.draw(this, direction, currentFrame);
		drawTaged();
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	protected void sendWalkToEnemy(Entity e, Entity target) {
	}

}
