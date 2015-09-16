package entity.scientists;

import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.animation.Attack;
import entity.animation.MeleeAttack;
import processing.core.PImage;
import shared.Helper.Timer;
import shared.ref;

public class Swamp extends Unit implements Attacker {

	private static PImage standingImg;

	MeleeAttack basicAttack;
	Timer decay = new Timer();

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Swamp");
	}

	public Swamp(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = basicAttack = new MeleeAttack(standingImg, 800);
		setAnimation(stand);

		// ************************************
		xSize = 70;
		ySize = 35;

		speed = 2.2f;
		radius = 0;
		sight = (byte) (35 + 5);
		groundPosition = Entity.GroundPosition.GROUND;

		basicAttack.range = 35;
		basicAttack.damage = 10;
		basicAttack.cooldown = 1000;
		basicAttack.setCastTime(500);
		basicAttack.targetable = groundPosition;

		decay.cooldown = 7000;
		// ************************************
	}

	@Override
	public void onSpawn(boolean isServer) {
		if (isServer)
			decay.startCooldown();
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (decay.isNotOnCooldown())
			ref.updater.send("<remove " + number);
		if (isServer && basicAttack.isNotOnCooldown()) {
			basicAttack.startCooldown();
			basicAttack.setTargetFrom(this, this);
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public boolean isCollision(Entity e) {
		return false;
	}

	@Override
	public boolean isCollidable(Entity entity) {
		return false;
	}

	@Override
	public boolean isEnemyTo(Entity e) {
		return false;
	}

	@Override
	public void calculateDamage(Attack a) {
		for (Entity e : ref.updater.entities) {
			if (e != null && e.isEnemyTo(this)
					&& e.isInRange(x, y, e.radius + a.range)) {
				ref.updater.send("<hit " + e.number + " " + a.damage + " "
						+ a.pirce);
			}
		}
	}

	@Override
	public void select() {
		// do not select
	}

	public void renderTerrain() {
		getAnimation().draw(this, direction, currentFrame);
		drawTaged();
	}

	@Override
	public void renderUnder() {
		// render nothing
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

}
