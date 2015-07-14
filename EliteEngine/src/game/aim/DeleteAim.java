package game.aim;

import shared.ref;
import entity.Entity;
import game.AimHandler.Cursor;

public class DeleteAim extends Aim {

	@Override
	public Cursor getCursor() {
		return Cursor.SHOOT;
	}

	@Override
	public void execute(float x, float y) {
		/*float x, y;
		x = Entity.xToGrid(Entity.gridToX());
		y = Entity.xToGrid(Entity.gridToY());*/
		for (Entity e2 : ref.updater.entities) {
			if (e2 != null && e2.isInRange(x, y, e2.radius + 10)) {
				ref.updater.send("<remove " + e2.number);
			}
		}
	}

}
