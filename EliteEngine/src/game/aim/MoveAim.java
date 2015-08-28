package game.aim;

import shared.ref;
import entity.Entity;
import entity.Unit;
import game.AimHandler.Cursor;

public class MoveAim extends Aim {

	private boolean aggresive;

	public MoveAim(boolean aggresive) {
		this.aggresive = aggresive;
	}

	@Override
	public Cursor getCursor() {
		if (!aggresive)
			return Cursor.MOVE;
		return Cursor.ARROW;
	}

	@Override
	public void execute(float x, float y) {
		// Entity target = null;
		for (Entity e : ref.updater.selected) {
			if (e instanceof Unit)
				e.sendAnimation("walk " + x + " " + y + "  " + aggresive);
			// if (PApplet.dist(x, y, e.x, e.y - e.height) <= e.radius)
			// target = e;
		}
		/*
		 * for (Entity e : ref.updater.selected) { if (e) { if (target != null)
		 * { e.sendAnimation("setTarget " + target.number); } else {
		 * e.sendAnimation("walk " + x + " " + y + " true"); } } }
		 */
	}
}
