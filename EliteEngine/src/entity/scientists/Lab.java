package entity.scientists;

import shared.ref;
import entity.Entity;
import entity.Unit;

public abstract class Lab extends Unit implements Equiping {
	protected static final int TRAINTIME = 3000;
	public byte equipRange;

	public Lab(String[] c) {
		super(c);
		// ************************************
		xSize = 20;
		ySize = 20;

		hp = hp_max = 700;
		armor = 1;
		speed = 0.7f;
		radius = 12;
		height = 30;
		sight = 70;
		groundPosition = Entity.GroundPosition.AIR;

		equipRange = 120;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void onSpawn(boolean isServer) {
		ref.updater.selectionChanged = true;
		ref.updater.keepGrid = true;
	}

	@Override
	public void renderAir() {
		drawSelected();
		getAnimation().draw(this, direction, currentFrame);
		drawTaged();
	}

}