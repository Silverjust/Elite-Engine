package game;

import java.awt.Toolkit;
import java.awt.event.MouseWheelEvent;

import main.MainApp;
import main.appdata.SettingHandler;
import processing.core.PConstants;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import shared.Coms;
import shared.Helper;
import shared.Mode;
import shared.Updater.GameState;
import shared.ref;

public class Input {

	MainApp app;

	boolean isChatVisible;
	boolean isOpeningChat;
	boolean isMPressedOutOfFocus;
	boolean strgMode;// rename
	boolean shiftMode;// rename

	int doubleClickIntervall;
	int doubleClickStart;

	public Input() {
		app = (MainApp) ref.app;

		app.registerMethod("mouseEvent", this);
		app.registerMethod("keyEvent", this);

		doubleClickIntervall = (int) Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
	}

	public boolean isMouseFocusInGame() {
		return Helper.isMouseOver(0, 0, ref.app.width, ref.app.height - HUD.height) || !ref.app.mousePressed;
	}

	public boolean isKeyFocusInGame() {
		return !HUD.chat.hasFocus();
	}

	public void update() {// ********************************************************
		int screenSpeed = 30;
		int rimSize = 10;
		if (ref.app.focused && !isMPressedOutOfFocus) {
			if (Helper.isMouseOver(0, 0, rimSize, ref.app.height) && GameDrawer.xMapOffset < 0)
				GameDrawer.xMapOffset += screenSpeed;
			if (Helper.isMouseOver(ref.app.width - rimSize, 0, ref.app.width, ref.app.height)
					&& -GameDrawer.xMapOffset + app.width <= ref.updater.map.width * GameDrawer.zoom)
				GameDrawer.xMapOffset -= screenSpeed;
			if (Helper.isMouseOver(0, 0, ref.app.width, rimSize) && GameDrawer.yMapOffset < 0)
				GameDrawer.yMapOffset += screenSpeed;
			if (Helper.isMouseOver(0, ref.app.height - rimSize, ref.app.width, ref.app.height)
					&& -GameDrawer.yMapOffset + app.height - HUD.height <= ref.updater.map.height / 2 * GameDrawer.zoom)
				GameDrawer.yMapOffset -= screenSpeed;
		}
	}

	public void keyPressed() {// ********************************************************
		HUD.chat.update();
		if (isKeyFocusInGame()) {
			if (app.key == SettingHandler.setting.togglePause) {
				if (ref.updater.gameState == GameState.PAUSE) {
					ref.updater.send(Coms.PAUSE + " false");
				} else if (ref.updater.gameState == GameState.PLAY) {
					ref.updater.send(Coms.PAUSE + " true");
				}
			}
			/*
			 * if (app.keyCode == SettingHandler.setting.changeAbilityMode) { if
			 * (GroupHandler.recentGroup != null) {
			 * GroupHandler.recentGroup.unitActives =
			 * !GroupHandler.recentGroup.unitActives; HUD.activesGrid
			 * .selectionChange(GroupHandler.recentGroup.unitActives); } else {
			 * HUD.activesGrid
			 * .selectionChange(!ActivesGridHandler.showUnitActives); } }
			 */
			if (app.keyCode == SettingHandler.setting.strg) {
				strgMode = true;
			}
			if (app.keyCode == SettingHandler.setting.shift) {
				shiftMode = true;
			}

			// System.out.println(app.key);
			for (int i = 0; i < SettingHandler.setting.hotKeys.length; i++) {
				if (app.keyCode == SettingHandler.setting.hotKeys[i]) {
					GroupHandler.fireGroup(i);

				}
			}
			for (int x = 0; x < 7; x++) {
				for (int y = 0; y < 3; y++) {
					if (app.key == SettingHandler.setting.baseShortcuts[y][x]) {
						HUD.activesGrid.fire(x, y);
					}
				}
			}

		}
	}

	public void keyReleased() {// ********************************************************
		HUD.chat.justOpened = false;
		if (app.keyCode == SettingHandler.setting.strg) {
			strgMode = false;
		}
		if (app.keyCode == SettingHandler.setting.shift) {
			shiftMode = false;
		}
	}

	public void mouseClicked() {// ********************************************************
	}

	public void mousePressed() {// ********************************************************
		isMPressedOutOfFocus = !isMouseFocusInGame();
		// HUD.chat.println("", "" + isMPressedOutOfFocus);
		if (doubleClickStart + doubleClickIntervall > ref.app.millis()) {
			if (isMouseFocusInGame()) {
				if (AimHandler.isDefault() && app.mouseButton == SettingHandler.setting.mouseSelect) {
					MouseSelection.selectDoubleClick(app.mouseX, app.mouseY);
				}
			}
		} else {
			doubleClickStart = ref.app.millis();
			if (isMouseFocusInGame()) {
				if (AimHandler.isDefault() && app.mouseButton == SettingHandler.setting.mouseSelect) {
					GameDrawer.mouseSelection = new game.MouseSelection(app.mouseX, app.mouseY);
				}
			}
		}
		if (app.mouseButton == SettingHandler.setting.mouseSelect) {
			SelectionDisplay.setIforming(null);
		}
		if (isMouseFocusInGame()) {
			mouseCommands(Helper.gridToX(app.mouseX), Helper.gridToY(app.mouseY));
		} else {
			Minimap.click(app.mouseX, app.mouseY, true);
		}
		// unabh�ngig von mouse fokus
	}

	public void mouseReleased() {// ********************************************************
		isMPressedOutOfFocus = false;
		if (GameDrawer.mouseSelection != null) {
			GameDrawer.mouseSelection.endSelection(app.mouseX, app.mouseY);
			GameDrawer.mouseSelection = null;
		}
	}

	public void mouseDragged() {// ********************************************************
		if (!isMouseFocusInGame()) {
			Minimap.click(app.mouseX, app.mouseY, true);
		}
	}

	public void mouseMoved() {// ********************************************************

	}

	public void mouseWheelMoved(MouseWheelEvent e) {// ********************************************************
		if (HUD.chat.hasFocus()) {
		} else {// chat out of focus
		} // unabh�ngig von chat fokus

	}

	public void keyEvent(KeyEvent event) {
		if (app.mode == Mode.GAME) {
			switch (event.getAction()) {
			case KeyEvent.PRESS:
				keyPressed();
				break;
			case KeyEvent.RELEASE:
				keyReleased();
				break;
			default:
				break;
			}
			if (app.key == PConstants.ESC) {
				app.key = 0;
			}
		}
	}

	public void mouseEvent(MouseEvent event) {
		if (app.mode == Mode.GAME) {
			switch (event.getAction()) {
			case MouseEvent.PRESS:
				mousePressed();
				break;
			case MouseEvent.RELEASE:
				mouseReleased();
				break;
			case MouseEvent.DRAG:
				mouseDragged();
				break;
			case MouseEvent.MOVE:
				mouseMoved();
				break;
			default:
				break;
			}
		}
	}

	void mouseCommands(float x, float y) {
		if (app.mouseButton == SettingHandler.setting.mouseSelect) {
			AimHandler.end();
		} else if (app.mouseButton == SettingHandler.setting.mouseCommand) {
			AimHandler.execute(x, y);
		}
	}
}
