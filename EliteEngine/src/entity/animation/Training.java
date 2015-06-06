package entity.animation;

import entity.Entity;
import entity.Trainer;
import entity.Unit;
import game.ContentListHandler;
import processing.core.PApplet;
import processing.core.PImage;
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
	public void updateAbility(Entity e) {
		if (toTrain != null && isNotOnCooldown()) {
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
	public float getCooldownPercent() {
		float f = 1 - (float) (cooldownTimer - ref.app.millis()) / cooldown;
		return f > 1 || f < 0 ? 1 : f;
	}

	public static void updateExecTraining(String[] c, Entity trainer) {
		if (c[2].equals("train") && trainer instanceof Trainer) {
			Entity toTrain = null;
			try {
				String name = ContentListHandler.getEntityContent().getString(
						c[3]);
				toTrain = (Entity) Class.forName(name)
						.getConstructor(String[].class)
						.newInstance(new Object[] { null });
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (toTrain != null && toTrain.canBeBought(trainer.player)) {
				toTrain.buyFrom(trainer.player);
				Training a = (Training) ((Trainer) trainer).getTraining();

				a.cooldown = ((Unit) toTrain).trainTime;// cooldown=traintime
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
