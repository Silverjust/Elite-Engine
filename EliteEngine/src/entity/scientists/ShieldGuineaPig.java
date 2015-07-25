package entity.scientists;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;
import entity.Attacker;
import entity.Entity;
import entity.Shooter;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.MeleeAttack;
import entity.animation.ShootAttack;

public class ShieldGuineaPig extends Unit implements Attacker, Shooter {

	private static PImage standingImg;
	private static PImage shieldImg;

	byte aggroRange;
	byte shield;
	byte shield_max;

	ShootAttack basicAttack;
	MeleeAttack regenerate;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "ShieldGuineaPig");
		shieldImg = game.ImageHandler.load(path, "Shield");
	}

	public ShieldGuineaPig(String[] c) {
		super(c);
		GuineaPig.setupEquip(this, c);

		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);
		regenerate = new MeleeAttack(shieldImg, 800);// shield regeneration

		setAnimation(walk);
		
		// ************************************
		xSize = 15;
		ySize = 15;

		kerit = 120;
		pax = 20;
		arcanum = 0;
		prunam = 4;
		trainTime = 1500;

		shield_max = 60;
		hp = hp_max = 100;
		armor = 1;
		speed = 0.9f;
		radius = 7;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 50);
		basicAttack.damage = 7;
		basicAttack.pirce = 0;
		basicAttack.cooldown = 1500;
		basicAttack.range = 40;
		basicAttack.setCastTime(100);// eventtime is defined by target distance
		basicAttack.speed = 0.6f;

		regenerate.damage = 6;
		regenerate.pirce = -2;
		regenerate.cooldown = 1500;
		regenerate.range = 1;
		regenerate.setCastTime(0);
		regenerate.setTargetFrom(this, this);

		descr = " unit with shield";
		stats = "shield: " + shield_max + " +" + regenerate.damage + "/"
				+ (regenerate.cooldown / 1000.0) + "s";
		// ************************************
	}

	@Override
	public void updateDecisions() {
		if (getAnimation() == walk && isAggro || getAnimation() == stand) {// ****************************************************
			boolean isEnemyInHitRange = false;
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
						}
						if (e.isInRange(x, y, basicAttack.range + e.radius)) {
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
				Attack.sendWalkToEnemy(this, importantEntity);
			}
		}
		basicAttack.updateAbility(this);
		regenerate.setTargetFrom(this, this);
		if (hp == hp_max)
			regenerate.updateAbility(this);
	}

	@Override
	public void calculateDamage(Attack a) {
		ref.updater.send("<hit " + ((MeleeAttack) a).getTarget().number + " "
				+ a.damage + " " + a.pirce);
		// SoundHandler.startIngameSound(HUD.hm, x, y);
	}

	@Override
	public void hit(int damage, byte pirce) {
		if (isMortal()) {
			if (pirce == -2) {
				shield += damage;
				if (shield > shield_max)
					shield = shield_max;
			} else if (shield > 0) {
				shield -= damage;
				if (shield < 0) {
					hp += shield * 2;
					shield = 0;
				}
			} else {
				hp -= damage
						* (1.0 - ((armor - pirce > 0) ? armor - pirce : 0) * 0.05);
				/** check if it was lasthit */
				if (hp <= 0 && hp != Integer.MIN_VALUE) {// marker
					hp = Integer.MIN_VALUE;
					onDeath();
				}
			}

		}
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, currentFrame);
		if (shield > 0) {
			ref.app.tint(255, ((float) shield / shield_max * 255f));
			regenerate.draw(this, direction, currentFrame);
			ref.app.tint(255);
		}
		basicAttack.drawAbility(this, direction);
		drawTaged();
	}

	@Override
	public void drawShot(Entity target, float progress) {
		float x = PApplet.lerp(this.x, target.x, progress);
		float y = PApplet.lerp(this.y - height, target.y - target.height,
				progress);
		ref.app.fill(255, 100, 0);
		ref.app.strokeWeight(0);
		ref.app.ellipse(xToGrid(x), yToGrid(y), 1, 1);
		ref.app.strokeWeight(1);
	}

	@Override
	public void display() {
		super.display();
		drawBar((float) shield / shield_max, ref.app.color(255, 50, 0));
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

}