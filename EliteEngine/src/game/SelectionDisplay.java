package game;

import java.awt.font.TextAttribute;

import entity.Entity;
import entity.Informing;
import g4p_controls.G4P;
import g4p_controls.GControlMode;
import g4p_controls.GCustomSlider;
import g4p_controls.GLabel;
import g4p_controls.StyledString;
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
	private static int col = ref.app.color(100, 255, 100);
	static Informing informing;
	static GLabel infoStats;

	public static void setup() {
		x = 300;
		y = 30 + ref.app.height - HUD.height;
		w = (iconSize + 10) * c;
		h = HUD.height - 35;
		graphic = ref.app.createGraphics(w, h, PApplet.JAVA2D);
		font = ref.app.createFont("Aharoni Fett", 40);
		graphic.textFont(font);
		graphic.textSize(20);
		// graphic.textLeading(100);
		selectedEntitiesSlider = new GCustomSlider(ref.app, x + w + 20, y + 10,
				h - 20, 20, ref.player.getNation().toString());
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
		// infoStats = new GLabel(ref.app, x + iconSize * 10, y, w, h);
		// infoStats.setFont(new Font("Aharoni Fett", Font.BOLD, 18));
		// infoStats.setTextAlign(GAlign.LEFT, GAlign.TOP);
	}

	public static void update() {
		graphic.beginDraw();
		graphic.clear();
		if (informing == null && ref.updater.selected.size() == 1)
			informing = ref.updater.selected.get(0);
		if (informing != null) {
			graphic.fill(200);
			graphic.rect(0, 0, iconSize * 4, iconSize * 4);
			informing.drawIcon(graphic, 0, 0, iconSize * 4);
			graphic.fill(col);
			String descr = informing.getDesription().replaceAll("�", " \n");
			graphic.text(descr, iconSize * 5, ref.app.textAscent()
					* ref.textScale + 10);
			String stats = informing.getStatistics().replaceAll("�", " \n");
			graphic.text(stats, iconSize * 10, ref.app.textAscent()
					* ref.textScale + 10);

			// StyledString styledStats = style(stats);
			// infoStats.setStyledText(styledStats);
		} else {
			r = PApplet.ceil(ref.updater.selected.size() / c) + 1;
			if (r - 3 <= 0) {
				selectedEntitiesSlider.setLimits(0, 0);
				selectedEntitiesSlider.setVisible(false);
			} else {
				selectedEntitiesSlider.setVisible(true);
				selectedEntitiesSlider.setLimits(0, r - 3);
			}
			float f = selectedEntitiesSlider.getValueF();
			yOffset = f * (iconSize + 10) /* + (r != 3 ? 10 * (f / (r - 3)) : 0) */;
			// System.out.println(r + " " + i + " "
			// + (r > 0 ? 10 * (f/ (r - 4)) : 0);

			for (Entity e : ref.updater.selected) {
				int xe = ref.updater.selected.indexOf(e) % c;
				int ye = (ref.updater.selected.indexOf(e) - xe) / c;
				graphic.fill(200);
				graphic.rect((iconSize + 10) * xe, -yOffset + (iconSize + 10)
						* ye, iconSize, iconSize);
				e.drawIcon(graphic, (iconSize + 10) * xe, -yOffset
						+ (iconSize + 10) * ye, iconSize);
				graphic.fill(0);
				graphic.text(e.number, 2 + (iconSize + 10) * xe, iconSize - 2
						- yOffset + (iconSize + 10) * ye);
			}

		}
		graphic.endDraw();
		ref.app.image(graphic, x, y);
	}

	@SuppressWarnings("unused")
	private static StyledString style(String stats) {
		StyledString styledStats = new StyledString(stats);
		int i = stats.indexOf("kerit");
		if (i != -1)
			styledStats.addAttribute(TextAttribute.FOREGROUND,
					ref.app.color(50), i, i + 1);
		i = stats.indexOf("pax");
		if (i != -1)
			styledStats.addAttribute(TextAttribute.FOREGROUND,
					ref.app.color(255, 200, 0), i, i + 1);
		i = stats.indexOf("arcanum");
		if (i != -1)
			styledStats.addAttribute(TextAttribute.FOREGROUND,
					ref.app.color(0, 100, 255), i, i + 1);
		i = stats.indexOf("prunam");
		if (i != -1)
			styledStats.addAttribute(TextAttribute.FOREGROUND,
					ref.app.color(255, 100, 0), i, i + 1);
		return styledStats;
	}

	public static void setIforming(Informing i) {
		informing = i;
	}
}
