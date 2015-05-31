package shared;

import java.util.ArrayList;

import processing.core.PGraphics;
import entity.Entity;

public class Player {
	public String ip;
	public String name;
	public int playerColor;
	public Nation nation;
	public int kerit, pax, arcanum, prunam;

	public ArrayList<Entity> visibleEntities = new ArrayList<Entity>();
	public boolean online;
	public boolean isReady;

	public static Player createNeutralPlayer() {
		Player p = new Player();
		p.online = true;
		p.playerColor = ref.app.color(150);
		p.nation = Nation.NEUTRAL;
		return p;
	}

	public static Player createPlayer(String ip, String name) {
		Player p = new Player();
		p.ip = ip;
		p.name = name;
		p.online = true;
		p.kerit = 100;
		return p;
	}

	private Player() {
	}

	public void display(PGraphics gr,int x, int y) {
		gr.fill(255);
		gr.rect(x, y, 280, 20);
		gr.fill(0);
		if (nation!=null)
		gr.text(nation.toString(), x, y + ref.app.textAscent()*ref.textScale);
		gr.text(name, x + 70, y + ref.app.textAscent()*ref.textScale);
	}

	@Override
	public String toString() {
		if (online) {
			return "[" + name + "]";
		}
		return "offline:[" + name + "]";
	}

}
