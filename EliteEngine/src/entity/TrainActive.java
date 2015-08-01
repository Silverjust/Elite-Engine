package entity;

import entity.Active;
import entity.Entity;
import entity.Unit;
import shared.ref;

public class TrainActive extends Active {
	public Class<? extends Unit> unit;
	String descr = " ", stats = " ";

	public TrainActive(int x, int y, char n, Unit u, 
			Class<? extends Entity> trainer) {
		super(x, y, n, u.iconImg);
		unit = u.getClass();
		descr = u.getDesription();
		stats = u.getStatistics();
		clazz = trainer;
	}

	@Override
	public void onActivation() {
		Entity trainer = null;
		for (Entity e : ref.updater.selected) {
			if (clazz.isAssignableFrom(e.getClass())
					&& e.getAnimation() == e.stand) {
				trainer = e;
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

	@Override
	public String getDesription() {
		return descr;
	}

	@Override
	public String getStatistics() {
		return stats;
	}
}