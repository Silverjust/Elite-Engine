package entity.entities;

import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.AreaAttack;
import entity.animation.Attack;
import entity.animation.Death;
import processing.core.PImage;
import shared.Nation;

public class Arol extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	AreaAttack basicAttack;

	public static void loadImages() {
		String path = path(Nation.ALIENS, new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Arol");
	}

	public Arol(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 100);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new AreaAttack(standingImg, 800);

		animation = nextAnimation = walk;
		// ************************************
		xSize = 50;
		ySize = 35;

		kerit = 1000;
		pax = 0;
		arcanum = 90;
		prunam = 0;
		trainTime =10000;

		hp = hp_max = 2200;
		armor = 3;
		speed = 0.4f;
		radius = 10;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 50);
		basicAttack.range = (byte) (radius + 10);
		basicAttack.damage = 40;
		basicAttack.cooldown = 5000;
		basicAttack.eventTime = 100;

		descr = " ";
		stats = " ";
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
			boolean isEnemyInRange = false;
			for (Entity e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isCollision(x, y, basicAttack.range + e.radius)
								&& e.groundPosition == GroundPosition.GROUND) {
							isEnemyInRange = true;
						}
					}
				}
			}
			if (isEnemyInRange && getBasicAttack().isNotOnCooldown()) {
				// System.out.println(thread);
				sendAnimation("basicAttack " + x + " " + y);
			}
		}
		basicAttack.updateAbility(this);
	}

	@Override
	public void renderGround() {
		drawSelected();
		animation.draw(this, direction, currentFrame);
		drawTaged();
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

}
