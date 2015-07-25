package entity.neutral;

import shared.ref;
import entity.Building;
import entity.animation.Extract;

public abstract class KeritMine extends Building {

	protected static final int efficenty = 17;
	protected static final String ressource = "kerit";
	protected static final int cooldown = 1000;
	protected static final int buildTime = 5000;

	public KeritMine(String[] c) {
		super(c);

		// ************************************
		xSize = 30;
		ySize = 30;

		kerit = 250;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		// buildtime in child

		hp = hp_max = 500;
		radius = 10;
		sight = 50;
		// ************************************
	}

	@Override
	public void updateDecisions() {
		if (getAnimation() == stand)
			((Extract) stand).updateAbility(this);
	}

	@Override
	protected void onDeath() {
		super.onDeath();
		ref.updater.send("<spawn Kerit 0 " + x + " " + y);
	}

}
