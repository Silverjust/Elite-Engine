package game.aim;

import shared.ref;
import entity.Entity;
import entity.Unit;
import game.AimHandler.Cursor;

public class MoveAim extends Aim {

	@Override
	public Cursor getCursor() {
		return Cursor.ARROW;
	}

	@Override
	public void execute(float x, float y) {
		for (Entity e : ref.updater.selected) {
			if (e instanceof Unit)
				e.sendAnimation("walk " + x + " " + y+" false");
		}
	}
}
