package entity;

import shared.ref;
import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import game.AimHandler;
import game.aim.BuildAim;

public class BuildActive extends Active {
	Class<? extends Building> building;
	String descr = " ", stats = " ";

	public BuildActive(int x, int y, char n, Building b,
			Class<?> builder) {
		super(x, y, n, b.iconImg);
		building = b.getClass();
		descr = b.getDesription();
		stats = b.getStatistics();
		clazz = builder;
	}

	@Override
	public void onButtonPressed(GGameButton gamebutton, GEvent event) {
		Entity builder = null;
		for (Entity e : ref.updater.selected) {
			if (clazz.isAssignableFrom(e.getClass())) {
				builder = e;
			}
		}

		if (builder != null) {
			try {
				Building b = building.getConstructor(String[].class)
						.newInstance(new Object[] { null });
				AimHandler.setAim(new BuildAim(builder, b));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getDesription() {
		return descr;
	}

	@Override
	public String getStatistics() {
		return stats;
	}
}
