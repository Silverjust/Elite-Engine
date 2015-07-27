package entity.aliens;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;
import entity.Building;
import entity.Entity;
import entity.Shooter;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.MeleeAttack;
import entity.animation.ShootAttack;

public class Ker extends Unit implements Shooter {

	private static PImage standingImg;

	byte aggroRange;

	MeleeAttack basicAttack;
	ShootAttack shoot;

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
		basicAttack = new MeleeAttack(standingImg, 800);
		shoot = new ShootAttack(standingImg, 800);

		setAnimation(walk);

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

		aggroRange = 100;
		basicAttack.range = (byte) (radius + 10);
		basicAttack.damage = 112;
		basicAttack.pirce = 5;
		basicAttack.cooldown = 700;
		basicAttack.setCastTime(500);
		basicAttack.targetable = groundPosition;

		shoot.range = 90;
		shoot.damage = 120;
		shoot.pirce = 3;
		shoot.cooldown = 2000;
		shoot.setCastTime(500);
		shoot.speed = 1;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (getAnimation() == walk && isAggro || getAnimation() == stand) {// ****************************************************
			boolean isEnemyInHitRange = false;
			boolean isEnemyInShootRange = false;
			float importance = 0;
			Entity importantEntity = null;
			for (Entity e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isInRange(x, y, aggroRange + e.radius)) {
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
							if (e.isInRange(x, y, basicAttack.range + e.radius)
									&& basicAttack.canTargetable(e))
								isEnemyInHitRange = true;
							if (e.isInRange(x, y, shoot.range + e.radius)
									&& shoot.canTargetable(e))
								isEnemyInShootRange = true;
						}

					}
				}
			}
			if (isEnemyInHitRange && basicAttack.isNotOnCooldown()) {
				sendAnimation("basicAttack " + importantEntity.number);
			} else if (isEnemyInShootRange && shoot.isNotOnCooldown()) {
				sendAnimation("shoot " + importantEntity.number);
			} else if (importantEntity != null) {
				Attack.sendWalkToEnemy(
						this,
						importantEntity,
						importantEntity.groundPosition == GroundPosition.AIR ? shoot.range
								: basicAttack.range);
			}
		}
		basicAttack.updateAbility(this, isServer);
		shoot.updateAbility(this, isServer);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("shoot") && shoot.isNotOnCooldown()) {
			int n = Integer.parseInt(c[3]);
			Entity e = ref.updater.namedEntities.get(n);
			shoot.setTargetFrom(this, e);
			setAnimation(shoot);
			isMoving = false;
		}

	}

	@Override
	public void calculateDamage(Attack a) {
		ref.updater.send("<hit " + a.getTarget().number + " "
				+ (a.getTarget() instanceof Building ? a.damage / 2 : a.damage)
				+ " " + a.pirce);
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, currentFrame);
		basicAttack.drawAbility(this, direction);

		drawTaged();
	}

	@Override
	public void renderRange() {
		super.renderRange();
		drawCircle(shoot.range);
		drawCircle((int) (shoot.range * shoot.getCooldownPercent()));
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
		ref.app.fill(200, 255, 0);
		ref.app.strokeWeight(0);
		ref.app.ellipse(xToGrid(x), yToGrid(y), 2, 2);
		ref.app.strokeWeight(1);
	}

}