package entity.animation;

import entity.Entity;
import entity.Trainer;
import entity.Unit;
import processing.core.PApplet;
import processing.core.PImage;
import shared.ContentListHandler;
import shared.ref;

public class Training extends Ability {
	Entity toTrain;

	public Training(PImage[][] IMG, int duration) {
		super(IMG, duration);
	}

	public Training(PImage[] IMG, int duration) {
		super(IMG, duration);
	}

	public Training(PImage IMG, int duration) {
		super(IMG, duration);
	}

	@Override
	public void setup(Entity e) {
		super.setup(e);
	}

	@Override
	public void updateAbility(Entity e) {
		if (isSetup() && isNotOnCooldown()) {
			float xt = ((Trainer) e).getXTarget();
			float yt = ((Trainer) e).getYTarget();

			ref.updater.send("<spawn "
					+ toTrain.getClass().getSimpleName()
					+ " "
					+ e.player.ip
					+ " "
					+ (e.x + (xt - e.x) / PApplet.dist(e.x, e.y, xt, yt)
							* (e.radius + toTrain.radius))
					+ " "
					+ (e.y + (yt - e.y) / PApplet.dist(e.x, e.y, xt, yt)
							* (e.radius + toTrain.radius))//
					+ " " //
					+ xt //
					+ " " //
					+ yt);
			toTrain = null;
		}
	}

	@Override
	public void update(Entity e) {
		if (isFinished()) {
			setup(e);
			if (isNotOnCooldown())
				e.sendDefaultAnimation(this);
		}
	}

	public void setEntity(Entity toTrain) {
		startCooldown();
		this.toTrain = toTrain;
	}

	@Override
	public boolean isSetup() {
		return toTrain != null;
	}

	@Override
	public boolean doRepeat() {
		System.out.println("Training.doRepeat()hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		if (isSetup()) {
			System.out.println("Training.nextAnimation()this");
			return true;
		}
		return false;
	}

	@Override
	public boolean isInterruptable() {
		return false;
	}

	public static void updateExecTraining(String[] c, Entity trainer) {
		if (c[2].equals("train") && trainer instanceof Trainer) {
			Entity toTrain = null;
			Training a = (Training) ((Trainer) trainer).getTraining();
			try {
				String name = ContentListHandler.getEntityContent().getString(
						c[3]);
				toTrain = (Entity) Class.forName(name)
						.getConstructor(String[].class)
						.newInstance(new Object[] { null });
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (toTrain != null && a.isNotOnCooldown()) {
				a.cooldown = ((Unit) toTrain).trainTime;
				a.setEntity(toTrain);
				trainer.setAnimation(a);
			}

		} else if (c[2].equals("setTarget") && trainer instanceof Trainer) {
			float x = Float.parseFloat(c[3]);
			float y = Float.parseFloat(c[4]);
			((Trainer) trainer).setTarget(x, y);
		}
	}
}
