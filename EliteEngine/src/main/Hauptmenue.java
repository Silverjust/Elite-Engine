package main;

import java.util.UUID;

import shared.Helper;
import shared.Mode;
import shared.ref;
import g4p_controls.GButton;
import g4p_controls.GEvent;
import g4p_controls.GPassword;
import g4p_controls.GTextArea;
import g4p_controls.GTextField;

public class Hauptmenue {

	GTextField playerName, serverIp;
	GPassword password;
	GButton acceptButton, localhost, startServer;
	GTextArea changes;

	public Hauptmenue() {
		playerName = new GTextField(ref.app, 300, 300, 300, 20);
		playerName.setText(UUID.randomUUID().toString().substring(0, 8));
		playerName.setPromptText("your name");

		password = new GPassword(ref.app, 300, 330, 300, 20);
		// password.

		serverIp = new GTextField(ref.app, 300, 360, 215, 20);
		serverIp.setPromptText("server ip4 adress");

		localhost = new GButton(ref.app, 525, 360, 75, 20);
		localhost.setText("127.0.0.1");
		localhost.addEventHandler(this, "handleLocalhostEvents");

		acceptButton = new GButton(ref.app, 300, 390, 300, 40);
		acceptButton.setText("connect");
		acceptButton.addEventHandler(this, "handleAcceptEvents");
		
		
		changes=new GTextArea(ref.app, 800, 100, 600, 700);
		changes.setTextEditEnabled(false);
		changes.setText(ref.app.loadStrings("data/changelog.txt"));


		// startServer = new GButton(ref.app, 300, 440, 300, 40);
		// startServer.setText("start a Server");
		// startServer.addEventHandler(this, "handleStartServerEvents");
	}

	public void handleAcceptEvents(GButton button, GEvent event) {
		String name = Helper.secureInput(playerName.getText());
		if (name.charAt(0) == ' ')
			name = name.substring(1);
		String ip = Helper.secureInput(serverIp.getText());
		if (ip != "" && ip.charAt(0) == ' ')
			ip = ip.substring(1);

		if (event == GEvent.CLICKED) {
			ref.preGame = new MainPreGame(name);
			ClientHandler.setup(ip);
			playerName.dispose();
			password.dispose();
			serverIp.dispose();
			localhost.dispose();
			acceptButton.dispose();
			changes.dispose();
			((MainApp) ref.app).mode = Mode.PREGAME;

		}
	}

	public void handleStartServerEvents(GButton button, GEvent event) {
		// server.ServerApp.main(new String[] { "server.ServerApp" });
	}

	public void handleLocalhostEvents(GButton button, GEvent event) {
		serverIp.setText("127.0.0.1");
	}
}
