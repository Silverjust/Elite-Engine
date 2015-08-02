package entity.ahnen;

import processing.core.PImage;
import shared.ref;
import entity.Active;
import entity.Building;
import entity.Commander;
import entity.Entity;
import entity.TrainActive;
import entity.Trainer;
import entity.Unit;
import entity.animation.Ability;
import entity.animation.Animation;
import entity.animation.Build;
import entity.animation.Death;
import entity.animation.Training;
import game.ImageHandler;

public class AhnenKaserne extends Building implements Trainer, Commander {
	protected float xTarget;
	protected float yTarget;

	byte level = 0;

	private Training training;

	private static PImage standImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standImg = ImageHandler.load(path, "AhnenKaserne");
	}

	public AhnenKaserne(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = new Build(standImg, 1000);
		death = new Death(standImg, 1000);
		training = new Training(standImg, 100);

		setAnimation(build);
		setupTarget();
		// ************************************
		xSize = 30;
		ySize = 30;

		kerit = 500;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		build.setBuildTime(3000);

		sight = 50;

		hp = hp_max = 1000;
		radius = 15;

		descr = " ";
		stats = "level 1";
		// ************************************
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("level")) {
			level = (byte) Integer.parseInt(c[3]);
			stats = "level " + (level + 1);
			setAnimation(build);
		} else if (c[2].equals("train")) {
			boolean b = false;
			if (level >= 0 && c[3].equals("Berserker"))
				b = true;
			if (level >= 1 && c[3].equals("Witcher"))
				b = true;
			if (level >= 2 && c[3].equals("Warrior"))
				b = true;
			if (level >= 3 && c[3].equals("Angel"))
				b = true;
			if (level >= 4
					&& (c[3].equals("Astrator") || c[3].equals("Destructor")))
				b = true;
			if (b) {
				Training.updateExecTraining(c, this);
			}
		} else if (c[2].equals("setTarget")) {
			Training.updateExecTraining(c, this);
		}
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, currentFrame);
	}

	public PImage preview() {
		return standImg;
	}

	@Override
	public Ability getTraining() {
		return training;
	}

	@Override
	public float getXTarget() {
		return xTarget;
	}

	@Override
	public float getYTarget() {
		return yTarget;
	}

	@Override
	public void setTarget(float x, float y) {
		xTarget = x;
		yTarget = y;

	}

	@Override
	// TODO remove this and add depot
	public int commandRange() {
		return 250;
	}

	public static class LevelActive extends Active {
		public LevelActive(int x, int y, char n) {
			super(x, y, n, standImg);
			clazz = AhnenKaserne.class;
		}

		@Override
		public String getDesription() {
			AhnenKaserne target = null;
			for (Entity e : ref.updater.selected) {
				if (e instanceof AhnenKaserne) {
					target = (AhnenKaserne) e;
				}
			}
			if (target != null) {
				if (target.level == 4)
					return "fully upgraded";
				return "upgrade to level " + (target.level + 2);
			}
			return "upgrade level";
		}

		@Override
		public String getStatistics() {
			AhnenKaserne target = null;
			for (Entity e : ref.updater.selected) {
				if (e instanceof AhnenKaserne) {
					target = (AhnenKaserne) e;
				}
			}
			if (target != null) {
				return "kerit: " + getCosts(target);
			}
			return " ";
		}

		@Override
		public void onActivation() {
			AhnenKaserne target = null;

			for (Entity e : ref.updater.selected) {
				if (e instanceof AhnenKaserne && e.getAnimation() == e.stand) {
					target = (AhnenKaserne) e;
				}
			}
			if (target != null && target.level < 4
					&& target.player.canBy(getCosts(target), 0, 0, 0)) {
				target.buyFrom(target.player, getCosts(target), 0, 0, 0);
				target.sendAnimation("level " + (target.level + 1));
			}
		}

		private int getCosts(AhnenKaserne target) {
			if (target.level == 4)
				return 0;
			return 100 + target.level * 20;
		}
	}

	public static class AhnenTrainActive extends TrainActive {

		public AhnenTrainActive(int x, int y, char n, Entity u,
				Class<? extends Entity> trainer) {
			super(x, y, n, (Unit) u, trainer);
		}

		public void onActivation() {
			AhnenKaserne trainer = null;
			for (Entity e : ref.updater.selected) {
				if (clazz.isAssignableFrom(e.getClass())
						&& e.getAnimation() == e.stand) {
					boolean b = false;
					AhnenKaserne t = (AhnenKaserne) e;
					if (t.level >= 0 && unit.equals(Berserker.class))
						b = true;
					if (t.level >= 1 && unit.equals(Witcher.class))
						b = true;
					if (t.level >= 2 && unit.equals(Warrior.class))
						b = true;
					if (t.level >= 3 && unit.equals(Angel.class))
						b = true;
					if (t.level >= 4
							&& (unit.equals(Astrator.class) || unit
									.equals(Destructor.class)))
						b = true;
					if (b)
						trainer = t;
				}
			}

			Entity toTrain = null;
			try {
				toTrain = unit.getConstructor(String[].class).newInstance(
						new Object[] { null });
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (trainer != null && toTrain != null
					&& toTrain.canBeBought(trainer.player)) {
				toTrain.buyFrom(trainer.player);
				trainer.sendAnimation("train " + unit.getSimpleName());

			}
		}
	}
}
