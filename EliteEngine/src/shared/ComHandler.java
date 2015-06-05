package shared;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.naming.NoInitialContextException;

import main.ClientHandler;
import processing.core.PApplet;
import shared.Updater.GameState;
import entity.Entity;
import entity.Unit;
import game.GameUpdater;

public class ComHandler {

	public static void setup(GameUpdater UPDATER) {
		ref.updater = UPDATER;
	}

	public static void executeCom(String com) {
		String[] c = PApplet.splitTokens(com, " " + ClientHandler.endSymbol);

		try {
			byte b;
			int n;
			float x, y;
			Entity e;
			switch (c[0]) {
			case "<move":
				n = Integer.parseInt(c[1]);
				e = ref.updater.namedEntities.get(n);
				x = Float.parseFloat(c[2]);
				y = Float.parseFloat(c[3]);
				if (e != null) {
					System.out.println("remove move");
					// ((Unit) e).move(x, y);
				} else {
					throw new IllegalArgumentException("no entity found");
				}
				break;
			case "<hit":
				n = Integer.parseInt(c[1]);
				e = ref.updater.namedEntities.get(n);
				n = Integer.parseInt(c[2]);
				b = Byte.parseByte(c[3]);
				if (e != null) {
					e.hit(n, b);
				} else {
					throw new IllegalArgumentException("no entity found");
				}
				break;
			case "<heal":
				n = Integer.parseInt(c[1]);
				e = ref.updater.namedEntities.get(n);
				n = Integer.parseInt(c[2]);
				if (e != null) {
					e.heal(n);
				} else {
					throw new IllegalArgumentException("no entity found");
				}
				break;
			case "<tp":
				n = Integer.parseInt(c[1]);
				e = ref.updater.namedEntities.get(n);
				x = Float.parseFloat(c[2]);
				y = Float.parseFloat(c[3]);
				if (e != null) {
					((Unit) e).tp(x, y);
				} else {
					throw new IllegalArgumentException("no entity found");
				}
				break;
			case "<spawn":
				Class<?> clazz = Class.forName("entity.entities." + c[1]);
				Constructor<?> ctor = clazz.getConstructor(String[].class);
				e = (Entity) ctor.newInstance(new Object[] { c });
				ref.updater.toAdd.add(e);
				break;
			case "<execute":// who what info
				n = Integer.parseInt(c[1]);
				e = ref.updater.namedEntities.get(n);
				if (e != null) {
					e.exec(c);
				} else {
					throw new IllegalArgumentException("no entity found");
				}
				break;
			case "<remove":
				n = Integer.parseInt(c[1]);
				e = ref.updater.namedEntities.get(n);
				if (e != null) {
					ref.updater.toRemove.add(e);
				} else {
					throw new IllegalArgumentException("no entity found");
				}
				break;
			case "<give":
				Player p = ref.updater.player.get(c[1]);
				p.give(c[2], Integer.parseInt(c[3]));
				break;
			case "<say":
				ref.updater.write(c[1], c);
				break;

			// before game
			case "<identify":
				System.out.println("identifying " + ref.player.name);
				ClientHandler.send("<identifying "
						+ ClientHandler.identification + " " + ref.player.name);
				ClientHandler.send("<setNation " + ClientHandler.identification
						+ " " + ref.player.nation.toString());
				// TODO send color
				// nur an clienthandler
				break;
			case "<identifying":
				ref.preGame.addPlayer(c[1], c[2]);
				break;
			case "<setNation":
				ref.preGame.player.get(c[1]).nation = Nation.fromString(c[2]);
				break;
			case "<load":
				ref.preGame.startLoading();
				break;
			case "<ready":
				ref.preGame.player.get(c[1]).isReady = true;
				ref.loader.tryStartGame();
				break;
			case "<startGame":
				ref.loader.startGame();
				break;
			case "<pause":
				if (Boolean.valueOf(c[1])) {
					ref.updater.gameState = GameState.PAUSE;
				} else {
					ref.updater.gameState = GameState.PLAY;
				}
				break;
			case "<lost":
				p = ref.updater.player.get(c[1]);
				if (p == ref.player) {
					ref.updater.gameState = GameState.LOST;
				} else {
					ref.updater.gameState = GameState.WON;
				}
				break;
			default:
				System.err.println(com + " was not found");
				throw new NoInitialContextException("no command found");
			}
		} catch (IllegalArgumentException e) {
			// e.printStackTrace();
			// can be ignored
		} catch (ClassCastException e) {
			// e.printStackTrace();
			// can be ignored
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			// can be ignored
		} catch (Exception e) {
			System.err.println("com error in " + com);
			e.printStackTrace();
		}

	}
}
