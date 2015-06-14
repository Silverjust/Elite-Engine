package game;

import processing.core.PConstants;
import shared.Updater.GameState;
import shared.ref;
import entity.Entity;

public class GameDrawer {

	public static float xMapOffset, yMapOffset;
	public static float zoom = 4f;

	public static boolean commandoutput;

	public static boolean godeye;
	public static boolean godhand;
	public static boolean nocosts;

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
				ref.updater.map.height / 2);
		ref.app.imageMode(PConstants.CENTER);
		ref.app.rectMode(PConstants.CENTER);
		for (Entity e : ref.updater.entities) {
			e.renderTerrain();
		}
		ref.app.imageMode(PConstants.CORNER);
		ref.app.rectMode(PConstants.CORNER);
		ref.updater.map.updateFogofWar(ref.player);
		ref.app.blendMode(PConstants.MULTIPLY);
		ref.app.image(ref.updater.map.fogOfWar, 0, 0, ref.updater.map.width,
				ref.updater.map.height / 2);// hä?
		ref.app.blendMode(PConstants.BLEND);
		ref.app.imageMode(PConstants.CENTER);
		ref.app.rectMode(PConstants.CENTER);
		for (Entity e : ref.updater.entities) {
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

		if (ref.updater.gameState != GameState.PLAY) {
			ref.app.fill(100, 100);
			ref.app.rect(0, 0, ref.app.width, ref.app.height);
			String s = "bla";
			if (ref.updater.gameState == GameState.PAUSE) {
				s = "PAUSE";
			} else if (ref.updater.gameState == GameState.WON) {
				s = "WON";
			} else if (ref.updater.gameState == GameState.LOST) {
				s = "LOST";
			}
			ref.app.fill(255);
			ref.app.text(s, ref.app.width / 2 - ref.app.textWidth(s) / 2,
					ref.app.height / 2);
		}
		// AimHandler.update();
		// app.image(render, 0, 0);
	}

}
