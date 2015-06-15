package entity.neutral;

import processing.core.PImage;
import entity.Entity;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Death;

public class TestLab extends Unit {

	private static PImage standingImg;

	byte aggroRange;

	public static void loadImages() {
		String path = path( new Object() {
		});
		standingImg = game.ImageHandler.load(path, "TestLab");

	}

	public TestLab(String[] c) {
		super(c);

		iconImg = standingImg;
		stand = new Animation(standingImg,1000);
		walk = new Animation(standingImg,1000);
		death = new Death(standingImg,200);

		animation = nextAnimation = walk;
		// ************************************
		xSize = 15;
		ySize = 15;
		

		hp = hp_max = 500;
		speed = 2.0f;
		radius = 10;
		height = 40;
		sight = 125;
		groundPosition = Entity.GroundPosition.AIR;
		// ************************************
	}

	@Override
	public void updateDecisions() {
		super.updateDecisions();
		isTaged = false;
		//boolean hasColided = false;
		//float xDeglich = 0;
		//float yDeglich = 0;

		if (animation == walk) {// ****************************************************
			/*for (Entity e : player.visibleEntities) {
				if (e != this) {
					if (e.isCollision(this)) {
						hasColided = true;
						if (isMoving) {
							xDeglich += x - e.x;
							yDeglich += y - e.y;
						}
					}
					if (e.isEnemyTo(this)) {
					}
				}
			}*/
		} else if (animation == death) {
			//isMoving = false;
		} else if (true) {
		}

		/*if (PApplet.dist(x, y, xTarget, yTarget) < 2) {
			isMoving = false;
			sendAnimation("stand");
		}*/
		/*if (isMoving && !hasColided) {
			x = xNext(xTarget + xDeglich, yTarget + yDeglich);
			y = yNext(xTarget + xDeglich, yTarget + yDeglich);
		} else if (PApplet.dist(x, y, x + xDeglich, y + yDeglich) > radius) {
			x = xNext(x + xDeglich, y + yDeglich);
			y = yNext(x + xDeglich, y + yDeglich);
			// isTaged = true;
		} else {
		}*/

	}

	@Override
	public void renderAir() {
		drawSelected();
		animation.draw(this, direction, currentFrame);
		drawTaged();
	}

}
