package entity.scientists;

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
import g4p_controls.GEvent;
import g4p_controls.GGameButton;

public class RailgunGuineaPig extends Unit implements Attacker, Shooter {

	private static PImage standingImg;

	byte aggroRange;

	ShootAttack basicAttack;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "RailgunGuineaPig");
	}

	public RailgunGuineaPig(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);

		animation = nextAnimation = walk;
		// ************************************
		xSize = 15;
		ySize = 15;

		kerit = 28;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		trainTime = 1500;

		hp = hp_max = 30;
		armor = 1;
		speed = 0.9f;
		radius = 6;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 50);
		basicAttack.damage = 90;
		basicAttack.pirce = 5;
		basicAttack.cooldown = 5000;
		basicAttack.range = 120;
		basicAttack.setCastTime(200);// eventtime is defined by target distance
		basicAttack.speed = 1f;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions() {
		if (animation == walk || animation == stand) {// ****************************************************
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
			} else if (importantEntity != null && !isEnemyInHitRange) {
				sendAnimation("walk " + importantEntity.x + " "
						+ importantEntity.y);
			}
		}
		basicAttack.updateAbility(this);
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
		animation.draw(this, direction, currentFrame);
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

	public static class EquipActive extends Active {
		Class<? extends Unit> unit;
		String descr = " ", stats = " ";

		public EquipActive(int x, int y, char n, Entity u, Class<?> trainer) {
			super(x, y, n, u.iconImg);
			clazz = trainer;
			unit = ((Unit) u).getClass();
			descr = u.getDesription();
			stats = u.getStatistics();
		}

		@Override
		public void onButtonPressed(GGameButton gamebutton, GEvent event) {
			Entity trainer = null;
			for (Entity e : ref.updater.selected) {
				if (clazz.isAssignableFrom(e.getClass())) {
					trainer = e;
				}
			}
			if (trainer != null) {
				ref.updater.send("<remove " + trainer.number);
				ref.updater.send("<spawn " + unit.getSimpleName()
						+ " " + trainer.player.ip + " " + trainer.x + " "
						+ trainer.y);
			}
		}

		@Override
		public String getDesription() {
			return descr;
		}

		@Override
		public String getStatistics() {
			return stats;
		}
	}
}