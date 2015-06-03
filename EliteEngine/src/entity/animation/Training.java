package entity.animation;

import entity.Entity;
import entity.Trainer;
import entity.Unit;
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
			if (toTrain.canBeBought(e.player)) {
				ref.updater.send("<spawn " + toTrain.getClass().getSimpleName()
						+ " " + e.player.ip + " " + (e.x + 25) + " "
						+ (e.y + 25) + " " + (e.x + 75) + " " + (e.y + 75));
				toTrain = null;
			}

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
		if (c[2].equals("train") && trainer instanceof Trainer
				) {
			Entity toTrain = null;
			try {
				toTrain = (Entity) Class.forName("entity.entities." + c[3])
						.getConstructor(String[].class)
						.newInstance(new Object[] { null });
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (toTrain != null && toTrain.canBeBought(trainer.player)) {
				toTrain.buyFrom(trainer.player);
				Training a = (Training) ((Trainer) trainer).getTraining();

				a.cooldown = ((Unit)toTrain).trainTime;// cooldown=traintime
				a.setEntity(toTrain);
				trainer.setAnimation(a);
			}
		}
	}
}
