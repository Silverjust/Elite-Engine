package shared;

import java.util.ArrayList;

import entity.Entity;
import game.GameDrawer;

public class Helper {
	@Deprecated
	public static String nationToString(Nation n) {
		String S = null;
		switch (n) {
		case AHNEN:
			S = "ahnen";
			break;
		case ALIENS:
			S = "aliens";
			break;
		case ROBOTS:
			S = "robots";
			break;
		case SCIENTISTS:
			S = "scientists";
			break;
		case HUMANS:
			S = "humans";
			break;
		case NEUTRAL:
			S = "neutral";
			break;
		}
		return S;
	}

	public static byte getDirection(float x, float y, float tx, float ty) {
		float a = (float) Math.toDegrees(Math.atan2(y - ty, x - tx));
		a += 112.5;
		if (a < 0) {
			a += 360;
		}
		byte b = (byte) (a * 8 / 360);
		return (0 <= b && b <= 8) ? b : 0;
	}

	public static boolean StringToBoolean(String S) {
		boolean b = false;
		if (S.equals("true")) {
			b = true;
		} else if (S.equals("false")) {
			b = false;
		} else {
			throw new IllegalArgumentException("String is no boolean");
		}
		return b;
	}

	public static boolean isOver(float x, float y, float x1, float y1,
			float x2, float y2) {
		boolean b = x1 <= x && x <= x2 && y1 <= y && y <= y2;
		return b;
	}

	public static boolean isBetween(float x, float y, float x1, float y1,
			float x2, float y2) {
		boolean b = (x1 < x2 ? (x1 <= x && x <= x2) : (x2 <= x && x <= x1))
				&& (y1 < y2 ? (y1 <= y && y <= y2) : (y2 <= y && y <= y1));
		return b;
	}

	public static boolean isMouseOver(float x1, float y1, float x2, float y2) {
		boolean b = x1 <= ref.app.mouseX && ref.app.mouseX <= x2
				&& y1 <= ref.app.mouseY && ref.app.mouseY <= y2;
		return b;
	}

	public static float gridToX(float x) {
		return ((x - GameDrawer.xMapOffset) / GameDrawer.zoom);
	}

	public static float gridToY(float y) {
		return ((y - GameDrawer.yMapOffset) / GameDrawer.zoom * 2);
	}

	public static String nameToIP(String name) {
		Player p;
		p = ref.updater.player.get(name);
		if (p != null)
			return p.ip;// ip from ip
		for (String key : ref.updater.player.keySet()) {
			if (ref.updater.player.get(key).name.equalsIgnoreCase(name))
				return ref.updater.player.get(key).ip;// ip from name
		}
		try {
			String[] a = ref.updater.player.keySet().toArray(
					new String[ref.updater.player.keySet().size()]);
			return ref.updater.player.get(a[Integer.parseInt(name)]).ip;
			// ip from number
		} catch (Exception e) { // not a number
		}
		return ref.player.ip; // ip from this player
	}

	public static String ipToName(String ip) {
		String name = ref.updater.player.get(ip).name;
		if (name != null)
			return name;
		return ip;
	}

	public static String secureInput(String in) {
		in.replace('<', ' ');// gegen command injektion
		in.replace('>', ' ');
		return in;
	}

	public static boolean listContainsInstanceOf(Class<?> c,
			ArrayList<Entity> arrlist) {
		if (c == null) {
			return true;
		}
		for (Entity e : arrlist) {
			if (c.isAssignableFrom(e.getClass())) {
				return true;
			}

		}
		return false;
	}

	public float fontHeight() {
		return ref.app.textAscent() * ref.textScale;
	}
}
