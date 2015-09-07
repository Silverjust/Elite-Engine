package entity.ahnen;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;
import entity.Active;
import entity.AimingActive;
import entity.Attacker;
import entity.Building;
import entity.Commander;
import entity.Entity;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Build;
import entity.animation.Death;
import game.AimHandler;
import game.ImageHandler;
import game.AimHandler.Cursor;
import game.aim.CustomAim;

public class AhnenTower extends Building implements Commander {
	private byte unitHeight;
	private int commandingRange;
	private static PImage standImg;

	private int selectRange;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standImg = ImageHandler.load(path, "AhnenTower");
	}

	Unit unit;

	public AhnenTower(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = new Build(standImg, 5000);
		death = new Death(standImg, 1000);

		setAnimation(build);

		// ************************************
		xSize = 17;
		ySize = 17;
		height = 5;
		unitHeight = 18;

		kerit = 400;
		pax = 300;
		arcanum = 0;
		prunam = 0;
		build.setBuildTime(2000);

		sight = 80;

		hp = hp_max = 500;
		radius = 12;

		commandingRange = 100;
		selectRange = 30;

		descr = " ";
		// ************************************
	}

	@Override
	public String getStatistics() {
		if (unit != null && unit instanceof Attacker
				&& ((Attacker) unit).getBasicAttack() != null) {
			stats = "";
			Attack a = ((Attacker) unit).getBasicAttack();
			if (a.pirce >= 0) {
				stats += "dps: " + a.damage + "/" + a.cooldown / 1000.0 + " ("
						+ a.pirce + ")";
				if (a.targetable == GroundPosition.GROUND)
					stats += " _§";
				else if (a.targetable == GroundPosition.AIR)
					stats += " °§";
				else if (a.targetable == null)
					stats += " _°§";
			} else {
				stats += "heal/s: " + a.damage + "/" + a.cooldown / 1000.0
						+ "§";
			}
		}
		return super.getStatistics();
	}

	@Override
	public void updateAnimation() {
		super.updateAnimation();
		if (unit != null) {
			unit.updateAnimation();
		}
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (unit != null) {
			if (!ref.updater.entities.contains(unit))
				unit.number = number;
			unit.updateDecisions(isServer);
		}
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("in")) {
			int n = Integer.parseInt(c[3]);
			Entity e = ref.updater.namedEntities.get(n);
			if (e.isAlive() && isAllyTo(player) && e instanceof Unit) {
				if (unit != null) {
					Entity normalUnit = null;
					try {
						normalUnit = unit.getClass()
								.getConstructor(String[].class)
								.newInstance(new Object[] { null });
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					normalUnit.player = unit.player;
					normalUnit.x = x;
					normalUnit.y = y + radius + 10;
					normalUnit.hp = unit.hp;
					armor = 0;
					ref.updater.toAdd.add(normalUnit);
				}
				unit = (Unit) e;
				unit.x = x;
				unit.y = y;
				unit.isMoving = false;
				unit.isSelected = false;
				unit.height = unitHeight;
				ref.updater.toRemove.add(unit);
				if (unit instanceof Berserker)
					((Berserker) unit).getBasicAttack().range = 20;
				if (unit instanceof Warrior) {
					armor = 3;
					((Warrior) unit).getBasicAttack().cooldown = 400;
				}
				if (unit instanceof Astrator) {
					((Astrator) unit).getBasicAttack().range = 30;
					armor = 5;
				}
			}
		}
		if (unit != null)
			unit.exec(c);
	}

	@Override
	public void renderRange() {
		if (unit != null)
			unit.renderRange();
		else
			drawCircle(selectRange);
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, currentFrame);
		if (unit != null) {
			unit.renderGround();
			unit.renderAir();
		}
	}

	public PImage preview() {
		return standImg;
	}

	@Override
	public int commandRange() {
		return commandingRange;
	}

	public static class SelectActive extends Active implements AimingActive {

		public SelectActive(int x, int y, char n) {
			super(x, y, n, null);
			// cooldown = 60000;
			clazz = AhnenTower.class;
		}

		@Override
		public void onActivation() {
			Entity tower = null;
			if (tower == null) {
				for (Entity e : ref.updater.selected) {
					if (e instanceof AhnenTower && e.getAnimation() == e.stand)
						tower = (AhnenTower) e;
				}
			}
			if (tower != null) {
				AimHandler.setAim(new CustomAim(this, Cursor.SELECT));
			}
		}

		@Override
		public String getDesription() {
			return "select unit to stand in tower§unit gets buffed";
		}

		@Override
		public void execute(float x, float y) {
			// float x, y;
			Entity target = null;
			// x = Entity.xToGrid(Entity.gridToX());
			// y = Entity.xToGrid(Entity.gridToY());
			for (Entity e : ref.updater.entities) {
				if (e.isAllyTo(ref.player)
						&& e instanceof Unit
						&& PApplet.dist(x, y, e.x, e.y - e.flyHeight()) <= e.radius)
					target = e;
			}
			if (target != null) {
				Entity tower = null;
				for (Entity e : ref.updater.selected) {
					if (e instanceof AhnenTower
							&& e.getAnimation() == e.stand
							&& target.isInRange(e.x, e.y,
									((AhnenTower) e).selectRange)
							&& ((AhnenTower) e).unit == null)
						tower = (AhnenTower) e;
				}
				if (tower == null) {
					for (Entity e : ref.updater.selected) {
						if (e instanceof AhnenTower
								&& e.getAnimation() == e.stand
								&& target.isInRange(e.x, e.y,
										((AhnenTower) e).selectRange))
							tower = (AhnenTower) e;
					}
				}
				if (tower != null)
					tower.sendAnimation("in " + target.number);
				AimHandler.end();
			}
		}
	}
}
