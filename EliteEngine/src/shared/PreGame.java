package shared;

import java.util.HashMap;

public abstract class PreGame {

	public HashMap<String, User> users = new HashMap<String, User>();
	 public String map;

	public abstract void startLoading();

	public abstract void addPlayer(String ip, String name);

	public abstract void update();

	public abstract void setMap(String string);

	public void dispose() {
	}

	public void setActive(boolean b) {		
	}

	public User getUser(String string) {
		return users.get(string);
	}
}
