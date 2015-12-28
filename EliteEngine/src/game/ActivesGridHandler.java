package game;

import main.preGame.MainPreGame.GameSettings;

import java.util.ArrayList;

import entity.Active;
import entity.BuildWallActive;
import entity.GridActive;
import entity.neutral.*;
import shared.Helper;
import shared.Nation;
import shared.ref;

public class ActivesGridHandler {
	@Deprecated
	public static boolean showUnitActives;
	public int x = ref.app.width - 420;
	public int y = ref.app.height - HUD.height + 10;
	int w = 60;
	static final int gridHeight = 3;
	static final int gridWidth = 7;

	public ActivesGrid baseGrid;
	public ArrayList<ActivesGrid> gridList = new ArrayList<ActivesGrid>();
	public Nation nation;
	public ActivesGrid displayGrid;

	public ActivesGridHandler() {
		Active.x = x;
		Active.y = y;
		displayGrid = baseGrid = new ActivesGrid(this, ActivesGrid.NORMAL);

		gridList.add(displayGrid);
		setup(ref.player.getNation());
		selectionChange();
	}

	void setup(Nation nation) {
		if (nation != null) {
			removeActives();
			this.nation = nation;
			nation.getNationInfo().setupActives(baseGrid, this);
			if (GameSettings.sandbox) {
				baseGrid.addActive(7, 1, SandboxBuilding.BuildSetup.class);
			}
		}
	}

	public void update() {
		for (int x = 0; x < gridWidth; x++)
			for (int y = 0; y < gridHeight; y++)
				if (displayGrid.get(x, y) != null && //
						displayGrid.get(x, y).isVisible())
					displayGrid.get(x, y).update();
	}

	public void fire(int x, int y) {
		try {
			displayGrid.get(x, y).pressManually();
		} catch (NullPointerException e) {
		}
	}

	public void setupSandbox() {
		System.out.println("setupSandbox");
		SandboxBuilding.commandRange = Integer.MAX_VALUE;
		baseGrid.addActive(1, 1, SandboxBuilding.DeleteActive.class);
		baseGrid.addBuildActive(2, 1, SandboxBuilding.class, ref.player.getNation().getNationInfo().getMainBuilding());
		baseGrid.addBuildActive(1, 2, SandboxBuilding.class, Kerit.class);
		baseGrid.addBuildActive(2, 2, SandboxBuilding.class, Pax.class);
		baseGrid.addBuildActive(1, 3, SandboxBuilding.class, Arcanum.class);
		baseGrid.addBuildActive(2, 3, SandboxBuilding.class, Prunam.class);
		baseGrid.addBuildActive(3, 2, SandboxBuilding.class, Rock.class);
		baseGrid.addActive(3, 3, BuildWallActive.class, SandboxBuilding.class, Rock.class);
		baseGrid.addActive(3, 1, SandboxBuilding.ChangeSide.class);
		baseGrid.addActive(4, 1, SandboxBuilding.AddPlayer.class);
	}

	public void removeActives() {
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				baseGrid.disposeActive(x, y);
			}
		}
	}

	public void dispose() {
		removeActives();
	}

	public void selectionChange() {
		int n = 0;
		Active ability = null;
		System.out.println("ActivesGridHandler.selectionChange()start " + displayGrid.getType());
		boolean b = true;
		clearlySearch: {
			while (b) {
				n = 0;
				ability = null;
				for (int x = 0; x < gridWidth; x++) {
					for (int y = 0; y < gridHeight; y++) {
						if (displayGrid.get(x, y) != null
								&& Helper.listContainsInstanceOf(displayGrid.get(x, y).clazz, ref.updater.selected)) {
							n++;
							ability = displayGrid.get(x, y);
							System.out.println("ActivesGridHandler.selectionChange()" + n + " " + displayGrid.getType()
									+ " " + (ability instanceof GridActive));
							if (n > 1 || !(ability instanceof GridActive))
								break clearlySearch;
						}
					}
				}
				if (ability == null)
					break clearlySearch;
				displayGrid = ((GridActive) ability).getGrid();
			}
		}
		System.out.println("ActivesGridHandler.selectionChange() " + displayGrid.getType());
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				if (displayGrid.get(x, y) != null)
					if (Helper.listContainsInstanceOf(displayGrid.get(x, y).clazz, ref.updater.selected)) {
						displayGrid.get(x, y).setVisible(true);
						displayGrid.get(x, y).selectionUpdate();

					} else
						displayGrid.get(x, y).setVisible(false);
				for (ActivesGrid activesGrid : gridList) {
					if (activesGrid != displayGrid && activesGrid.get(x, y) != null)
						activesGrid.get(x, y).setVisible(false);
				}

			}

		}

	}

	public void resetGrid() {
		displayGrid = baseGrid;
	}
}
