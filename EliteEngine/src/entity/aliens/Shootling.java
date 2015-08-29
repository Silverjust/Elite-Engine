package entity.aliens;

import entity.Entity;
import entity.Shooter;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.ShootAttack;
import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;

public class Shootling extends Unit implements Shooter {

	private static PImage standingImg;

	byte aggroRange;

	ShootAttack basicAttack;

	private Death splashDeath;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Shootling");
	}

	public Shootling(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		splashDeath = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);

		setAnimation(walk);
		// ************************************
		xSize = 30;
		ySize = 30;

		hp = hp_max = 80;
		speed = 3.2f;
		radius = 4;
		sight = 50;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 100);
		basicAttack.range = (byte) (radius + 12);
		basicAttack.damage = 10;
		basicAttack.cooldown = 1500;
		basicAttack.setCastTime(500);
		basicAttack.speed = 1;
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isServer && (getAnimation() == walk || getAnimation() == stand)) {// ****************************************************
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
			} else if (importantEntity == null) {
				sendAnimation("splashDeath");
			}
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public boolean isCollision(Entity e) {
		boolean b = e.getClass() != Rug.class;
		return super.isCollision(e) && b;
	}

	@Override
	public void calculateDamage(Attack a) {
		ref.updater.send("<hit " + basicAttack.getTarget().number + " "
				+ a.damage + " " + a.pirce);

	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("splashDeath")) {
			setAnimation(splashDeath);
		}
	}

	@Override
	public void renderGround() {
		isSelected = false;
		drawSelected();
		getAnimation().draw(this, direction, currentFrame);
		basicAttack.drawAbility(this, direction);
		drawTaged();
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	@Override
	public void drawShot(Entity target, float progress) {
		float x = PApplet.lerp(this.x, target.x, progress);
		float y = PApplet.lerp(this.y - height, target.y - target.height,
				progress);
		ref.app.fill(100, 100, 0);
		ref.app.strokeWeight(0);
		ref.app.ellipse(xToGrid(x), yToGrid(y), 3, 3);
		ref.app.strokeWeight(1);
	}

}
