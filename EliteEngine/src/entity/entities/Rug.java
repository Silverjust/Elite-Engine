package entity.entities;

import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.animation.Ability;
import entity.animation.Animation;
import entity.animation.AreaAttack;
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

		stand = new Animation(standingImg, 100);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new AreaAttack(standingImg, 500);
		spawn = new RuglingSpawn(standingImg, 800);

		animation = nextAnimation = walk;
		// ************************************
		xSize = 70;
		ySize = 70;
		
		kerit=120;

		hp = hp_max = 120;
		speed = 1.5f;
		radius = 15;
		sight = 120;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 10);
		basicAttack.range = (byte) (radius + 10);
		basicAttack.damage = 20;
		basicAttack.cooldown = 800;
		basicAttack.eventTime = 100;

		spawnRange = (byte) (radius + 100);
		spawn.cooldown = 5000;
		spawn.eventTime = 500;
		// ************************************
	}

	@Override
	public void updateDecisions() {

		// isTaged = false;
		if (animation == stand) {// ****************************************************
			String s = "";
			for (Entity e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {// server
						if (e.isCollision(x, y, aggroRange + e.radius)) {
							s = ("walk " + e.x + " " + e.y);
						}
					}
				}
			}
			sendAnimation(s);
		}
		if (animation == walk) {// ****************************************************
			boolean isEnemyTooClose = false;
			float importance = 0;
			Entity importantEntity = null;
			for (Entity e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isCollision(x, y, basicAttack.range + e.radius)) {
							isEnemyTooClose = true;
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
			if (isEnemyTooClose && getBasicAttack().isNotOnCooldown()) {
				sendAnimation("basicAttack " + x + " " + y);
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
		drawCircle(spawnRange);
		animation.draw(this, direction, currentFrame);
		drawTaged();
	}

	@Override
	public Ability getBasicAttack() {
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
				ref.updater.send("<spawn Rugling " + e.player.ip + " " + e.x
						+ " " + (e.y - e.radius - 8) + " " + target.x + " "
						+ target.y);
				target = null;
				startCooldown();
			}
		}
	}

}
