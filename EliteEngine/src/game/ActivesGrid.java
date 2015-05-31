package game;

import java.lang.reflect.Constructor;

import entity.*;
import entity.entities.Arol;
import entity.entities.Brux;
import entity.entities.Colum;
import entity.entities.Ker;
import entity.entities.Prunam;
import entity.entities.PrunamExtractor;
import entity.entities.TestBuilding;
import entity.entities.Ticul;
import entity.entities.Valcyrix;
import main.Settings;
import shared.Helper;
import shared.ref;

public class ActivesGrid {
	public static boolean showUnitActives;
	public int x = ref.app.width - 420;
	public int y = ref.app.height - HUD.height + 10;
	int w = 60;

	Active[][] unitGrid = new Active[7][3];
	Active[][] buildingGrid = new Active[7][3];

	public ActivesGrid() {
		Active.x = x;
		Active.y = y;
		addActive(4, 2, Ticul.Smite.class, true);
		addActive(3, 2, Ticul.Flash.class, true);
		addBuildActive(5, 3, TestBuilding.class, TestBuilding.class, false);
		addUpgradeActive(7, 1, TestBuilding.class, PrunamExtractor.class,
				Prunam.class, false);
		addTrainActive(1, 2, TestBuilding.class, Brux.class, false);
		addTrainActive(2, 2, TestBuilding.class, Colum.class, false);
		addTrainActive(1, 3, TestBuilding.class, Valcyrix.class, false);
		addTrainActive(3, 3, TestBuilding.class, Ticul.class, false);
		addTrainActive(5, 1, TestBuilding.class, Arol.class, false);
		addTrainActive(4, 2, TestBuilding.class, Ker.class, false);
	}

	public void update() {
		if (showUnitActives) {
			for (int x = 0; x < 7; x++) {
				for (int y = 0; y < 3; y++) {
					if (unitGrid[x][y] != null)
						if (Helper.listContainsInstanceOf(unitGrid[x][y].clazz,
								ref.updater.selected)) {
							unitGrid[x][y].setVisible(true);
							unitGrid[x][y].update();
						} else
							unitGrid[x][y].setVisible(false);
					if (buildingGrid[x][y] != null)
						buildingGrid[x][y].setVisible(false);
				}
			}
		} else {
			for (int x = 0; x < 7; x++) {
				for (int y = 0; y < 3; y++) {
					if (unitGrid[x][y] != null)
						unitGrid[x][y].setVisible(false);
					if (buildingGrid[x][y] != null)
						if (Helper.listContainsInstanceOf(
								buildingGrid[x][y].clazz, ref.updater.selected)) {
							buildingGrid[x][y].setVisible(true);
							buildingGrid[x][y].update();
						} else
							buildingGrid[x][y].setVisible(false);
				}
			}
		}
	}

	public void fire(int x, int y) {
		try {
			if (showUnitActives) {
				unitGrid[x][y].pressManually();
			} else {
				buildingGrid[x][y].pressManually();
			}
		} catch (NullPointerException e) {
		}
	}

	void addActive(int x, int y, Class<? extends Active> a, boolean isUnitActive) {
		x--;
		y--;
		try {
			Constructor<?> ctor = a.getConstructor(int.class, int.class,
					char.class);
			if (isUnitActive) {
				unitGrid[x][y] = (Active) ctor.newInstance(//
						new Object[] { x, y, Settings.unitsShortcuts[y][x] });
			} else {
				buildingGrid[x][y] = (Active) ctor.newInstance(//
						new Object[] { x, y, Settings.buildingsShortcuts[y][x] });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void addTrainActive(int x, int y, Class<? extends Entity> trainer,
			Class<? extends Unit> toTrain, boolean isUnitActive) {
		x--;
		y--;
		try {
			Unit u = toTrain.getConstructor(String[].class).newInstance(
					new Object[] { null });
			if (isUnitActive) {
				unitGrid[x][y] = new TrainActive(x, y,
						Settings.unitsShortcuts[y][x], u, trainer);
			} else {
				buildingGrid[x][y] = new TrainActive(x, y,
						Settings.buildingsShortcuts[y][x], u, trainer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void addBuildActive(int x, int y, Class<? extends Entity> builder,
			Class<? extends Building> building, boolean isUnitActive) {
		x--;
		y--;
		try {
			Building b = building.getConstructor(String[].class)
					.newInstance(new Object[] { null });
			if (isUnitActive) {
				unitGrid[x][y] = new BuildActive(x, y,
						Settings.unitsShortcuts[y][x], b, builder);
			} else {
				buildingGrid[x][y] = new BuildActive(x, y,
						Settings.buildingsShortcuts[y][x], b, builder);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void addUpgradeActive(int x, int y, Class<? extends Entity> builder,
			Class<? extends Building> newBuilding,
			Class<? extends Building> oldBuilding, boolean isUnitActive) {
		x--;
		y--;
		try {
			Building b = newBuilding.getConstructor(String[].class)
					.newInstance(new Object[] { null });
			if (isUnitActive) {
				unitGrid[x][y] = new UpgradeActive(x, y,
						Settings.unitsShortcuts[y][x], b, oldBuilding, builder);
			} else {
				buildingGrid[x][y] = new UpgradeActive(x, y,
						Settings.buildingsShortcuts[y][x], b, oldBuilding,
						builder);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
