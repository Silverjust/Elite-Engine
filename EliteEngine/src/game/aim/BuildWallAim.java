package game.aim;

import processing.core.PApplet;
import shared.ref;
import entity.Building;
import entity.Entity;
import game.AimHandler;

public class BuildWallAim extends BuildAim {
	boolean isStartWall;
	float xStartWall;
	float yStartWall;

	public BuildWallAim(Entity builder, Entity building) {
		super(builder, building);
		isStartWall = true;
	}

	@Override
	public void update() {
		if (isStartWall) {
			float x, y;
			x = Building.xToGrid(Building.gridToX());
			y = Building.xToGrid(Building.gridToY());
			if (canPlaceAt(x, y)) {
				ref.app.tint(255, 150);
			} else {
				ref.app.tint(255, 100, 100, 150);
			}
			ref.app.image(buildable.preview(), x, y / 2, buildable.xSize,
					buildable.ySize);
			ref.app.tint(255);
		} else {
			float x1 = xStartWall, y1 = yStartWall;
			float x2 = Building.xToGrid(Building.gridToX()), y2 = Building
					.xToGrid(Building.gridToY());
			float speed = buildable.radius * 2;
			while (PApplet.dist(x1, y1, x2, y2) > speed) {
				x1 = (x1 + (x2 - x1) / PApplet.dist(x1, y1, x2, y2) * (speed));
				y1 = (y1 + (y2 - y1) / PApplet.dist(x1, y1, x2, y2) * (speed));
				if (canPlaceAt(x1, y1)) {
					ref.app.tint(255, 150);
				} else {
					ref.app.tint(255, 100, 100, 150);
				}
				ref.app.image(buildable.preview(), x1, y1 / 2, buildable.xSize,
						buildable.ySize);
				ref.app.tint(255);
			}
			if (canPlaceAt(x2, y2)) {
				ref.app.tint(255, 150);
			} else {
				ref.app.tint(255, 100, 100, 150);
			}
			ref.app.image(buildable.preview(), x2, y2 / 2, buildable.xSize,
					buildable.ySize);
			ref.app.tint(255);
		}
	}

	public void startedAt(float x, float y) {
		xStartWall = x;
		yStartWall = y;
		isStartWall = false;
	}

	@Override
	public void execute(float x, float y) {
		if (isStartWall) {
			/*
			 * float x, y; x = Entity.xToGrid(Entity.gridToX()); y =
			 * Entity.xToGrid(Entity.gridToY());
			 */
			if (canPlaceAt(x, y)) {
				ref.updater.send("<spawn "
						+ buildable.getClass().getSimpleName() + " "
						+ builder.player.user.ip + " " + x + " " + y + " start");
				// start nur beim startbuilding
				buildable.buyFrom(builder.player);
			}
		} else {
			float x1 = xStartWall, y1 = yStartWall;
			/*
			 * float x2 = Building.xToGrid(Building.gridToX()), y2 = Building
			 * .xToGrid(Building.gridToY());
			 */
			float x2 = x, y2 = y;
			float speed = buildable.radius * 2;
			while (PApplet.dist(x1, y1, x2, y2) > speed) {
				x1 = (x1 + (x2 - x1) / PApplet.dist(x1, y1, x2, y2) * (speed));
				y1 = (y1 + (y2 - y1) / PApplet.dist(x1, y1, x2, y2) * (speed));
				if (canPlaceAt(x1, y1)) {
					ref.updater
							.send("<spawn "
									+ buildable.getClass().getSimpleName()
									+ " " + builder.player.user.ip + " " + x1 + " "
									+ y1 + " part");
					// part nur beim partbuilding
					buildable.buyFrom(builder.player);
				}
			}
			if (canPlaceAt(x2, y2)) {
				ref.updater.send("<spawn "
						+ buildable.getClass().getSimpleName() + " "
						+ builder.player.user.ip + " " + x2 + " " + y2 + " start");
				// start nur beim startbuilding
				buildable.buyFrom(builder.player);
			}
		}
	}

	public static void setupWall(Entity e, String[] c) {
		if (c != null && c.length > 5 && c[5] != null && c[5].equals("start")
				&& AimHandler.getAim() instanceof BuildWallAim)
			((BuildWallAim) AimHandler.getAim()).startedAt(e.x, e.y);
		else if (c != null && c.length > 5 && c[5] != null
				&& c[5].equals("part")
				&& AimHandler.getAim() instanceof BuildWallAim) {
			e.x = Float.parseFloat(c[3]);
			e.y = Float.parseFloat(c[4]);
		}
	}
}
