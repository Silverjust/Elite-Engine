package entity.aliens;

import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.TargetAttack;
import processing.core.PImage;
import shared.Nation;

public class Rugling extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	TargetAttack basicAttack;

	private Death splashDeath;

	public static void loadImages() {
		String path = path(Nation.ALIENS, new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Rugling");
	}

	public Rugling(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		splashDeath = new Death(standingImg, 500);
		basicAttack = new TargetAttack(standingImg, 800);

		animation = nextAnimation = walk;
		// ************************************
		xSize = 35;
		ySize = 35;

		hp = hp_max = 20;
		speed = 2.2f;
		radius = 4;
		sight = 50;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 100);
		basicAttack.range = (byte) (radius + 7);
		basicAttack.damage = 10;
		basicAttack.cooldown = 1500;
		basicAttack.eventTime = 500;
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
						if (e.isCollision(x, y, aggroRange + e.radius)) {
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
						}
						if (e.isCollision(x, y, basicAttack.range + e.radius)
								&& e.groundPosition == GroundPosition.GROUND) {
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
			} else if (importantEntity == null) {
				sendAnimation("splashDeath");
			}
		}
		basicAttack.updateAbility(this);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("splashDeath")) {
			setAnimation(splashDeath);
		}
	}

	@Override
	public void renderGround() {
		isSelected = false;// cant be selected
		// drawSelected();
		animation.draw(this, direction, currentFrame);
		drawTaged();
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

}
