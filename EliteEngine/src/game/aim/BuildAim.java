package game.aim;

import shared.ref;
import entity.Building;
import entity.Entity;
import entity.Commander;
import game.AimHandler.Cursor;

public class BuildAim extends Aim {
	Building buildable;
	Entity builder;

	public BuildAim(Entity builder, Entity building) {
		try {
			this.builder = builder;
			this.buildable =  (Building) building;
		} catch (ClassCastException e) {
			System.err.println(building + " is not buildable \n");
		}

	}

	@Override
	public
	Cursor getCursor() {
		return Cursor.BUILD;
	}

	@Override
	public
	void update() {
		float x, y;
		x = Building.xToGrid(Building.gridToX());
		y = Building.xToGrid(Building.gridToY());
		if (canPlaceAt(x, y)) {
			ref.app.tint(255, 150);
		} else {
			ref.app.tint(255, 100, 100, 150);
		}
		ref.app.image(buildable.preview(), x, y / 2,
				((Entity) buildable).xSize, ((Entity) buildable).ySize);
		ref.app.tint(255);
	}

	boolean canPlaceAt(float x, float y) {
		boolean placeFree = true;
		boolean inCommanderRange = false;
		for (Entity e : ref.updater.entities) {
			if (e.isInArea(x, y, ((Entity) buildable).radius + e.radius))
				placeFree = false;
			if (isInCommandingRange(e, x, y))
				inCommanderRange = true;
		}

		return placeFree && inCommanderRange
				&& ((Entity) buildable).canBeBought(builder.player);
	}

	public boolean isInCommandingRange(Entity e, float x, float y) {
		if (e instanceof Commander && e.player == builder.player
				&& e.isInArea(x, y, ((Commander) e).commandRange())) {
			return true;
		}
		return false;
	}

	@Override
	public
	void execute() {
		float x, y;
		x = Entity.xToGrid(Entity.gridToX());
		y = Entity.xToGrid(Entity.gridToY());
		if (canPlaceAt(x, y)) {
			ref.updater.send("<spawn " + buildable.getClass().getSimpleName()
					+ " " + builder.player.ip + " " + x + " " + y);
			((Entity) buildable).buyFrom(builder.player);
		}
	}
}
