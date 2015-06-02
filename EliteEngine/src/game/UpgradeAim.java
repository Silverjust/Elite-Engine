package game;

import shared.ref;
import entity.Building;
import entity.Entity;

public class UpgradeAim extends BuildAim {
	Class<? extends Entity> oldBuildable;

	Building replaced;

	public UpgradeAim(Entity builder, Entity newBuildable,
			Class<? extends Entity> oldBuildable) {
		super(builder, newBuildable);
		this.oldBuildable = oldBuildable;
	}

	@Override
	void execute() {
		float x, y;
		x = Building.xToGrid(Building.gridToX());
		y = Building.xToGrid(Building.gridToY());
		if (canPlaceAt(x, y)) {
			ref.updater.send("<remove " + replaced.number);
			ref.updater.send("<spawn " + buildable.getClass().getSimpleName()
					+ " " + builder.player.ip + " " + x + " " + y);
			ref.updater.send("<give " + builder.player.ip + " " + "kerit"
					+ " -" + ((Entity) buildable).kerit);
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
