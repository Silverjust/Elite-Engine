package entity.humans;

import processing.core.PImage;
import entity.Building;
import entity.animation.Animation;
import entity.animation.Build;
import entity.animation.Death;
import game.ImageHandler;
import game.aim.BuildWallAim;

public class HumanWall extends Building {

	private static PImage standImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standImg = ImageHandler.load(path, "HumanWall");
	}

	public HumanWall(String[] c) {
		super(c);
		BuildWallAim.setupWall(this, c);
		// TODO für wall

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = new Build(standImg, 5000);
		death = new Death(standImg, 1000);

		animation = nextAnimation = build;
		// ************************************
		xSize = 20;
		ySize = 25;

		kerit = 300;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		build.setBuildTime(5000);

		sight = 20;

		hp = hp_max = 500;
		armor = 2;
		radius = 10;

		descr = "wall ";
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

}
