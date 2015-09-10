package shared;

import java.util.ArrayList;

import entity.Entity;
import entity.MainBuilding;
import game.GameDrawer;

public class Player {
	public int color;
	public int kerit, pax, arcanum, prunam;

	public ArrayList<Entity> visibleEntities = new ArrayList<Entity>();
	public User user;
	public MainBuilding mainBuilding;

	public static Player createNeutralPlayer() {
		Player p = new Player();
		p.user = new User("", "neutral");
		p.user.online = true;
		p.color = ref.app.color(150);
		p.setNation(Nation.NEUTRAL);
		return p;
	}

	public static Player createPlayer(User user) {
		Player p = new Player();
		p.user = user;
		p.kerit = 200;
		return p;
	}

	private Player() {
	}

	@Override
	public String toString() {
		if (user.online) {
			return "[" + user.name + "]";
		}
		return "offline:[" + user.name + "]";
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

	public Nation getNation() {
		return user.nation;
	}

	public void setNation(Nation nation) {
		user.nation = nation;
	}

}
