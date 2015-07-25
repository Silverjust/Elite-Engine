package entity.scientists;

import processing.core.PGraphics;
import processing.core.PImage;
import entity.Commander;
import entity.MainBuilding;
import entity.animation.Animation;
import entity.animation.Death;
import game.ImageHandler;

public class ScientistMainBuilding extends MainBuilding implements Commander {

	private int commandingRange;

	private static PImage standImg;
	private static PImage previewImg;

	// static PImage groundImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		previewImg = standImg = ImageHandler.load(path, "ScientistMainBuilding");
		// groundImg = ImageHandler.load(path, "AlienGround");
	}

	public ScientistMainBuilding(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = null;
		death = new Death(standImg, 1000);

		setAnimation(stand);
		
		// ************************************
		xSize = 60;
		ySize = 60;

		sight = 50;

		hp = hp_max = 1500;
		radius = RADIUS;

		commandingRange = 250;

		descr = "Main Building of the scientists§when it dies, you loose";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions() {
		super.updateDecisions();

	}

	@Override
	public void renderTerrain() {
		// ref.app.image(groundImg, xToGrid(x), yToGrid(y), commandingRange * 2,
		// commandingRange);
	}

	@Override
	public void drawOnMinimapUnder(PGraphics graphics) {
		// graphics.image(HumanMainBuilding.groundImg, x, y, commandingRange *
		// 2,
		// commandingRange * 2);
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, currentFrame);
	}

	public PImage preview() {
		return previewImg;
	}

	@Override
	public int commandRange() {
		return commandingRange;
	}

}
