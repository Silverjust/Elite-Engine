package entity.campain;

import game.HUD;
import game.Map;
import game.MapCode;
import main.preGame.MainPreGame;
import shared.Nation;
import shared.ref;

public class MapRobots1 extends MapCode {
	public MapRobots1(Map map) {
		super(map);
	}

	@Override
	public void setup() {
		MainPreGame.addPlayer("Scientists", Nation.SCIENTISTS);
	}

	@Override
	public void onGameStart() {
		String name = "this";
		ref.updater.send("<give " + ref.player.getUser().ip + " kerit " + 200);
		HUD.chat.println(name, "todo:find and select Mainbuilding");
	}

	@Override
	public void update() {

	}
}
