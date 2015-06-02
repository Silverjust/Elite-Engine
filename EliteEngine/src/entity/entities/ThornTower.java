package entity.entities;

import processing.core.PImage;
import shared.Nation;
import shared.ref;
import entity.Attacker;
import entity.Buildable;
import entity.Building;
import entity.Commanding;
import entity.Entity;
import entity.animation.Ability;
import entity.animation.Animation;
import entity.animation.AreaAttack;
import entity.animation.Death;
import entity.animation.TargetAttack;
import game.ImageHandler;

public class ThornTower extends Building implements Buildable, Attacker,
		Commanding {
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
		stand = new Animation(standImg, 100);
		build = new Animation(standImg, 100);
		death = new Death(standImg, 100);
		basicAttack = new TargetAttack(standImg, 800);

		animation = nextAnimation = stand;
		// ************************************
		xSize = 30;
		ySize = 30;

		sight = 70;

		hp = hp_max = 1000;
		radius = 15;

		basicAttack.range = 70;
		basicAttack.damage = 55;
		basicAttack.cooldown = 2000;
		basicAttack.eventTime = 500;

		commandingRange = 250;
		// ************************************
	}

	@Override
	public void updateDecisions() {
		float importance = 0;
		Entity importantEntity = null;
		if (animation == stand) {
			for (Entity e : player.visibleEntities) {
				if (e.isEnemyTo(this)) {
					if (e.isCollision(x, y, basicAttack.range + e.radius)
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
		if (c[2].equals("basicAttack") && this instanceof Attacker) {
			Ability a = ((Attacker) this).getBasicAttack();
			if (a instanceof TargetAttack) {
				int n = Integer.parseInt(c[3]);
				Entity e = ref.updater.namedEntities.get(n);
				((TargetAttack) a).setTarget(e);
			} else if (a instanceof AreaAttack) {
				float x = Float.parseFloat(c[3]);
				float y = Float.parseFloat(c[4]);
				((AreaAttack) a).setPosition(x, y);
			}
			setAnimation(a);
		}
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
	public Ability getBasicAttack() {
		return basicAttack;
	}

	@Override
	public int commandRange() {
		return commandingRange;
	}

}
