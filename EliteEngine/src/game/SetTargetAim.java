package game;

import shared.ref;
import entity.Entity;
import entity.Trainer;
import game.AimHandler.Cursor;

public class SetTargetAim extends Aim {
	Class<?> trainerClass;

	public SetTargetAim(Class<?> clazz) {
		this.trainerClass = clazz;
	}

	@Override
	Cursor getCursor() {
		return Cursor.SELECT;
	}

	@Override
	void update() {
	}

	@Override
	void execute() {
		float x, y;
		x = Entity.xToGrid(Entity.gridToX());
		y = Entity.xToGrid(Entity.gridToY());
		for (Entity e : ref.updater.selected) {
			if (trainerClass.isAssignableFrom(e.getClass())
					&& e.getAnimation() == e.stand) {
				((Trainer) e).setTarget(x, y);
			}
		}
	}

}
