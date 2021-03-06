package entity.humans;

import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.MeleeAttack;
import processing.core.PImage;
import shared.ref;

public class Exo extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	MeleeAttack basicAttack;
	MeleeAttack instaAttack;
	Hook hook;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Exo");
	}

	public Exo(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new MeleeAttack(standingImg, 800);
		instaAttack = new MeleeAttack(standingImg, 800);
		hook = new Hook(standingImg, 800);

		setAnimation(walk);

		// ************************************
		xSize = 20;
		ySize = 20;

		kerit = 127;
		pax = 0;
		arcanum = 40;
		prunam = 0;
		trainTime = 3000;

		hp = hp_max = 160;
		speed = 0.7f;
		radius = 8;
		sight = 80;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 50);
		basicAttack.range = (byte) (radius + 10);
		basicAttack.damage = 30;
		basicAttack.cooldown = 1200;
		basicAttack.setCastTime(500);
		basicAttack.targetable=groundPosition;

		instaAttack.range = (byte) (radius + 10);
		instaAttack.damage = 30;
		instaAttack.cooldown = 4000;
		instaAttack.setCastTime(100);

		hook.range = (byte) (radius + 10);
		hook.damage = 55;
		hook.pirce = 2;
		hook.cooldown = 5000;
		hook.speed = 2.2f;

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
			} else if (importantEntity != null && !isEnemyInHitRange
					&& hook.isNotOnCooldown()) {
				sendAnimation("hook " + importantEntity.number);
			} else if (importantEntity != null) {
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			}
		}
		basicAttack.updateAbility(this, isServer);
		instaAttack.updateAbility(this, isServer);
		hook.updateAbility(this, isServer);
	}

	@Override
	public void updateMovement() {
		if (getAnimation() == hook) {
			speed += hook.speed;
		}
		super.updateMovement();
		if (getAnimation() == hook) {
			speed -= hook.speed;
		}
	}

	@Override
	public boolean isCollision(Entity e) {
		boolean b = !(getAnimation() == hook && e != hook.getTarget());
		return super.isCollision(e) && b;
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("hook")) {
			int n = Integer.parseInt(c[3]);
			Entity e = ref.updater.namedEntities.get(n);
			hook.setTargetFrom(this, e);
			xTarget = e.x;
			yTarget = e.y;
			setAnimation(hook);
		} else if (c[2].equals("instaAttack")) {
			int n = Integer.parseInt(c[3]);
			Entity e = ref.updater.namedEntities.get(n);
			instaAttack.setTargetFrom(this, e);
			setAnimation(instaAttack);
		}
	}

	@Override
	public void sendDefaultAnimation(Animation oldAnimation) {
		if (oldAnimation == hook && ((Hook) oldAnimation).getTarget() != null) {
			sendAnimation("instaAttack "
					+ ((MeleeAttack) oldAnimation).getTarget().number);
		} else {
			sendAnimation("walk " + xTarget + " " + yTarget + " true");
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		ref.updater.send("<hit " + ((MeleeAttack) a).getTarget().number + " "
				+ a.damage + " " + a.pirce);
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, currentFrame);
		if (getAnimation() == hook)
			drawShot();
		drawTaged();
	}

	public void drawShot() {
		if (hook.getTarget() != null) {
			Entity e = hook.getTarget();
			ref.app.stroke(150);
			ref.app.line(xToGrid(x), yToGrid(y), xToGrid(e.x), yToGrid(e.y));
			ref.app.stroke(0);
		}
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	public static class Hook extends MeleeAttack {

		public float speed;

		public Hook(PImage IMG, int duration) {
			super(IMG, duration);
		}

		@Override
		public void updateAbility(Entity e, boolean isServer) {
			if (isSetup() && isNotOnCooldown()
					&& target.isInRange(e.x, e.y, e.radius + target.radius)) {
				if (isServer) {
					e.sendDefaultAnimation(this);
				}
				isSetup = false;
				// startCooldown();
			}
		}

	}

}
