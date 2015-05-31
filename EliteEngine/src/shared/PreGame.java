package shared;

import java.util.HashMap;

public abstract class PreGame {

	public HashMap<String,Player> player = new HashMap<String, Player>();

	public abstract void startLoading();

	public abstract void addPlayer(String ip, String name);

	public abstract void update();
}
