package entity.entities;

import entity.Entity;
import entity.Unit;
import entity.animation.AreaHeal;
import entity.animation.Death;
import processing.core.PImage;
import shared.Nation;

public class Colum extends Unit {

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
		xSize = 45;
		ySize = 45;

		kerit = 600;
		pax = 300;
		arcanum = 0;
		prunam = 0;
		trainTime = 5000;

		hp = hp_max = 300;
		speed = 0.9f;
		radius = 7;
		sight = 70;
		groundPosition = Entity.GroundPosition.AIR;
		height = 50;

		heal.range = (byte) (radius + 25);
		heal.heal = 25;
		heal.cooldown = 5000;
		heal.eventTime = 100;

		descr = " ";
		stats = "heal/s: " + heal.heal + "/" + heal.cooldown / 1000.0;
		// ************************************
	}

	@Override
	public void updateDecisions() {

		// isTaged = false;
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
