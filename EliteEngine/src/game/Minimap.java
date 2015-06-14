package game;

import entity.Entity;
import processing.core.PConstants;
import processing.core.PGraphics;
import shared.Helper;
import shared.ref;

public class Minimap {
	private static PGraphics graphics;

	private static float s;
	private static int w, h;
	private static int dw, dh;

	public static void setup() {
		w = ref.updater.map.width;
		h = ref.updater.map.height;
		s = ((w) < (h)) ? (180.0f / h) : (180.0f / w);
		dw = (int) ((180 - w * s) / 2);
		dh = (int) ((180 - h * s) / 2);
		graphics = ref.app.createGraphics((int) (w * s), (int) (h * s));
	}

	public static void update() {
		graphics.beginDraw();
		graphics.clear();
		graphics.pushMatrix();
		// graphics.translate(10, graphics.height - HUD.height + 10);
		graphics.scale(s);
		// graphics.translate(dw, dh);
		graphics.noStroke();

		graphics.image(ref.updater.map.textur, 0, 0, w, h);
		graphics.imageMode(PConstants.CENTER);
		graphics.rectMode(PConstants.CENTER);
		for (Entity e : ref.updater.entities) {
			e.drawOnMinimapUnder(graphics);
		}
		graphics.imageMode(PConstants.CORNER);
		graphics.rectMode(PConstants.CORNER);
		graphics.blendMode(PConstants.MULTIPLY);
		// graphics.image(ref.updater.map.fogOfWar, 0, 0, w, h);
		graphics.blendMode(PConstants.BLEND);
		graphics.noStroke();
		graphics.rectMode(PConstants.CENTER);
		for (Entity e : ref.updater.entities) {
			e.drawOnMinimap(graphics);
		}
		graphics.rectMode(PConstants.CORNER);
		graphics.fill(255, 50);
		graphics.rect(Helper.gridToX(0), Helper.gridToY(0), ref.app.width
				/ GameDrawer.zoom, (ref.app.height - HUD.height)
				/ GameDrawer.zoom * 2);
		graphics.popMatrix();
		graphics.endDraw();
		ref.app.image(graphics, 10 + dw, ref.app.height - HUD.height + 10 + dh);

	}
}
