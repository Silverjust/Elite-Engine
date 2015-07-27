package entity.humans;

import processing.core.PGraphics;
import processing.core.PImage;
import entity.Commander;
import entity.MainBuilding;
import entity.animation.Animation;
import entity.animation.Death;
import game.ImageHandler;

public class HumanMainBuilding extends MainBuilding implements Commander {

	private int commandingRange;

	private static PImage standImg;
	private static PImage previewImg;

	// static PImage groundImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		previewImg = standImg = ImageHandler.load(path, "HumanMainBuilding");
		// groundImg = ImageHandler.load(path, "AlienGround");
	}

	public HumanMainBuilding(String[] c) {
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

		descr = "United Humans Main Building§when it dies, you loose";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		super.updateDecisions(isServer);

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
