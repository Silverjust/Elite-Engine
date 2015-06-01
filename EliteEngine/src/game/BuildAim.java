package game;

import shared.ref;
import entity.Buildable;
import entity.Building;
import entity.Entity;
import entity.commanding;
import game.AimHandler.Cursor;
import main.ClientHandler;

public class BuildAim extends Aim {
	Buildable buildable;

	public BuildAim(Entity building) {
		try {
			this.buildable = (Buildable) building;
		} catch (ClassCastException e) {
			System.err.println(building + " is not buildable \n");
		}

	}

	@Override
	Cursor getCursor() {
		return Cursor.BUILD;
	}

	@Override
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
			if (e.isCollision(x, y, ((Entity) buildable).radius + e.radius))
				placeFree = false;
			if (isInCommandingRange(e, x, y))
				inCommanderRange = true;
		}
		return placeFree && inCommanderRange;
	}

	public boolean isInCommandingRange(Entity e, float x, float y) {
		if (e instanceof commanding && e.player == ref.player
		// TODO ally oder nur eigene range
				&& e.isCollision(x, y, ((commanding) e).commandRange())) {
			return true;
		}
		return false;
	}

	@Override
	void execute() {
		float x, y;
		x = Building.xToGrid(Building.gridToX());
		y = Building.xToGrid(Building.gridToY());
		if (canPlaceAt(x, y)) {
			ClientHandler.send("<spawn " + buildable.getClass().getSimpleName()
					+ " " + ref.player.ip + " " + x + " " + y);
		}
	}
}
