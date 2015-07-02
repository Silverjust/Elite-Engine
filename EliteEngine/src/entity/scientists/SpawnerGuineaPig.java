package entity.scientists;

import processing.core.PImage;
import shared.ref;
import entity.Entity;
import entity.Unit;
import entity.animation.Ability;
import entity.animation.Animation;
import entity.animation.Death;

public class SpawnerGuineaPig extends Unit {

	private static PImage standingImg;

	RuglingSpawn spawn;

	private int spawnRange;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "SpawnerGuineaPig");
	}

	public SpawnerGuineaPig(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		spawn = new RuglingSpawn(standingImg, 800);

		animation = nextAnimation = walk;
		// ************************************
		xSize = 25;
		ySize = 25;

		kerit = 230;
		pax = 400;
		arcanum = 0;
		prunam = 0;
		trainTime = 5000;

		hp = hp_max = 120;
		speed = 0.7f;
		radius = 7;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		spawnRange = 90;
		spawn.cooldown = 10000;
		spawn.setCastTime(500);

		descr = " ";
		stats = "spawns/s: " + 1 + "/" + spawn.cooldown / 1000.0;
		// ************************************
	}

	@Override
	public void updateDecisions() {
		// isTaged = false;
		if (animation == walk || animation == stand) {// ****************************************************
			float importance = 0;
			Entity importantEntity = null;
			for (Entity e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
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
			if (importantEntity != null && spawn.isNotOnCooldown()) {
				sendAnimation("spawn " + importantEntity.number);
			}
		}
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
				ref.updater.send("<spawn GuineaPig " + e.player.ip + " " + e.x
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
