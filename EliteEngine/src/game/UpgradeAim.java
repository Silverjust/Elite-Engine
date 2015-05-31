package game;

import shared.ref;
import main.ClientHandler;
import entity.Building;
import entity.Entity;

public class UpgradeAim extends BuildAim {
	Class<? extends Entity> oldBuildable;

	Building replaced;

	public UpgradeAim(Entity newBuildable, Class<? extends Entity> oldBuildable) {
		super(newBuildable);
		this.oldBuildable = oldBuildable;
	}

	@Override
	void execute() {
		float x, y;
		x = Building.xToGrid(Building.gridToX());
		y = Building.xToGrid(Building.gridToY());
		if (canPlaceAt(x, y)) {
			ClientHandler.send("<remove " + replaced.number);
			ClientHandler.send("<spawn " + buildable.getClass().getSimpleName()
					+ " " + ref.player.ip + " " + x + " " + y);
		}
	}

	@Override
	boolean canPlaceAt(float x, float y) {
		boolean rightPlace = false;
		boolean inMainRange = false;
		for (Entity e : ref.updater.entities) {
			if (e.getClass().equals(oldBuildable) && e.x == x && e.y == y) {
				replaced = (Building) e;
				rightPlace = true;
			}
			if (isInCommandingRange(e, x, y))
				inMainRange = true;
		}
		return rightPlace && inMainRange;
	}
}
