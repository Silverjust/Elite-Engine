package server;

import processing.net.Client;
import processing.net.Server;
import shared.ComHandler;
import shared.Mode;
import shared.Player;
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
		if (app.mode == Mode.GAME
				&& ref.updater.player.containsKey(someClient.ip())) {
			Player p = ref.updater.player.get(someClient.ip());
			app.gui.addChatText(p.name + " has reconnected");
			send("<identify reconnect");
			send("<setNation " + p.ip + " " + p.nation.toString());
			send("<setMap " + p.ip + " " + ref.preGame.map);
		} else {
			app.gui.addChatText("We have a new client: " + someClient.ip());
			ref.preGame.addPlayer(someClient.ip(), someClient.ip());
			send("<identify server");
		}
	}

	public void send(String out) {
		if (app.gui.displayCommands.isSelected())
			app.gui.addChatText("out " + out + endSymbol);
		server.write(out + endSymbol);
	}
}
