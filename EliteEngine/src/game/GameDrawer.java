package game;

import processing.core.PConstants;
import shared.ref;
import entity.Entity;

public class GameDrawer {

	public static float xMapOffset, yMapOffset;
	public static float zoom = 2f;

	public static boolean commandoutput;
	public static boolean godeye;
	public static boolean godhand;

	public static MouseSelection mouseSelection;

	public static void loadImages() {
		ref.updater.map.loadImages();
	}

	public static void setup() {

		HUD.setup();
		AimHandler.setup();

		commandoutput = true;

	}

	public static void update() {
		ref.app.background(255);
		ref.app.pushMatrix();
		ref.app.translate(xMapOffset, yMapOffset);
		ref.app.scale(zoom);
		ref.app.stroke(0);

		ref.app.image(ref.updater.map.textur, 0, 0, ref.updater.map.width,
				ref.updater.map.height);
		ref.updater.map.updateFogofWar(ref.player);
		ref.app.blendMode(PConstants.MULTIPLY);
		ref.app.image(ref.updater.map.fogOfWar, 0, 0, ref.updater.map.width,
				ref.updater.map.height);
		ref.app.blendMode(PConstants.BLEND);
		ref.app.imageMode(PConstants.CENTER);
		ref.app.rectMode(PConstants.CENTER);
		for (Entity e : ref.player.visibleEntities) {
			e.renderUnder();
		}
		for (Entity e : ref.player.visibleEntities) {
			e.renderGround();
		}
		for (Entity e : ref.player.visibleEntities) {
			e.renderAir();
		}

		for (Entity e : ref.player.visibleEntities) {
			e.display();
		}

		AimHandler.update();
		ref.app.rectMode(PConstants.CORNER);
		if (mouseSelection != null)
			mouseSelection.disp();
		ref.app.popMatrix();
		ref.app.imageMode(PConstants.CORNER);

		HUD.update();
		if (((GameUpdater) ref.updater).pause) {
			ref.app.fill(100,100);
			ref.app.rect(0, 0, ref.app.width, ref.app.height);
		}
		// AimHandler.update();
		// app.image(render, 0, 0);
	}

}
