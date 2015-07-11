package entity.scientists;

import processing.core.PImage;
import shared.ref;
import entity.Active;
import entity.Entity;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Death;
import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import game.AimHandler;
import game.ImageHandler;
import game.aim.TeleportAim;

public class PhysicsLab extends Lab {

	private static PImage standingImg;
	public static PImage teleportImg;

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

		animation = nextAnimation = walk;
		// ************************************

		kerit = 600;
		pax = 0;
		arcanum = 0;
		prunam = 10;
		trainTime = TRAINTIME;

		descr = " ";
		stats = " ";
		// ************************************
	}

	public static class TeleportActive extends Active {

		public TeleportActive(int x, int y, char n) {
			super(x, y, n, teleportImg);
			cooldown = 60000;
			clazz = PhysicsLab.class;
		}

		@Override
		public void onButtonPressed(GGameButton gamebutton, GEvent event) {
			PhysicsLab trainer = null;
			for (Entity e : ref.updater.selected) {
				if (e instanceof PhysicsLab
						&& (e.getAnimation() == e.stand || e.getAnimation() == ((Unit) e).walk))
					trainer = (PhysicsLab) e;
			}
			if (trainer != null) {
				AimHandler.setAim(new TeleportAim(trainer, this));
			}
		}

		@Override
		public String getDesription() {
			return "teleports units  from §physicslab to physicslab";
		}
	}

}