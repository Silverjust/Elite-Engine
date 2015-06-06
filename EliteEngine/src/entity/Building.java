package entity;

import processing.core.PImage;
import shared.Nation;
import shared.ref;
import entity.animation.Build;
import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import game.AimHandler;
import game.Chat;
import game.ImageHandler;
import game.aim.SetTargetAim;

public abstract class Building extends Entity {

	private static PImage setTarget;
	public Build build;

	public static void loadImages() {
		String path = path(Nation.NEUTRAL, new Object() {
		});
		setTarget = ImageHandler.load(path, "setTarget");
	}

	public Building(String[] c) {
		if (c != null) {
			player = ref.updater.player.get(c[2]);// server fähig machen

			x = Float.parseFloat(c[3]);
			y = Float.parseFloat(c[4]);
			x = xToGrid(x);
			y = yToGrid(y) * 2;

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
		if (animation == build)
			drawBar(build.getCooldownPercent());

	}

	public static float xToGrid(float x) {
		return Math.round(x / 20) * 20;
	}

	public static float yToGrid(float y) {
		return Math.round(y / 20) * 10;
	}

	public void drawOnMinimap() {
		ref.app.fill(player.color);
		ref.app.rect(x, y, radius * 2, radius * 2);
	}

	@Override
	void drawShadow() {
		System.out.println(this.getClass().getSimpleName()
				+ "should not have a shadow");
		Chat.println(this.getClass().getSimpleName() + "",
				"should not have a shadow");
	}

	public static class SetTargetActive extends Active {

		public SetTargetActive(int x, int y, char n) {
			super(x, y, n, setTarget);
			clazz = Trainer.class;
		}

		@Override
		public void onButtonPressed(GGameButton gamebutton, GEvent event) {
			
				AimHandler.setAim(new SetTargetAim(clazz));
			
		}

		@Override
		public String getDesription() {
			return "sets the Target,§where new units walk to";
		}

		@Override
		public String getStatistics() {
			return null;
		}
		
	}

}
