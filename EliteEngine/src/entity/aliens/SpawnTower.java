package entity.aliens;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import shared.ref;
import entity.Building;
import entity.Commander;
import entity.Entity;
import entity.animation.Ability;
import entity.animation.Animation;
import entity.animation.Build;
import entity.animation.Death;
import game.ImageHandler;

public class SpawnTower extends Building implements Commander {
	private int commandingRange;

	RuglingSpawn spawn;

	private int spawnRange;

	private byte maxShootlings;

	private static PImage standImg;
	private static PImage previewImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		previewImg = standImg = ImageHandler.load(path, "SpawnTower");
	}

	public SpawnTower(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = new Build(standImg, 1000);
		death = new Death(standImg, 100);
		spawn = new RuglingSpawn(standImg, 800);

		setAnimation(build);

		// ************************************
		xSize = 30;
		ySize = 30;

		kerit = 450;
		pax = 600;
		arcanum = 0;
		prunam = 0;
		build.setBuildTime(10000);

		sight = 70;

		hp = hp_max = 200;
		radius = 15;

		spawnRange = 150;
		maxShootlings = 12;
		spawn.cooldown = 1900;
		spawn.setCastTime(500);

		commandingRange = 250;

		descr = " ";
		stats = "spawns/s: " + 1 + "/" + spawn.cooldown / 1000.0;
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		float importance = 0;
		Entity importantEntity = null;
		if (isServer && (getAnimation() == stand)) {// ****************************************************
			for (Entity e : player.visibleEntities) {
				if (e.isEnemyTo(this)) {
					if (e.isInRange(x, y, spawnRange + e.radius)
							&& !(e instanceof Building)) {
						float newImportance = calcImportanceOf(e);
						if (newImportance > importance) {
							importance = newImportance;
							importantEntity = e;
						}
					}
				}
			}
			if (importantEntity != null && spawn.isNotOnCooldown()
					&& getShootlingsInRange() < maxShootlings) {
				sendAnimation("spawn " + importantEntity.number);
			}
		}
		spawn.updateAbility(this, isServer);

	}

	private int getShootlingsInRange() {
		int n = 0;
		for (Entity e : player.visibleEntities) {
			if (e instanceof Shootling && e.isAllyTo(this)
					&& e.isInRange(x, y, spawnRange))
				n++;
		}
		return n;
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("spawn")) {
			int n = Integer.parseInt(c[3]);
			Entity e = ref.updater.namedEntities.get(n);
			spawn.setTarget(e);
			setAnimation(spawn);
		}
	}

	@Override
	public void renderTerrain() {
		ref.app.image(AlienMainBuilding.groundImg, xToGrid(x), yToGrid(y),
				commandingRange * 2, commandingRange);
	}

	@Override
	public void drawOnMinimapUnder(PGraphics graphics) {
		graphics.image(AlienMainBuilding.groundImg, x, y, commandingRange * 2,
				commandingRange * 2);
	}
	@Override
	public void renderUnder() {}//dont display buildrange
	@Override
	public void renderRange() {
		drawCircle(spawnRange);
		drawCircle((int) (spawnRange * spawn.getCooldownPercent()));
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, currentFrame);
	}

	public PImage preview() {
		return previewImg;
	}

	@Override
	public int commandRange() {
		return commandingRange;
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
					float yt = target.y;
					float xt = target.x;
					byte shootlingRadius = 5;
					ref.updater.send("<spawn Shootling "
							+ e.player.user.ip
							+ " "
							+ (e.x + (xt - e.x)
									/ PApplet.dist(e.x, e.y, xt, yt)
									* (e.radius + shootlingRadius))
							+ " "
							+ (e.y + (yt - e.y)
									/ PApplet.dist(e.x, e.y, xt, yt)
									* (e.radius + shootlingRadius)) + " " + xt
							+ " " + yt + " " + e.number);
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
}
