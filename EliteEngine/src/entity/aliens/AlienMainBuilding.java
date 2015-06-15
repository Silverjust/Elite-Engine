package entity.aliens;

import processing.core.PGraphics;
import processing.core.PImage;
import shared.Nation;
import shared.ref;
import entity.Commander;
import entity.MainBuilding;
import entity.animation.Animation;
import entity.animation.Death;
import game.ImageHandler;

public class AlienMainBuilding extends MainBuilding implements Commander {
	private int commandingRange;

	private static PImage standImg;
	private static PImage previewImg;
	static PImage groundImg;

	public static void loadImages() {
		String path = path(Nation.ALIENS, new Object() {
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

		animation = nextAnimation = stand;
		// ************************************
		xSize = 85;
		ySize = 85;

		sight = 50;

		hp = hp_max = 1500;
		radius = 35;

		commandingRange = 250;

		descr = "Alien Main Building§when it dies, you loose";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions() {
		super.updateDecisions();
		
	}

	@Override
	public void renderTerrain() {
		ref.app.image(groundImg, xToGrid(x), yToGrid(y), commandingRange * 2,
				commandingRange);
	}

	@Override
	public void drawOnMinimapUnder(PGraphics graphics) {
		graphics.image(AlienMainBuilding.groundImg, x, y, commandingRange * 2,
				commandingRange * 2);
	}

	@Override
	public void renderGround() {
		drawSelected();
		animation.draw(this, (byte) 0, currentFrame);
	}

	public PImage preview() {
		return previewImg;
	}

	@Override
	public int commandRange() {
		return commandingRange;
	}

	static PImage getGround() {
		return groundImg;
	}

}
