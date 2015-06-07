package entity.aliens;

import entity.Attacker;
import entity.Building;
import entity.Entity;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.AreaAttack;
import entity.animation.Attack;
import entity.animation.Death;
import processing.core.PApplet;
import processing.core.PImage;
import shared.Nation;
import shared.ref;

public class Arol extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	AreaAttack basicAttack;
	byte attackDistance;

	public static void loadImages() {
		String path = path(Nation.ALIENS, new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Arol");
	}

	public Arol(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
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
		trainTime = 10000;

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
		attackDistance = 10;

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
						if (e.isInRange(x, y, aggroRange + e.radius)) {
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
						if (e.isInRange(x, y, basicAttack.range + e.radius)
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
	public void calculateDamage(Attack a) {
		float x, y;
		x = (this.x + (xTarget - this.x)
				/ PApplet.dist(this.x, this.y, xTarget, yTarget) * (attackDistance));
		y = (this.y + (yTarget - this.y)
				/ PApplet.dist(this.x, this.y, xTarget, yTarget) * (attackDistance));
		for (Entity e : ref.updater.entities) {
			if (e != null & e.isEnemyTo(this)
					&& e.isInRange(x, y, e.radius + a.range)
					&& e.groundPosition == GroundPosition.GROUND) {
				ref.updater.send("<hit " + e.number + " "
						+ (e instanceof Building ? a.damage * 2 : a.damage)
						+ " " + a.pirce);
			}
		}
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
