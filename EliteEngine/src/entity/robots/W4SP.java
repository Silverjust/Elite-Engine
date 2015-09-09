package entity.robots;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;
import entity.Attacker;
import entity.Entity;
import entity.MultiCDActive;
import entity.Shooter;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.Explosion;
import entity.animation.MeleeAttack;
import entity.animation.ShootAttack;

public class W4SP extends Unit implements Attacker, Shooter {

	private static PImage standingImg;

	byte aggroRange;

	ShootAttack basicAttack, basicAttack2;

	private byte splashrange;

	private MeleeAttack speeding;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "W4SP");
	}

	public W4SP(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = walk = new Animation(standingImg, 1000);
		// walk = new Animation(standingImg, 1000);
		speeding = new MeleeAttack(standingImg, 1000);// for 2 cooldowns
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);
		basicAttack2 = new ShootAttack(standingImg, 800);
		basicAttack.explosion = new Explosion(standingImg, 800);

		setAnimation(walk);

		// ************************************
		xSize = 40;
		ySize = 40;
		height = 30;

		kerit = 200;
		pax = 0;
		arcanum = 0;
		prunam = 10;
		trainTime = 5000;

		hp = hp_max = 100;
		armor = 3;
		speed = 0.8f;
		radius = 5;
		sight = 70;
		groundPosition = Entity.GroundPosition.AIR;

		aggroRange = 60;
		splashrange = 10;
		basicAttack.damage = 60;
		basicAttack.pirce = 0;
		basicAttack.cooldown = 3000;
		basicAttack.range = 45;
		basicAttack.setCastTime(100);// eventtime is defined by target distance
		basicAttack.speed = 0.6f;
		basicAttack.targetable = GroundPosition.GROUND;

		speeding.setCastTime(1800);
		speeding.cooldown = 20000;

		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isServer
				&& ((getAnimation() == walk && isMoving) && isAggro || (getAnimation() == stand && !isMoving))) {
			// ****************************************************
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
							if (e.isInRange(x, y, basicAttack.range + e.radius))
								isEnemyInHitRange = true;
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
		if (speeding.isSetup() && speeding.isEvent()) {
			System.out.println("W4SP.updateDecisions()");
			speeding.setTargetFrom(null, null);
			sendAnimation("speeddown");
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("speed")) {
			if (speeding.isNotOnCooldown()) {
				armor = 5;
				speed += 0.7f;
				speeding.setTargetFrom(this, this);
				speeding.setup(this);
			}
		} else if (c[2].equals("speeddown")) {
			armor = 3;
			speed -= 0.7f;
			// speeding.setup(this);
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		// ref.updater.send("<hit " + basicAttack.getTarget().number + " "
		// + a.damage + " " + a.pirce);
		Entity target = ((ShootAttack) a).getTarget();
		for (Entity e : ref.updater.entities) {
			if (e != null & e.isEnemyTo(this)
					&& e.isInRange(target.x, target.y, e.radius + splashrange)
					&& e.groundPosition == GroundPosition.GROUND) {
				ref.updater.send("<hit " + e.number + " " + a.damage + " "
						+ a.pirce);
			}
		}
	}

	@Override
	public void display() {
		super.display();
		if (speeding.isSetup() && !speeding.isEvent())
			drawBar(speeding.getProgressPercent());
	}

	@Override
	public void renderAir() {
		drawSelected();
		if (speeding.isSetup() && !speeding.isEvent()) {
			if (getAnimation() == stand)
				speeding.draw(this, direction, currentFrame);
			else if (getAnimation() == basicAttack)
				basicAttack2.draw(this, direction, currentFrame);
		} else
			getAnimation().draw(this, direction, currentFrame);
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
		// if (progress < 0.9) {
		ref.app.ellipse(xToGrid(x), yToGrid(y), 1, 1);
		// } else {
		// ref.app.ellipse(xToGrid(x), yToGrid(y), splashrange * 2,
		// splashrange);
		// }
		ref.app.strokeWeight(1);
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	public Attack getAbility() {
		return speeding;
	}

	public static class SpeedActive extends MultiCDActive {
		public SpeedActive(int x, int y, char n) {
			super(x, y, n, standingImg);
			clazz = W4SP.class;
			setAbilityGetter("getAbility");
		}

		@Override
		public String getDesription() {
			return "speeds up and§gives armor";
		}

		@Override
		public void onActivation() {
			for (Entity e : ref.updater.selected) {
				if (e instanceof W4SP) {
					e.sendAnimation("speed");
				}
			}
		}
	}
}