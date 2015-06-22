package entity.neutral;

import shared.ref;
import entity.Building;
import entity.animation.Extract;

public abstract class ArcanumMine extends Building   {

	protected static final int efficenty = 20;
	protected static final String ressource = "arcanum";
	protected static final int cooldown = 5000;
	protected static final int buildTime = 10000;

	public ArcanumMine(String[] c) {
		super(c);
		// ************************************
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
		super.updateDecisions();
		((Extract) stand).updateAbility(this);
	}

	@Override
	protected void onDeath() {
		super.onDeath();
		ref.updater.send("<spawn Arcanum 0 " + x + " " + y);
	}

	@Override
	public void renderGround() {
		drawSelected();
		animation.draw(this, (byte) 0, currentFrame);
	}


}
