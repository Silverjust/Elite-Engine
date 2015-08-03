package game;

import entity.Entity;
import processing.core.PApplet;
import shared.Helper;
import shared.Nation;
import shared.ref;

public class MouseSelection {
	float x1, y1;
	float x2, y2;

	public MouseSelection(float X1, float Y1) {
		x1 = Helper.gridToX(X1);
		y1 = Helper.gridToY(Y1);
	}

	public void disp() {
		ref.app.stroke(ref.player.color);
		ref.app.fill(0, 0);
		ref.app.rect(x1, y1 / 2, Helper.gridToX(ref.app.mouseX) - x1,
				Helper.gridToY(ref.app.mouseY) / 2 - y1 / 2);
		ref.app.stroke(0);
	}

	public void endSelection(float X2, float Y2) {
		x2 = Helper.gridToX(X2);
		y2 = Helper.gridToY(Y2);
		
		if (!((GameUpdater) ref.updater).input.shiftMode) {
			for (Entity e : ref.updater.entities) {
				e.isSelected = false;
			}
			ref.updater.selected.clear();
			ref.updater.selectionChanged = true;
		}

		for (Entity e : ref.updater.entities)
			if (GameDrawer.godhand || e.isAllyTo(ref.player)) {
				if (shared.Helper.isBetween(e.x, e.y, x1, y1, x2, y2))
					e.select();
				if (PApplet.dist(x1, y1, e.x, e.y - e.flyHeight()) <= e.radius) {
					e.select();
					if (e.player.nation != HUD.activesGrid.nation
							&& e.player.nation != Nation.NEUTRAL)
						// for commanding other nations
						HUD.activesGrid.setup(e.player.nation);
				}
			}
		GroupHandler.recentGroup = null;
	}

	public static void selectDoubleClick(int X, int Y) {
		int x = (int) Helper.gridToX(X);
		int y = (int) Helper.gridToY(Y);
		Class<? extends Entity> type = null;
		for (Entity e : ref.updater.entities) {
			if (GameDrawer.godhand || e.isAllyTo(ref.player))
				if (PApplet.dist(x, y, e.x, e.y - e.flyHeight()) <= e.radius)
					type = e.getClass();
		}
		if (type != null) {
			for (Entity e : ref.updater.entities)
				if (GameDrawer.godhand || e.isAllyTo(ref.player))
					if (e.getClass().equals(type))
						e.select();
			GroupHandler.recentGroup = null;
		}
	}

}
