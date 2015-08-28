package entity.humans;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;
import entity.Attacker;
import entity.Entity;
import entity.Shooter;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.ShootAttack;

public class Helicopter extends Unit implements Attacker, Shooter {

	private static PImage standingImg;

	byte aggroRange;

	ShootAttack basicAttack;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Helicopter");
	}

	public Helicopter(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);

		setAnimation(walk);

		// ************************************
		xSize = 30;
		ySize = 30;

		kerit = 1000;
		pax = 0;
		arcanum = 0;
		prunam = 50;
		trainTime = 1500;

		hp = hp_max = 200;
		armor = 1;
		speed = 1.5f;
		radius = 10;
		sight = 120;
		height = 20;
		groundPosition = Entity.GroundPosition.AIR;

		aggroRange = 120;
		basicAttack.damage = 100;
		basicAttack.pirce = 1;
		basicAttack.cooldown = 2000;
		basicAttack.range = 120;
		basicAttack.setCastTime(100);// eventtime is defined by target distance
		basicAttack.speed = 0.6f;

		descr = "heli ";
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
						if (e.isInRange(x, y, aggroRange + e.radius)) {
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
								if (e.isInRange(x, y, basicAttack.range
										+ e.radius)
										&& e.groundPosition == GroundPosition.AIR) {
									isEnemyInHitRange = true;
								} else if (e.isInRange(x, y, basicAttack.range
										/ 2 + e.radius)
										&& e.groundPosition == GroundPosition.GROUND) {
									isEnemyInHitRange = true;
								}
							}
						}
					}
				}
			}
			if (isEnemyInHitRange && basicAttack.isNotOnCooldown()) {
				sendAnimation("basicAttack " + importantEntity.number);
			} else if (importantEntity != null) {
				Attack.sendWalkToEnemy(
						this,
						importantEntity,
						(byte) (importantEntity.groundPosition == GroundPosition.GROUND ? basicAttack.range / 2
								: basicAttack.range));
			}
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public void calculateDamage(Attack a) {
		ref.updater.send("<hit " + basicAttack.getTarget().number + " "
				+ a.damage + " " + a.pirce);
		// SoundHandler.startIngameSound(HUD.hm, x, y);
	}

	@Override
	public void renderAir() {
		drawSelected();
		getAnimation().draw(this, direction, currentFrame);
		basicAttack.drawAbility(this, direction);
		drawTaged();
	}

	@Override
	public void drawShot(Entity target, float progress) {
		float x = PApplet.lerp(this.x, target.x, progress);
		float y = PApplet.lerp(this.y - height, target.y - target.height,
				progress);
		ref.app.fill(50);
		ref.app.strokeWeight(0);
		ref.app.ellipse(xToGrid(x), yToGrid(y), 2, 2);
		ref.app.strokeWeight(1);
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

}