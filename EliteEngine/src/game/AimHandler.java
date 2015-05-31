package game;

import processing.core.PConstants;
import processing.core.PImage;
import shared.ref;

public class AimHandler {

	private static PImage buildImg;
	static Aim aim;

	public static void loadImages() {
		buildImg = ImageHandler.load("", "build");
	}

	public static void setup() {
	}

	public static void update() {
		if (aim != null) {
			aim.update();
		}
	}

	public static void execute() {
		if (aim != null) {
			aim.execute();
			//abort();
		}
	}

	public static void setAim(Aim a) {
		end();
		aim = a;
		setCursor(aim.getCursor());
	}

	public static void end() {
		aim = null;
		setCursor(Cursor.ARROW);
	}

	public static boolean isAiming() {
		return aim != null;
	}

	public static void setCursor(Cursor c) {
		switch (c) {
		case ARROW:
			ref.app.cursor(PConstants.ARROW);
			break;
		case BUILD:
			ref.app.cursor(buildImg, 15, 15);
			break;
		default:
			break;
		}
	}

	enum Cursor {
		ARROW, BUILD, SHOOT, SELECT
	}
}
