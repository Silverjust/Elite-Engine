package entity.scientists;

import entity.Entity;
import entity.Unit;

public abstract class Lab extends Unit {

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
		height = 20;
		sight = 70;
		groundPosition = Entity.GroundPosition.AIR;

		equipRange = 120;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions() {
		if (animation == walk || animation == stand) {// ****************************************************
		}
	}

	@Override
	public void renderAir() {
		drawSelected();
		animation.draw(this, direction, currentFrame);
		drawTaged();
	}

}