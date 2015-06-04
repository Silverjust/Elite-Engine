package game;

import java.awt.Toolkit;
import java.awt.event.MouseWheelEvent;

import main.ClientHandler;
import main.MainApp;
import main.Settings;
import processing.core.PConstants;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import shared.Helper;
import shared.Mode;
import shared.ref;
import entity.Entity;

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

		doubleClickIntervall = (int) Toolkit.getDefaultToolkit()
				.getDesktopProperty("awt.multiClickInterval");
	}

	public boolean isMouseFocusInGame() {
		return Helper.isMouseOver(0, 0, ref.app.width, ref.app.height
				- HUD.height)
				|| !ref.app.mousePressed;
	}

	public boolean isKeyFocusInGame() {
		return !Chat.chatLine.hasFocus();
	}

	public void update() {// ********************************************************
		int screenSpeed = 30;
		int rimSize = 10;
		if (ref.app.focused && !isMPressedOutOfFocus) {
			if (Helper.isMouseOver(0, 0, rimSize, ref.app.height)
					&& GameDrawer.xMapOffset < 0)
				GameDrawer.xMapOffset += screenSpeed;
			if (Helper.isMouseOver(ref.app.width - rimSize, 0, ref.app.width,
					ref.app.height)
					&& GameDrawer.xMapOffset - app.width
							+ ref.updater.map.width * GameDrawer.zoom > 0)
				GameDrawer.xMapOffset -= screenSpeed;
			if (Helper.isMouseOver(0, 0, ref.app.width, rimSize)
					&& GameDrawer.yMapOffset < 0)
				GameDrawer.yMapOffset += screenSpeed;
			if (Helper.isMouseOver(0, ref.app.height - rimSize, ref.app.width,
					ref.app.height)
					&& GameDrawer.yMapOffset - app.height + HUD.height
							+ ref.updater.map.height / 2 * GameDrawer.zoom > 0)
				GameDrawer.yMapOffset -= screenSpeed;
		}
	}

	public void keyPressed() {// ********************************************************
		if (!isChatVisible && app.keyCode == Settings.toggleChat) {
			isOpeningChat = true;
		}
		if (isChatVisible && app.keyCode == Settings.toggleChat
				&& !Chat.chatLine.hasFocus()) {
			Chat.hide();
			isChatVisible = false;
		}

		if (app.keyCode == PConstants.BACKSPACE
				&& Chat.chatLine.getText().equals(" ")) {
			Chat.hide();
			isChatVisible = false;
		}

		if (isKeyFocusInGame()) {
			if (app.keyCode == Settings.togglePause) {
				((GameUpdater) ref.updater).pause = !((GameUpdater) ref.updater).pause;
			}
			if (app.keyCode == Settings.changeAbilityMode) {
				if (GroupHandler.recentGroup != null) {
					GroupHandler.recentGroup.unitActives = !GroupHandler.recentGroup.unitActives;
					ActivesGrid.showUnitActives = GroupHandler.recentGroup.unitActives;
				} else {
					ActivesGrid.showUnitActives = !ActivesGrid.showUnitActives;
				}
			}
			if (app.keyCode == Settings.strg) {
				strgMode = true;
			}
			if (app.keyCode == Settings.shift) {
				shiftMode = true;
			}

			// System.out.println(app.key);
			for (int i = 0; i < Settings.hotKeys.length; i++) {
				if (app.keyCode == Settings.hotKeys[i]) {
					GroupHandler.fireGroup(i);

				}
			}
			for (int x = 0; x < 7; x++) {
				for (int y = 0; y < 3; y++) {
					if (app.key == Settings.unitsShortcuts[y][x]) {
						HUD.activesGrid.fire(x, y);
					} else if (app.key == Settings.unitsShortcuts[y][x]) {
						HUD.activesGrid.fire(x, y);
					}
				}
			}
		}

	}

	public void keyReleased() {// ********************************************************
		if (isOpeningChat) {
			isOpeningChat = false;
			Chat.show();
			isChatVisible = true;
		}
		if (app.keyCode == Settings.strg) {
			strgMode = false;
		}
		if (app.keyCode == Settings.shift) {
			shiftMode = false;
		}
	}

	public void mouseClicked() {// ********************************************************

	}

	public void mousePressed() {// ********************************************************
		isMPressedOutOfFocus = !isMouseFocusInGame();
		// Chat.println("", "" + isMPressedOutOfFocus);
		if (doubleClickStart + doubleClickIntervall > ref.app.millis()) {
			if (isMouseFocusInGame()) {
				if (!AimHandler.isAiming()
						&& app.mouseButton == Settings.mouseSelect) {
					MouseSelection.selectDoubleClick(app.mouseX, app.mouseY);
				}
			}
		} else {
			doubleClickStart = ref.app.millis();
			if (isMouseFocusInGame()) {
				if (!AimHandler.isAiming()
						&& app.mouseButton == Settings.mouseSelect) {
					GameDrawer.mouseSelection = new game.MouseSelection(
							app.mouseX, app.mouseY);
				}
			}
		}
		if (isMouseFocusInGame()) {
			if (AimHandler.isAiming()
					&& app.mouseButton == Settings.mouseSelect) {
				AimHandler.end();
			}
			if (!AimHandler.isAiming()
					&& app.mouseButton == Settings.mouseCommand) {
				for (Entity entity : ref.updater.selected) {
					ClientHandler.send("<execute " + entity.number + " walk "
							+ Helper.gridToX(app.mouseX) + " "
							+ Helper.gridToY(app.mouseY));
				}// TODO Pfeil anzeigen
			}

			if (AimHandler.isAiming()
					&& app.mouseButton == Settings.mouseCommand) {
				AimHandler.execute();
			}

		}// unabhängig von mouse fokus
	}

	public void mouseReleased() {// ********************************************************
		isMPressedOutOfFocus = false;
		if (GameDrawer.mouseSelection != null) {
			GameDrawer.mouseSelection.endSelection(app.mouseX, app.mouseY);
			GameDrawer.mouseSelection = null;
		}
	}

	public void mouseDragged() {// ********************************************************
		if (Chat.chatLine.hasFocus()) {
		} else {// chat out of focus
		}// unabhängig von chat fokus

	}

	public void mouseMoved() {// ********************************************************
		if (Chat.chatLine.hasFocus()) {
		} else {// chat out of focus
		}// unabhängig von chat fokus

	}

	public void mouseWheelMoved(MouseWheelEvent e) {// ********************************************************
		if (Chat.chatLine.hasFocus()) {
		} else {// chat out of focus
		}// unabhängig von chat fokus

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
}
