package entity.gameAI;

import processing.core.PApplet;
import entity.Commander;
import entity.Entity;
import shared.Player;
import shared.User;
import shared.ref;

public abstract class GameAI extends User {

	public GameAI(String ip, String name) {
		super(ip, name + "AI");
	}

	public abstract void update();

	protected void spawn(Entity e, float x, float y) {
		if (e.canBeBought(player) && canBePlacedAt(x, y)) {
			System.out.println("testAliensAI.update() build "
					+ e.getClass().getSimpleName());
			e.buyFrom(player);
			ref.updater.send("<spawn " + e.getClass().getSimpleName() + " "
					+ player.getUser().ip + " " + x + " " + y);
		}
	}

	protected void spawn(Entity e, float xo, float yo, float dx, float dy, int r) {
		float x = xo + (dx - xo) / PApplet.dist(xo, yo, dx, dy) * (r);
		float y = yo + (dy - yo) / PApplet.dist(xo, yo, dx, dy) * (r);
		if (e.canBeBought(player) && canBePlacedAt(x, y)) {
			System.out.println("testAliensAI.update() build "
					+ e.getClass().getSimpleName());
			e.buyFrom(player);
			ref.updater.send("<spawn " + e.getClass().getSimpleName() + " "
					+ player.getUser().ip + " " + x + " " + y);
		}
	}

	protected void upgrade(Entity e, Entity e2) {
		if (e.canBeBought(player) && canBePlacedAt(e2.x, e2.y)) {
			System.out.println("testAliensAI.update() upgrade "
					+ e.getClass().getSimpleName());
			e.buyFrom(player);
			ref.updater.send("<spawn " + e.getClass().getSimpleName() + " "
					+ player.getUser().ip + " " + e2.x + " " + e2.y);
			ref.updater.send("<remove " + e2.number);
		}
	}

	protected void train(Entity toTrain, Entity trainer) {
		if (toTrain.canBeBought(player)
				&& trainer.getAnimation() == trainer.stand) {
			System.out.println("testAliensAI.update() train "
					+ toTrain.getClass().getSimpleName());
			toTrain.buyFrom(trainer.player);
			trainer.sendAnimation("train " + toTrain.getClass().getSimpleName());
		}
	}

	private boolean canBePlacedAt(float x, float y) {
		for (Entity e : player.visibleEntities) {
			if (e instanceof Commander && e.player == player
					&& e.isInRange(x, y, ((Commander) e).commandRange())) {
				return true;
			}
		}
		return false;
	}

	protected Player getWeakestEnemy() {
		/*
		 * Set<String> keys = ref.updater.player.keySet(); int random = (int)
		 * ref.app.random(keys .size()); return
		 * ref.updater.player.get(keys.toArray()[random]);
		 */
		Player enemy = null;
		for (String key : ref.updater.player.keySet()) {
			if (ref.updater.player.get(key).mainBuilding.isEnemyTo(player))
				enemy = ref.updater.player.get(key);
		}
		return enemy;
	}
}
