package entity;

import processing.core.PApplet;
import processing.core.PGraphics;
import shared.Helper;
import shared.ref;
import entity.animation.Animation;
import entity.animation.Attack;
import game.AimHandler;
import game.AimHandler.Cursor;
import game.HUD;
import game.aim.CustomAim;
import game.aim.MoveAim;

public abstract class Unit extends Entity {
	// TODO Flocking einf�gen
	// FIXME ADD Verhalten der Einheiten
	// TODO deglich verbessern
	public float xTarget;
	public float yTarget;
	protected byte direction;
	protected float speed;
	public boolean isMoving;
	protected boolean isAggro;
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
						if (e instanceof Unit && e.getAnimation() == e.stand
								&& ((Unit) e).xTarget == xTarget
								&& ((Unit) e).yTarget == yTarget && !isAggro)
							sendAnimation("stand");
						hasColided = true;
						xDeglich += x - e.x;
						yDeglich += y - e.y;
					}
				}
			}
		} else {// ****************************************************
			// stand still
		}

		if (PApplet.dist(x, y, xTarget, yTarget) < 2 && getAnimation() == walk) {
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
		if ("walk".equals(string) && getAnimation().isInterruptable()) {
			xTarget = Float.parseFloat(c[3]);
			yTarget = Float.parseFloat(c[4]);
			isMoving = true;
			isAggro = Boolean.valueOf(c[5]);
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
		HUD.chat.println(this.getClass().getSimpleName() + number, "(" + x + "|"
				+ y + ")->(" + xTarget + "|" + yTarget + ")\nhp:" + hp);
	}

	@Override
	public void sendDefaultAnimation(Animation oldAnimation) {
		/*
		 * if (oldAnimation instanceof Attack) { sendAnimation("stand"); } else
		 * {
		 */
		sendAnimation("walk " + xTarget + " " + yTarget + " " + isAggro);
		// }
	}

	public static class AttackActive extends Active implements AimingActive {

		public AttackActive(int x, int y, char n) {
			super(x, y, n, null);
			clazz = Attacker.class;
		}

		@Override
		public void onActivation() {
			AimHandler.setAim(new CustomAim(this, Cursor.SHOOT));
		}

		@Override
		public String getDesription() {
			return "attack unit or to position";
		}

		@Override
		public void execute(float x, float y) {
			Entity target = null;
			for (Entity e : ref.updater.entities) {
				if (PApplet.dist(x, y, e.x, e.y - e.height) <= e.radius)
					target = e;
			}
			for (Entity e : ref.updater.selected) {
				if (clazz.isAssignableFrom(e.getClass())) {
					if (target != null) {
						e.sendAnimation("setTarget " + target.number);
					} else {
						e.sendAnimation("walk " + x + " " + y + " true");
					}
				}
			}
			AimHandler.end();
		}

	}

	public static class WalkActive extends Active {

		public WalkActive(int x, int y, char n) {
			super(x, y, n, null);
			clazz = Unit.class;
		}

		@Override
		public void onActivation() {
			AimHandler.setAim(new MoveAim());
		}

		@Override
		public String getDesription() {
			return "move units";
		}

	}

	public static class StopActive extends Active {

		public StopActive(int x, int y, char n) {
			super(x, y, n, null);
			clazz = Unit.class;
		}

		@Override
		public void onActivation() {
			for (Entity e : ref.updater.selected) {
				if (clazz.isAssignableFrom(e.getClass())) {
					e.sendAnimation("walk " + e.x + " " + e.y + " false");
				}
			}
		}

		@Override
		public String getDesription() {
			return "stop units";
		}

	}
}
