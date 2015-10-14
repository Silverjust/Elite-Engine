package entity.ahnen;

import processing.core.PApplet;
import processing.core.PImage;
import shared.Nation;
import shared.Player;
import shared.ref;
import entity.Active;
import entity.Attacker;
import entity.Entity;
import entity.Shooter;
import entity.Unit;
import entity.animation.Ability;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.ShootAttack;
import game.ImageHandler;

public class Angel extends Unit implements Attacker, Shooter {

	private static PImage standingImg;

	private static PImage cloakSym;

	byte aggroRange;
	boolean isCloaked;

	ShootAttack basicAttack;
	Ability cloak;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Angel");
		cloakSym = ImageHandler.load(Nation.AHNEN.toString() + "/symbols/",
				"cloak");
	}

	public Angel(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);
		cloak = new Ability(standingImg, 1000);

		setAnimation(walk);

		// ************************************
		xSize = 15;
		ySize = 15;
		height = 30;

		kerit = 300;
		pax = 0;
		arcanum = 0;
		prunam = 10;
		trainTime = 4000;

		hp = hp_max = 100;
		armor = 1;
		speed = 0.7f;
		radius = 7;
		sight = 90;
		groundPosition = Entity.GroundPosition.AIR;

		aggroRange = (byte) (radius + 60);
		basicAttack.range = (byte) (radius + 20);
		basicAttack.damage = 80;
		basicAttack.pirce = 1;
		basicAttack.cooldown = 3000;
		basicAttack.range = 25;
		basicAttack.setCastTime(100);// eventtime is defined by target distance
		basicAttack.speed = 0.6f;

		cloak.cooldown = 40000;
		cloak.doRepeat = true;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isServer
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
							if (e.isInRange(x, y, basicAttack.range + e.radius))
								isEnemyInHitRange = true;
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
		if (isServer && isCloaked && cloak.isNotOnCooldown()) {
			sendDefaultAnimation(getAnimation());
		}
	}

	@Override
	public boolean isCollidable(Entity entity) {
		return !isCloaked;
	}

	@Override
	public boolean isVisibleTo(Player p) {
		return super.isVisibleTo(p) && (!isCloaked || player == p);
	}

	@Override
	public void hit(int damage, byte pirce) {
		if (!isCloaked)
			super.hit(damage, pirce);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("cloak")) {
			if (getAnimation() != cloak) {
				isCloaked = true;
				height = 5;
				radius = 5;
				isMoving = false;
				groundPosition = GroundPosition.GROUND;
				cloak.startCooldown();
				setAnimation(cloak);
			}
		} else if (c[2].equals("stand") || c[2].equals("walk")) {
			if (getAnimation() == cloak) {
				isCloaked = false;
				height = 30;
				radius = 7;
				groundPosition = GroundPosition.AIR;
			}
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		ref.updater.send("<hit " + basicAttack.getTarget().number + " "
				+ a.damage + " " + a.pirce);
		// SoundHandler.startIngameSound(HUD.hm, x, y);
	}

	@Override
	public void renderAir() {
		drawSelected();
		if (isCloaked) {
			ref.app.tint(255, 150);
		}
		getAnimation().draw(this, direction, currentFrame);
		ref.app.tint(255);
		basicAttack.drawAbility(this, direction);
		drawTaged();
	}

	@Override
	public void display() {
		super.display();
		if (isCloaked)
			drawBar(1 - cloak.getCooldownPercent());
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

	public static class CloakActive extends Active {
		public CloakActive(int x, int y, char n) {
			super(x, y, n, cloakSym);
			clazz = Angel.class;
		}

		@Override
		public String getDesription() {
			return "cloak";
		}

		@Override
		public void onActivation() {
			for (Entity e : ref.updater.selected) {
				if (e instanceof Angel) {
					e.sendAnimation("cloak");
				}
			}
		}
	}

}