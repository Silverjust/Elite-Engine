package game.aim;

import shared.ref;
import entity.Entity;
import game.AimHandler.Cursor;

public class SetTargetAim extends Aim {
	Class<?> trainerClass;

	public SetTargetAim(Class<?> clazz) {
		this.trainerClass = clazz;
	}

	@Override
	public Cursor getCursor() {
		return Cursor.SELECT;
	}

	@Override
	public void update() {
	}

	@Override
	public void execute() {
		float x, y;
		x = Entity.xToGrid(Entity.gridToX());
		y = Entity.xToGrid(Entity.gridToY());
		for (Entity e : ref.updater.selected) {
			if (trainerClass.isAssignableFrom(e.getClass())
					&& e.getAnimation() == e.stand) {
				e.sendAnimation("setTarget " + x + " " + y);
			}
		}
	}

}
