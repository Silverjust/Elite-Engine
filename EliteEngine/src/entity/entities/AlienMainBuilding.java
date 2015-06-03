package entity.entities;

import processing.core.PImage;
import shared.Nation;
import shared.ref;
import entity.Buildable;
import entity.Building;
import entity.Entity;
import entity.Commander;
import entity.animation.Animation;
import entity.animation.Build;
import entity.animation.Death;
import game.ImageHandler;

public class AlienMainBuilding extends Building implements Buildable, Commander {
	TestLab testLab;

	private int commandingRange;

	private static PImage standImg;
	private static PImage previewImg;

	public static void loadImages() {
		String path = path(Nation.ALIENS, new Object() {
		});
		previewImg = standImg = ImageHandler.load(path, "AlienMainBuilding");
	}

	public AlienMainBuilding(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Animation(standImg, 100);
		build = new Build(standImg, 100);
		death = new Death(standImg, 100);

		animation = nextAnimation = stand;
		// ************************************
		xSize = 60;
		ySize = 60;

		sight = 50;

		hp = hp_max = 1000;
		radius = 15;

		commandingRange = 250;

		descr = "Alien Main Building§when it dies, you loose";
		stats = "hp: " + hp_max;
		// ************************************
	}

	@Override
	public void updateDecisions() {
		super.updateDecisions();
		if (animation == stand) {
			for (Entity e : player.visibleEntities) {
				if (e.isCollision(x, y, 100)) {
					try {
						testLab = (TestLab) e;
					} catch (Exception e1) {
					}
				}
			}
			if (testLab != null && testLab.isCollision(x, y, 100)) {
			} else {
				testLab = null;
			}
		}
	}

	@Override
	public void renderGround() {
		drawSelected();
		animation.draw(this, (byte) 0, currentFrame);
		if (testLab != null) {
			ref.app.stroke(100);
			ref.app.line(xToGrid(x), yToGrid(y - ySize), testLab.x,
					(testLab.y - testLab.height) / 2);
			ref.app.stroke(0);
		}
	}

	public PImage preview() {
		return previewImg;
	}

	@Override
	public int commandRange() {
		return commandingRange;
	}

}
