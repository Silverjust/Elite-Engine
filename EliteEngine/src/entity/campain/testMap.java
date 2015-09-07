package entity.campain;

import entity.Entity;
import shared.Helper.Timer;
import shared.ref;
import game.Map;
import game.MapCode;

public class testMap extends MapCode {
	public testMap(Map map) {
		super(map);
	}

	Timer rockTimer;

	@Override
	public void setup() {
		rockTimer = new Timer(10000);
	}

	@Override
	public void onGameStart() {
		rockTimer.startCooldown();
	}

	@Override
	public void update() {
		if (rockTimer.isNotOnCooldown()) {
			int i = (int) ref.app.random(ref.updater.entities.size());
			Entity e = ref.updater.namedEntities.get(i);
			if (e != null) {
				System.out.println("testMap.update()");
				ref.updater
						.send("<spawn Asteroid " + 2 + " " + e.x + " " + e.y);
				rockTimer.startCooldown();
			}
		}
	}
}
