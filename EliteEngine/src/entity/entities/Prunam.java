package entity.entities;

import processing.core.PImage;
import shared.Nation;
import shared.ref;
import entity.Building;
import entity.animation.Animation;

public class Prunam extends Building {

	private static PImage standImg;

	public static void loadImages() {
		String path = path(Nation.NEUTRAL, new Object() {
		});
		standImg = game.ImageHandler.load(path, "Prunam");
	}

	public Prunam(String[] c) {
		super(c);
		player = ref.updater.neutral;// neutral

		iconImg = standImg;
		stand = new Animation(standImg,1);
		build = new Animation(standImg,1);
		death = null;

		animation = nextAnimation = stand;
		// ************************************
		xSize = 60;
		ySize = 60;

		radius = 30;
		// ************************************
	}

	@Override
	public void updateDecisions() {
	}

	@Override
	public void renderGround() {
		animation.draw(this, (byte) 0, currentFrame);
	}

	@Override
	public void updateAnimation() {
	}
}
