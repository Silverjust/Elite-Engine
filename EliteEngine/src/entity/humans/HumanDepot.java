package entity.humans;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;
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

public class HumanDepot extends Building implements Commander, Shooter {
	private int commandingRange;
	ShootAttack basicAttack;
	private static PImage standImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standImg = ImageHandler.load(path, "HumanDepot");
	}

	public HumanDepot(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = new Build(standImg, 5000);
		death = new Death(standImg, 1000);
		basicAttack = new ShootAttack(standImg, 1000);

		animation = nextAnimation = build;
		// ************************************
		xSize = 20;
		ySize = 20;

		kerit = 500;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		build.setBuildTime(5000);

		sight = 50;

		hp = hp_max = 1000;
		radius = 13;

		basicAttack.range = 70;
		basicAttack.damage = 55;
		basicAttack.cooldown = 2000;
		basicAttack.setCastTime(500);// eventtime is defined by target distance
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
	public void renderGround() {
		drawSelected();
		animation.draw(this, (byte) 0, currentFrame);
		basicAttack.drawAbility(this, (byte) 0);
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

	public PImage preview() {
		return standImg;
	}

	@Override
	public int commandRange() {
		return commandingRange;
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

}
