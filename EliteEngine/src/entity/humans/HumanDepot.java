package entity.humans;

import processing.core.PImage;
import entity.Building;
import entity.Commander;
import entity.animation.Animation;
import entity.animation.Build;
import entity.animation.Death;
import game.ImageHandler;

public class HumanDepot extends Building implements Commander{
	private int commandingRange;

	private static PImage standImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standImg = ImageHandler.load(path, "HumanDepot");
	}

	public HumanDepot(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = new Build(standImg, 5000);
		death = new Death(standImg, 1000);

		animation = nextAnimation = build;
		// ************************************
		xSize = 30;
		ySize = 30;

		kerit = 500;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		build.setBuildTime(5000);

		sight = 50;

		hp = hp_max = 1000;
		radius = 15;

		commandingRange = 250;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void renderGround() {
		drawSelected();
		animation.draw(this, (byte) 0, currentFrame);
	}

	public PImage preview() {
		return standImg;
	}

	@Override
	public int commandRange() {
		return commandingRange;
	}

}
