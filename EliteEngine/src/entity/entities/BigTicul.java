package entity.entities;

import processing.core.PApplet;
import processing.core.PImage;
import shared.Nation;
import shared.ref;
import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.animation.Ability;
import entity.animation.Animation;
import entity.animation.AreaAttack;
import entity.animation.Death;

public class BigTicul extends Unit implements Attacker{

	private static PImage[][] walkingImg;

	byte aggroRange;

	AreaAttack basicAttack;

	public static void loadImages() {
		String path = path(Nation.ALIENS, new Object() {
		});
		walkingImg = game.ImageHandler.load(path, "BigTicul", 's', (byte) 8,
				(byte) 1);
		/*
		 * attackImg = game.ImageHandler.load(path, "BigTicul", 's', (byte) 8,
		 * (byte) 1);
		 */

	}

	public BigTicul(String[] c) {
		super(c);
		iconImg = walkingImg[1][0];
		walk = new Animation(walkingImg,100);
		death = new Death(walkingImg,100);
		basicAttack = new AreaAttack(walkingImg,100);

		animation = nextAnimation = walk;
		// ************************************
		xSize = 180;
		ySize = 180;

		hp = hp_max = 1000;
		speed = 1.0f;
		radius = 60;
		sight = 100;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 60);
		basicAttack.range = (byte) (radius + 10);
		basicAttack.damage = 10;
		basicAttack.cooldown = 1200;
		basicAttack.eventTime = 0;
		// ************************************
	}

	@Override
	public void updateDecisions() {
		super.updateDecisions();

		isTaged = false;
		boolean hasColided = false;
		float xDeglich = 0;
		float yDeglich = 0;

		if (animation == walk) {// ****************************************************
			for (Entity e : ref.updater.entities) {
				if (e != this) {
					if (e.isCollision(this)) {
						hasColided = true;
						if (isMoving) {
							xDeglich += x - e.x;
							yDeglich += y - e.y;
						}
					}
					if (e.isEnemyTo(this)) {
						if (e.isCollision(x, y, aggroRange + e.radius)) {
							sendAnimation("basicAttack");
						}
					}
				}
			}
		}  else if (animation == basicAttack) {// ************************************
			for (Entity e : ref.updater.entities) {
				if (e != this) {
					if (e.isCollision(this)) {
						hasColided = true;
						if (isMoving) {
							xDeglich += x - e.x;
							yDeglich += y - e.y;
						}
					}
					if (e.isEnemyTo(this)) {
						if (e.isCollision(x, y, aggroRange + e.radius)) {
							xTarget = e.x;
							yTarget = e.y;
							isMoving = true;
						}
						if (e.isCollision(x, y, basicAttack.range + e.radius)) {
							basicAttack.setPosition(x, y);
						}
					}
				}
			}
			basicAttack.updateAbility(this);
		} else if (animation == death) {
			isMoving = false;
		} else if (true) {
		}

		if (PApplet.dist(x, y, xTarget, yTarget) < 2) {
			isMoving = false;
		}
		if (isMoving && !hasColided) {
			x = xNext(xTarget + xDeglich, yTarget + yDeglich);
			y = yNext(xTarget + xDeglich, yTarget + yDeglich);
		} else if (PApplet.dist(x, y, x + xDeglich, y + yDeglich) > radius) {
			x = xNext(x + xDeglich, y + yDeglich);
			y = yNext(x + xDeglich, y + yDeglich);
			// isTaged = true;
		} else {
		}

	}

	@Override
	public void renderGround() {
		drawSelected();
		drawCircle(aggroRange);
		drawCircle(basicAttack.range);
		drawCircle((int) (basicAttack.range * basicAttack.getCooldownPercent()));
		ref.app.tint(50);
		animation.draw(this, direction, (byte) 0);
		ref.app.tint(255);
		drawTaged();
	}

	@Override
	public Ability getBasicAttack() {
		return basicAttack;
	}

}
