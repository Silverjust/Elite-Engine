package entity.entities;

import processing.core.PImage;
import shared.Nation;
import shared.ref;
import entity.Active;
import entity.Buildable;
import entity.Building;
import entity.Entity;
import entity.commanding;
import entity.animation.Animation;
import entity.animation.Death;
import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import game.AimHandler;
import game.BuildAim;
import game.ImageHandler;

public class TestBuilding extends Building implements Buildable, commanding {
	TestLab testLab;

	private int mainRange;

	private static PImage standImg;
	private static PImage previewImg;

	public static void loadImages() {
		String path = path(Nation.SCIENTISTS, new Object() {
		});
		previewImg = standImg = ImageHandler.load(path, "TestBuilding");
	}

	public TestBuilding(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Animation(standImg, 100);
		build = new Animation(standImg, 100);
		death = new Death(standImg, 100);

		animation = nextAnimation = stand;
		// ************************************
		xSize = 60;
		ySize = 60;
		sight = 100;

		hp = hp_max = 1000;
		radius = 30;

		mainRange = 500;
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
		return mainRange;
	}

	static public class BuildTestBuilding extends Active {

		public BuildTestBuilding(int x, int y, char n) {
			super(x, y, n, new TestBuilding(null).iconImg);
			clazz = TestBuilding.class;
		}

		@Override
		public void onButtonPressed(GGameButton gamebutton, GEvent event) {
			AimHandler.setAim(new BuildAim(new TestBuilding(null)));
		}
	}

}
