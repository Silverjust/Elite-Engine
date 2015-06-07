package server;

import shared.ContentListHandler;
import shared.Mode;
import shared.Player;
import shared.PreGame;
import shared.ref;

public class ServerPreGame extends PreGame {

	public ServerPreGame() {
		ContentListHandler.load();
		int i = ContentListHandler.getMapContent().size();

		@SuppressWarnings("unchecked")
		String[] intNames = (String[]) ContentListHandler.getMapContent()
				.keys().toArray(new String[i]);
		map = intNames[0];

	}

	@Override
	public void update() {

	}

	@Override
	public void startLoading() {
		ref.loader = new MultiplayerLoader();
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

	@Override
	public void setMap(String string) {
		if (ContentListHandler.getMapContent().keys().contains(string))
			map = string;
	}

}
