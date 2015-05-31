package entity;

import entity.entities.TestBuilding;
import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import game.AimHandler;
import game.BuildAim;

public class BuildActive extends Active {
	Class<? extends Building> building;

	public BuildActive(int x, int y, char n, Building b,
			Class<? extends Entity> builder) {
		super(x, y, n, b.iconImg);
		building = b.getClass();
		clazz = TestBuilding.class;
	}

	@Override
	public void onButtonPressed(GGameButton gamebutton, GEvent event) {
		try {
			Building b = building.getConstructor(String[].class)
					.newInstance(new Object[] { null });
			AimHandler.setAim(new BuildAim(b));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
