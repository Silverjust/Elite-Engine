package entity.entities;

import entity.Entity;
import entity.Unit;
import entity.animation.AreaHeal;
import entity.animation.Death;
import processing.core.PImage;
import shared.Nation;

public class Colum extends Unit {// FIXME colum sets ismoving to false

	private static PImage standingImg;

	AreaHeal heal;

	public static void loadImages() {
		String path = path(Nation.ALIENS, new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Colum");
	}

	public Colum(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = walk = heal = new AreaHeal(standingImg, 500);
		death = new Death(standingImg, 500);

		animation = nextAnimation = walk;
		// ************************************
		xSize = 90;
		ySize = 90;

		hp = hp_max = 300;
		speed = 1.8f;
		radius = 15;
		sight = 120;
		groundPosition = Entity.GroundPosition.AIR;
		height = 100;

		heal.range = (byte) (radius + 50);
		heal.heal = 25;
		heal.cooldown = 5000;
		heal.eventTime = 100;
		// ************************************
	}

	@Override
	public void updateDecisions() {

		//isTaged = false;
		if (animation == stand) {// ****************************************************
			/*
			 * String s = ""; for (Entity e : player.visibleEntities) { if (e !=
			 * this) { if (e.isa(this)) {// server if (e.isCollision(x, y,
			 * healRange + e.radius)) { s = ("walk " + e.x + " " + e.y); } } } }
			 * sendAnimation(s);
			 */
		}
		if (animation == walk) {// ****************************************************

		}
		heal.setPosition(x, y);
		heal.updateAbility(this);
	}

	@Override
	public void renderAir() {
		drawSelected();
		animation.draw(this, direction, currentFrame);
		drawCircle(heal.range);
		drawCircle((int) (heal.range * heal.getCooldownPercent()));
		drawTaged();
	}

}
