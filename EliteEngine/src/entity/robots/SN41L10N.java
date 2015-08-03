package entity.robots;

import processing.core.PApplet;
import processing.core.PImage;
import shared.Nation;
import shared.ref;
import entity.Active;
import entity.Entity;
import entity.Shooter;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.ShootAttack;
import game.ImageHandler;

public class SN41L10N extends Unit implements Shooter {

	private static PImage standingImg;
	private static PImage anchoredImg;
	private static PImage anchorSym;

	byte aggroRange;
	boolean isAnchored;

	ShootAttack basicAttack;
	private Animation anchored;
	private byte splashrange;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "SN41L10N");
		anchoredImg = game.ImageHandler.load(path, "SN41L10N_c");
		anchorSym = ImageHandler.load(Nation.ROBOTS.toString() + "/symbols/",
				"anchor");
	}

	public SN41L10N(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		anchored = new Animation(anchoredImg, 1000);
		basicAttack = new ShootAttack(anchoredImg, 800);

		setAnimation(walk);

		// ************************************
		xSize = 30;
		ySize = 30;
		height = 5;

		kerit = 700;
		pax = 0;
		arcanum = 50;
		prunam = 0;
		trainTime = 4000;

		hp = hp_max = 700;
		armor = 3;
		speed = 0.7f;
		radius = 8;
		sight = 90;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = 120;
		splashrange = 10;
		basicAttack.damage = 40;
		basicAttack.pirce = 5;
		basicAttack.cooldown = 2000;
		basicAttack.range = 40;
		basicAttack.setCastTime(200);// eventtime is defined by target distance
		basicAttack.speed = 1f;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isAnchored && isServer) {// ****************************************************
			boolean isEnemyInHitRange = false;
			float importance = 0;
			Entity importantEntity = null;
			for (Entity e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isInRange(x, y, aggroRange + e.radius)
								&& basicAttack.canTargetable(e)) {
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
						}
						if (e.isInRange(x, y, basicAttack.range + e.radius)
								&& basicAttack.canTargetable(e)) {
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
			}
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public void sendDefaultAnimation(Animation oldAnimation) {
		if (isAnchored)
			sendAnimation("anchored");
		else
			super.sendDefaultAnimation(oldAnimation);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("anchor")) {
			isAnchored = true;
			isMoving = false;
			armor = 5;
			setAnimation(anchored);// later anchor?
		} else if (c[2].equals("anchored")) {
			isMoving = false;
			setAnimation(anchored);
		} else if (c[2].equals("walk")) {
			System.out.println("ANT10N.exec()");
			isAnchored = false;
			armor = 3;
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		Entity target = ((ShootAttack) a).getTarget();
		for (Entity e : ref.updater.entities) {
			if (e != null & e.isEnemyTo(this)
					&& e.isInRange(target.x, target.y, e.radius + splashrange)
					&& a.canTargetable(e)) {
				ref.updater.send("<hit " + e.number + " " + a.damage + " "
						+ a.pirce);
			}
		}
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, currentFrame);
		basicAttack.drawAbility(this, direction);
		drawTaged();
	}

	@Override
	public void drawShot(Entity target, float progress) {
		float x = PApplet.lerp(this.x, target.x, progress);
		float y = PApplet.lerp(this.y - height, target.y - target.height,
				progress);
		ref.app.fill(0, 200, 255);
		ref.app.strokeWeight(0);
		ref.app.ellipse(xToGrid(x), yToGrid(y), 3, 3);
		ref.app.strokeWeight(1);
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	public static class AnchorActive extends Active {
		public AnchorActive(int x, int y, char n) {
			super(x, y, n, anchorSym);
			clazz = SN41L10N.class;
		}

		@Override
		public String getDesription() {
			return "anchor to heal";
		}

		@Override
		public void onActivation() {
			for (Entity e : ref.updater.selected) {
				if (e instanceof SN41L10N) {
					e.sendAnimation("anchor");
				}
			}
		}
	}

}