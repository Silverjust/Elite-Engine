package game;

import java.lang.reflect.Constructor;

import entity.Active;
import entity.BuildActive;
import entity.Building;
import entity.Entity;
import entity.TrainActive;
import entity.Unit;
import entity.UpgradeActive;
import entity.neutral.*;
import entity.aliens.*;
import entity.humans.*;
import main.ClientHandler;
import main.Settings;
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

	Active[][] unitGrid = new Active[gridWidth][gridHeight];
	Active[][] buildingGrid = new Active[gridWidth][gridHeight];
	public Nation nation;

	public ActivesGrid() {
		Active.x = x;
		Active.y = y;
		setup(ref.player.nation);
		if (ClientHandler.sandbox) {
			addActive(7, 1, SandboxBuilding.BuildSetup.class, false);
		}
	}

	void setup(Nation nation) {
		removeActives();
		this.nation = nation;

		switch (nation) {
		case ALIENS:
			setupAliens();
			break;
		case HUMANS:
			setupHumans();
			break;
		default:
			break;
		}

	}

	public void setupAliens() {
		addActive(3, 2, Ticul.Flash.class, true);

		addActive(1, 1, Building.SetTargetActive.class, false);
		addBuildActive(5, 1, AlienMainBuilding.class, ThornTower.class, false);
		addBuildActive(4, 3, AlienMainBuilding.class, AlienKaserne.class, false);
		addBuildActive(4, 2, AlienKaserne.class, AlienKaserneArcanum.class,
				false);
		addBuildActive(4, 1, AlienKaserne.class, AlienKasernePrunam.class,
				false);
		addUpgradeActive(5, 2, AlienMainBuilding.class, AlienKeritMine.class,
				Kerit.class, false);
		addUpgradeActive(6, 2, AlienMainBuilding.class, PaxDrillTower.class,
				Pax.class, false);
		addUpgradeActive(5, 3, AlienMainBuilding.class, ArcanumMine.class,
				Arcanum.class, false);
		addUpgradeActive(6, 3, AlienMainBuilding.class, PrunamHarvester.class,
				Prunam.class, false);

		addTrainActive(1, 3, AlienKaserne.class, Ticul.class, false);
		addTrainActive(2, 3, AlienKaserne.class, Brux.class, false);
		addTrainActive(3, 3, AlienKaserne.class, Valcyrix.class, false);
		addTrainActive(2, 2, AlienKaserneArcanum.class, Colum.class, false);
		addTrainActive(3, 2, AlienKaserneArcanum.class, Arol.class, false);
		addTrainActive(2, 1, AlienKasernePrunam.class, Rug.class, false);
		addTrainActive(3, 1, AlienKasernePrunam.class, Ker.class, false);
	}

	public void setupHumans() {
		addActive(1, 1, Building.SetTargetActive.class, false);
		addBuildActive(4, 3, HumanMainBuilding.class, HumanKaserne.class, false);
		addBuildActive(4, 2, HumanMainBuilding.class, HumanMechKaserne.class,
				false);
		addTrainActive(1, 3, HumanKaserne.class, Scout.class, false);
		addTrainActive(2, 3, HumanKaserne.class, HeavyAssault.class, false);
		addTrainActive(1, 2, HumanKaserne.class, Medic.class, false);
		addTrainActive(3, 3, HumanKaserne.class, Exo.class, false);
		addTrainActive(2, 2, HumanMechKaserne.class, SmallTank.class, false);
		addTrainActive(3, 2, HumanMechKaserne.class, Tank.class, false);
		addTrainActive(2, 1, HumanMechKaserne.class, Drone.class, false);
		addTrainActive(3, 1, HumanMechKaserne.class, Helicopter.class, false);
	}

	public void update() {
		if (showUnitActives) {
			for (int x = 0; x < gridWidth; x++) {
				for (int y = 0; y < gridHeight; y++) {
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
			for (int x = 0; x < gridWidth; x++) {
				for (int y = 0; y < gridHeight; y++) {
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
			Building b = building.getConstructor(String[].class).newInstance(
					new Object[] { null });
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

	public void setupSandbox() {
		System.out.println("setupSandbox");
		addActive(1, 1, SandboxBuilding.DeleteActive.class, true);
		addBuildActive(2, 1, SandboxBuilding.class, ref.player.nation
				.getNationInfo().getMainBuilding(), true);
		addBuildActive(1, 2, SandboxBuilding.class, Kerit.class, true);
		addBuildActive(2, 2, SandboxBuilding.class, Pax.class, true);
		addBuildActive(1, 3, SandboxBuilding.class, Arcanum.class, true);
		addBuildActive(2, 3, SandboxBuilding.class, Prunam.class, true);
		addBuildActive(3, 2, SandboxBuilding.class, Rock.class, true);
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
}
