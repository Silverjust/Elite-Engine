package entity.campain;

import entity.Active;
import entity.Entity;
import entity.MainBuilding;
import game.ActivesGrid;
import game.HUD;
import game.Map;
import game.MapCode;

public class CampainMapCode extends MapCode {

	protected int n = 0;
	protected int i = 0;
	private boolean report;
	protected Entity tutorial;
	protected MainBuilding mb;

	public CampainMapCode(Map map) {
		super(map);
	}

	protected boolean isNext() {
		if (report)
			System.out.println("CampainMapCode.isNext() " + n + " " + i);
		boolean b = n == i;
		if (b) {
			HUD.chat.printSpace();
			n++;
		}
		i++;
		return b;
	}

	protected void nextIf(boolean b) {
		if (report)
			System.out.println("CampainMapCode.nextIf() " + (b && n == i));
		if (report)
			System.out.println("CampainMapCode.nextIf() " + n + " " + i);
		if (b && n == i) {
			n++;
		}
		i++;
	}

	protected void MarkerTo(Entity e) {
		if (tutorial != null)
			tutorial.sendAnimation("walk " + e.x + " " + e.y);
	}

	void MarkerTo(float x, float y) {
		if (tutorial != null)
			tutorial.sendAnimation("walk " + x + " " + y);
	}

	@Deprecated
	protected String getActiveBindingText(ActivesGrid grid, int i, int j) {
		char key = grid.getShortcuts()[j - 1][i - 1];
		return "{press " + key + " , click the " + key + "-button}";
	}

	protected String getActiveBindingText(Active a) {
		char key = '?';
		for (ActivesGrid grid : HUD.activesGrid.gridList) {
			for (int x = 0; x < ActivesGrid.gridWidth; x++) {
				for (int y = 0; y < ActivesGrid.gridHeight; y++) {
					if (a == (grid.get(x, y)))
						key = grid.getShortcuts()[y][x];
				}
			}
		}
		return "{press " + key + " , click the " + key + "-button}";
	}

}
