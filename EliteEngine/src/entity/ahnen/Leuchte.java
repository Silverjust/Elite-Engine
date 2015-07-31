package entity.ahnen;

import processing.core.PImage;
import shared.ref;
import entity.Attacker;
import entity.Building;
import entity.Entity;
import entity.animation.Ability;
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
	Ability timer;
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
		timer = new Ability(standingImg, 100);

		setAnimation(stand);

		// ************************************
		xSize = 10;
		ySize = 10;
		height = 5;

		kerit = 0;
		pax = 0;
		arcanum = 0;
		prunam = 0;

		timer.cooldown = 30000;
		timer.startCooldown();
		hp = hp_max = 50;
		radius = 3;
		sight = 127;
		groundPosition = Entity.GroundPosition.GROUND;

		heal.range = sight;
		heal.damage = 2;// heal
		heal.pirce = -1;// heal
		heal.cooldown = 2000;
		heal.setCastTime(300);

		buff.range = sight;
		buff.damage = 3;// heal
		buff.pirce = -1;// heal
		buff.cooldown = 2000;
		buff.setCastTime(300);

		descr = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (upgrade == Upgrade.HEAL) {
			heal.setTargetFrom(this, this);
			heal.updateAbility(this, isServer);
		} else if (upgrade == Upgrade.BUFF) {
			buff.setTargetFrom(this, this);
			buff.updateAbility(this, isServer);
		}
		timer.updateAbility(this, isServer);
		if (timer.isNotOnCooldown()) {
			if (upgrade == Upgrade.STANDARD) {
				sendAnimation("death");
			} else if (upgrade == Upgrade.HEAL) {
				sendAnimation("stand");
			} else if (upgrade == Upgrade.BUFF) {
				sendAnimation("heal");
			}
		}
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("stand")) {
			if (upgrade != Upgrade.STANDARD) {
				iconImg = standingImg;
				timer.startCooldown();
				hp_max = 50;
				hp = hp_max;
			}
			upgrade = Upgrade.STANDARD;
			setAnimation(stand);
		} else if (c[2].equals("heal")) {
			if (upgrade != Upgrade.HEAL) {
				iconImg = healImg;
				timer.startCooldown();
				hp_max = 150;
				hp = hp_max;
			}
			upgrade = Upgrade.HEAL;
			setAnimation(heal);
		} else if (c[2].equals("buff")) {
			if (upgrade != Upgrade.BUFF) {
				iconImg = buffImg;
				timer.startCooldown();
				hp_max = 300;
				hp = hp_max;
			}
			upgrade = Upgrade.BUFF;
			setAnimation(buff);
		}
	}

	@Override
	public boolean isCollidable(Entity entity) {
		return !entity.isAllyTo(this);
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
		getAnimation().draw(this, (byte) 0, currentFrame);
		drawTaged();
	}

	@Override
	public void display() {
		super.display();
		drawBar(1 - timer.getCooldownPercent());
	}

	@Override
	public PImage preview() {
		return standingImg;
	}

	enum Upgrade {
		STANDARD, HEAL, BUFF
	}
}
