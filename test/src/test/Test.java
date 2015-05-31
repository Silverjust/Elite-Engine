package test;

import g4p_controls.G4P;
import g4p_controls.GEditableTextControl;
import g4p_controls.GEvent;
import g4p_controls.GTextArea;
import g4p_controls.GTextField;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.net.Client;

@SuppressWarnings("serial")
public class Test extends PApplet {
	public static void main(String args[]) {
		PApplet.main(new String[] { "test.Test" });
	}

	GTextField f;
	GTextArea answer;
	Client client;

	public void setup() {
		size(800, 800, PConstants.P2D);
		G4P.changeCursor(false);
		f = new GTextField(this, 0, 0, 500, 100);
		answer = new GTextArea(this, 0, 100, 500, 700,
				G4P.SCROLLBARS_VERTICAL_ONLY | G4P.SCROLLBARS_AUTOHIDE);
		// URL url = this.getClass().getClassLoader().getResource("data");
		// System.out.println(url.getPath());
		cursor(PConstants.HAND);

		client = new Client(this, "127.0.0.1", 5204);
	}

	public void draw() {
	}

	public void mousePressed() {
		System.out.println(mouseButton);
	}

	public void handleTextEvents(GEditableTextControl textcontrol, GEvent event) {
		if (event == GEvent.ENTERED) {
			String s = textcontrol.getText().length() > 0 ? (textcontrol
					.getText().charAt(0) == ' ' ? (textcontrol.getText()
					.substring(1)) : (textcontrol.getText())) : ("");

			client.write(s);

			textcontrol.setText("");
		}
	}

	public void clientEvent(Client someClient) {
		if (client != null) {
			String in = "" + client.readStringUntil('>');
			if (in != null && !in.equals("null")) {
				// System.out.println("in: " + in);
				if (in.charAt(0) == '<') {
					answer.appendText(in);
				}
			}
		}
	}
}
