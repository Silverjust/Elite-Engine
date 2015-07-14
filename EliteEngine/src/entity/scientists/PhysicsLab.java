package entity.scientists;

import processing.core.PImage;
import shared.ref;
import entity.Entity;
import entity.MultiCDActive;
import entity.Unit;
import entity.animation.Ability;
import entity.animation.Animation;
import entity.animation.Death;
import game.AimHandler;
import game.ImageHandler;
import game.aim.TeleportAim;

public class PhysicsLab extends Lab {

	private static PImage standingImg;
	public static PImage teleportImg;

	private Ability sendTeleport;
	private Animation recievieTeleport;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = ImageHandler.load(path, "PhysicsLab");
	}

	public PhysicsLab(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		sendTeleport = new Ability(standingImg, 800);
		recievieTeleport = new Animation(standingImg, 800);

		animation = nextAnimation = walk;
		// ************************************

		kerit = 600;
		pax = 0;
		arcanum = 0;
		prunam = 10;
		trainTime = TRAINTIME;

		sendTeleport.cooldown = 60000;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if ("sendTeleport".equals(c[2])) {
			setAnimation(sendTeleport);
		} else if ("recievieTeleport".equals(c[2])) {
			setAnimation(recievieTeleport);
		}
	}

	public Ability getTeleport() {
		return sendTeleport;
	}

	public static class TeleportActive extends MultiCDActive {

		public TeleportActive(int x, int y, char n) {
			super(x, y, n, teleportImg);
			// cooldown = 60000;
			clazz = PhysicsLab.class;
			setAbilityGetter("getTeleport");
		}

		@Override
		public void onActivation() {
			PhysicsLab trainer = null;
			for (Entity e : ref.updater.selected) {
				if (e instanceof PhysicsLab
						&& (e.getAnimation() == e.stand || e.getAnimation() == ((Unit) e).walk))
					trainer = (PhysicsLab) e;
			}
			if (trainer != null) {
				trainer.sendAnimation("sendTeleport");
				AimHandler.setAim(new TeleportAim(trainer, this));
			}
		}

		@Override
		public String getDesription() {
			return "teleports units  from §physicslab to physicslab";
		}
	}

}