package game;

import java.util.ArrayList;

import shared.ref;
import entity.Building;
import entity.Entity;
import g4p_controls.GEvent;
import g4p_controls.GGameButton;

public class Group {
	private ArrayList<Entity> groupEntities = new ArrayList<Entity>();
	GGameButton button;
	int x, y;
	static int w = 50, h = 20;
	public boolean unitActives;

	public Group(int x, int y, char n) {
		this.x = x;
		this.y = y;
		button = new GGameButton(ref.app, x, y, w, h, HUD.buttonImageFilename());
		button.setText(n + "");
		button.addEventHandler(this, "handleClickEvent");
	}

	void update() {
		if (!groupEntities.isEmpty())
			button.setSymbol(groupEntities.get(0).iconImg);
		else
			button.setSymbol(null);
	}

	void add(Entity e) {
		groupEntities.add(e);
	}

	void remove(Entity e) {
		groupEntities.remove(e);
	}

	public void handleClickEvent(GGameButton gamebutton, GEvent event) {
		if (event == GEvent.PRESSED) {
			if (((GameUpdater) ref.updater).input.shiftMode) {

				for (Entity entity : ref.updater.selected) {
					if (!groupEntities.contains(entity))
						groupEntities.add(entity);
				}
			} else if (((GameUpdater) ref.updater).input.strgMode) {

				boolean containsUnits = false;
				groupEntities.clear();
				for (Entity entity : ref.updater.selected) {
					groupEntities.add(entity);
					if (!(entity instanceof Building))
						containsUnits = true;
				}
				unitActives = containsUnits;

			}
			GroupHandler.recentGroup = this;
			ActivesGrid.showUnitActives = unitActives;
			for (Entity entity : ref.updater.selected) {
				entity.isSelected = false;
			}
			for (Entity entity : groupEntities) {
				entity.isSelected = true;
			}
		}
	}
}
