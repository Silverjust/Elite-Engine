package entity.ahnen;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;
import entity.Entity;
import entity.Shooter;
import entity.Unit;
import entity.ahnen.Leuchte.Upgrade;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.ShootAttack;

public class Astrator extends Unit implements Shooter, Buffing {

	private static PImage standingImg;

	byte aggroRange;

	ShootAttack basicAttack;

	public byte upgradeRange;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Astrator");
	}

	public Astrator(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);
		// basicAttack.explosion = new Explosion(selectedImg, 1000);

		setAnimation(walk);

		// ************************************
		xSize = 15;
		ySize = 15;
		height = 10;

		kerit = 400;
		pax = 0;
		arcanum = 50;
		prunam = 0;
		trainTime = 3000;

		hp = hp_max = 800;// buffed 1000
		armor = 3;// buffed 5
		speed = 0.9f;
		radius = 7;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = 110;
		basicAttack.damage = 20;
		basicAttack.pirce = 3;
		basicAttack.cooldown = 900;
		basicAttack.range = 20;
		basicAttack.setCastTime(200);// eventtime is defined by target distance
		basicAttack.speed = 1f;

		upgradeRange = 100;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isServer && (getAnimation() == walk && isAggro || getAnimation() == stand)) {// ****************************************************
			boolean isEnemyInHitRange = false;
			float importance = 0;
			Entity importantEntity = null;
			for (Entity e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isInRange(x, y, aggroRange + e.radius) && basicAttack.canTargetable(e)) {
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
						}
						if (e.isInRange(x, y, basicAttack.range + e.radius) && basicAttack.canTargetable(e)) {
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
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public void hit(int damage, byte pirce) {
		boolean isBuffed = false;
		for (Entity e : ref.updater.entities) {
			if (e instanceof Leuchte && ((Leuchte) e).upgrade == Upgrade.BUFF
					&& isInRange(e.x, e.y, ((Leuchte) e).getBasicAttack().range))
				isBuffed = true;
		}
		byte armor = this.armor;
		if (isBuffed) {
			armor = 5;
		}
		if (isMortal()) {// only for nonimmortal objects
			hp -= damage * (1.0 - ((armor - pirce > 0) ? armor - pirce : 0) * 0.05) * (isBuffed ? 0.75 : 1);
			/** check if it was lasthit */
			if (hp <= 0 && hp != Integer.MAX_VALUE) {// marker
				hp = -32768;
				onDeath();
			}

		}
	}

	@Override
	public void calculateDamage(Attack a) {
		Entity target = a.getTarget();
		ref.updater.send(HIT + " " + target.number + " " + a.damage + " " + a.pirce);
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
		float y = PApplet.lerp(this.y - height, target.y - target.height, progress);
		ref.app.fill(50, 0, 255);
		ref.app.strokeWeight(0);
		ref.app.ellipse(xToGrid(x), yToGrid(y), 1, 1);
		ref.app.strokeWeight(1);
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	@Override
	public byte getUpgradeRange() {
		return upgradeRange;
	}

}