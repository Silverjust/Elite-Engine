package entity.ahnen;

import processing.core.PImage;
import shared.ref;
import entity.Attacker;
import entity.Building;
import entity.Entity;
import entity.animation.Animation;
import entity.animation.Attack;
import entity.animation.Death;
import entity.animation.MeleeAttack;

public class Leuchte extends Building implements Attacker {

	private static PImage standingImg;
	static PImage healImg;
	static PImage buffImg;

	MeleeAttack heal;
	MeleeAttack buff;

	Upgrade upgrade = Upgrade.STANDARD;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = game.ImageHandler.load(path, "Leuchte");
		healImg = game.ImageHandler.load(path, "HeilLeuchte");
		buffImg = game.ImageHandler.load(path, "DestruktorLeuchte");
	}

	public Leuchte(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		heal = new MeleeAttack(healImg, 500);
		buff = new MeleeAttack(buffImg, 500);
		death = new Death(standingImg, 500);

		animation = nextAnimation = stand;
		// ************************************
		xSize = 10;
		ySize = 10;
		height = 5;

		kerit = 600;
		pax = 300;
		arcanum = 0;
		prunam = 0;

		hp = hp_max = 50;
		radius = 3;
		sight = 127;
		groundPosition = Entity.GroundPosition.GROUND;

		heal.range = sight;
		heal.damage = 2;// heal
		heal.pirce = -1;// heal
		heal.cooldown = 1000;
		heal.setCastTime(300);

		buff.range = sight;
		buff.damage = 3;// heal
		buff.pirce = -1;// heal
		buff.cooldown = 1000;
		buff.setCastTime(300);

		descr = " ";
		// ************************************
	}

	@Override
	public void updateDecisions() {
		if (upgrade == Upgrade.HEAL) {
			heal.setTargetFrom(this, this);
			heal.updateAbility(this);
		} else if (upgrade == Upgrade.BUFF) {
			buff.setTargetFrom(this, this);
			buff.updateAbility(this);
		}
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("heal")) {
			upgrade = Upgrade.HEAL;
			iconImg = healImg;
			setAnimation(heal);
		}
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
	public void sendDefaultAnimation(Animation oldAnimation) {
		if (upgrade == Upgrade.HEAL) {
			sendAnimation("heal");
		} else if (upgrade == Upgrade.BUFF) {
			sendAnimation("buff");
		} else
			sendAnimation("stand");
	}

	@Override
	public Attack getBasicAttack() {
		if (upgrade == Upgrade.HEAL) {
			return heal;
		} else if (upgrade == Upgrade.BUFF) {
			return buff;
		}
		return null;
	}

	@Override
	public void renderUnder() {
		drawShadow();
	}

	@Override
	public void renderGround() {
		drawSelected();
		animation.draw(this, (byte) 0, currentFrame);
		drawTaged();
	}

	@Override
	public PImage preview() {
		return standingImg;
	}

	enum Upgrade {
		STANDARD, HEAL, BUFF
	}
}
