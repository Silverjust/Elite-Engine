package main;

import java.util.UUID;

import shared.Helper;
import shared.Mode;
import shared.ref;
import g4p_controls.G4P;
import g4p_controls.GButton;
import g4p_controls.GEvent;
import g4p_controls.GPassword;
import g4p_controls.GTextArea;
import g4p_controls.GTextField;

public class Hauptmenue {

	GTextField playerName, serverIp;
	GPassword password;
	GButton multiplayerButton, singleplayerButton, sandboxButton,
			tutorialButton, localhost, startServer;
	GTextArea changes;

	public Hauptmenue() {
		playerName = new GTextField(ref.app, 300, 300, 300, 20);
		playerName.setText(UUID.randomUUID().toString().substring(0, 8));
		playerName.setPromptText("your name");
		playerName.addEventHandler(this, "handleNameEvents");

		password = new GPassword(ref.app, 300, 330, 300, 20);
		// password.

		serverIp = new GTextField(ref.app, 300, 360, 215, 20);
		serverIp.setPromptText("server ip4 adress");

		localhost = new GButton(ref.app, 525, 360, 75, 20);
		localhost.setText("127.0.0.1");
		localhost.addEventHandler(this, "handleLocalhostEvents");

		multiplayerButton = new GButton(ref.app, 300, 390, 300, 40);
		multiplayerButton.setText("multiplayer");
		multiplayerButton.addEventHandler(this, "handleAcceptEvents");

		singleplayerButton = new GButton(ref.app, 300, 440, 300, 40);
		singleplayerButton.setText("singleplayer");
		singleplayerButton.addEventHandler(this, "handleAcceptEvents");

		sandboxButton = new GButton(ref.app, 300, 490, 300, 40);
		sandboxButton.setText("sandbox");
		sandboxButton.addEventHandler(this, "handleAcceptEvents");

		tutorialButton = new GButton(ref.app, 300, 540, 300, 40);
		tutorialButton.setText("tutorial");
		tutorialButton.addEventHandler(this, "handleAcceptEvents");

		changes = new GTextArea(ref.app, 800, 100, 600, 700,
				G4P.SCROLLBARS_VERTICAL_ONLY | G4P.SCROLLBARS_AUTOHIDE);
		changes.setTextEditEnabled(false);
		changes.setText(ref.app.loadStrings("data/changelog.txt"));

		// startServer = new GButton(ref.app, 300, 440, 300, 40);
		// startServer.setText("start a Server");
		// startServer.addEventHandler(this, "handleStartServerEvents");
	}

	public void handleAcceptEvents(GButton button, GEvent event) {
		String name = Helper.secureInput(playerName.getText());
		if (name != "" && name.charAt(0) == ' ')
			name = name.substring(1);
		String ip = Helper.secureInput(serverIp.getText());
		if (ip != "" && ip.charAt(0) == ' ')
			ip = ip.substring(1);

		if (event == GEvent.CLICKED) {
			if (name.equals("")) {
				playerName.setFocus(true);
				return;
			}
			if (button == multiplayerButton) {
				if (ip.equals("")) {
					serverIp.setFocus(true);
					return;
				}
			} else if (button == singleplayerButton) {
				ClientHandler.singlePlayer = true;
			} else if (button == sandboxButton) {
				ClientHandler.singlePlayer = true;
				ClientHandler.sandbox = true;
			} else if (button == tutorialButton) {
				ClientHandler.singlePlayer = true;
				ClientHandler.tutorial = true;
			}

			ref.preGame = new MainPreGame(name);
			ClientHandler.setup(ip);
			if (!ClientHandler.singlePlayer && ClientHandler.client == null) {
				serverIp.setFocus(true);
				((MainPreGame) ref.preGame).closeBecauseServer();
				return;
			}
			((MainPreGame) ref.preGame).setup();

			if (ClientHandler.singlePlayer) {
				((MainApp) ref.app).mode = Mode.PREGAME;
			}
			dispose();

		}
	}

	void dispose() {
		playerName.dispose();
		password.dispose();
		serverIp.dispose();
		localhost.dispose();
		multiplayerButton.dispose();
		singleplayerButton.dispose();
		sandboxButton.dispose();
		tutorialButton.dispose();
		changes.dispose();
	}

	public void handleStartServerEvents(GButton button, GEvent event) {
		// server.ServerApp.main(new String[] { "server.ServerApp" });
	}

	public void handleLocalhostEvents(GButton button, GEvent event) {
		serverIp.setText("127.0.0.1");
	}

	public void handleNameEvents(GTextField textfield, GEvent event) {
		if (event == GEvent.CHANGED) {
			
		}
	}
}
