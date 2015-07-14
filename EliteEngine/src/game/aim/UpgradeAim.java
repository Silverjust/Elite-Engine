package game.aim;

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
	public
	void execute(float x, float y) {
		/*float x, y;
		x = Building.xToGrid(Building.gridToX());
		y = Building.xToGrid(Building.gridToY());*/
		if (canPlaceAt(x, y)) {
			ref.updater.send("<remove " + replaced.number);
			ref.updater.send("<spawn " + buildable.getClass().getSimpleName()
					+ " " + builder.player.ip + " " + x + " " + y);
			((Entity) buildable).buyFrom(builder.player);
		}
	}

	@Override
	boolean canPlaceAt(float x, float y) {
		boolean rightPlace = false;
		boolean inCommanderRange = false;
		for (Entity e : ref.updater.entities) {
			if (e.getClass().equals(oldBuildable) && e.x == x && e.y == y) {
				replaced = (Building) e;
				rightPlace = true;
			}
			if (isInCommandingRange(e, x, y))
				inCommanderRange = true;
		}
		return rightPlace && inCommanderRange
				&& ((Entity) buildable).canBeBought(builder.player);
	}
}
