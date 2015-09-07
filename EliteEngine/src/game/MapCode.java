package game;

import entity.Entity;

public abstract class MapCode {
	protected Map map;

	public MapCode(Map map) {
		this.map = map;
	}

	public void setup() {
	}

	public void onGameStart() {// gamestart
	}

	public void onEntitySpawn(Entity e) {// gamestart
	}

	public void update() {
	}

	public void onEnd() {// gameend
	}

	public void dispose() {
	}
}
