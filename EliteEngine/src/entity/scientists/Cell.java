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
import g4p_controls.GEvent;
import g4p_controls.GGameButton;

public class Cell extends Unit implements Attacker {

	private static PImage standingImg;

	MeleeAttack heal;

	private int damage;

	private byte aggroRange;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Cell");
	}

	public Cell(String[] c) {
		super(c);		GuineaPig.setupEquip(this, c);

		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		heal = new MeleeAttack(standingImg, 800);

		animation = nextAnimation = walk;
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
		damage = 5;
		heal.damage = 10;
		heal.pirce = 0;
		heal.cooldown = 1500;
		heal.range = 40;
		heal.setCastTime(100);

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions() {
		if (animation == walk || animation == stand) {// ****************************************************
			float importance = 0;
			Entity importantEntity = null;
			for (Entity e : player.visibleEntities) {
				if (e.isAllyTo(this)) {
					if (e.isInRange(x, y, aggroRange + e.radius)) {
						float newImportance = calcImportanceOf(e);
						if (newImportance > importance && e.hp < e.hp_max) {
							importance = newImportance;
							importantEntity = e;
						}
					}

				}

			}
			if (importantEntity != null) {
				sendAnimation("walk " + importantEntity.x + " "
						+ importantEntity.y);
			}
		}
		heal.setTargetFrom(this, this);
		heal.updateAbility(this);
	}

	@Override
	public void calculateDamage(Attack a) {
		for (Entity e : ref.updater.entities) {
			if (e != null && e.isInRange(x, y, e.radius + a.range))
				if (e.isAllyTo(this)) {
					ref.updater.send("<heal " + e.number + " " + heal.damage);
				} else if (e.isEnemyTo(this)) {
					ref.updater.send("<hit " + e.number + " " + damage + " 0");
				}
		}
	}

	@Override
	public void renderGround() {
		drawSelected();
		animation.draw(this, direction, currentFrame);
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
		public void onButtonPressed(GGameButton gamebutton, GEvent event) {
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