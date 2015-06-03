package game;

import g4p_controls.G4P;
import g4p_controls.GCScheme;
import processing.core.PImage;
import shared.ref;

public class HUD {

	static MouseSelection mouseSelection;
	static PImage keritImg, paxImg, arcImg, prunImg;
	public static int height = 200;
	public static ActivesGrid activesGrid;

	public static String[] buttonImageFilename = ref.player != null ? new String[] {
			ref.player.nation.toString() + "/button_normal.png",
			ref.player.nation.toString() + "/button_mouseover.png",
			ref.player.nation.toString() + "/button_clicked.png" }
			: null;

	public static void loadImages() {
		keritImg = ImageHandler.load("", "Kerit");
		paxImg = ImageHandler.load("", "Pax");
		arcImg = ImageHandler.load("", "Arcanum");
		prunImg = ImageHandler.load("", "Prunam");
	}

	public static void setup() {
		G4P.setGlobalColorScheme(8);// TODO GUI setup muss woanders hin
		G4P.changeCursor(false);
		Chat.setup();
		SelectionDisplay.setup();
		Minimap.setup();
		GroupHandler.setup();
		activesGrid = new ActivesGrid();

		GCScheme.setScheme(8, 0, ref.app.color(0));
		GCScheme.setScheme(8, 6, ref.app.color(0, 100));
		GCScheme.setScheme(8, 7, ref.app.color(0, 50));
		GCScheme.setScheme(8, 12, ref.app.color(255));

	}

	public static void update() {
		ref.app.textSize(40);
		ref.app.fill(255);
		ref.app.image(keritImg, ref.app.width - 100, 10);
		ref.app.text(ref.player.kerit,
				ref.app.width - ref.app.textWidth(ref.player.kerit + "") - 100,
				ref.app.textAscent() + 15 + 10);
		ref.app.image(paxImg, ref.app.width - 100, 10 + 64);
		ref.app.text(ref.player.pax,
				ref.app.width - ref.app.textWidth(ref.player.pax + "") - 100,
				ref.app.textAscent() + 15 + 10 + 64);
		ref.app.image(arcImg, ref.app.width - 100, 10 + 64 * 2);
		ref.app.text(ref.player.arcanum,
				ref.app.width - ref.app.textWidth(ref.player.arcanum + "")
						- 100, ref.app.textAscent() + 15 + 10 + 64 * 2);
		ref.app.image(prunImg, ref.app.width - 100, 10 + 64 * 3);
		ref.app.text(
				ref.player.prunam,
				ref.app.width - ref.app.textWidth(ref.player.prunam + "") - 100,
				ref.app.textAscent() + 15 + 10 + 64 * 3);

		ref.app.fill(ref.app.color(255));
		ref.app.rect(0, ref.app.height - height, ref.app.width, height);
		// TODO wünderschönes overlay bild

		SelectionDisplay.update();
		Minimap.update();
		GroupHandler.update();
		activesGrid.update();
	}

}
