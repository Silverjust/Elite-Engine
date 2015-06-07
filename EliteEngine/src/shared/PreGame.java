package shared;

import java.util.HashMap;

public abstract class PreGame {

	public HashMap<String, Player> player = new HashMap<String, Player>();
	 public String map;

	public abstract void startLoading();

	public abstract void addPlayer(String ip, String name);

	public abstract void update();

	public abstract void setMap(String string);
}
