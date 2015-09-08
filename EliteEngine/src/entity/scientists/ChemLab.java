package entity.scientists;

import processing.core.PImage;
import shared.ref;
import entity.BuildWallActive;
import entity.Building;
import entity.Entity;
import entity.animation.Animation;
import entity.animation.Death;
import game.AimHandler;
import game.ImageHandler;
import game.aim.BuildWallAim;

public class ChemLab extends Lab {

	private static PImage standingImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = ImageHandler.load(path, "ChemLab");
	}

	public int buildRange;

	public ChemLab(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);

		setAnimation(walk);

		// ************************************
		kerit = 600;
		pax = 0;
		arcanum = 0;
		prunam = 10;
		trainTime = TRAINTIME;

		buildRange = 200;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void renderUnder() {
		super.renderUnder();
		if (isAlive() && AimHandler.getAim() instanceof ScientistWallAim) {
			ref.app.tint(player.color);
			ref.app.image(selectedImg, xToGrid(x), yToGrid(y), buildRange * 2,
					buildRange);
			ref.app.tint(255);
		}
	}

	public static class ScientistWallActive extends BuildWallActive {

		public ScientistWallActive(int x, int y, char n) {
			super(x, y, n, new ScientistWall(null), ChemLab.class);
		}

		@Override
		public void onActivation() {
			Entity builder = null;
			for (Entity e : ref.updater.selected) {
				if (clazz.isAssignableFrom(e.getClass())) {
					builder = e;
				}
			}

			if (builder != null) {
				try {
					Building b = building.getConstructor(String[].class)
							.newInstance(new Object[] { null });
					AimHandler.setAim(new ScientistWallAim(builder, b));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static class ScientistWallAim extends BuildWallAim {

		public ScientistWallAim(Entity builder, Entity building) {
			super(builder, building);
		}

		protected boolean canPlaceAt(float x, float y) {
			boolean placeFree = true;
			boolean inLabRange = false;
			boolean inCommanderRange = false;

			for (Entity e : ref.updater.entities) {
				if (e.isInRange(x, y, buildable.radius + e.radius)
						&& e.groundPosition == GroundPosition.GROUND)
					placeFree = false;
				if (e instanceof ChemLab && e.player == builder.player
						&& e.isInRange(x, y, ((ChemLab) e).buildRange)) {
					inLabRange = true;
				}
				if (isInCommandingRange(e, x, y))
					inCommanderRange = true;
			}
			return placeFree && (inLabRange || inCommanderRange);
		}
	}

}