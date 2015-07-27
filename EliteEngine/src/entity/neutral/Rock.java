package entity.neutral;

import processing.core.PImage;
import shared.ref;
import entity.Building;
import entity.animation.Animation;
import game.aim.BuildWallAim;

public class Rock extends Building {

	private static PImage standImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standImg = game.ImageHandler.load(path, "Rock");
	}

	public Rock(String[] c) {
		super(c);
		BuildWallAim.setupWall(this, c);
		player = ref.updater.neutral;// neutral

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = null;
		death = null;

		setAnimation(stand);
		
		// ************************************
		xSize = 40;
		ySize = 40;

		radius = 20;

		descr = "Rock";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
	}

	@Override
	public void renderGround() {
		getAnimation().draw(this, (byte) 0, currentFrame);
	}

	@Override
	public void updateAnimation() {
	}

	@Override
	public PImage preview() {
		return standImg;
	}
}
