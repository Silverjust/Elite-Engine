package game;

import g4p_controls.GDropList;
import processing.core.PConstants;
import shared.ref;
import main.Setting;

public class SettingHandler {
	public static Setting setting;
	
	GDropList settingList;
	Setting[] settings;
	
	public static void setup(){
		setting=new Setting();
		setDefaultSetting();
	}
	
	public SettingHandler() {
	settingList=new GDropList(ref.app, 200, 200, 100, 400, 5);
	}

	private static void setDefaultSetting() {
		setting.mouseCommand = PConstants.RIGHT;
		setting.mouseSelect = PConstants.LEFT;
		setting.toggleChat = PConstants.ENTER;
		setting.strg = PConstants.CONTROL;
		setting.shift = PConstants.SHIFT;
		setting.changeAbilityMode = PConstants.TAB;

		setting.togglePause =PConstants.ESC;
		setting.unitsShortcuts = new char[][] {
				{ 'q', 'w', 'e', 'r', 't', 'z', 'u' }, //
				{ 'a', 's', 'd', 'f', 'g', 'h', 'j' }, //
				{ 'y', 'x', 'c', 'v', 'b', 'n', 'm' } };
		setting.buildingsShortcuts = new char[][] {
				{ 'q', 'w', 'e', 'r', 't', 'z', 'u' }, //
				{ 'a', 's', 'd', 'f', 'g', 'h', 'j' }, //
				{ 'y', 'x', 'c', 'v', 'b', 'n', 'm' } };
		setting.hotKeys = new char[] { //
		'1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

	}
}
