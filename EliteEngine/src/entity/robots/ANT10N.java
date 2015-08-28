package entity.robots;

import processing.core.PApplet;
import processing.core.PImage;
import shared.Nation;
import shared.ref;
import entity.Active;
import entity.Attacker;
import entity.Entity;
import entity.Shooter;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.MeleeAttack;
import entity.animation.ShootAttack;
import game.ImageHandler;

public class ANT10N extends Unit implements Attacker, Shooter {

	private static PImage standingImg;

	private static PImage anchorSym;

	byte aggroRange;
	boolean isAnchored;

	MeleeAttack heal;

	private ShootAttack basicAttack;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "ANT10N");
		anchorSym = ImageHandler.load(Nation.ROBOTS.toString() + "/symbols/",
				"anchor");
	}

	public ANT10N(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);
		heal = new MeleeAttack(standingImg, 800);

		setAnimation(walk);

		// ************************************
		xSize = 30;
		ySize = 30;
		height = 5;

		kerit = 300;
		pax = 100;
		arcanum = 0;
		prunam = 0;
		trainTime = 4000;

		hp = hp_max = 200;
		armor = 1;
		speed = 0.7f;
		radius = 10;
		sight = 90;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = 110;
		basicAttack.damage = 20;
		basicAttack.pirce = 3;
		basicAttack.cooldown = 900;
		basicAttack.range = 30;
		basicAttack.setCastTime(200);// eventtime is defined by target distance
		basicAttack.speed = 1f;

		heal.range = (byte) (radius + 25);
		heal.damage = 25;// heal
		heal.pirce = -1;// heal
		heal.cooldown = 5000;
		heal.setCastTime(100);
		heal.doRepeat = true;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isServer
				&& !isAnchored
				&& (getAnimation() == walk && isAggro || getAnimation() == stand)) {// ****************************************************
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
			} else if (importantEntity != null) {
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			}
		} else if (isServer && isAnchored) {// ****************************************************
			if (heal.isNotOnCooldown()) {
				sendAnimation("heal");
			}
		}
		basicAttack.updateAbility(this, isServer);
		heal.updateAbility(this, isServer);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("anchor")) {
			isAnchored = true;
			isMoving = false;
			height = 0;
		} else if (c[2].equals("walk")) {
			isAnchored = false;
			height = 5;
		} else if (c[2].equals("heal")) {
			if (heal.isNotOnCooldown() && !heal.isSetup()) {
				heal.setTargetFrom(this, this);
				setAnimation(heal);
			}
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		if (a == heal) {
			for (Entity e : ref.updater.entities)
				if (e != null && e.isAllyTo(this)
						&& e.isInRange(x, y, e.radius + a.range))
					ref.updater.send("<heal " + e.number + " " + heal.damage);
		} else
			ref.updater.send("<hit " + basicAttack.getTarget().number + " "
					+ a.damage + " " + a.pirce);
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, currentFrame);
		heal.drawAbility(this, direction);
		drawTaged();
	}

	@Override
	public void drawShot(Entity target, float progress) {
		float x = PApplet.lerp(this.x, target.x, progress);
		float y = PApplet.lerp(this.y - height, target.y - target.height,
				progress);
		ref.app.fill(0, 255, 0);
		ref.app.strokeWeight(0);
		ref.app.ellipse(xToGrid(x), yToGrid(y), 1, 1);
		ref.app.strokeWeight(1);
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	@Override
	public void renderRange() {
		super.renderRange();
		drawCircle(heal.range);
		drawCircle((int) (heal.range * heal.getCooldownPercent()));
	}

	public static class AnchorActive extends Active {
		public AnchorActive(int x, int y, char n) {
			super(x, y, n, anchorSym);
			clazz = ANT10N.class;
		}

		@Override
		public String getDesription() {
			return "anchor to heal";
		}

		@Override
		public void onActivation() {
			for (Entity e : ref.updater.selected) {
				if (e instanceof ANT10N) {
					e.sendAnimation("anchor");
				}
			}
		}
	}

}