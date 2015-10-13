package entity.aliens;

import processing.core.PGraphics;
import processing.core.PImage;
import shared.ref;
import entity.Commander;
import entity.MainBuilding;
import entity.animation.Animation;
import entity.animation.Death;
import game.ImageHandler;

public class AlienMainBuilding extends MainBuilding implements Commander {
	private static PImage standImg;
	private static PImage previewImg;
	static PImage groundImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		previewImg = standImg = ImageHandler.load(path, "AlienMainBuilding");
		groundImg = ImageHandler.load(path, "AlienGround");
	}

	public AlienMainBuilding(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = null;
		death = new Death(standImg, 1000);

		setAnimation(stand);
		
		// ************************************
		xSize = 85;
		ySize = 85;

		sight = 50;

		hp = hp_max = 1500;
		radius = RADIUS;

		commandingRange = 250;

		descr = "Alien Main Building§when it dies, you loose";
		stats = " ";
		// ************************************
	}

	@Override
	public void renderTerrain() {
		ImageHandler.drawImage(ref.app, groundImg, xToGrid(x), yToGrid(y),
				commandingRange * 2, commandingRange);
	}

	@Override
	public void drawOnMinimapUnder(PGraphics graphics) {
		graphics.image(AlienMainBuilding.groundImg, x, y, commandingRange * 2,
				commandingRange * 2);
	}

	@Override
	public void renderUnder() {
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, currentFrame);
	}

	public PImage preview() {
		return previewImg;
	}

	static PImage getGround() {
		return groundImg;
	}

}
