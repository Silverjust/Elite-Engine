package entity;

import shared.ref;
import game.AimHandler;
import game.aim.UpgradeAim;

public class UpgradeActive extends Active {
	protected Class<? extends Building> newBuilding;
	protected Class<? extends Building> oldBuilding;
	String descr = " ", stats = " ";

	public UpgradeActive(int x, int y, char n, Entity b,
			Class<? extends Building> oldBuilding, Class<?> builder) {
		super(x, y, n, b.iconImg);
		this.newBuilding = ((Building) b).getClass();
		this.oldBuilding = oldBuilding;
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
				Building b = newBuilding.getConstructor(String[].class)
						.newInstance(new Object[] { null });
				AimHandler.setAim(new UpgradeAim(builder, b, oldBuilding));
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