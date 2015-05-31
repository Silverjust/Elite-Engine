package game;

import entity.Entity;
import g4p_controls.G4P;
import g4p_controls.GControlMode;
import g4p_controls.GCustomSlider;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import shared.ref;

public class SelectionDisplay {
	static PGraphics graphic;
	static int x, y, h, w;
	static int c = 15, r = PApplet.ceil(ref.updater.selected.size());
	static int iconSize = 40;
	static float yOffset;
	public static GCustomSlider selectedEntitiesSlider;
	static PFont font;

	public static void setup() {
		x = 300;
		y = 30 + ref.app.height - HUD.height;
		w = (iconSize + 10) * c;
		h = HUD.height - 35;
		graphic = ref.app.createGraphics(w, h, PApplet.JAVA2D);
		font = ref.app.createFont("Georgia", 32);
		graphic.textFont(font);
		selectedEntitiesSlider = new GCustomSlider(ref.app, x + w + 20, y + 10,
				h - 20, 20, ref.player.nation.toString());
		selectedEntitiesSlider.setRotation(PConstants.TAU / 4,
				GControlMode.CORNER);
		selectedEntitiesSlider.setLimits(0, 0, 1);
		selectedEntitiesSlider.setNumberFormat(G4P.DECIMAL, 2);
		// selectedEntitiesSlider.setStickToTicks(true);
		selectedEntitiesSlider.setVisible(false);
		/*
		 * selectedEntitiesSlider.addEventHandler(ref.app,
		 * "custom_slider1_change1");
		 */
	}

	public static void update() {
		r = PApplet.ceil(ref.updater.selected.size() / c) + 1;
		if (r - 3 <= 0) {
			selectedEntitiesSlider.setLimits(0, 0);
			selectedEntitiesSlider.setVisible(false);
		} else {
			selectedEntitiesSlider.setVisible(true);
			selectedEntitiesSlider.setLimits(0, r - 3);
		}
		float f = selectedEntitiesSlider.getValueF();
		yOffset = f * (iconSize + 10) /*+ (r != 3 ? 10 * (f / (r - 3)) : 0)*/;
		// System.out.println(r + " " + i + " "
		// + (r > 0 ? 10 * (f/ (r - 4)) : 0);
		graphic.beginDraw();
		graphic.clear();
		for (Entity e : ref.updater.selected) {
			int xe = ref.updater.selected.indexOf(e) % c;
			int ye = (ref.updater.selected.indexOf(e) - xe) / c;
			graphic.fill(255);
			graphic.rect((iconSize + 10) * xe, -yOffset + (iconSize + 10) * ye,
					iconSize, iconSize);
			e.drawIcon(graphic, (iconSize + 10) * xe, -yOffset + (iconSize + 10)
					* ye, iconSize);
			graphic.fill(0);
			graphic.text(e.number, 5 + (iconSize + 10) * xe, 30 - yOffset
					+ (iconSize + 10) * ye);
		}
		graphic.endDraw();
		ref.app.image(graphic, x, y);
	}
}
