package game;

import ddf.minim.AudioPlayer;
import g4p_controls.G4P;
import g4p_controls.GCScheme;
import processing.core.PImage;
import shared.Menu;
//import shared.Menu;
import shared.ref;

public class HUD {

	static MouseSelection mouseSelection;
	static PImage keritImg, paxImg, arcImg, prunImg;
	static PImage overlay;
	public static int height = 200;
	public static ActivesGrid activesGrid;

	public static AudioPlayer sound;

	public static Menu menue;
	public static Chat chat;

	public static void loadImages() {
		keritImg = ImageHandler.load("", "Kerit");
		paxImg = ImageHandler.load("", "Pax");
		arcImg = ImageHandler.load("", "Arcanum");
		prunImg = ImageHandler.load("", "Prunam");
		if (ref.player != null) {
			overlay = ImageHandler.load(
					ref.player.getNation().toString() + "/", "overlay");
			sound = ref.minim.loadFile(ref.player.getNation().toString() + "/"
					+ ref.player.getNation().toString() + ".mp3");
			/** soundfiles are only tests and will be removed */
		}
	}

	public static void setup() {
		setupG4P();
		chat = new Chat();
		SelectionDisplay.setup();
		Minimap.setup();
		GroupHandler.setup();
		activesGrid = new ActivesGrid();
		boolean b = true;
		if (b) {
			sound.play();
			sound.setGain(-25);
			// sound.loop();
		}
	}

	static void setupG4P() {
		G4P.setGlobalColorScheme(8);
		G4P.changeCursor(false);
		GCScheme.setScheme(8, 0, ref.app.color(0));
		GCScheme.setScheme(8, 6, ref.app.color(0, 100));
		GCScheme.setScheme(8, 7, ref.app.color(0, 50));
		GCScheme.setScheme(8, 12, ref.app.color(255));
	}

	public static void update() {
		SoundHandler.update();
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
		ImageHandler.drawImage(ref.app, overlay, 0, ref.app.height - height,
				ref.app.width, height);

		SelectionDisplay.update();
		Minimap.update();
		GroupHandler.update();
		activesGrid.update();
	}

	public static String[] buttonImageFilename() {
		if (ref.player == null || ref.player.getNation() == null)
			return null;
		return new String[] {
				ref.player.getNation().toString() + "/button_normal.png",
				ref.player.getNation().toString() + "/button_mouseover.png",
				ref.player.getNation().toString() + "/button_clicked.png" };
	}

	public static void dispose() {
		try {
			if (activesGrid != null)
				activesGrid.dispose();
			GroupHandler.dispose();
			ImageHandler.dispose();
			if (sound != null)
				sound.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
