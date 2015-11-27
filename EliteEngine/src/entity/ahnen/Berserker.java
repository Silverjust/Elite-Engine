package entity.ahnen;

import entity.Attacker;
import entity.Building;
import entity.Entity;
import entity.MultiCDActive;
import entity.Unit;
import entity.animation.Ability;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.MeleeAttack;
import game.AimHandler;
import game.AimHandler.Cursor;
import game.ImageHandler;
import game.aim.Aim;
import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;

public class Berserker extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	MeleeAttack basicAttack;
	Ability buildLeuchte;
	byte attackDistance;

	public byte buildRange;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Berserker");

	}

	public Berserker(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new MeleeAttack(standingImg, 800);
		buildLeuchte = new Ability(standingImg, 500);

		setAnimation(walk);

		// ************************************
		xSize = 15;
		ySize = 15;
		height = 10;

		kerit = 300;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		trainTime = 4000;

		hp = hp_max = 250;
		armor = 1;
		speed = 0.9f;
		radius = 7;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		aggroRange = (byte) (radius + 50);
		basicAttack.range = 10;
		basicAttack.damage = 45;
		basicAttack.cooldown = 1500;
		basicAttack.setCastTime(100);
		attackDistance = 10;

		buildRange = 127;
		buildLeuchte.cooldown = 40000;

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
		buildLeuchte.updateAbility(this, isServer);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("buildLeuchte")) {
			buildLeuchte.startCooldown();
			setAnimation(buildLeuchte);
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		float x, y, xDirection = a.getTarget().x, yDirection = a.getTarget().y;
		x = (this.x + (xDirection - this.x) / PApplet.dist(this.x, this.y, xDirection, yDirection) * (attackDistance));
		y = (this.y + (yDirection - this.y) / PApplet.dist(this.x, this.y, xDirection, yDirection) * (attackDistance));
		for (Entity e : ref.updater.entities) {
			if (e != null & e.isEnemyTo(this) && e.isInRange(x, y, e.radius + a.range)) {
				ref.updater.send(
						HIT + S + e.number + " " + (e instanceof Building ? a.damage / 4 : a.damage) + " " + a.pirce);
			}
		}
	}

	@Override
	public void renderUnder() {
		super.renderUnder();
		if (isAlive() && AimHandler.getAim() instanceof LeuchteAim && buildLeuchte.isNotOnCooldown()) {
			ref.app.tint(player.color);
			ImageHandler.drawImage(ref.app, selectedImg, xToGrid(x), yToGrid(y), buildRange * 2, buildRange);
			ref.app.tint(255);
		}
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, currentFrame);
		drawTaged();
	}

	@Override
	public void renderRange() {
		if (this instanceof Unit) {
			ref.app.line(x, y / 2, ((Unit) this).xTarget, ((Unit) this).yTarget / 2);
		}
		if (basicAttack.getTarget() != null) {

			float x, y, xDirection = basicAttack.getTarget().x, //
					yDirection = basicAttack.getTarget().y;
			x = (this.x
					+ (xDirection - this.x) / PApplet.dist(this.x, this.y, xDirection, yDirection) * (attackDistance));
			y = (this.y
					+ (yDirection - this.y) / PApplet.dist(this.x, this.y, xDirection, yDirection) * (attackDistance));
			drawCircle(x, y, basicAttack.range);
			drawCircle(x, y, (byte) (basicAttack.range * basicAttack.getCooldownPercent()));
		}
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	public Ability getAbility() {
		return buildLeuchte;
	}

	public static class LeuchteActive extends MultiCDActive {
		Class<? extends Building> building;
		String descr = " ", stats = " ";

		public LeuchteActive(int x, int y, char n, Entity u, Class<?> trainer) {
			super(x, y, n, u.iconImg);
			clazz = trainer;
			setAbilityGetter("getAbility");
			building = ((Building) u).getClass();
			descr = u.getDesription();
			stats = u.getStatistics();
		}

		@Override
		public void onActivation() {
			Entity builder = null;
			for (Entity e : ref.updater.selected) {
				if (clazz.isAssignableFrom(e.getClass())) {
					builder = e;
				}
			}
			if (builder != null) {
				AimHandler.setAim(new LeuchteAim(this, builder));
			}
		}

		@Override
		public String getDesription() {
			return descr;
		}

		@Override
		public String getStatistics() {
			return "cooldown: " + cooldown + stats;
		}
	}

	public static class LeuchteAim extends Aim {

		private Entity builder;
		private LeuchteActive active;
		private Leuchte buildable;

		public LeuchteAim(LeuchteActive leuchteActive, Entity builder) {
			this.active = leuchteActive;
			this.builder = builder;
			buildable = new Leuchte(null);
		}

		@Override
		public Cursor getCursor() {
			return Cursor.BUILD;
		}

		@Override
		public void update() {
			float x, y;
			x = Building.xToGrid(Building.gridToX());
			y = Building.xToGrid(Building.gridToY());
			if (canPlaceAt(x, y)) {
				ref.app.tint(255, 150);
			} else {
				ref.app.tint(255, 100, 100, 150);
			}
			ImageHandler.drawImage(ref.app, buildable.preview(), x, y / 2, buildable.xSize, buildable.ySize);
			ref.app.tint(255);
		}

		@Override
		public void execute(float x, float y) {
			if (canPlaceAt(x, y)) {
				ref.updater.send(SPAWN + S + buildable.getClass().getSimpleName() + " " + builder.player.getUser().ip
						+ " " + x + " " + y);
				buildable.buyFrom(builder.player);
			}
			Entity builder = null;
			for (Entity e : ref.updater.selected) {
				if (e instanceof Berserker && e.player == this.builder.player
						&& e.isInRange(x, y, ((Berserker) e).buildRange)
						&& ((Berserker) e).buildLeuchte.isNotOnCooldown()) {
					builder = e;
				}
			}
			if (builder != null) {
				active.startCooldown();
				builder.sendAnimation("buildLeuchte " + x + " " + y);
			}
		}

		protected boolean canPlaceAt(float x, float y) {
			boolean placeFree = true;
			boolean inBerserkerRange = false;
			for (Entity e : ref.updater.entities) {
				if (e.isInRange(x, y, buildable.radius + e.radius) && e.groundPosition == GroundPosition.GROUND)
					placeFree = false;
				if (e instanceof Berserker && e.player == builder.player
						&& e.isInRange(x, y, ((Berserker) e).buildRange)
						&& ((Berserker) e).buildLeuchte.isNotOnCooldown()) {
					inBerserkerRange = true;
				}
			}
			return placeFree && inBerserkerRange;
		}
	}
}
