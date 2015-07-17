package entity.aliens;

import processing.core.PImage;
import shared.ref;
import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.animation.Ability;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.MeleeAttack;

public class Rug extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	MeleeAttack basicAttack;
	RuglingSpawn spawn;

	private int spawnRange;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Rug");
	}

	public Rug(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new MeleeAttack(standingImg, 500);
		spawn = new RuglingSpawn(standingImg, 800);

		animation = nextAnimation = walk;
		// ************************************
		xSize = 35;
		ySize = 35;

		kerit = 230;
		pax = 400;
		arcanum = 0;
		prunam = 0;
		trainTime = 5000;

		hp = hp_max = 120;
		speed = 0.7f;
		radius = 8;
		sight = 90;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 10);
		basicAttack.range = (byte) (radius + 5);
		basicAttack.damage = 20;
		basicAttack.cooldown = 1500;
		basicAttack.setCastTime(100);

		spawnRange = 150;
		spawn.cooldown = 4000;
		spawn.setCastTime(500);

		descr = " ";
		stats = "spawns/s: " + 1 + "/" + spawn.cooldown / 1000.0;
		// ************************************
	}

	@Override
	public void updateDecisions() {
		// isTaged = false;
		if (animation == walk && isAggro || animation == stand) {// ****************************************************
			boolean isEnemyTooClose = false;
			boolean isEnemyInHitRange = false;
			float importance = 0;
			Entity importantEntity = null;
			for (Entity e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isInRange(x, y, aggroRange + e.radius)) {
							isEnemyTooClose = true;
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
						}
						if (e.isInRange(x, y, basicAttack.range + e.radius)) {
							isEnemyInHitRange = true;
						}
						if (e.isInRange(x, y, spawnRange + e.radius)) {
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
				sendAnimation("basicAttack " + number);
			} else if (isEnemyTooClose && importantEntity != null) {
				Attack.sendWalkToEnemy(this,importantEntity);
			} else if (importantEntity != null && spawn.isNotOnCooldown()) {
				sendAnimation("spawn " + importantEntity.number);
			}
		}
		basicAttack.updateAbility(this);
		spawn.updateAbility(this);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("spawn")) {
			isMoving = false;
			int n = Integer.parseInt(c[3]);
			Entity e = ref.updater.namedEntities.get(n);
			spawn.setTarget(e);
			setAnimation(spawn);
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		for (Entity e : ref.updater.entities) {
			if (e != null & e.isEnemyTo(this)
					&& e.isInRange(x, y, e.radius + a.range)) {
				ref.updater.send("<hit " + e.number + " " + a.damage + " "
						+ a.pirce);
			}
		}
	}

	@Override
	public void renderGround() {
		drawSelected();
		// drawCircle(spawnRange);
		animation.draw(this, direction, currentFrame);
		drawTaged();
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	protected void sendWalkToEnemy(Entity e, Entity target) {
	}

	static class RuglingSpawn extends Ability {

		private Entity target;

		public RuglingSpawn(PImage IMG, int duration) {
			super(IMG, duration);
		}

		public void setTarget(Entity e) {
			target = e;
		}

		@Override
		public void updateAbility(Entity e) {
			if (target != null && isEvent() && isNotOnCooldown()) {
				ref.updater.send("<spawn Rugling " + e.player.ip + " " + e.x
						+ " " + (e.y + e.radius + 8) + " " + target.x + " "
						+ target.y);
				/*
				 * ref.updater.send("<spawn Rugling " + e.player.ip + " " + e.x
				 * + " " + (e.y - e.radius - 8) + " " + target.x + " " +
				 * target.y);
				 */
				target = null;
				startCooldown();
			}
		}
	}

}
