package main;

import g4p_controls.G4P;
import g4p_controls.GEditableTextControl;
import g4p_controls.GEvent;
import g4p_controls.GTextField;
import g4p_controls.GValueControl;
import game.Chat;
import game.GameDrawer;

import javax.swing.JFrame;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import shared.Client;
import shared.CommandHandler;
import shared.Helper;
import shared.Mode;
import shared.ref;

@SuppressWarnings("serial")
public class MainApp extends PApplet {
	public static void main(String args[]) {
		boolean fullscreen = false;
		fullscreen = true;
		if (fullscreen) {
			PApplet.main(new String[] { "--present", "main.MainApp" });
		} else {
			PApplet.main(new String[] { "main.MainApp" });
		}
	}

	PFont font;
	float xc = (float) (width / 2.0), yc = (float) (height / 2.0);

	public float s = ((width / 1600.0F) < (height / 900.0F)) ? (width / 1600.0F)
			: (height / 900.0F);

	public Mode mode;

	Hauptmenue hauptmenue;

	public void setup() {
		size(displayWidth, displayHeight, P2D);
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.setResizable(true);
		frame.setTitle("Battle of Orion");
		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		// frame.setVisible(true);
		frameRate(60);
		font = createFont("Aharoni Fett", 40);
		ref.setTextScale(0.5F);// so ungefär
		ref.setFont(font);
		//System.out.println(font.ascent());
		textFont(font);
		noSmooth();
		mode = Mode.HAUPTMENUE;
		G4P.messagesEnabled(false);

		ref.setApp(this);

		hauptmenue = new Hauptmenue();
	}

	public void draw() {

		switch (mode) {
		case HAUPTLADESCREEN:
			break;
		case HAUPTMENUE:
			break;
		case PREGAME:
			ref.preGame.update();
			break;
		case LADESCREEN:
			ref.loader.update();
			break;
		case GAME:
			// if (frameCount % 100 == 0)// DEBUG
			// CommandHandler.executeCommands("/fps");
			// HUD.chatPrintln("fps", ""+frameRate);
			ref.updater.update();
			GameDrawer.update();

			break;

		default:
			break;
		}
		// text("ccccccccccccccc", 100, 100);// habs gefunden, war zum test

	}

	public void chatEvents(GTextField textfield, GEvent event) {
		// System.out.println(event);
		switch (event) {
		case ENTERED:
			String s = textfield.getText().length() > 0 ? (textfield.getText()
					.charAt(0) == ' ' ? (textfield.getText().substring(1))
					: (textfield.getText())) : ("");
			s = Helper.secureInput(s);
			if (s.equals("") || s.equals(" ")) {
				Chat.hide();
			} else {
				println(s);

				if (s.length() > 0 && s.charAt(0) == '/') {
					Chat.println(ref.player.name, s);
					CommandHandler.executeCommands(s);
				} else {
					ClientHandler.send("<say " + ref.player.ip + " " + s);
				}
			}
			textfield.setText("");
			break;
		default:
			break;
		}
	}

	public void handleTextEvents(GEditableTextControl textcontrol, GEvent event) {
		if (event == GEvent.CHANGED) {
			GameDrawer.mouseSelection = null;
		}
	}

	public void handleSliderEvents(GValueControl slider, GEvent event) { /* code */
	}

	public void clientEvent(Client someClient) {
		ClientHandler.clientEvent(someClient);
	}

	@Override
	public void keyPressed() {
		if (key == PConstants.ESC) {

		}
	}

}
