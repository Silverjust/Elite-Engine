package entity.aliens;

import entity.Attacker;
import entity.Entity;
import entity.Unit;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.MeleeAttack;
import processing.core.PImage;
import shared.ref;

public class Colum extends Unit implements Attacker {

	private static PImage standingImg;

	MeleeAttack heal;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Colum");
	}

	public Colum(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = walk = heal = new MeleeAttack(standingImg, 500);
		death = new Death(standingImg, 500);

		setAnimation(walk);
		
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
		sight = (byte) 127;
		groundPosition = Entity.GroundPosition.AIR;
		height = 50;

		heal.range = (byte) (radius + 25);
		heal.damage = 25;// heal
		heal.pirce = -1;// heal
		heal.cooldown = 5000;
		heal.setCastTime(100);

		descr = " ";
		// ************************************
	}

	@Override
	public void updateDecisions() {
		// colum: no info to client
		heal.setTargetFrom(this, this);
		heal.updateAbility(this);
	}

	@Override
	public void calculateDamage(Attack a) {
		for (Entity e : ref.updater.entities) {
			if (e != null && e.isAllyTo(this)
					&& e.isInRange(x, y, e.radius + a.range)) {
				ref.updater.send("<heal " + e.number + " " + heal.damage);
			}
		}
	}

	@Override
	public void renderAir() {
		drawSelected();
		getAnimation().draw(this, direction, currentFrame);
		drawTaged();
	}

	@Override
	public Attack getBasicAttack() {
		return heal;
	}
}
