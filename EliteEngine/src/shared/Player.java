package shared;

import java.util.ArrayList;

import processing.core.PGraphics;
import entity.Entity;
import entity.MainBuilding;
import game.GameDrawer;

public class Player {
	public String ip;
	public String name;
	public int color;
	public Nation nation;
	public int kerit, pax, arcanum, prunam;

	public ArrayList<Entity> visibleEntities = new ArrayList<Entity>();
	public boolean online;
	public boolean isReady;
	public MainBuilding mainBuilding;

	public static Player createNeutralPlayer() {
		Player p = new Player();
		p.online = true;
		p.color = ref.app.color(150);
		p.nation = Nation.NEUTRAL;
		return p;
	}

	public static Player createPlayer(String ip, String name) {
		Player p = new Player();
		p.ip = ip;
		p.name = name;
		p.kerit = 200;
		p.online = true;
		return p;
	}

	private Player() {
	}

	public void display(PGraphics gr, int x, int y) {
		gr.fill(255);
		gr.rect(x, y, 280, 20);
		gr.fill(0);
		if (nation != null)
			gr.text(nation.toString(), x, y + ref.app.textAscent()
					* ref.textScale);
		gr.text(name, x + 70, y + ref.app.textAscent() * ref.textScale);
	}

	@Override
	public String toString() {
		if (online) {
			return "[" + name + "]";
		}
		return "offline:[" + name + "]";
	}

	public void give(String resource, int amount) {
		switch (resource) {
		case "kerit":
			kerit += amount;
			break;
		case "pax":
			pax += amount;
			break;
		case "arcanum":
			arcanum += amount;
			break;
		case "prunam":
			prunam += amount;
			break;
		default:
			break;
		}

	}

	public boolean canBy(int kerit, int pax, int arcanum, int prunam) {
		boolean buyable = true;
		if (!GameDrawer.nocosts) {
			if (kerit != 0 && kerit > this.kerit)
				buyable = false;
			if (pax != 0 && pax > this.pax)
				buyable = false;
			if (arcanum != 0 && arcanum > this.arcanum)
				buyable = false;
			if (prunam != 0 && prunam > this.prunam)
				buyable = false;
		}
		return buyable;
	}

}
