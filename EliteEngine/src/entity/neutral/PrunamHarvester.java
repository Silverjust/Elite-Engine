package entity.neutral;

import shared.ref;
import entity.Building;
import entity.animation.Extract;

public abstract class PrunamHarvester extends Building {

	protected static final int efficenty = 10;
	protected static final String ressource = "prunam";
	protected static final int cooldown = 10000;
	protected static final int buildTime = 8000;

	public PrunamHarvester(String[] c) {
		super(c);
		// ************************************
		xSize = 30;
		ySize = 30;

		kerit = 500;
		pax = 0;
		arcanum = 0;
		prunam = 0;

		hp = hp_max = 500;
		radius = 15;
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
		ref.updater.send("<spawn Prunam 0 " + x + " " + y);
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, currentFrame);
	}
}
