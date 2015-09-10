package entity.ahnen;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;
import entity.Active;
import entity.Entity;
import entity.Shooter;
import entity.Unit;
import entity.ahnen.Leuchte.Upgrade;
import entity.animation.Ability;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.Explosion;
import entity.animation.ShootAttack;

public class Destructor extends Unit implements Shooter, Buffing {

	private static PImage standingImg;

	byte aggroRange;

	ShootAttack basicAttack;

	public byte upgradeRange;

	private byte splashrange;

	private byte spawnRange;

	private RuglingSpawn spawn;

	private int orb;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Destructor");
	}

	public Destructor(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);
		basicAttack.explosion = new Explosion(selectedImg, 1000);
		spawn = new RuglingSpawn(standingImg, 400);

		setAnimation(walk);
		// ************************************
		xSize = 15;
		ySize = 15;
		height = 10;

		kerit = 250;
		pax = 0;
		arcanum = 0;
		prunam = 10;
		trainTime = 3000;

		hp = hp_max = 100;
		armor = 3;
		speed = 0.9f;
		radius = 7;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = 110;
		splashrange = 10;
		basicAttack.damage = 45;// buffed *2
		basicAttack.pirce = 3;// buffed 5
		basicAttack.cooldown = 1500;
		basicAttack.range = 40;
		basicAttack.setCastTime(200);// eventtime is defined by target distance
		basicAttack.speed = 1f;

		spawnRange = 50;
		spawn.cooldown = 4000;
		spawn.setCastTime(500);

		upgradeRange = 100;

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
						if (e.isInRange(x, y, spawnRange + e.radius)) {
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
			if (isEnemyInHitRange && basicAttack.isNotOnCooldown()) {
				sendAnimation("basicAttack " + importantEntity.number);
			} else if (importantEntity != null && spawn.isNotOnCooldown()
					&& hasNoOrb() && isBuffed()) {
				sendAnimation("spawn " + importantEntity.number);
			} else if (importantEntity != null) {
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			}
		}
		basicAttack.updateAbility(this, isServer);
		spawn.updateAbility(this, isServer);
	}

	private boolean isBuffed() {
		boolean isBuffed = false;
		for (Entity e : ref.updater.entities) {
			if (e instanceof Leuchte
					&& ((Leuchte) e).upgrade == Upgrade.BUFF
					&& isInRange(e.x, e.y, ((Leuchte) e).getBasicAttack().range))
				isBuffed = true;
		}
		return isBuffed;
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("spawn")) {
			if (hasNoOrb()) {
				isMoving = false;
				int n = Integer.parseInt(c[3]);
				Entity e = ref.updater.namedEntities.get(n);
				spawn.setTarget(e);
				setAnimation(spawn);
			}
		} else if (c[2].equals("spawned")) {
			int n = Integer.parseInt(c[3]);
			orb = n;
		}
	}

	private boolean hasNoOrb() {
		Entity e = ref.updater.namedEntities.get(orb);
		return e == null || !e.isAlive();
	}

	@Override
	public void calculateDamage(Attack a) {
		boolean isBuffed = isBuffed();
		Entity target = ((ShootAttack) a).getTarget();
		for (Entity e : ref.updater.entities) {
			if (e != null & e.isEnemyTo(this)
					&& e.isInRange(target.x, target.y, e.radius + splashrange)
					&& e.groundPosition == GroundPosition.GROUND) {
				ref.updater.send("<hit " + e.number + " "
						+ (isBuffed ? a.damage * 2 : a.damage) + " "
						+ (isBuffed ? 5 : a.pirce));
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
		ref.app.fill(50, 255, 0);
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

	static class RuglingSpawn extends Ability {

		private Entity target;

		public RuglingSpawn(PImage IMG, int duration) {
			super(IMG, duration);
		}

		public void setTarget(Entity e) {
			target = e;
		}

		@Override
		public void updateAbility(Entity e, boolean isServer) {
			if (target != null && isEvent()) {
				if (isServer) {
					ref.updater.send("<spawn Orb " + e.player.user.ip + " " + e.x
							+ " " + (e.y + e.radius + 8) + " " + target.x + " "
							+ target.y + " " + e.number);
					/*
					 * ref.updater.send("<spawn Rugling " + e.player.ip + " " +
					 * e.x + " " + (e.y - e.radius - 8) + " " + target.x + " " +
					 * target.y);
					 */
				}
				target = null;
				// startCooldown();
			}
		}

		@Override
		public boolean isSetup() {
			return target != null;
		}
	}

	public static class UpgradeActive extends Active {
		public UpgradeActive(int x, int y, char n) {
			super(x, y, n, Leuchte.buffImg);
			clazz = Buffing.class;
		}

		@Override
		public String getDesription() {
			return "upgrade lampe";
		}

		@Override
		public void onActivation() {
			for (Entity e : ref.updater.entities) {
				if (e instanceof Leuchte
						&& e.getAnimation() == ((Leuchte) e).heal) {
					for (Entity e2 : ref.updater.selected) {
						if (e2 instanceof Buffing
								&& e.isInRange(e2.x, e2.y,
										((Buffing) e2).getUpgradeRange())) {
							e.sendAnimation("buff");
						}
					}
				}
			}
		}
	}

}