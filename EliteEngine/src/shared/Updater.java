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
}
