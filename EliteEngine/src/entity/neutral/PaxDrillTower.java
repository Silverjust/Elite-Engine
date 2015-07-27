package entity.neutral;

import shared.ref;
import entity.Building;
import entity.animation.Extract;

public abstract class PaxDrillTower extends Building {

	protected static final int efficenty = 14;
	protected static final String ressource = "pax";
	protected static final int cooldown = 1000;
	protected static final int buildTime = 7000;

	public PaxDrillTower(String[] c) {
		super(c);
		// ************************************
		xSize = 40;
		ySize = 40;

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
	public void updateDecisions(boolean isServer) {
		if (getAnimation() == stand)
			((Extract) stand).updateAbility(this, isServer);
	}

	@Override
	protected void onDeath() {
		super.onDeath();
		ref.updater.send("<spawn Pax 0 " + x + " " + y);
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, currentFrame);
	}

}
