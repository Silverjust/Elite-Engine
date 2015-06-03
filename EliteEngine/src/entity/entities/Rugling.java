package entity.entities;

import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.animation.Ability;
import entity.animation.Animation;
import entity.animation.Death;
import entity.animation.TargetAttack;
import processing.core.PImage;
import shared.Nation;

public class Rugling extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	TargetAttack basicAttack;

	public static void loadImages() {
		String path = path(Nation.ALIENS, new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Rugling");
	}

	public Rugling(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 100);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
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
		basicAttack.range = (byte) (radius + 10);
		basicAttack.damage = 10;
		basicAttack.cooldown = 1500;
		basicAttack.eventTime = 500;
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
			float thread = 0;
			Entity threadEntity = null;
			for (Entity e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isCollision(x, y, basicAttack.range + e.radius)
								&& e.groundPosition == GroundPosition.GROUND) {
							// evtl abfrage wegen groundposition

							float newThread = calcImportanceOf(e);
							if (newThread > thread) {
								thread = newThread;
								threadEntity = e;
							}
						}
					}
				}
			}
			if (threadEntity != null && getBasicAttack().isNotOnCooldown()) {
				// System.out.println(thread);
				sendAnimation("basicAttack " + threadEntity.number);
			}
		}
		basicAttack.updateAbility(this);
	}

	@Override
	public void renderGround() {
		isSelected=false;//cant be selected
		//drawSelected();
		animation.draw(this, direction, currentFrame);
		drawTaged();
	}

	@Override
	public Ability getBasicAttack() {
		return basicAttack;
	}

}