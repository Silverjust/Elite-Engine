package entity.neutral;

import java.util.ArrayList;

import entity.Active;
import processing.core.PImage;
import shared.ref;
import entity.AimingActive;
import entity.Building;
import entity.Commander;
import entity.Entity;
import entity.animation.Animation;
import game.AimHandler;
import game.AimHandler.Cursor;
import game.HUD;
import game.aim.CustomAim;

public class SandboxBuilding extends Building implements Commander {

	private static PImage standImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standImg = game.ImageHandler.load(path, "SandboxBuilding");
	}

	public static int commandRange = 0;

	public SandboxBuilding(String[] c) {
		super(c);
		player = ref.player;// neutral

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = null;
		death = null;

		setAnimation(stand);

		// ************************************
		xSize = 30;
		ySize = 30;

		radius = 15;

		descr = "only for sandbox";
		// ************************************
	}

	@Override
	public void renderGround() {
		super.renderGround();
		getAnimation().draw(this, (byte) 0, currentFrame);
	}

	@Override
	public PImage preview() {
		System.out.println("woat?");
		ref.updater.send("<say SERVER " + player.name + "cheats");
		return standImg;
	}

	@Override
	public int commandRange() {
		return commandRange;
	}

	public static class BuildSetup extends Active {

		public BuildSetup(int x, int y, char n) {
			super(x, y, n, standImg);
			clazz = SandboxBuilding.class;
		}

		@Override
		public void onActivation() {
			HUD.activesGrid.setupSandbox();
		}

		@Override
		public String getDesription() {
			return "start mapedit";
		}

	}

	public static class DeleteActive extends Active implements AimingActive {

		public DeleteActive(int x, int y, char n) {
			super(x, y, n, standImg);
			clazz = SandboxBuilding.class;
		}

		@Override
		public void onActivation() {
			AimHandler.setAim(new CustomAim(this, Cursor.SHOOT));
		}

		@Override
		public String getDesription() {
			return "delete";
		}

		@Override
		public void execute(float x, float y) {
			for (Entity e2 : ref.updater.entities) {
				if (e2 != null && e2.isInRange(x, y, e2.radius + 10)) {
					ref.updater.send("<remove " + e2.number);
				}
			}
		}

	}

	public static class ChangeSide extends Active {

		public ChangeSide(int x, int y, char n) {
			super(x, y, n, standImg);
			clazz = SandboxBuilding.class;
		}

		@Override
		public void onActivation() {
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

	public static class AddPlayer extends Active {

		public AddPlayer(int x, int y, char n) {
			super(x, y, n, standImg);
			clazz = SandboxBuilding.class;
		}

		@Override
		public void onActivation() {
			ref.preGame.addPlayer((ref.updater.player.size() + 1) + "",
					"player" + (ref.updater.player.size() + 1));
		}

		@Override
		public String getDesription() {
			return "add player";
		}

	}
}
