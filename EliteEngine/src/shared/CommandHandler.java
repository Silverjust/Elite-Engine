package shared;

import javax.naming.NoInitialContextException;

import main.ClientHandler;
import processing.core.PApplet;
import entity.Entity;
import g4p_controls.GCScheme;
import game.Chat;
import game.GameDrawer;
import game.ImageHandler;

public class CommandHandler {

	public static void executeCommands(String command) {
		String[] c = PApplet.splitTokens(command, " ");

		try {
			int i;
			float f;
			Entity e;
			switch (c[0]) {
			case "/move":
				if (c[1].equals("s")) {
					for (Entity entity : ref.updater.selected) {
						ClientHandler.send("<execute " + entity.number
								+ " walk " + c[2] + " " + c[3]);
					}
				} else {
					ClientHandler.send("<execute " + c[1] + " walk " + c[2]
							+ " " + c[3]);
				}
				break;
			case "/amove":
				// ClientHandler.send(command.replaceFirst("/", "<"));
				break;
			case "/hit":
				if (c[1].equals("s")) {
					for (Entity entity : ref.updater.selected) {
						ClientHandler.send("<hit " + entity.number + c[2]);
					}
				} else {
					ClientHandler.send("<hit " + c[1] + " " + c[2]);
				}
				break;
			case "/tp":
				if (c[1].equals("s")) {
					for (Entity entity : ref.updater.selected) {
						ClientHandler.send("<tp " + entity.number + c[2] + " "
								+ c[3]);
					}
				} else {
					ClientHandler.send("<tp " + c[1] + " " + c[2] + " " + c[3]);
				}
				break;
			case "/spawn":
				c[0] = c[0].replaceFirst("/", "<");
				c[2] = Helper.nameToIP(c[2]);
				ClientHandler.send(PApplet.join(c, " "));
				break;
			case "/kill":
				ClientHandler.send(command.replaceFirst("/", "<"));
				break;
			case "/remove":
				ClientHandler.send(command.replaceFirst("/", "<"));
				break;
			case "/say":
				ClientHandler.send(command.replaceFirst("/", "<"));
				break;
			case "/load":
				ref.preGame.startLoading();
				break;
			case "/ready":
				ref.loader.tryStartGame();
				break;
			case "/info":
				i = Integer.parseInt(c[1]);
				e = ref.updater.namedEntities.get(i);
				if (e != null) {
					e.info();
				} else {
					throw new IllegalArgumentException("no entity found");
				}
				break;
			case "/zoom":
				f = Float.parseFloat(c[1]);
				GameDrawer.zoom = f;
				GameDrawer.xMapOffset *= f;
				GameDrawer.yMapOffset *= f;
				break;
			case "/reloadImages":
				ImageHandler.requestAllImages();
				break;
			case "/fps":
				Chat.println("fps", ref.app.frameRate + "");
				break;
			case "/scheme":
				i = Integer.parseInt(c[1]);
				int r = Integer.parseInt(c[2]);
				int g = Integer.parseInt(c[3]);
				int b = Integer.parseInt(c[4]);
				GCScheme.setScheme(8, i, ref.app.color(r, g, b));
				break;
			case "/gamerule":
				if (c[1].equals("commandoutput")) {
					GameDrawer.commandoutput = Helper.StringToBoolean(c[2]);
				}
				if (c[1].equals("godeye")) {
					GameDrawer.godeye = Helper.StringToBoolean(c[2]);
				}
				if (c[1].equals("godhand")) {
					GameDrawer.godhand = Helper.StringToBoolean(c[2]);
				}
				break;
			default:
				throw new NoInitialContextException("no command found");
			}
		} catch (IllegalArgumentException e) {
			System.err.println("no entity found " + command);
			Chat.println("Chat", "no entity found ");
		} catch (ClassCastException e) {
			System.err.println("wrong entity " + command);
			Chat.println("Chat", "wrong entity ");
		} catch (NoInitialContextException e) {
			System.err.println(command + " was not found");
			Chat.println("Chat", "command was not found");
		} catch (Exception e) {
			System.err.println("command error in " + command);
			// e.printStackTrace();
			Chat.println("Chat", "command error");
		}

	}
}
