package main;

import processing.core.PConstants;

public class Settings {
	public static int mouseCommand = PConstants.RIGHT;
	public static int mouseSelect = PConstants.LEFT;
	public static int toggleChat = PConstants.ENTER;
	public static int strg = PConstants.CONTROL;
	public static int shift = PConstants.SHIFT;
	public static int changeAbilityMode = PConstants.TAB;

	public static int togglePause = 'p';
	public static char buildTestBuilding = 'b';
	public static char upgradeTestBuilding = 'v';
	public static char[][] unitsShortcuts = new char[][] {
			{ 'q', 'w', 'e', 'r', 't', 'z', 'u' }, //
			{ 'a', 's', 'd', 'f', 'g', 'h', 'j' }, //
			{ 'y', 'x', 'c', 'v', 'b', 'n', 'm' } };
	public static char[][] buildingsShortcuts = new char[][] {
			{ 'q', 'w', 'e', 'r', 't', 'z', 'u' }, //
			{ 'a', 's', 'd', 'f', 'g', 'h', 'j' }, //
			{ 'y', 'x', 'c', 'v', 'b', 'n', 'm' } };
	public static char[] hotKeys = new char[] { //
	'1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

	public class Aliens {
	}

	public class Ahnen {
	}

	public class Robots {
	}

	public class Scientists {
	}

	public class Humans {
	}
}
