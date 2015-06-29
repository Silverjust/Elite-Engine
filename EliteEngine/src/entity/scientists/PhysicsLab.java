package entity.scientists;

import processing.core.PImage;
import entity.animation.Animation;
import entity.animation.Death;
import game.ImageHandler;

public class PhysicsLab extends Lab {

	private static PImage standingImg;

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
		prunam = 0;
		trainTime = 1000;

		descr = " ";
		stats = " ";
		// ************************************
	}

}