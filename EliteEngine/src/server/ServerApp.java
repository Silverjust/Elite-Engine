package server;

import g4p_controls.G4P;
import processing.core.PApplet;
import processing.core.PConstants;
import shared.Client;
import shared.Server;
import shared.Mode;
import shared.ref;

@SuppressWarnings("serial")
public class ServerApp extends PApplet {
	public static void main(String args[]) {
		PApplet.main(new String[] { "server.ServerApp" });
	}

	ServerHandler serverHandler;

	public GUI gui;

	Mode mode;

	public void setup() {
		size(500, 500, PConstants.P2D);
		frame.setTitle("Server EliteEngine");
		mode = Mode.PREGAME;
		G4P.messagesEnabled(false);

		ref.setTextScale(1.4F);// so ungefär

		ref.setApp(this);

		ref.setPreGame(new ServerPreGame());

		gui = new GUI();
		serverHandler = new ServerHandler();

	}

	public void draw() {
		background(240);
		gui.update();
		serverHandler.update();

		switch (mode) {
		case PREGAME:
			ref.preGame.update();
			break;
		case LADESCREEN:
			ref.loader.update();
			break;
		case GAME:
			if (frameCount % 2 == 0)
				thread("serverUpdate");
			break;
		default:
			break;
		}

	}

	public void serverUpdate() {
		ref.updater.update();

	}

	public void disconnectEvent(Client client) {
		serverHandler.disconnectEvent( client); 
	}

	public void serverEvent(Server server, Client someClient) {
		serverHandler.serverEvent(server, someClient);
	}

}
