package entity;

import game.ActivesGrid;
import game.ActivesGridHandler;

public class GridActive extends Active {
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
	public void onActivation() {
		handler.displayGrid = grid;
		handler.selectionChange();
	}

	@Override
	public String getDesription() {
		return grid.getDesription();
	}

	@Override
	public String getStatistics() {
		return "";
	}
}
