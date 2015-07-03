package entity.scientists;

import processing.core.PImage;
import shared.ref;
import entity.Active;
import entity.Entity;
import entity.Unit;
import entity.animation.Animation;
import entity.animation.Death;
import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import game.ImageHandler;

public class PhysicsLab extends Lab {

	private static PImage standingImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standingImg = ImageHandler.load(path, "PhysicsLab");
	}

	public PhysicsLab(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);

		animation = nextAnimation = walk;
		// ************************************

		kerit = 600;
		pax = 0;
		arcanum = 0;
		prunam = 10;
		trainTime = 1000;

		descr = " ";
		stats = " ";
		// ************************************
	}
	public static class EquipActive extends Active {
		Class<? extends Unit> unit;
		Class<?> lab;
		String descr = " ", stats = " ";

		public EquipActive(int x, int y, char n, Entity u, Class<?> trainer) {
			super(x, y, n, u.iconImg);
			clazz = GuineaPig.class;
			lab = trainer;
			unit = ((Unit) u).getClass();
			descr = u.getDesription();
			stats = u.getStatistics();
		}

		@Override
		public void onButtonPressed(GGameButton gamebutton, GEvent event) {
			Entity trainer = null;
			for (Entity e : ref.updater.selected) {
				if (clazz.isAssignableFrom(e.getClass())
						&& (e.getAnimation() == e.stand || e.getAnimation() == ((Unit) e).walk))
					for (Entity e2 : ref.player.visibleEntities) {
						if (e2.player == e.player
								&& e2.getClass().equals(lab)
								&& e.isInRange(e2.x, e2.y, e.radius
										+ ((Lab) e2).equipRange)) {
							trainer = e;
						}
					}
			}
			if (trainer != null) {
				trainer.sendAnimation("equip " + unit.getSimpleName());
			}
		}

		@Override
		public String getDesription() {
			return descr;
		}

		@Override
		public String getStatistics() {
			return stats;
		}
	}

}