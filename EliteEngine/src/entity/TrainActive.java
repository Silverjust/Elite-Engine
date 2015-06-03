package entity;

import entity.Active;
import entity.Entity;
import entity.Unit;
import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import main.ClientHandler;
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
			if (clazz.isAssignableFrom(e.getClass())) {
				trainer = e;
			}
		}
		Unit toTrain = null;
		try {
			toTrain = unit.getConstructor(String[].class).newInstance(
					new Object[] { null });
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (toTrain.canBeBought(trainer.player)) {
			ClientHandler.send("<spawn " + unit.getSimpleName() + " "
					+ trainer.player.ip + " " + (trainer.x + 25) + " "
					+ (trainer.y + 25) + " " + (trainer.x + 75) + " "
					+ (trainer.y + 75));
			ref.updater.send("<give " + trainer.player.ip + " " + "kerit"
					+ " -" + toTrain.kerit);
		}
	}
}