package server;

import main.ClientHandler;
import entity.Entity;
import entity.aliens.AlienKaserne;
import processing.core.PApplet;
import shared.Mode;
import shared.Updater;
import shared.ref;

public class Protocol {
	static int protocollNumber;
	static String protocollText = "";

	static Class<?> clazz = AlienKaserne.class;

	public static void collectInfos() {
		if (((ServerApp) ref.app).mode == Mode.GAME) {
			for (Entity e : ref.updater.entities) {
				if (e.getClass().equals(clazz)) {
					addInfo("0 " + e.number + " :"
							+ e.getAnimation().toString());
					addInfo("0 " + e.number + " :" + e.player.kerit);
				}
			}
		}
	}

	public static void filterComs(String info, String s) {
		String[] c = PApplet.splitTokens(s, " " + ClientHandler.endSymbol);
		if (c[0].equals("<execute")
				&& ref.updater.namedEntities.get(Integer.parseInt(c[1]))
						.getClass().equals(clazz))
			addInfo(info + s);

		if (c[0].equals("<give") && Integer.parseInt(c[3]) < 0)
			addInfo(info + s);

		if (!c[0].equals("<execute") && !c[0].equals("<give"))
			addInfo(info + s);
	}

	private static void addInfo(String s) {
		protocollText += getTime() + s + "\n";
		System.out.println(getTime() + s);

	}

	static String getTime() {
		return " m"
				+ min()
				+ " s"
				+ (min() == 0 ? sec() : (sec() % (min() * 60)))
				+ " ms"
				+ (sec() == 0 ? Updater.Time.getMillis() : (Updater.Time
						.getMillis() % (sec() * 1000)) + " :");
	}

	static int sec() {
		return PApplet.floor(Updater.Time.getMillis() / 1000);
	}

	static int min() {
		return PApplet.floor(Updater.Time.getMillis() / 60000);
	}
}
