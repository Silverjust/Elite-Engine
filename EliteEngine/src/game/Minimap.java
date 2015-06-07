package game;

import entity.Entity;
import processing.core.PConstants;
import shared.Helper;
import shared.ref;

public class Minimap {
	static float s = 0.2f;

	public static void setup() {
	}

	public static void update() {
		float w = ref.updater.map.width;
		float h = ref.updater.map.height;

		float ss = ((w) < (h)) ? (180 / h) : (180 / w);

		float dw = (180 / ss - w) / 2;
		float dh = (180 / ss - h) / 2;

		ref.app.pushMatrix();
		ref.app.translate(10, ref.app.height - HUD.height + 10);
		ref.app.scale(ss);
		ref.app.translate(dw, dh);
		ref.app.noStroke();

		ref.app.image(ref.updater.map.textur, 0, 0, ref.updater.map.width,
				ref.updater.map.height);
		ref.app.blendMode(PConstants.MULTIPLY);
		ref.app.image(ref.updater.map.fogOfWar, 0, 0, ref.updater.map.width,
				ref.updater.map.height);

		ref.app.blendMode(PConstants.BLEND);
		// ref.app.stroke(255);
		ref.app.rectMode(PConstants.CENTER);
		for (Entity e : ref.updater.entities) {
			e.drawOnMinimap();
		}
		ref.app.rectMode(PConstants.CORNER);
		ref.app.fill(255, 50);
		ref.app.rect(Helper.gridToX(0), Helper.gridToY(0), ref.app.width
				/ GameDrawer.zoom, (ref.app.height - HUD.height)
				/ GameDrawer.zoom * 2);
		ref.app.strokeWeight(1);
		ref.app.popMatrix();

	}
}
