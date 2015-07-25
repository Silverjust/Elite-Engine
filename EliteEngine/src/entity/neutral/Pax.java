package entity.neutral;

import processing.core.PGraphics;
import processing.core.PImage;
import shared.ref;
import entity.Building;
import entity.animation.Animation;

public class Pax extends Building {

	private static PImage standImg;

	public static void loadImages() {
		String path = path( new Object() {
		});
		standImg = game.ImageHandler.load(path, "Pax");
	}

	public Pax(String[] c) {
		super(c);
		player = ref.updater.neutral;// neutral

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = null;
		death = null;

		setAnimation(stand);
		
		// ************************************
		xSize = 40;
		ySize = 40;

		radius = 0;

		descr = "pax";
		// ************************************
	}

	@Override
	public void updateDecisions() {
	}

	@Override
	public void renderUnder() {
		getAnimation().draw(this, (byte) 0, currentFrame);
	}

	@Override
	public void drawOnMinimap(PGraphics graphics) {
		graphics.fill(player.color);
		graphics.rect(x, y, 15 * 2, 15 * 2);
	}

	@Override
	public void updateAnimation() {
	}

	@Override
	public PImage preview() {
		return standImg;
	}
}
