package server;

import shared.Mode;
import shared.Player;
import shared.PreGame;
import shared.ref;

public class ServerPreGame extends PreGame {
	
	@Override
	public void update() {

	}

	@Override
	public void startLoading() {
		ref.loader = new ServerLoader();
		((ServerApp) ref.app).mode = Mode.LADESCREEN;
	}

	@Override
	public void addPlayer(String ip, String name) {
		if (!player.containsKey(ip)) {
			Player p = Player.createPlayer(ip, name);
			player.put(ip, p);
		} else if (player.get(ip).name.equals(ip)) {
			player.get(ip).name = name;
		}
	}

}
