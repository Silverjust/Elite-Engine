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
import game.ImageHandler;

public class HeavyAssault extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	MeleeAttack basicAttack;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = ImageHandler.load(path, "HeavyAussault");
	}

	public HeavyAssault(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new MeleeAttack(standingImg, 500);// 1000

		setAnimation(walk);

		// ************************************
		xSize = 20;
		ySize = 20;

		kerit = 200;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		trainTime = 2000;

		hp = hp_max = 120;
		armor = 2;
		speed = 0.9f;
		radius = 5;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 50);
		basicAttack.damage = 11;
		basicAttack.pirce = 0;
		basicAttack.cooldown = 500;
		basicAttack.range = 30;
		basicAttack.setCastTime(400);// eventtime is defined by target distance
		basicAttack.doRepeat = true;
		//basicAttack.targetable = groundPosition;

		descr = "heavy assault";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (getAnimation() == walk && isAggro || getAnimation() == stand) {// ****************************************************
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
								if (e.isInRange(x, y, basicAttack.range
										+ e.radius)) {
									isEnemyInHitRange = true;
								}
							}
						}

					}
				}
			}
			if (isEnemyInHitRange && basicAttack.isNotOnCooldown()
					&& !basicAttack.isSetup()) {
				sendAnimation("basicAttack " + importantEntity.number);
			} else if (importantEntity != null) {
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			}
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public void calculateDamage(Attack a) {
		isTaged = true;
		ref.updater.send("<hit " + basicAttack.getTarget().number + " "
				+ a.damage + " " + a.pirce);
		// SoundHandler.startIngameSound(HUD.hm, x, y);
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, currentFrame);
		drawShot();
		drawTaged();
	}

	public void drawShot() {
		if (basicAttack.getTarget() != null
				&& basicAttack.getTarget().isAlive()
				&& getAnimation() == basicAttack) {
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