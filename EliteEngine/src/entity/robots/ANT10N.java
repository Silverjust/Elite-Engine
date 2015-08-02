package entity.robots;

import processing.core.PImage;
import shared.ref;
import entity.Active;
import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.MeleeAttack;

public class ANT10N extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;
	boolean isAnchored;

	MeleeAttack basicAttack;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "ANT10N");
	}

	public ANT10N(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new MeleeAttack(standingImg, 800);

		setAnimation(walk);

		// ************************************
		xSize = 15;
		ySize = 15;
		height = 5;

		kerit = 300;
		pax = 100;
		arcanum = 0;
		prunam = 0;
		trainTime = 4000;

		hp = hp_max = 200;
		armor = 1;
		speed = 0.7f;
		radius = 7;
		sight = 90;
		groundPosition = Entity.GroundPosition.GROUND;

		basicAttack.range = (byte) (radius + 25);
		basicAttack.damage = 25;// heal
		basicAttack.pirce = -1;// heal
		basicAttack.cooldown = 5000;
		basicAttack.setCastTime(100);
		basicAttack.doRepeat = true;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isAnchored) {// ****************************************************
			boolean isEnemyInHitRange = false;
			float importance = 0;
			Entity importantEntity = null;
			for (Entity e : player.visibleEntities) {
				if (e.isAllyTo(this)) {
					if (e.isInRange(x, y, aggroRange + e.radius)) {
						float newImportance = calcImportanceOf(e);
						if (newImportance > importance && e.hp < e.hp_max) {
							importance = newImportance;
							importantEntity = e;
							if (e.isInRange(x, y, basicAttack.range + e.radius)) {
								isEnemyInHitRange = true;
							}
						}
					}

				}

			}
			if (isEnemyInHitRange && basicAttack.isNotOnCooldown()
					&& !basicAttack.isSetup()) {
				sendAnimation("basicAttack " + importantEntity.number);
			}
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("anchor")) {
			isAnchored = true;
			isMoving = false;
			height = 0;
		} else if (c[2].equals("walk")) {
			System.out.println("ANT10N.exec()");
			isAnchored = false;
			height = 5;
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		for (Entity e : ref.updater.entities) {
			if (e != null && e.isAllyTo(this)
					&& e.isInRange(x, y, e.radius + a.range)) {
				ref.updater.send("<heal " + e.number + " " + basicAttack.damage);
			}
		}
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, currentFrame);
		basicAttack.drawAbility(this, direction);
		drawTaged();
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	public static class AnchorActive extends Active {
		public AnchorActive(int x, int y, char n) {
			super(x, y, n, standingImg);
			clazz = ANT10N.class;
		}

		@Override
		public String getDesription() {
			return "anchor to heal";
		}

		@Override
		public void onActivation() {
			for (Entity e : ref.updater.selected) {
				if (e instanceof ANT10N) {
					e.sendAnimation("anchor");
				}
			}
		}
	}

}