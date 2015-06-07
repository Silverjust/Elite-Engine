package entity.aliens;

import processing.core.PImage;
import shared.Nation;
import shared.ref;
import entity.Attacker;
import entity.Building;
import entity.Commander;
import entity.Entity;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Build;
import entity.animation.Death;
import entity.animation.TargetAttack;
import game.ImageHandler;

public class ThornTower extends Building implements Attacker, Commander {
	private int commandingRange;

	TargetAttack basicAttack;

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
		basicAttack = new TargetAttack(standImg, 800);

		animation = nextAnimation = build;
		// ************************************
		xSize = 30;
		ySize = 30;

		kerit = 250;
		pax = 100;
		arcanum = 0;
		prunam = 0;
		build.setBuildTime(10000);

		sight = 70;

		hp = hp_max = 1500;
		radius = 15;

		basicAttack.range = 70;
		basicAttack.damage = 55;
		basicAttack.cooldown = 2000;
		basicAttack.eventTime = 500;

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
					if (e.isInArea(x, y, basicAttack.range + e.radius)
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
		if (basicAttack.getTarget() != null) {
			ref.app.stroke(100, 100, 0);
			ref.app.line(
					xToGrid(x),
					yToGrid(y - ySize),
					basicAttack.getTarget().x,
					(basicAttack.getTarget().y - basicAttack.getTarget().height) / 2);
			ref.app.stroke(0);
		}
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
