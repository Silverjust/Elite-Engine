package entity.campain;

import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.animation.Attack;
import entity.animation.Explosion;
import entity.animation.MeleeAttack;
import processing.core.PImage;
import shared.Player;
import shared.ref;

public class Asteroid extends Unit implements Attacker {

	private static PImage standingImg;
	MeleeAttack basicAttack;

	private Player side;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Asteroid");
	}

	public Asteroid(String[] c) {
		super(c);
		side = player;
		player = ref.updater.neutral;
		iconImg = standingImg;
		stand = basicAttack = new MeleeAttack(standingImg, 5100);
		basicAttack.explosion = new Explosion(standingImg, 1500);
		setAnimation(basicAttack);
		// ************************************
		xSize = 20;
		ySize = 20;
		height = 10;

		speed = 0.9f;
		if (c != null && c.length > 5 && c[5] != null)
			radius = Byte.parseByte(c[5]);
		else
			radius = 15;
		sight = 70;
		groundPosition = Entity.GroundPosition.GROUND;

		basicAttack.range = radius;
		basicAttack.damage = 100;
		basicAttack.cooldown = 5000;
		basicAttack.setCastTime(5000);

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void onSpawn(boolean isServer) {
		basicAttack.startCooldown();
		basicAttack.setTargetFrom(this, this);
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isServer && getAnimation() == stand) {// ****************************************************
		}
		if (basicAttack.explosion.isFinished() && basicAttack.isNotOnCooldown()) {
			ref.updater.toRemove.add(this);
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public boolean isCollidable(Entity entity) {
		return false;
	}

	@Override
	public void calculateDamage(Attack a) {
		System.out.println("Asteroid.calculateDamage()");
		for (Entity e : ref.updater.entities) {
			if (e != null & e.isEnemyTo(side)
					&& e.isInRange(x, y, e.radius + a.range)) {
				ref.updater.send("<hit " + e.number + " " + (a.damage) + " "
						+ a.pirce);
			}
		}
	}

	@Override
	public void renderAir() {
		drawSelected();
		float h = 2000 * (basicAttack.getCooldownPercent() < 0 ? 0
				: 1 - basicAttack.getCooldownPercent());
		if (h < 100)
			getAnimation().draw(this, x, y - h, direction, currentFrame);
		basicAttack.drawAbility(this, direction);
		drawTaged();
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

}
