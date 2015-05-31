package game;

import g4p_controls.G4P;
import g4p_controls.GTextArea;
import g4p_controls.GTextField;

import java.awt.Font;
import java.util.ArrayList;

import shared.ref;

public class Chat {

	public static GTextField chatLine;
	public static GTextArea chatHistory;

	String out;

	protected static void setup() {// public?

		chatLine = new GTextField(ref.app, 10, ref.app.height - HUD.height-20, 500, 20);
		chatLine.setPromptText("chat");
		chatLine.setFont(new Font("PLAIN", Font.BOLD, 15));
		chatLine.addEventHandler(ref.app, "chatEvents");

		chatHistory = new GTextArea(ref.app, 10, ref.app.height - HUD.height- 220, 500,
				200, G4P.SCROLLBARS_VERTICAL_ONLY | G4P.SCROLLBARS_AUTOHIDE);
		chatHistory.setTextEditEnabled(false);
		chatHistory.setOpaque(false);
		chatHistory.setFont(new Font("PLAIN", Font.BOLD, 17));

		hide();
	}

	public static void hide() {
		chatLine.setVisible(false);
		chatLine.setFocus(false);
	}

	public static void show() {
		chatLine.setVisible(true);
		chatLine.setText("");
		chatLine.setFocus(true);
	}

	public static void println(String name, String s) {
		if (!name.equals("game") && GameDrawer.commandoutput) {
			ArrayList<String> chatText = new ArrayList<String>();
			chatText.add(name + ">>" + s);
			// chatHistory.setText((String[]) chatText.toArray());
			chatHistory.appendText(name + ">>" + s + "\n");
		}
	}
}
