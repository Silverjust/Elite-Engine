package game;

import entity.Entity;
import pathfinder.Graph;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import shared.Player;
import shared.ref;

public class Map {
	public Graph graph;

	public int width = 800, height = 900;
	public final int fogScale = 7;

	public PImage textur;
	public PImage collision;
	public PGraphics fogOfWar;

	public Map() {

	}

	public void loadImages() {
		textur = ImageHandler.load("", "mapTextur");
		collision = ImageHandler.load("", "mapColl");
		fogOfWar = ref.app.createGraphics(width / fogScale, height / 2
				/ fogScale, PConstants.P2D);
	}

	public void setup() {
		// PathHandler.makeGraph(graph, collision, 20, 20);
	}

	public void updateFogofWar(Player player) {
		fogOfWar.beginDraw();
		fogOfWar.clear();
		fogOfWar.background(80);
		fogOfWar.noStroke();
		fogOfWar.fill(200);
		for (Entity e : ref.updater.entities) {
			if (e.player == player) {
				e.drawSight(ref.updater);
			}
		}
		fogOfWar.endDraw();
	}
}
