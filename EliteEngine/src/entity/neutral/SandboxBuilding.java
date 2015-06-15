package entity.neutral;

import java.util.ArrayList;

import entity.Active;
import processing.core.PImage;
import shared.Nation;
import shared.ref;
import entity.Building;
import entity.Commander;
import entity.Entity;
import entity.animation.Animation;
import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import game.AimHandler;
import game.HUD;
import game.aim.DeleteAim;

public class SandboxBuilding extends Building implements Commander {

	private static PImage standImg;

	public static void loadImages() {
		String path = path( new Object() {
		});
		standImg = game.ImageHandler.load(path, "SandboxBuilding");
	}

	public SandboxBuilding(String[] c) {
		super(c);
		player = ref.player;// neutral

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = null;
		death = null;

		animation = nextAnimation = stand;
		// ************************************
		xSize = 30;
		ySize = 30;

		radius = 15;

		descr = "only for sandbox";
		// ************************************
	}

	@Override
	public void updateDecisions() {
	}

	@Override
	public void renderGround() {
		super.renderGround();
		animation.draw(this, (byte) 0, currentFrame);
	}

	@Override
	public void updateAnimation() {
	}

	@Override
	public PImage preview() {
		System.out.println("woat?");
		ref.updater.send("<say SERVER " + player.name + "cheats");
		return standImg;
	}

	@Override
	public int commandRange() {
		return Integer.MAX_VALUE;
	}

	public static class BuildSetup extends Active {

		public BuildSetup(int x, int y, char n) {
			super(x, y, n, standImg);
			clazz = SandboxBuilding.class;
		}

		@Override
		public void onButtonPressed(GGameButton gamebutton, GEvent event) {
			HUD.activesGrid.setupSandbox();
		}

		@Override
		public String getDesription() {
			return "start mapedit";
		}

	}

	public static class DeleteActive extends Active {

		public DeleteActive(int x, int y, char n) {
			super(x, y, n, standImg);
			clazz = SandboxBuilding.class;
		}

		@Override
		public void onButtonPressed(GGameButton gamebutton, GEvent event) {
			AimHandler.setAim(new DeleteAim());
		}

		@Override
		public String getDesription() {
			return "delete";
		}

	}

	public static class ChangeSide extends Active {

		public ChangeSide(int x, int y, char n) {
			super(x, y, n, standImg);
			clazz = SandboxBuilding.class;
		}

		@Override
		public void onButtonPressed(GGameButton gamebutton, GEvent event) {
			int i = 0;
			for (Entity e : ref.updater.entities) {
				if (e instanceof SandboxBuilding) {
					i = new ArrayList<String>(ref.updater.player.keySet())
							.indexOf(e.player.ip) + 1;
				}
			}
			if (i >= ref.updater.player.keySet().size())
				i = 0;
			for (Entity e : ref.updater.entities) {
				if (e instanceof SandboxBuilding) {
					e.player = ref.updater.player.get(new ArrayList<String>(
							ref.updater.player.keySet()).get(i));
				}
			}

		}

		@Override
		public String getDesription() {
			return "switch player";
		}

	}
}
