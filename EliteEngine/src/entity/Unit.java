package entity;

import processing.core.PApplet;
import processing.core.PGraphics;
import shared.Helper;
import shared.ref;
import entity.animation.Animation;
import entity.animation.Attack;
import game.Chat;

public abstract class Unit extends Entity {
	// TODO Flocking einfügen
	// FIXME ADD Verhalten der Einheiten
	// TODO deglich verbessern
	protected float xTarget;
	protected float yTarget;
	protected byte direction;
	protected float speed;
	protected boolean isMoving;
	public int trainTime;

	public Animation walk;

	public Unit(String[] c) {
		if (c != null) {
			player = ref.updater.player.get(c[2]);
			x = Float.parseFloat(c[3]);
			y = Float.parseFloat(c[4]);
			xTarget = c.length > 5 ? Float.parseFloat(c[5]) : x;
			yTarget = c.length > 6 ? Float.parseFloat(c[6]) : y;
			isMoving = true;
		}
	}

	@Override
	public void updateMovement() {
		float xDeglich = 0;
		float yDeglich = 0;
		boolean hasColided = false;
		if (isMoving) {// ****************************************************
			for (Entity e : player.visibleEntities) {
				if (e != this) {
					if (isCollision(e)) {
						hasColided = true;
						xDeglich += x - e.x;
						yDeglich += y - e.y;

					}
				}
			}
		} else {// ****************************************************
			// stand still
		}

		if (PApplet.dist(x, y, xTarget, yTarget) < 2 && animation == walk) {
			// System.out.println(1000000000+" "+(animation == walk));
			isMoving = false;
			setAnimation(stand);
			sendAnimation("stand");
		}

		if (isMoving
				&& !hasColided
				&& PApplet.dist(x, y, xTarget + xDeglich, yTarget + yDeglich) > speed) {
			x = xNext(xTarget + xDeglich, yTarget + yDeglich);
			y = yNext(xTarget + xDeglich, yTarget + yDeglich);
		} else if (PApplet.dist(x, y, x + xDeglich, y + yDeglich) > speed) {
			x = xNext(x + xDeglich, y + yDeglich);
			y = yNext(x + xDeglich, y + yDeglich);
		}
	}

	@Override
	public void renderUnder() {
		direction = Helper.getDirection(x, y, xTarget, yTarget);
		drawShadow();
	}

	protected float xNext(float X, float Y) {
		return x + (X - x) / PApplet.dist(x, y, X, Y) * speed;
	}

	protected float yNext(float X, float Y) {
		return y + (Y - y) / PApplet.dist(x, y, X, Y) * speed;
	}

	@Override
	public void drawOnMinimap(PGraphics graphics) {
		graphics.fill(player.color);
		graphics.ellipse(x, y, radius * 2, radius * 2);
	}

	@Override
	public void exec(String[] c) {
		// PApplet.printArray(c);
		super.exec(c);
		String string = c[2];
		if ("walk".equals(string)) {
			xTarget = Float.parseFloat(c[3]);
			yTarget = Float.parseFloat(c[4]);
			isMoving = true;
			setAnimation(walk);
		}
		Attack.updateExecAttack(c, this);
	}

	@Deprecated
	public void move(float X, float Y) {
		xTarget = X;
		yTarget = Y;
		isMoving = true;
		setAnimation(walk);
	}

	public void tp(float X, float Y) {
		x = X;
		y = Y;
		xTarget = X;
		yTarget = Y;
	}

	public void info() {
		Chat.println(this.getClass().getSimpleName() + number, "(" + x + "|"
				+ y + ")->(" + xTarget + "|" + yTarget + ")\nhp:" + hp);
	}

	@Override
	public void sendDefaultAnimation(Animation oldAnimation) {
		sendAnimation("walk " + xTarget + " " + yTarget);
	}

}
