package entity;

import shared.ref;
import entity.entities.AlienMainBuilding;
import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import game.AimHandler;
import game.UpgradeAim;

public class UpgradeActive extends Active {
	Class<? extends Building> newBuilding, oldBuilding;

	public UpgradeActive(int x, int y, char n, Building b,
			Class<? extends Building> oldBuilding,
			Class<? extends Entity> builder) {
		super(x, y, n, b.iconImg);
		this.newBuilding = b.getClass();
		this.oldBuilding = oldBuilding;
		clazz = AlienMainBuilding.class;
	}

	@Override
	public void onButtonPressed(GGameButton gamebutton, GEvent event) {
		Entity builder = null;
		for (Entity e : ref.updater.selected) {
			if (clazz.isAssignableFrom(e.getClass())) {
				builder = e;
			}
		}
		try {
			Building b = newBuilding.getConstructor(String[].class)
					.newInstance(new Object[] { null });
			AimHandler.setAim(new UpgradeAim(builder, b, oldBuilding));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}