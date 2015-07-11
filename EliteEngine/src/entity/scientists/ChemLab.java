package entity.scientists;

import processing.core.PImage;
import entity.animation.Animation;
import entity.animation.Death;
import game.ImageHandler;

public class ChemLab extends Lab {

	private static PImage standingImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = ImageHandler.load(path, "ChemLab");
	}

	public ChemLab(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);

		animation = nextAnimation = walk;
		// ************************************
		kerit = 600;
		pax = 0;
		arcanum = 30;
		prunam = 0;
		trainTime = TRAINTIME;

		descr = " ";
		stats = " ";
		// ************************************
	}

}