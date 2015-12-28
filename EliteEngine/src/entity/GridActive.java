package entity;

import game.ActivesGrid;
import game.ActivesGridHandler;

public class GridActive extends Active {
	/** aktive fähigkeit die grid aufruft */
	Class<? extends Building> building;
	String descr = " ", stats = " ";
	private ActivesGrid grid;
	private ActivesGridHandler handler;

	public GridActive(int x, int y, char n, Class<?> displayer, ActivesGrid grid, ActivesGridHandler handler) {
		super(x, y, n, null);// TODO bilder
		this.grid = grid;
		this.handler = handler;
		clazz = displayer;
	}

	@Override
	protected void onActivation() {
		handler.displayGrid = getGrid();
		handler.selectionChange();
	}

	@Override
	public String getDesription() {
		return getGrid().getDesription();
	}

	@Override
	public String getStatistics() {
		return "";
	}

	public ActivesGrid getGrid() {
		return grid;
	}

}
