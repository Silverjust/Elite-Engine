package game;

import java.lang.reflect.Constructor;

import main.MainPreGame.GameSettings;
import main.appdata.SettingHandler;
import entity.Active;
import entity.BuildActive;
import entity.BuildWallActive;
import entity.Building;
import entity.Entity;
import entity.TrainActive;
import entity.Unit;
import entity.UpgradeActive;
import entity.neutral.*;
import shared.Helper;
import shared.Nation;
import shared.ref;

public class ActivesGrid {
	private static final int gridHeight = 3;
	private static final int gridWidth = 7;
	public static boolean showUnitActives;
	public int x = ref.app.width - 420;
	public int y = ref.app.height - HUD.height + 10;
	int w = 60;

	public Active[][] unitGrid = new Active[gridWidth][gridHeight];
	public Active[][] buildingGrid = new Active[gridWidth][gridHeight];
	public Nation nation;

	public ActivesGrid() {
		Active.x = x;
		Active.y = y;
		setup(ref.player.nation);
		selectionChange(false);
	}

	void setup(Nation nation) {
		if (nation != null) {
			removeActives();
			this.nation = nation;
			nation.getNationInfo().setupActives(this);
			if (GameSettings.sandbox) {
				addActive(7, 1, SandboxBuilding.BuildSetup.class, false);
			}
		}
	}

	public void update() {
		for (int x = 0; x < gridWidth; x++)
			for (int y = 0; y < gridHeight; y++)
				if (unitGrid[x][y] != null && //
						unitGrid[x][y].isVisible())
					unitGrid[x][y].update();

		for (int x = 0; x < gridWidth; x++)
			for (int y = 0; y < gridHeight; y++)
				if (buildingGrid[x][y] != null && //
						buildingGrid[x][y].isVisible())
					buildingGrid[x][y].update();
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

	public void setupSandbox() {
		System.out.println("setupSandbox");
		SandboxBuilding.commandRange = Integer.MAX_VALUE;
		addActive(1, 1, SandboxBuilding.DeleteActive.class, true);
		addBuildActive(2, 1, SandboxBuilding.class, ref.player.nation
				.getNationInfo().getMainBuilding(), true);
		addBuildActive(1, 2, SandboxBuilding.class, Kerit.class, true);
		addBuildActive(2, 2, SandboxBuilding.class, Pax.class, true);
		addBuildActive(1, 3, SandboxBuilding.class, Arcanum.class, true);
		addBuildActive(2, 3, SandboxBuilding.class, Prunam.class, true);
		addBuildActive(3, 2, SandboxBuilding.class, Rock.class, true);
		addActive(3, 3, BuildWallActive.class, SandboxBuilding.class,
				Rock.class, true);
		addActive(3, 1, SandboxBuilding.ChangeSide.class, true);
		addActive(4, 1, SandboxBuilding.AddPlayer.class, true);
	}

	private void removeActives() {
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				if (unitGrid[x][y] != null) {
					unitGrid[x][y].button.dispose();
					unitGrid[x][y] = null;
				}
				if (buildingGrid[x][y] != null) {
					buildingGrid[x][y].button.dispose();
					buildingGrid[x][y] = null;
				}
			}
		}
	}

	public void addActive(int x, int y, Class<? extends Active> a,
			boolean isUnitActive) {
		x--;
		y--;
		try {
			Constructor<?> ctor = a.getConstructor(int.class, int.class,
					char.class);
			if (isUnitActive) {
				unitGrid[x][y] = (Active) ctor.newInstance(//
						new Object[] { x, y,
								SettingHandler.setting.unitsShortcuts[y][x] });
			} else {
				buildingGrid[x][y] = (Active) ctor.newInstance(//
						new Object[] { x, y,
								SettingHandler.setting.buildingsShortcuts[y][x] });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addActive(int x, int y, Class<? extends Active> a,
			Class<?> builder, Class<? extends Entity> building,
			boolean isUnitActive) {
		x--;
		y--;
		try {
			Constructor<?> ctor = a.getConstructor(int.class, int.class,
					char.class, Entity.class, Class.class);
			Entity b = building.getConstructor(String[].class).newInstance(
					new Object[] { null });
			if (isUnitActive) {
				unitGrid[x][y] = (Active) ctor.newInstance(//
						new Object[] { x, y,
								SettingHandler.setting.unitsShortcuts[y][x], b,
								builder });
			} else {
				buildingGrid[x][y] = (Active) ctor.newInstance(//
						new Object[] {
								x,
								y,
								SettingHandler.setting.buildingsShortcuts[y][x],
								b, builder });
			}

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			System.out.println(a.getConstructors());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addTrainActive(int x, int y, Class<? extends Entity> trainer,
			Class<? extends Unit> toTrain, boolean isUnitActive) {
		x--;
		y--;
		try {
			Unit u = toTrain.getConstructor(String[].class).newInstance(
					new Object[] { null });
			if (isUnitActive) {
				unitGrid[x][y] = new TrainActive(x, y,
						SettingHandler.setting.unitsShortcuts[y][x], u, trainer);
			} else {
				buildingGrid[x][y] = new TrainActive(x, y,
						SettingHandler.setting.buildingsShortcuts[y][x], u,
						trainer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addBuildActive(int x, int y, Class<?> builder,
			Class<? extends Building> building, boolean isUnitActive) {
		x--;
		y--;
		try {
			Building b = building.getConstructor(String[].class).newInstance(
					new Object[] { null });
			if (isUnitActive) {
				unitGrid[x][y] = new BuildActive(x, y,
						SettingHandler.setting.unitsShortcuts[y][x], b, builder);
			} else {
				buildingGrid[x][y] = new BuildActive(x, y,
						SettingHandler.setting.buildingsShortcuts[y][x], b,
						builder);
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
						SettingHandler.setting.unitsShortcuts[y][x], b,
						oldBuilding, builder);
			} else {
				buildingGrid[x][y] = new UpgradeActive(x, y,
						SettingHandler.setting.buildingsShortcuts[y][x], b,
						oldBuilding, builder);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dispose() {
		removeActives();
	}

	public void selectionChange(boolean containsUnits) {
		showUnitActives = containsUnits;
		if (showUnitActives) {
			for (int x = 0; x < gridWidth; x++) {
				for (int y = 0; y < gridHeight; y++) {
					if (unitGrid[x][y] != null)
						if (Helper.listContainsInstanceOf(unitGrid[x][y].clazz,
								ref.updater.selected)) {
							unitGrid[x][y].setVisible(true);
						} else
							unitGrid[x][y].setVisible(false);
					if (buildingGrid[x][y] != null)
						buildingGrid[x][y].setVisible(false);
				}
			}
		} else {
			for (int x = 0; x < gridWidth; x++) {
				for (int y = 0; y < gridHeight; y++) {
					if (unitGrid[x][y] != null)
						unitGrid[x][y].setVisible(false);
					if (buildingGrid[x][y] != null)
						if (Helper.listContainsInstanceOf(
								buildingGrid[x][y].clazz, ref.updater.selected)) {
							buildingGrid[x][y].setVisible(true);
						} else
							buildingGrid[x][y].setVisible(false);
				}
			}
		}
	}
}
