package entity;

import entity.Active;
import entity.Entity;
import entity.Unit;
import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import main.ClientHandler;
import shared.ref;

public class TrainActive extends Active {
	String unit;

	public TrainActive(int x, int y, char n, Unit u,
			Class<? extends Entity> trainer) {
		super(x, y, n, u.iconImg);
		unit = u.getClass().getSimpleName();
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

		ClientHandler.send("<spawn " + unit + " " + trainer.player.ip + " "
				+ (trainer.x + 50) + " " + (trainer.y + 50));
	}
}