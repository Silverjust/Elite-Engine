package entity.ahnen;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;
import entity.Active;
import entity.AimingActive;
import entity.Attacker;
import entity.Entity;
import entity.MultiCDActive;
import entity.Shooter;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.Explosion;
import entity.animation.MeleeAttack;
import entity.animation.ShootAttack;
import game.AimHandler;
import game.AimHandler.Cursor;
import game.aim.CustomAim;

public class Witcher extends Unit implements Attacker, Shooter {

	private static PImage standingImg;

	byte aggroRange;

	ShootAttack basicAttack;
	MeleeAttack burst;
	Explosion burstplosion;
	static boolean displayBurstArea = false;

	float burstX, burstY;

	public byte upgradeRange;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Witcher");
	}

	public Witcher(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);
		burst = new MeleeAttack(standingImg, 300);
		burstplosion = new Explosion(selectedImg, 1000);

		setAnimation(walk);

		// ************************************
		xSize = 15;
		ySize = 15;
		height = 10;

		kerit = 300;
		pax = 200;
		arcanum = 0;
		prunam = 0;
		trainTime = 1500;

		hp = hp_max = 100;
		armor = 1;
		speed = 0.9f;
		radius = 7;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = 110;
		basicAttack.damage = 90;// x2
		basicAttack.pirce = 3;
		basicAttack.cooldown = 8000;
		basicAttack.range = 100;
		basicAttack.setCastTime(200);// eventtime is defined by target distance
		basicAttack.speed = 0.5f;

		burst.damage = 30;
		burst.pirce = 0;
		burst.cooldown = 20000;
		burst.range = 60;
		burst.setCastTime(1000);

		upgradeRange = 100;

		descr = " ";
		stats = "active: " + burst.damage + "/" + burst.cooldown / 1000.0
				+ " (" + burst.pirce + ")" + " _°§";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (getAnimation() == walk && isAggro || getAnimation() == stand) {// ****************************************************
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
		}
		basicAttack.updateAbility(this, isServer);
		burst.updateAbility(this, isServer);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("burst")) {
			burst.setTargetFrom(this, this);
			burstX = Float.parseFloat(c[3]);
			burstY = Float.parseFloat(c[4]);
			burstplosion.setup(null);
			setAnimation(burst);
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		if (a == basicAttack) {
			ref.updater.send("<hit " + a.getTarget().number + " " + a.damage
					* 2 + " " + a.pirce);
			// SoundHandler.startIngameSound(HUD.hm, x, y);
		} else {
			for (Entity e : ref.updater.entities) {
				if (e != null & e.isEnemyTo(this)
						&& e.isInRange(burstX, burstY, e.radius + burst.range)) {
					ref.updater.send("<hit " + e.number + " " + a.damage + " "
							+ a.pirce);
				}
			}
		}
	}

	@Override
	public void renderUnder() {
		super.renderUnder();
		if (!(AimHandler.getAim() instanceof CustomAim))
			displayBurstArea = false;
		if (isAlive() && displayBurstArea && burst.isNotOnCooldown()) {
			ref.app.tint(player.color);
			ref.app.image(selectedImg, xToGrid(x), yToGrid(y),
					basicAttack.range * 2, basicAttack.range);
			ref.app.tint(255);
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
	public void renderAir() {
		if (burst.isSetup())
			burstplosion.draw(burstX, burstY);
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

	public Attack getBurst() {
		return burst;
	}

	public static class BurstActive extends MultiCDActive implements
			AimingActive {

		public BurstActive(int x, int y, char n) {
			super(x, y, n, standingImg);
			// cooldown = 60000;
			clazz = Witcher.class;
			setAbilityGetter("getBurst");
		}

		@Override
		public void onActivation() {
			Witcher caster = null;
			for (Entity e : ref.updater.selected) {
				if (e instanceof Witcher
						&& (e.getAnimation() == e.stand || e.getAnimation() == ((Unit) e).walk)
						&& ((Witcher) e).getBurst().isNotOnCooldown()
						&& !((Witcher) e).getBurst().isSetup())
					caster = (Witcher) e;
			}
			if (caster != null) {
				Witcher.displayBurstArea = true;
				AimHandler.setAim(new CustomAim(this, Cursor.SHOOT));
			}
		}

		@Override
		public String getDesription() {
			return "teleports units  from §physicslab to physicslab";
		}

		@Override
		public void execute(float x, float y) {
			Witcher caster = null;
			for (Entity e : ref.updater.selected) {
				if (e instanceof Witcher
						&& (e.getAnimation() == e.stand || e.getAnimation() == ((Unit) e).walk)
						&& ((Witcher) e).getBurst().isNotOnCooldown()
						&& !((Witcher) e).getBurst().isSetup()
						&& e.isInRange(x, y,
								((Witcher) e).getBasicAttack().range))
					caster = (Witcher) e;
			}
			if (caster != null) {
				Witcher.displayBurstArea = false;
				caster.sendAnimation("burst " + x + " " + y);
				startCooldown();
			}
		}
	}

	public static class UpgradeActive extends Active {
		public UpgradeActive(int x, int y, char n) {
			super(x, y, n, Leuchte.healImg);
			clazz = Witcher.class;
		}

		@Override
		public String getDesription() {
			return "upgrade lampe";
		}

		@Override
		public void onActivation() {
			for (Entity e : ref.updater.entities) {
				if (e instanceof Leuchte && e.getAnimation() == e.stand) {
					for (Entity e2 : ref.updater.selected) {
						if (e2 instanceof Witcher
								&& e.isInRange(e2.x, e2.y,
										((Witcher) e2).upgradeRange)) {
							e.sendAnimation("heal");
						}
					}
				}
			}
		}
	}
}