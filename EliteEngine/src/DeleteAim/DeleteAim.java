package DeleteAim;

import shared.ref;
import entity.Entity;
import game.AimHandler.Cursor;
import game.aim.Aim;

public class DeleteAim extends Aim {

	@Override
	public Cursor getCursor() {
		return Cursor.SHOOT;
	}

	@Override
	public void execute() {
		float x, y;
		x = Entity.xToGrid(Entity.gridToX());
		y = Entity.xToGrid(Entity.gridToY());
		for (Entity e2 : ref.updater.entities) {
			if (e2 != null && e2.isCollision(x, y, e2.radius + 10)) {
				ref.updater.send("<remove " + e2.number);
			}
		}
	}

}
