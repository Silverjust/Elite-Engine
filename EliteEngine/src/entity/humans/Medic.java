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

public class Medic extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	MeleeAttack basicAttack;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = ImageHandler.load(path, "Medic");
	}

	public Medic(String[] c) {
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

		kerit = 20;
		pax = 50;
		arcanum = 0;
		prunam = 0;
		trainTime = 1000;

		hp = hp_max = 120;
		armor = 1;
		speed = 0.9f;
		radius = 5;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 50);
		basicAttack.damage = 3;// heal
		basicAttack.pirce = -1;// heal
		basicAttack.cooldown = 500;
		basicAttack.range = 15;
		basicAttack.setCastTime(400);// eventtime is defined by target distance
		basicAttack.doRepeat = true;

		descr = "medic heals";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (getAnimation() == walk || getAnimation() == stand) {// ****************************************************
			boolean isEnemyInHitRange = false;
			float importance = 0;
			Entity importantEntity = null;
			for (Entity e : player.visibleEntities) {
				if (e.isAllyTo(this) && e instanceof Scout
						|| e instanceof HeavyAssault || e instanceof Exo
						|| e instanceof Medic) {
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
			} else if (importantEntity != null) {
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			}
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public void calculateDamage(Attack a) {
		isTaged = true;
		ref.updater.send("<heal " + basicAttack.getTarget().number + " "
				+ a.damage);
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
		if (basicAttack.getTarget() != null&&basicAttack.getTarget().isAlive()) {
			Entity e = basicAttack.getTarget();
			ref.app.stroke(0, 255, 0);
			ref.app.line(xToGrid(x), yToGrid(y), xToGrid(e.x), yToGrid(e.y));
			ref.app.stroke(0);
		}
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

}