package entity.scientists;

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

public class ScientistKaserne extends Building implements Commander, Trainer,
		Equiping {
	private int commandingRange;
	protected float xTarget;
	protected float yTarget;

	private Ability training;

	private static PImage standImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standImg = ImageHandler.load(path, "ScientistKaserne");
	}

	public ScientistKaserne(String[] c) {
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
		build.setBuildTime(5000);

		sight = 50;

		hp = hp_max = 1000;
		radius = 15;

		commandingRange = 250;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		Training.updateExecTraining(c, this);
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, currentFrame);

		for (Entity e : ref.player.visibleEntities) {
			if (e instanceof Lab && e.isAllyTo(e) && e.isAlive()
					&& isInRange(x, y, commandingRange)) {
				ref.app.stroke(100);
				ref.app.line(xToGrid(x), yToGrid(y - ySize), e.x,
						(e.y - e.flyHeight()) / 2);
				ref.app.stroke(0);
			}
		}
	}

	@Override
	public void display() {
		super.display();
		if (getAnimation() == training)
			drawBar(training.getCooldownPercent());
	}

	public PImage preview() {
		return standImg;
	}

	@Override
	public int commandRange() {
		return commandingRange;
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

	public static class EquipActive extends Active {
		Class<? extends Unit> unit;
		Class<?> lab;
		String descr = " ", stats = " ";

		public EquipActive(int x, int y, char n, Entity u,
				Class<? extends Entity> trainerHeper) {

			super(x, y, n, u.iconImg);
			clazz = Equiping.class;
			lab = trainerHeper;
			unit = ((Unit) u).getClass();
			descr = u.getDesription();
			stats = u.getStatistics();
		}

		@Override
		public void onActivation() {
			ScientistKaserne trainer = null;
			Entity trained = null;
			for (Entity e : ref.updater.selected) {
				if (e instanceof ScientistKaserne && e.isAlive()) {
					for (Entity e2 : ref.player.visibleEntities) {
						if (e2 instanceof Lab
								&& e2.getClass().equals(lab)
								&& e2.isAllyTo(e)
								&& e2.isAlive()
								&& e2.isInRange(e.x, e.y,
										((ScientistKaserne) e).commandingRange)) {
							trainer = (ScientistKaserne) e;

						}
					}
				}
			}
			if (trainer != null) {
				for (Entity e : ref.player.visibleEntities) {
					if (e instanceof GuineaPig
							&& e.isAlive()
							&& e.isAllyTo(trainer)
							&& e.isInRange(trainer.x, trainer.y,
									trainer.commandingRange)) {
						trained = e;
					}
				}
			}
			Unit newUnit = null;
			try {
				newUnit = unit.getConstructor(String[].class).newInstance(
						new Object[] { null });
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (trained != null && newUnit != null
					&& newUnit.canBeBought(trained.player)) {
				newUnit.buyFrom(trained.player);
				trained.sendAnimation("equip " + unit.getSimpleName());
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

	@Deprecated
	public static class ScientistTrainActive extends TrainActive {

		private Class<? extends Entity> lab;

		public ScientistTrainActive(int x, int y, char n, Entity u,
				Class<? extends Entity> trainerHeper) {
			super(x, y, n, (Unit) u, ScientistKaserne.class);
			lab = trainerHeper;
		}

		public void onActivation() {
			Entity trainer = null;
			for (Entity e : ref.updater.selected) {
				if (clazz.isAssignableFrom(e.getClass())
						&& e.getAnimation() == e.stand) {
					for (Entity e2 : ref.player.visibleEntities) {
						if (e2.player == e.player
								&& e2.getClass().equals(lab)
								&& e.isInRange(e2.x, e2.y, e.radius
										+ ((Lab) e2).equipRange)) {
							trainer = e;
						}
					}
				}
			}

			Entity toTrain = null;
			try {
				toTrain = unit.getConstructor(String[].class).newInstance(
						new Object[] { null });
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			if (trainer != null && toTrain != null
					&& toTrain.canBeBought(trainer.player)) {
				toTrain.buyFrom(trainer.player);
				trainer.sendAnimation("train " + unit.getSimpleName());

			}
		}

		@Override
		public boolean isActivateable() {
			boolean isActivateable = false;
			for (Entity e : ref.updater.selected) {
				if (clazz.isAssignableFrom(e.getClass())) {
					for (Entity e2 : ref.player.visibleEntities) {
						if (e2.player == e.player
								&& e2.getClass().equals(lab)
								&& e.isInRange(e2.x, e2.y, e.radius
										+ ((Lab) e2).equipRange)) {
							isActivateable = true;
						}
					}
				}
			}
			return isActivateable;
		}
	}
}
