package game;

import entity.Entity;
import pathfinder.Graph;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.data.JSONObject;
import shared.ContentListHandler;
import shared.Player;
import shared.ref;

public class Map {
	public Graph graph;

	public int width = 800, height = 900;
	public final int fogScale = 7;

	public PImage textur;
	public PImage collision;
	public PGraphics fogOfWar;

	public JSONObject mapData;

	public Map(String map) {

		try {
			mapData = ref.app.loadJSONObject("data/"
					+ ContentListHandler.getMapContent().getString(map)
					+ ".json");
		} catch (Exception e) {
			System.err.println(map + " could not be loaded");
			e.printStackTrace();
		}
	}

	public void loadImages() {
		try {
			textur = ImageHandler.load("", mapData.getString("texture"));
			collision = ImageHandler.load("", mapData.getString("coll"));
		} catch (Exception e) {
			System.err.println(mapData.getString("texture") + " "
					+ mapData.getString("coll"));
			e.printStackTrace();
		}
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
