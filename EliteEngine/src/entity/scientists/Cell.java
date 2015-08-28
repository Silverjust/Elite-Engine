package entity.scientists;

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

public class Cell extends Unit implements Attacker {

	private static PImage standingImg;

	MeleeAttack heal;

	private byte aggroRange;

	private byte healAmount;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Cell");
	}

	public Cell(String[] c) {
		super(c);
		GuineaPig.setupEquip(this, c);

		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		heal = new MeleeAttack(standingImg, 800);

		setAnimation(walk);

		// ************************************
		xSize = 15;
		ySize = 15;

		kerit = 35;
		pax = 40;
		arcanum = 0;
		prunam = 0;
		trainTime = 1500;

		hp = hp_max = 30;
		armor = 1;
		speed = 0.9f;
		radius = 7;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = 60;
		healAmount = 10;
		heal.damage = 5;
		heal.pirce = 0;
		heal.cooldown = 1500;
		heal.range = 30;
		heal.setCastTime(100);

		descr = " ";
		stats = "heal/s: " + healAmount + "/" + (heal.cooldown / 1000.0);
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isServer
				&& (getAnimation() == walk && isAggro || getAnimation() == stand)) {// ****************************************************
			float importance = 0;
			Entity importantEntity = null;
			for (Entity e : player.visibleEntities) {
				if (e.isAllyTo(this)) {
					if (e.isInRange(x, y, aggroRange + e.radius)
							&& heal.canTargetable(e)) {
						float newImportance = calcImportanceOf(e);
						if (newImportance > importance && e.hp < e.hp_max) {
							importance = newImportance;
							importantEntity = e;
						}
					}

				}

			}
			if (importantEntity != null) {
				Attack.sendWalkToEnemy(this, importantEntity, heal.range);
			}
		}
		if (heal.isNotOnCooldown()) {
			heal.startCooldown();
			heal.setTargetFrom(this, this);
		}
		heal.updateAbility(this, isServer);
	}

	@Override
	public void calculateDamage(Attack a) {
		for (Entity e : ref.updater.entities) {
			if (e != null && e.isInRange(x, y, e.radius + a.range))
				if (e.isAllyTo(this)) {
					ref.updater.send("<heal " + e.number + " " + healAmount);
				} else if (e.isEnemyTo(this)) {
					ref.updater.send("<hit " + e.number + " " + heal.damage
							+ " 0");
				}
		}
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, currentFrame);
		heal.drawAbility(this, direction);
		drawTaged();
	}

	@Override
	public Attack getBasicAttack() {
		return heal;
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
		public void onActivation() {
			Entity trainer = null;
			for (Entity e : ref.updater.selected) {
				if (clazz.isAssignableFrom(e.getClass())) {
					trainer = e;
				}
			}
			if (trainer != null) {
				ref.updater.send("<remove " + trainer.number);
				ref.updater
						.send("<spawn " + unit.getSimpleName() + " "
								+ trainer.player.ip + " " + trainer.x + " "
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