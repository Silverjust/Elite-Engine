package entity;

import processing.core.PApplet;
import shared.Helper;
import shared.ref;
import entity.animation.Ability;
import entity.animation.Animation;
import entity.animation.AreaAttack;
import entity.animation.TargetAttack;
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

	protected Animation walk;

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
					if (e.isCollision(this)) {
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

		if (isMoving && !hasColided) {
			x = xNext(xTarget + xDeglich, yTarget + yDeglich);
			y = yNext(xTarget + xDeglich, yTarget + yDeglich);
		} else if (PApplet.dist(x, y, x + xDeglich, y + yDeglich) > 0.1) {
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

	public void drawOnMinimap() {
		ref.app.fill(player.playerColor);
		ref.app.ellipse(x, y, radius * 2, radius * 2);
	}

	@Override
	public void exec(String[] c) {
		// PApplet.printArray(c);
		super.exec(c);
		switch (c[2]) {
		case "walk":
			xTarget = Float.parseFloat(c[3]);
			yTarget = Float.parseFloat(c[4]);
			isMoving = true;
			setAnimation(walk);
			break;
		case "basicAttack":
			if (this instanceof Attacker) {

				Ability a = ((Attacker) this).getBasicAttack();
				if (a instanceof TargetAttack) {
					int n = Integer.parseInt(c[3]);
					Entity e = ref.updater.namedEntities.get(n);
					((TargetAttack) a).setTarget(e);
				} else if (a instanceof AreaAttack) {
					float x = Float.parseFloat(c[3]);
					float y = Float.parseFloat(c[4]);
					((AreaAttack) a).setPosition(x, y);
				}
				isMoving = false;
				setAnimation(a);
			}
			break;
		default:
			break;
		}
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
