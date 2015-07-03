package shared;

import java.util.ArrayList;
import java.util.HashMap;

import entity.Entity;
import game.Map;

public abstract class Updater {
	public HashMap<Integer, Entity> namedEntities = new HashMap<Integer, Entity>();
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<Entity> toAdd = new ArrayList<Entity>();
	public ArrayList<Entity> toRemove = new ArrayList<Entity>();
	public ArrayList<Entity> selected = new ArrayList<Entity>();

	public Map map;

	public HashMap<String, Player> player;
	public Player neutral;

	public GameState gameState = GameState.PLAY;

	public abstract void update();

	public abstract void write(String ip, String[] c);

	public boolean arePlayerReady() {
		boolean b = true;
		for (String key : player.keySet()) {
			if (!player.get(key).isReady)
				b = false;
		}
		return b;
	}

	public abstract void send(String string);

	public enum GameState {
		PLAY, PAUSE, LOST, WON
	}

	public static class Time {

		private static int pauseStart;
		private static int pauseTime;

		public static void startPause() {
			if (ref.updater.gameState == GameState.PLAY) {
				ref.updater.gameState = GameState.PAUSE;
				pauseStart = ref.app.millis();
			}
		}

		public static void endPause() {
			if (ref.updater.gameState != GameState.PLAY) {
				ref.updater.gameState = GameState.PLAY;
				pauseTime += ref.app.millis() - pauseStart;
				pauseStart = 0;
			}
		}

		public static int getMillis() {
			if (pauseStart == 0)
				return ref.app.millis() - pauseTime;
			else
				return ref.app.millis()
						- (pauseTime + ref.app.millis() - pauseStart);
		}
	}

	public void startPause() {
	}

	public void endPause() {
	}
}
