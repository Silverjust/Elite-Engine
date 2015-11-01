package entity.campain;

import entity.Entity;
import entity.MainBuilding;
import game.HUD;
import game.Map;
import game.MapCode;
import main.appdata.SettingHandler;

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

	protected String getActiveBindingText(int i, int j) {
		char key = SettingHandler.setting.buildingsShortcuts[j - 1][i - 1];
		return "{press " + key + " , click the " + key + "-button}";
	}

}
