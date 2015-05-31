package entity;

import shared.ref;
import entity.animation.Animation;
import game.Chat;

public abstract class Building extends Entity {

	public Animation build;

	public Building(String[] c) {
		if (c != null) {
			player = ref.updater.player.get(c[2]);// server fähig machen

			x = Float.parseFloat(c[3]);
			y = Float.parseFloat(c[4]);
			x = xToGrid(x);
			y = yToGrid(y * 2);
			

			groundPosition = GroundPosition.GROUND;
		}
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		switch (c[2]) {
		case "build":
			setAnimation(build);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void renderGround() {
		drawSelected();
	}

	@Override
	public void display() {
		super.display();
		if (animation == build) {

		}
	}

	public static float xToGrid(float x) {
		return Math.round(x / 20) * 20;
	}

	public static float yToGrid(float y) {
		return Math.round(y / 20) * 10;
	}

	public void drawOnMinimap() {
		ref.app.fill(player.playerColor);
		ref.app.rect(x, y, radius * 2, radius * 2);
	}

	@Override
	void drawShadow() {
		System.out.println(this.getClass().getSimpleName()
				+ "should not have a shadow");
		Chat.println(this.getClass().getSimpleName() + "",
				"should not have a shadow");
	}

}
