package entity.scientists;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;
import entity.Active;
import entity.Entity;
import entity.MultiCDActive;
import entity.animation.Ability;
import entity.animation.Animation;
import entity.animation.Death;
import game.AimHandler;
import game.ImageHandler;
import game.AimHandler.Cursor;
import game.aim.Aim;

public class BioLab extends Lab {

	private static PImage standingImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = ImageHandler.load(path, "BioLab");
	}

	public Ability swampify;

	public BioLab(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		swampify = new Ability(standingImg, 1000);

		setAnimation(walk);

		// ************************************
		kerit = 600;
		pax = 50;
		arcanum = 0;
		prunam = 0;
		trainTime = TRAINTIME;

		swampify.cooldown = 20000;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void renderUnder() {
		super.renderUnder();
		if (isAlive() && AimHandler.getAim() instanceof SwampAim
				&& swampify.isNotOnCooldown()) {
			ref.app.tint(player.color);
			ImageHandler.drawImage(ref.app, selectedImg, xToGrid(x),
					yToGrid(y), equipRange * 2, equipRange);
			ref.app.tint(255);
		}
	}

	public Ability getSwampify() {
		return swampify;
	}

	public static class CreateSwampActive extends MultiCDActive {
		Entity builder = null;
		private int acooldown, adamage;

		public CreateSwampActive(int x, int y, char n) {
			super(x, y, n, new Swamp(null).iconImg);
			Swamp swamp = new Swamp(null);
			acooldown = swamp.getBasicAttack().cooldown;
			adamage = swamp.getBasicAttack().damage;
			clazz = BioLab.class;
			setAbilityGetter("getSwampify");
		}

		@Override
		public void onActivation() {
			for (Entity e : ref.updater.selected) {
				if (clazz.isAssignableFrom(e.getClass())) {
					builder = e;
				}
			}
			if (builder != null) {
				AimHandler.setAim(new SwampAim(this, builder));
			}
		}

		@Override
		public String getDesription() {
			return "creates a swamp§damaging ground units";
		}

		@Override
		public String getStatistics() {
			return super.getStatistics() + "dps: " + adamage + "/"
					+ (acooldown / 1000.0) + " ="
					+ PApplet.nfc(adamage / (acooldown / 1000.0f), 2) + " (0)";
		}
	}

	public static class SwampAim extends Aim {

		private Entity builder;
		private Active active;

		public SwampAim(Active active, Entity builder) {
			this.active = active;
			this.builder = builder;
		}

		@Override
		public Cursor getCursor() {
			return Cursor.SHOOT;
		}

		@Override
		public void execute(float x, float y) {
			if (canPlaceAt(x, y)) {
				ref.updater.send("<spawn " + Swamp.class.getSimpleName() + " "
						+ builder.player.getUser().ip + " " + x + " " + y);
				active.startCooldown();
				((BioLab) builder).getSwampify().startCooldown();
			}
		}

		protected boolean canPlaceAt(float x, float y) {
			boolean inLabRange = false;
			for (Entity e : ref.updater.entities) {
				if (e instanceof BioLab && e.player == builder.player
						&& e.isInRange(x, y, ((BioLab) e).equipRange)
						&& ((BioLab) e).swampify.isNotOnCooldown()) {
					inLabRange = true;
				}
			}
			return inLabRange;
		}
	}
}