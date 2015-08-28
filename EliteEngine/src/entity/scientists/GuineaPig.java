package entity.scientists;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ContentListHandler;
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

public class GuineaPig extends Unit implements Attacker, Shooter {
	private static PImage standingImg;

	byte aggroRange;

	ShootAttack basicAttack;
	Equip equip;
	String EquipedUnit;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "GuineaPig");
	}

	public GuineaPig(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);
		equip = new Equip(standingImg, 800);

		setAnimation(walk);

		// ************************************
		xSize = 15;
		ySize = 15;

		kerit = 130;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		trainTime = 2000;

		hp = hp_max = 100;
		armor = 2;
		speed = 0.9f;
		radius = 5;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 50);
		basicAttack.damage = 20;
		basicAttack.pirce = 0;
		basicAttack.cooldown = 1500;
		basicAttack.range = 40;
		basicAttack.setCastTime(100);// eventtime is defined by target distance
		basicAttack.speed = 0.6f;

		descr = "scout zum scouten";
		stats = " ";
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
		equip.updateAbility(this, isServer);
	}

	@Override
	public void calculateDamage(Attack a) {
		ref.updater.send("<hit " + basicAttack.getTarget().number + " "
				+ a.damage + " " + a.pirce);
		// SoundHandler.startIngameSound(HUD.hm, x, y);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("equip")) {
			Unit unit = null;
			try {
				String name = ContentListHandler.getEntityContent().getString(
						c[3]);
				unit = (Unit) Class.forName(name)
						.getConstructor(String[].class)
						.newInstance(new Object[] { null });
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (unit != null) {
				isMoving = false;
				setAnimation(equip);
				equip.setUnit(unit);
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
		ref.app.fill(255, 100, 0);
		ref.app.strokeWeight(0);
		ref.app.ellipse(xToGrid(x), yToGrid(y), 1, 1);
		ref.app.strokeWeight(1);
	}

	@Override
	public void display() {
		super.display();
		if (getAnimation() == equip)
			drawBar(equip.getCooldownPercent());
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	static void setupEquip(Entity e, String[] c) {
		if (c != null && c.length > 7 && c[7] != null && c[7].equals("select")) {
			e.select();
		}
	}

	public static class Equip extends Ability {

		private String unit;

		public Equip(PImage IMG, int duration) {
			super(IMG, duration);
		}

		public void setUnit(Unit unit) {
			this.unit = unit.getClass().getSimpleName();
			cooldown = unit.trainTime;
			startCooldown();
		}

		@Override
		public void updateAbility(Entity e, boolean isServer) {
			if (isSetup() && isEvent()) {
				if (isServer) {
					ref.updater.send("<remove " + e.number);
					ref.updater.send(//
							"<spawn " + unit + " " + e.player.ip + " " + e.x
									+ " " + e.y + " " + ((Unit) e).xTarget
									+ " " + ((Unit) e).yTarget + " select");
				}
				unit = null;
			}
		}

		@Override
		public boolean isSetup() {
			return unit != "" && unit != null;
		}

		@Override
		public boolean isInterruptable() {
			return false;
		}
	}

	public static class EquipActive extends Active {
		Class<? extends Unit> unit;
		Class<?> lab;
		String descr = " ", stats = " ";

		public EquipActive(int x, int y, char n, Entity u, Class<?> trainer) {
			super(x, y, n, u.iconImg);
			clazz = GuineaPig.class;
			lab = trainer;
			unit = ((Unit) u).getClass();
			descr = u.getDesription();
			stats = u.getStatistics();
		}

		@Override
		public void onActivation() {
			Entity trainer = null;
			for (Entity e : ref.updater.selected) {
				if (clazz.isAssignableFrom(e.getClass())
						&& (e.getAnimation() == e.stand || e.getAnimation() == ((Unit) e).walk))
					for (Entity e2 : ref.player.visibleEntities) {
						if (e2.player == e.player
								&& e2.getClass().equals(lab)
								&& e.isInRange(e2.x, e2.y, e.radius
										+ ((Lab) e2).equipRange)) {
							trainer = e;
						}
					}
			}
			Unit newUnit = null;
			try {
				newUnit = unit.getConstructor(String[].class).newInstance(
						new Object[] { null });
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (trainer != null && newUnit != null
					&& newUnit.canBeBought(trainer.player)) {
				newUnit.buyFrom(trainer.player);

				trainer.sendAnimation("equip " + unit.getSimpleName());
			}
		}

		@Override
		public String getDesription() {
			return descr;
		}

		@Override
		public String getStatistics() {
			return stats;
		}
	}
}