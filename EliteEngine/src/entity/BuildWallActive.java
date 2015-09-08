package entity;

import shared.ref;
import game.AimHandler;
import game.aim.BuildWallAim;

public class BuildWallActive extends Active {
	protected Class<? extends Building> building;
	String descr = " ", stats = " ";

	public BuildWallActive(int x, int y, char n, Entity b, Class<?> builder) {
		super(x, y, n, b.iconImg);
		building = ((Building) b).getClass();
		descr = b.getDesription();
		stats = b.getStatistics();
		clazz = builder;
	}

	@Override
	public void onActivation() {
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
				AimHandler.setAim(new BuildWallAim(builder, b));
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
