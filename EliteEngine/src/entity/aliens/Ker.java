package entity.aliens;

import processing.core.PImage;
import shared.Nation;
import shared.ref;
import entity.Attacker;
import entity.Building;
import entity.Entity;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.TargetAttack;

public class Ker extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	TargetAttack basicAttack;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Ker");
	}

	public Ker(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new TargetAttack(standingImg, 800);

		animation = nextAnimation = walk;
		// ************************************
		xSize = 30;
		ySize = 30;

		kerit = 335;
		pax = 0;
		arcanum = 0;
		prunam = 100;
		trainTime = 10000;

		hp = hp_max = 300;
		speed = 0.9f;
		radius = 8;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 50);
		basicAttack.range = (byte) (radius + 10);
		basicAttack.damage = 112;
		basicAttack.pirce = 5;
		basicAttack.cooldown = 700;
		basicAttack.eventTime = 500;

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
			float thread = 0;
			Entity threadEntity = null;
			for (Entity e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isInRange(x, y, basicAttack.range + e.radius)) {
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
	public void calculateDamage(Attack a) {
		ref.updater.send("<hit " + basicAttack.getTarget().number + " "
				+ (basicAttack.getTarget() instanceof Building ? a.damage / 2 : a.damage) + " "
				+ a.pirce);
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