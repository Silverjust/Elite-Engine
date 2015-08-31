package entity.scientists;

import processing.core.PApplet;
import processing.core.PImage;
import shared.Nation;
import shared.ref;
import entity.AimingActive;
import entity.Entity;
import entity.MultiCDActive;
import entity.Unit;
import entity.animation.Ability;
import entity.animation.Animation;
import entity.animation.Death;
import game.AimHandler;
import game.AimHandler.Cursor;
import game.ImageHandler;
import game.aim.CustomAim;

public class PhysicsLab extends Lab {

	private static PImage standingImg;
	public static PImage teleportImg;

	private Ability sendTeleport;
	private Animation recievieTeleport;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = ImageHandler.load(path, "PhysicsLab");
		teleportImg = ImageHandler.load(Nation.SCIENTISTS.toString()
				+ "/symbols/", "teleport");
	}

	public PhysicsLab(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		sendTeleport = new Ability(standingImg, 800);
		recievieTeleport = new Animation(standingImg, 800);

		setAnimation(walk);
		
		// ************************************

		kerit = 600;
		pax = 0;
		arcanum = 0;
		prunam = 10;
		trainTime = TRAINTIME;

		sendTeleport.cooldown = 60000;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if ("sendTeleport".equals(c[2])) {
			setAnimation(sendTeleport);
		} else if ("recievieTeleport".equals(c[2])) {
			setAnimation(recievieTeleport);
		}
	}

	public Ability getTeleport() {
		return sendTeleport;
	}

	public static class TeleportActive extends MultiCDActive implements
			AimingActive {

		private PhysicsLab origin;

		public TeleportActive(int x, int y, char n) {
			super(x, y, n, teleportImg);
			// cooldown = 60000;
			clazz = PhysicsLab.class;
			setAbilityGetter("getTeleport");
		}

		@Override
		public void onActivation() {
			origin = null;
			for (Entity e : ref.updater.selected) {
				if (e instanceof PhysicsLab
						&& (e.getAnimation() == e.stand || e.getAnimation() == ((Unit) e).walk))
					origin = (PhysicsLab) e;
			}
			if (origin != null) {
				origin.sendAnimation("sendTeleport");
				AimHandler.setAim(new CustomAim(this, Cursor.SELECT));
			}
		}

		@Override
		public String getDesription() {
			return "teleports units  from §physicslab to physicslab";
		}

		@Override
		public void execute(float x, float y) {
			// float x, y;
			Entity target = null;
			// x = Entity.xToGrid(Entity.gridToX());
			// y = Entity.xToGrid(Entity.gridToY());
			for (Entity e : ref.updater.entities) {
				if (e.isAllyTo(ref.player) && e instanceof PhysicsLab
						&& PApplet.dist(x, y, e.x, e.y - e.flyHeight()) <= e.radius)
					target = e;
			}
			if (target != null) {
				PhysicsLab origin = this.origin;
				origin.getTeleport().startCooldown();
				target.sendAnimation("recieveTeleport");
				for (Entity e : ref.updater.entities) {
					if (e.isAllyTo(ref.player)
							&& e instanceof Unit
							&& !(e instanceof Lab)
							&& e.isInRange(origin.x, origin.y,
									origin.equipRange))
						ref.updater.send("<tp " + e.number + " "
								+ (e.x + target.x - origin.x) + " "
								+ (e.y + target.y - origin.y));
				}
				startCooldown();
				AimHandler.end();
			}
		}
	}

}