package game;

import main.Settings;
import shared.ref;

public class GroupHandler {
	public static Group[] groups;
	public static Group recentGroup;

	static void setup() {
		groups = new Group[10];
		for (int i = 0; i < groups.length; i++) {
			groups[i] = new Group(300 + (Group.w + 30) * i, ref.app.height
					- HUD.height + 5, Settings.hotKeys[i]);
		}
		recentGroup = groups[0];
	}

	static void update() {
		ref.app.stroke(0);
		ref.app.fill(255);
		for (int i = 0; i < groups.length; i++) {
			groups[i].update();
		}
	}

	public static void fireGroup(int i) {
		groups[i].button.pressManually();
	}
}
