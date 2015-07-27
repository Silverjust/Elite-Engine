package entity.aliens;

import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.MeleeAttack;
import processing.core.PImage;
import shared.ref;

public class Brux extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	MeleeAttack basicAttack;
	Jump jump;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Brux");
	}

	public Brux(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new MeleeAttack(standingImg, 800);
		jump = new Jump(standingImg, 800);

		setAnimation(walk);

		// ************************************
		xSize = 30;
		ySize = 30;

		kerit = 180;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		trainTime = 3000;

		hp = hp_max = 160;
		speed = 0.8f;
		radius = 8;
		sight = 80;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 50);
		basicAttack.range = (byte) (radius + 10);
		basicAttack.damage = 15;
		basicAttack.cooldown = 1200;
		basicAttack.setCastTime(500);

		jump.range = (byte) (radius + 10);
		jump.damage = 20;
		jump.pirce = 2;
		jump.cooldown = 7000;
		jump.speed = 2.2f;

		descr = " ";
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
			} else if (importantEntity != null && !isEnemyInHitRange
					&& jump.isNotOnCooldown()) {
				sendAnimation("jump " + importantEntity.number);
			} else if (importantEntity != null) {
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			}
		}
		basicAttack.updateAbility(this, isServer);
		jump.updateAbility(this, isServer);
	}

	@Override
	public void updateMovement() {
		if (getAnimation() == jump) {
			speed += jump.speed;
		}
		super.updateMovement();
		if (getAnimation() == jump) {
			speed -= jump.speed;
		}
	}

	@Override
	public boolean isCollision(Entity e) {
		boolean b = !(getAnimation() == jump && e != jump.getTarget());
		return super.isCollision(e) && b;
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("jump")) {
			int n = Integer.parseInt(c[3]);
			Entity e = ref.updater.namedEntities.get(n);
			jump.setTargetFrom(this, e);
			xTarget = e.x;
			yTarget = e.y;
			setAnimation(jump);
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

	public static class Jump extends MeleeAttack {

		public float speed;

		public Jump(PImage IMG, int duration) {
			super(IMG, duration);
		}

		@Override
		public void updateAbility(Entity e, boolean isServer) {
			if (target != null && isNotOnCooldown()
					&& target.isInRange(e.x, e.y, e.radius + target.radius)) {
				if (isServer) {
					ref.updater.send("<hit " + target.number + " " + damage
							+ " " + pirce);
					e.sendDefaultAnimation(this);
				}
				target = null;
				isSetup = false;
				// startCooldown();
			}
		}

	}

}
