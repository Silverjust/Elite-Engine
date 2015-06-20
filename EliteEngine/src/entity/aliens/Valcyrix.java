package entity.aliens;

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

public class Valcyrix extends Unit implements Attacker, Shooter {

	private static PImage standingImg;

	byte aggroRange;

	ShootAttack basicAttack;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Valcyrix");
	}

	public Valcyrix(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);

		animation = nextAnimation = walk;
		// ************************************
		xSize = 30;
		ySize = 30;

		kerit = 60;
		pax = 0;
		arcanum = 0;
		prunam = 11;
		trainTime = 3000;

		hp = hp_max = 60;
		speed = 1.8f;
		radius = 7;
		sight = 90;
		groundPosition = Entity.GroundPosition.AIR;
		height = 20;

		aggroRange = (byte) (radius + 50);
		basicAttack.range = (byte) (radius + 10);
		basicAttack.damage = 50;
		basicAttack.pirce = 3;
		basicAttack.cooldown = 1500;
		basicAttack.range = 30;
		basicAttack.setCastTime(100);// eventtime is defined by target distance
		basicAttack.speed = 0.6f;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions() {
		if (animation == walk || animation == stand) {// ****************************************************
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
						if (e.isInRange(x, y, basicAttack.range + e.radius)) {
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
			} else if (importantEntity != null && !isEnemyInHitRange) {
				sendAnimation("walk " + importantEntity.x + " "
						+ importantEntity.y);
			}
		}
		basicAttack.updateAbility(this);
	}

	@Override
	public void calculateDamage(Attack a) {
		ref.updater.send("<hit " + basicAttack.getTarget().number + " "
				+ a.damage + " " + a.pirce);
		//SoundHandler.startIngameSound(HUD.hm, x, y);
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
		ref.app.fill(100, 100, 0);
		ref.app.strokeWeight(0);
		ref.app.ellipse(xToGrid(x), yToGrid(y), 3, 3);
		ref.app.strokeWeight(1);
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

}