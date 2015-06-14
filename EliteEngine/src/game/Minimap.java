package game;

import entity.Entity;
import processing.core.PConstants;
import processing.core.PGraphics;
import shared.Helper;
import shared.ref;

public class Minimap {
	static float s = 0.2f;
	private static PGraphics graphics;
	private static int w, h;
	private static float ss;
	private static int dw;
	private static int dh;

	public static void setup() {
		w = ref.updater.map.width;
		h = ref.updater.map.height;

		ss = ((w) < (h)) ? (180 / h) : (180 / w);

		dw = (int) ((180 / ss - w) / 2);
		dh = (int) ((180 / ss - h) / 2);
		System.out.println(w + " " + h);
		graphics = ref.app.createGraphics((int) (w * ss), (int) (h * ss));
	}

	public static void update() {
		graphics.beginDraw();
		graphics.clear();
		// graphics.pushMatrix();
		// graphics.translate(10, graphics.height - HUD.height + 10);
		// graphics.scale(ss);
		// graphics.translate(dw, dh);
		graphics.noStroke();

		graphics.image(ref.updater.map.textur, 0, 0, ref.updater.map.width,
				ref.updater.map.height);
		graphics.imageMode(PConstants.CENTER);
		graphics.rectMode(PConstants.CENTER);
		for (Entity e : ref.updater.entities) {
			e.drawOnMinimapUnder(graphics);
		}
		graphics.imageMode(PConstants.CORNER);
		graphics.rectMode(PConstants.CORNER);

		graphics.blendMode(PConstants.MULTIPLY);
		graphics.image(ref.updater.map.fogOfWar, 0, 0, ref.updater.map.width,
				ref.updater.map.height);

		graphics.blendMode(PConstants.BLEND);
		// graphics.stroke(255);
		graphics.rectMode(PConstants.CENTER);
		for (Entity e : ref.updater.entities) {
			e.drawOnMinimap(graphics);
		}
		graphics.rectMode(PConstants.CORNER);
		graphics.fill(255, 50);
		graphics.rect(Helper.gridToX(0), Helper.gridToY(0), graphics.width
				/ GameDrawer.zoom, (graphics.height - HUD.height)
				/ GameDrawer.zoom * 2);
		graphics.strokeWeight(1);
		graphics.popMatrix();
		graphics.endDraw();
		ref.app.image(graphics, 10 + dw, ref.app.height - HUD.height + 10 + dh);

	}
}
