package entity.animation;

import entity.Entity;
import processing.core.PImage;
import shared.Updater;
import shared.ref;

public class Extract extends Ability {

	public float efficenty;
	public String ressource;
	private boolean isSetup;
	private static boolean isRFNew=true;

	public Extract(PImage[][] IMG, int duration) {
		super(IMG, duration);
	}

	public Extract(PImage[] IMG, int duration) {
		super(IMG, duration);
	}

	public Extract(PImage IMG, int duration) {
		super(IMG, duration);
	}

	@Override
	public void updateAbility(Entity e, boolean isServer) {
		if (isSetup() && isEvent()) {
			int amount = (int) (e.hp * 1.0 / e.hp_max * efficenty);
			amount = amount < 0 ? 0 : amount;
			if (isServer) {
				if (Updater.resfreeze != null && Updater.resfreeze.isNotOnCooldown()) {
					if (isRFNew) {
						System.out.println("Extract.updateAbility() resfreeze");
						ref.preGame.write("GAME", "resfreze");
						isRFNew=false;
					}
				} else
					ref.updater.send("<give " + e.player.getUser().ip + " " + ressource + " " + amount);
			}
			isSetup = false;
		}
		if (isNotOnCooldown()) {
			isSetup = true;
			startCooldown();
		}
	}

	@Override
	public boolean isSetup() {
		return isSetup;
	}

	@Override
	public boolean doRepeat(Entity e) {
		return true;
	}
}
