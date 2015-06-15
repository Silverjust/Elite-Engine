package server;

import shared.ContentListHandler;
import shared.Mode;
import shared.Player;
import shared.PreGame;
import shared.ref;

public class ServerPreGame extends PreGame {

	public ServerPreGame() {
		ContentListHandler.load();
		int i = ContentListHandler.getModeMaps().size();

		@SuppressWarnings("unchecked")
		String[] intNames = (String[]) ContentListHandler.getModeMaps().keys()
				.toArray(new String[i]);
		map = ContentListHandler.getModeMaps().getString(intNames[0]);
		// PApplet.printArray(intNames);
		// System.out.println(map);
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
		System.out.println(string);
		if (ContentListHandler.getMapContent().keys().contains(string))
			map = ContentListHandler.getModeMaps().getString(string);
		System.out.println(map);

	}

}
