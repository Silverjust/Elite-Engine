package entity.entities;

import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.animation.Ability;
import entity.animation.Animation;
import entity.animation.AreaAttack;
import entity.animation.Attack;
import entity.animation.Death;
import processing.core.PImage;
import shared.Nation;
import shared.ref;

public class Rug extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	AreaAttack basicAttack;
	RuglingSpawn spawn;

	Rugling[] children = new Rugling[10];

	private byte spawnRange;

	public static void loadImages() {
		String path = path(Nation.ALIENS, new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Rug");
	}

	public Rug(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new AreaAttack(standingImg, 500);
		spawn = new RuglingSpawn(standingImg, 800);

		animation = nextAnimation = walk;
		// ************************************
		xSize = 35;
		ySize = 35;

		kerit = 480;
		pax = 300;
		arcanum = 0;
		prunam = 0;
		trainTime = 5000;

		hp = hp_max = 120;
		speed = 0.5f;
		radius = 8;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 10);
		basicAttack.range = (byte) (radius + 5);
		basicAttack.damage = 20;
		basicAttack.cooldown = 1500;
		basicAttack.eventTime = 100;

		spawnRange = (byte) (radius + 100);
		spawn.cooldown = 5000;
		spawn.eventTime = 500;

		descr = " ";
		stats = "spawns/s: " + 2 + "/" + spawn.cooldown / 1000.0;
		// ************************************
	}

	@Override
	public void updateDecisions() {
		// isTaged = false;
		if (animation == walk || animation == stand) {// ****************************************************
			boolean isEnemyTooClose = false;
			boolean isEnemyInHitRange = false;
			float importance = 0;
			Entity importantEntity = null;
			for (Entity e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isCollision(x, y, aggroRange + e.radius)) {
							isEnemyTooClose = true;
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
						}
						if (e.isCollision(x, y, basicAttack.range + e.radius)) {
							isEnemyInHitRange = true;
						}
						if (e.isCollision(x, y, spawnRange + e.radius)) {
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
				sendAnimation("basicAttack " + x + " " + y);
			} else if (isEnemyTooClose && importantEntity != null) {
				sendAnimation("walk " + importantEntity.x + " "
						+ importantEntity.y);
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
