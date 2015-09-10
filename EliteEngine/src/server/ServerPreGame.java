package server;

import main.PreGameNormalDisplay;
import shared.ContentListHandler;
import shared.Mode;
import shared.PreGame;
import shared.User;
import shared.ref;

public class ServerPreGame extends PreGame {

	public ServerPreGame() {
		ContentListHandler.load();
		int i = ContentListHandler.getModeMaps().size();

		@SuppressWarnings("unchecked")
		String[] intNames = (String[]) ContentListHandler.getModeMaps().keys()
				.toArray(new String[i]);
		map = ContentListHandler.getModeMaps().getString(intNames[PreGameNormalDisplay.startMap]);
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
		if (!users.containsKey(ip)) {
			User u = new User(ip, name);
			users.put(ip, u);
		} else if (users.get(ip).name.equals(ip)) {
			users.get(ip).name = name;
		}
	}

	@Override
	public void setMap(String string) {
		if (ContentListHandler.getModeMaps().keys().contains(string))
			map = ContentListHandler.getModeMaps().getString(string);
		((ServerApp) ref.app).gui.addChatText("map set to " + string);

	}

}
