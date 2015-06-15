package entity.aliens;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import shared.Nation;
import shared.ref;
import entity.Attacker;
import entity.Building;
import entity.Commander;
import entity.Entity;
import entity.Shooter;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Build;
import entity.animation.Death;
import entity.animation.ShootAttack;
import game.ImageHandler;

public class ThornTower extends Building implements Attacker, Shooter,
		Commander {
	private int commandingRange;

	ShootAttack basicAttack;

	private static PImage standImg;
	private static PImage previewImg;

	public static void loadImages() {
		String path = path(Nation.ALIENS, new Object() {
		});
		previewImg = standImg = ImageHandler.load(path, "ThornTower");
	}

	public ThornTower(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = new Build(standImg, 1000);
		death = new Death(standImg, 100);
		basicAttack = new ShootAttack(standImg, 800);

		animation = nextAnimation = build;
		// ************************************
		xSize = 30;
		ySize = 30;

		kerit = 450;
		pax = 100;
		arcanum = 0;
		prunam = 0;
		build.setBuildTime(10000);

		sight = 70;

		hp = hp_max = 800;
		radius = 15;

		basicAttack.range = 70;
		basicAttack.damage = 55;
		basicAttack.cooldown = 2000;
		basicAttack.beginTime = 500;// eventtime is defined by target distance
		basicAttack.speed = 0.1f;

		commandingRange = 250;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions() {
		float importance = 0;
		Entity importantEntity = null;
		if (animation == stand) {
			for (Entity e : player.visibleEntities) {
				if (e.isEnemyTo(this)) {
					if (e.isInRange(x, y, basicAttack.range + e.radius)
							&& !(e instanceof Building)) {
						float newImportance = calcImportanceOf(e);
						if (newImportance > importance) {
							importance = newImportance;
							importantEntity = e;
						}
					}
				}
			}
			if (importantEntity != null && getBasicAttack().isNotOnCooldown()) {
				sendAnimation("basicAttack " + importantEntity.number);
			}
		}
		basicAttack.updateAbility(this);

	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		Attack.updateExecAttack(c, this);
	}

	@Override
	public void calculateDamage(Attack a) {
		ref.updater.send("<hit " + basicAttack.getTarget().number + " "
				+ a.damage + " " + a.pirce);

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
	public void renderGround() {
		drawSelected();
		animation.draw(this, (byte) 0, currentFrame);
		basicAttack.drawAbility(this, (byte) 0);
	}

	@Override
	public void drawShot(Entity target, float progress) {
		float x = PApplet.lerp(this.x, target.x, progress);
		float y = PApplet.lerp(this.y, target.y, progress);
		ref.app.stroke(100, 100, 0);
		ref.app.line(xToGrid(this.x), yToGrid(this.y), x, y / 2);
		ref.app.stroke(0);
	}

	public PImage preview() {
		return previewImg;
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	@Override
	public int commandRange() {
		return commandingRange;
	}

}
