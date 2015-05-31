package server;

import processing.net.Client;
import processing.net.Server;
import shared.ComHandler;
import shared.ref;

public class ServerHandler {
	ServerApp app;

	Server server;

	String input;

	char endSymbol = '>';

	ServerHandler() {
		app = (ServerApp) ref.app;

		try {
			server = new Server(ref.app, 5204);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void update() {
		if (server != null) {
			while (server.available() != null) {
				Client client = server.available();
				if (client != null) {
					input = "" + client.readStringUntil(endSymbol);
					if (input != null) {

						if (app.gui.displayCommands.isSelected())
							app.gui.addChatText("in  " + input);

						if (input.charAt(0) == '<')
							ComHandler.executeCom(input);

						send(input.replace(endSymbol + "", ""));
					}
				}
			}
		}
	}

	public void serverEvent(Server server, Client someClient) {
		app.gui.addChatText("We have a new client: " + someClient.ip());
		ref.preGame.addPlayer(someClient.ip(), someClient.ip());
		// app.gui.chatText.clear();
		send("<identify");
	}

	public void send(String out) {
		if (app.gui.displayCommands.isSelected())
			app.gui.addChatText("out " + out + endSymbol);
		server.write(out + endSymbol);
	}
}
