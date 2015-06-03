package entity;

import entity.Active;
import entity.Entity;
import entity.Unit;
import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import shared.ref;

public class TrainActive extends Active {
	Class<? extends Unit> unit;

	public TrainActive(int x, int y, char n, Unit u,
			Class<? extends Entity> trainer) {
		super(x, y, n, u.iconImg);
		unit = u.getClass();
		clazz = trainer;
	}

	@Override
	public void onButtonPressed(GGameButton gamebutton, GEvent event) {
		Entity trainer = null;
		for (Entity e : ref.updater.selected) {
			if (clazz.isAssignableFrom(e.getClass())
					&& e.getAnimation() == e.stand) {
				trainer = e;
			}
		}
		trainer.sendAnimation("train " + unit.getSimpleName());
	}
}