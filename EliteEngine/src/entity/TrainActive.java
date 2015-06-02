package entity;

import entity.Active;
import entity.Entity;
import entity.Unit;
import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import game.GameDrawer;
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
		// TODO wo spawnt einheit
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
		if (trainer.player.kerit > toTrain.kerit || GameDrawer.nocosts) {
			ClientHandler.send("<spawn " + unit.getSimpleName() + " "
					+ trainer.player.ip + " " + (trainer.x + 50) + " "
					+ (trainer.y + 50) + " " + (trainer.x + 150) + " "
					+ (trainer.y + 150));
			ref.updater.send("<give " + trainer.player.ip + " " + "kerit"
					+ " -" + toTrain.kerit);
		}
	}
}