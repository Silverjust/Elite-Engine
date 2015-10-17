package entity.robots;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;
import entity.Active;
import entity.Attacker;
import entity.Entity;
import entity.Shooter;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.ShootAttack;

public class M1N1B0T extends Unit implements Attacker, Shooter {

	private static PImage standingImg;

	byte aggroRange;

	ShootAttack basicAttack;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "M1N1B0T");
	}

	public M1N1B0T(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);

		setAnimation(walk);

		// ************************************
		xSize = 15;
		ySize = 15;

		kerit = 28;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		trainTime = 1000;

		hp = hp_max = 30;
		armor = 1;
		speed = 1.05f;
		radius = 4;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 50);
		basicAttack.damage = 40;
		basicAttack.pirce = 0;
		basicAttack.cooldown = 3000;
		basicAttack.range = 30;
		basicAttack.setCastTime(100);// eventtime is defined by target distance
		basicAttack.speed = 0.6f;

		descr = "M1N1B0T§can attack§and build Depots";
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
			} else if (importantEntity != null) {
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			}
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public void calculateDamage(Attack a) {
		ref.updater.send("<hit " + basicAttack.getTarget().number + " "
				+ a.damage + " " + a.pirce);
		// SoundHandler.startIngameSound(HUD.hm, x, y);
	}

	@Override
	public void renderGround() {
		drawSelected();
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
		ref.app.ellipse(xToGrid(x), yToGrid(y), 1, 1);
		ref.app.strokeWeight(1);
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	public static class BuildDepotActive extends Active {
		String stats;

		public BuildDepotActive(int x, int y, char n) {
			super(x, y, n, standingImg);
			clazz = M1N1B0T.class;
			stats = new RobotsDepot(null).getStatistics();
		}

		@Override
		public void onActivation() {
			Entity trainer = null;
			for (Entity e : ref.updater.selected) {
				if (clazz.isAssignableFrom(e.getClass())
						&& (e.getAnimation() == e.stand || e.getAnimation() == ((Unit) e).walk))
					trainer = e;
			}

			Entity newUnit = new RobotsDepot(null);
			if (trainer != null && newUnit != null
					&& newUnit.canBeBought(trainer.player)) {
				newUnit.buyFrom(trainer.player);
				ref.updater.send("<spawn RobotsDepot " + trainer.player.getUser().ip
						+ " " + trainer.x + " " + trainer.y + " select");
				ref.updater.send("<remove " + trainer.number);
			}
		}

		@Override
		public String getDesription() {
			return "change to Depot";
		}

		@Override
		public String getStatistics() {
			return stats;
		}
	}
}