package entity.scientists;

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
import entity.animation.MeleeAttack;
import entity.animation.ShootAttack;

public class AirshipGuineaPig extends Unit implements Attacker, Shooter {

	private static PImage standingImg;

	byte aggroRange;
	boolean isAnchored;
	ShootAttack basicAttack;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "AirshipGuineaPig");
	}

	public AirshipGuineaPig(String[] c) {
		super(c);
		GuineaPig.setupEquip(this, c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);

		animation = nextAnimation = walk;
		// ************************************
		xSize = 20;
		ySize = 20;

		kerit = 800;
		pax = 0;
		arcanum = 150;
		prunam = 0;
		trainTime = 1500;

		hp = hp_max = 700;
		armor = 4;
		speed = 0.7f;
		radius = 9;
		sight = 70;
		height = 25;
		groundPosition = Entity.GroundPosition.AIR;

		aggroRange = (byte) (radius + 50);
		basicAttack.damage = 50;
		basicAttack.pirce = 3;
		basicAttack.cooldown = 5000;
		basicAttack.range = 120;
		basicAttack.setCastTime(200);// eventtime is defined by target distance
		basicAttack.speed = 1f;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions() {
		if (animation == walk&& isAggro || animation == stand) {// ****************************************************
			boolean isEnemyInHitRange = false;
			float importance = 0;
			Entity importantEntity = null;
			for (Entity e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isInRange(x, y, aggroRange + e.radius)
								/*&& (e.groundPosition == GroundPosition.AIR || isAnchored)*/) {
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
							if (e.isInRange(x, y, basicAttack.range + e.radius))
								isEnemyInHitRange = true;
						}
					}
				}
			}
			if (isEnemyInHitRange && basicAttack.isNotOnCooldown()) {
				sendAnimation("basicAttack " + importantEntity.number);
			} else if (importantEntity != null && !isEnemyInHitRange) {
				Attack.sendWalkToEnemy(this,importantEntity);
			}
		}
		basicAttack.updateAbility(this);
	}

	@Override
	public void calculateDamage(Attack a) {
		ref.updater
				.send("<hit "
						+ basicAttack.getTarget().number
						+ " "
						+ (((MeleeAttack) a).getTarget().groundPosition == GroundPosition.AIR ? a.damage
								: a.damage / 2) + " " + a.pirce);
		// SoundHandler.startIngameSound(HUD.hm, x, y);
	}

	@Override
	public void renderAir() {
		drawSelected();
		animation.draw(this, direction, currentFrame);
		basicAttack.drawAbility(this, direction);
		drawTaged();
	}

	@Override
	public void drawShot(Entity target, float progress) {
		float x = PApplet.lerp(this.x, target.x, progress);
		float y = PApplet.lerp(this.y - height, target.y - target.height,
				progress);
		ref.app.fill(255, 100, 0);
		ref.app.strokeWeight(0);
		ref.app.ellipse(xToGrid(x), yToGrid(y), 1, 1);
		ref.app.strokeWeight(1);
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	protected void sendWalkToEnemy(Entity e, Entity target) {
	}

}