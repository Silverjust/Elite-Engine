package game;

import java.lang.reflect.Constructor;

import entity.Active;
import entity.BuildActive;
import entity.Building;
import entity.Entity;
import entity.TrainActive;
import entity.Unit;
import entity.UpgradeActive;
import main.appdata.SettingHandler;
import shared.Helper;
import shared.ref;
import entity.GridActive;

public class ActivesGrid {
	public static final int gridHeight = 3;
	public static final int gridWidth = 7;
	public static final String UNITS = "unit-abilities";
	public static final String BUILDINGS = "construction";
	public static final String TRAINING = "training";
	public static final String BASEGRID = "base-grid";
	private Active[][] baseActivesGrid;
	private char[][] shortcuts;// TODO load shortcuts
	private String descr = "";

	/** only use when individual grid */
	public ActivesGrid(ActivesGridHandler handler, String description) {
		baseActivesGrid = new Active[gridWidth][gridHeight];
		handler.gridList.add(this);
		this.descr = description;
		this.shortcuts = SettingHandler.setting.getShortcut(handler.nation.toString(), descr);
		if (shortcuts == null){System.out.println("ActivesGrid.ActivesGrid() use base shortcuts");
			this.shortcuts = SettingHandler.setting.baseShortcuts;}
	}

	public ActivesGrid(ActivesGridHandler handler) {
		this(handler, "");
	}

	public String getType() {
		return descr;
	}

	public char[][] getShortcuts() {
		return shortcuts;
	}

	public Active get(int x, int y) {
		return baseActivesGrid[x][y];
	}

	/**
	 * returns the most obvious grid
	 * <p>
	 * if grid only contains one visible gridactive it returns the grid of the
	 * active
	 */
	public ActivesGrid getObviousGrid() {
		int n;
		Active ability;
		ActivesGrid grid = this;
		while (true) {
			n = 0;
			ability = null;
			for (int x = 0; x < gridWidth; x++) {
				for (int y = 0; y < gridHeight; y++) {
					if (grid.get(x, y) != null
							&& Helper.listContainsInstanceOf(grid.get(x, y).clazz, ref.updater.selected)) {
						n++;
						ability = grid.get(x, y);
						if (n > 1 || !(ability instanceof GridActive))
							return grid;
					}
				}
			}
			if (ability == null)
				return grid;
			grid = ((GridActive) ability).getGrid();
		}
	}

	public void disposeActive(int x, int y) {
		if (baseActivesGrid[x][y] != null) {
			baseActivesGrid[x][y].button.dispose();
			baseActivesGrid[x][y] = null;
		}
	}

	/**
	 * @param grid
	 *            active displays this grid
	 * @return
	 */
	public Active addGridActive(int x, int y, Class<?> displayer, ActivesGrid grid, ActivesGridHandler handler) {
		x--;
		y--;
		try {
			baseActivesGrid[x][y] = new GridActive(x, y, shortcuts[y][x], displayer, grid, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baseActivesGrid[x][y];
	}

	public Active addActive(int x, int y, Class<? extends Active> a) {
		x--;
		y--;
		try {
			Constructor<?> ctor = a.getConstructor(int.class, int.class, char.class);
			baseActivesGrid[x][y] = (Active) ctor.newInstance(//
					new Object[] { x, y, shortcuts[y][x] });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baseActivesGrid[x][y];
	}

	public Active addActive(int x, int y, Class<? extends Active> a, Class<?> builder,
			Class<? extends Entity> building) {
		x--;
		y--;
		try {
			Constructor<?> ctor = a.getConstructor(int.class, int.class, char.class, Entity.class, Class.class);
			Entity b = building.getConstructor(String[].class).newInstance(new Object[] { null });
			baseActivesGrid[x][y] = (Active) ctor.newInstance(//
					new Object[] { x, y, shortcuts[y][x], b, builder });
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baseActivesGrid[x][y];
	}

	public Active addTrainActive(int x, int y, Class<? extends Entity> trainer, Class<? extends Unit> toTrain) {
		x--;
		y--;
		try {
			Unit u = toTrain.getConstructor(String[].class).newInstance(new Object[] { null });
			baseActivesGrid[x][y] = new TrainActive(x, y, shortcuts[y][x], u, trainer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baseActivesGrid[x][y];

	}

	public Active addBuildActive(int x, int y, Class<?> builder, Class<? extends Building> building) {
		x--;
		y--;
		try {
			Building b = building.getConstructor(String[].class).newInstance(new Object[] { null });
			baseActivesGrid[x][y] = new BuildActive(x, y, shortcuts[y][x], b, builder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baseActivesGrid[x][y];

	}

	public Active addUpgradeActive(int x, int y, Class<? extends Entity> builder, Class<? extends Building> newBuilding,
			Class<? extends Building> oldBuilding) {
		x--;
		y--;
		try {
			Building b = newBuilding.getConstructor(String[].class).newInstance(new Object[] { null });
			baseActivesGrid[x][y] = new UpgradeActive(x, y, shortcuts[y][x], b, oldBuilding, builder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baseActivesGrid[x][y];
	}

	public String getDesription() {
		if (descr.equals(BASEGRID))
			return "back to " + descr;
		return "go to " + descr;
	}

	/**
	 * creates ActiveGrid and GridActive to new grid and a back-GridActive at
	 * 5,3
	 * 
	 * @param displayer
	 *            TODO
	 */
	public ActivesGrid createTab(int x, int y, Class<?> displayer, ActivesGridHandler handler, String descr) {
		ActivesGrid newGrid = new ActivesGrid(handler, descr);
		this.addGridActive(x, y, displayer, newGrid, handler);
		newGrid.addGridActive(5, 3, Entity.class, this, handler);
		return newGrid;
	}
}